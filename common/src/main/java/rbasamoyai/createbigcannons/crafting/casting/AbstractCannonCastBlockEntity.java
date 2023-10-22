package rbasamoyai.createbigcannons.crafting.casting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.foundation.utility.animation.LerpedFloat.Chaser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public abstract class AbstractCannonCastBlockEntity extends SmartBlockEntity implements WandActionable, IMultiBlockEntityContainer, IHaveHoveringInformation {

	private static final Object CASTING_RECIPES_KEY = new Object();

	protected List<CannonCastShape> structure = new ArrayList<>();
	protected CannonCastShape castShape = null;
	protected BlockPos controllerPos;
	protected BlockPos lastKnownPos;
	protected int height;
	protected boolean forceFluidLevelUpdate;
	protected boolean forceCastLevelUpdate;
	protected int castingTime;
	protected int startCastingTime;
	protected Map<CannonCastShape, CannonCastingRecipe> recipes = new HashMap<>();
	protected List<BlockState> resultPreview = new ArrayList<>();
	protected InvalidCastingError invalidCastingError = null;
	protected int castDelay = 0;
	protected boolean updateRecipes;

	private static final int SYNC_RATE = 8;
	protected boolean queuedSync;
	protected int syncCooldown;

	protected LerpedFloat fluidLevel;
	protected LerpedFloat castLevel;

	public AbstractCannonCastBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.height = 1;
		this.forceFluidLevelUpdate = true;
		this.forceCastLevelUpdate = true;
		this.updateRecipes = true;
		this.startCastingTime = 1;
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
	}

	@Override
	public void initialize() {
		super.initialize();
		this.sendData();
		if (level.isClientSide) this.invalidateRenderBoundingBox();
	}

	@Override
	public void sendData() {
		if (this.syncCooldown > 0) {
			this.queuedSync = true;
			return;
		}
		super.sendData();
		this.queuedSync = false;
		this.syncCooldown = SYNC_RATE;
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		Registry<CannonCastShape> shapeRegistry = CBCRegistries.cannonCastShapes();
		if (this.canRenderCastModel() && this.castShape != null) {
			tag.putString("Size", shapeRegistry.getKey(this.castShape).toString());
		}
		if (this.lastKnownPos != null) tag.put("LastKnownPos", NbtUtils.writeBlockPos(this.lastKnownPos));

		if (this.isController()) {
			if (!this.structure.isEmpty()) {
				ListTag structureTag = new ListTag();
				for (CannonCastShape sz : this.structure) {
					structureTag.add(StringTag.valueOf(shapeRegistry.getKey(sz).toString()));
				}
				tag.put("Structure", structureTag);
			}
			tag.putInt("Height", this.height);
			tag.putInt("CastingTime", this.castingTime);
			this.writeFluidToTag(tag);
			if (this.startCastingTime > 1) tag.putInt("StartCastingTime", this.startCastingTime);
			if (this.updateRecipes) tag.putBoolean("UpdateRecipes", true);

			if (!this.recipes.isEmpty() && !this.structure.isEmpty()) {
				ListTag previewList = new ListTag();
				for (CannonCastShape shape : this.structure) {
					if (this.recipes.containsKey(shape)) {
						BlockState state = this.recipes.get(shape).getResultBlock().defaultBlockState();
						if (state.hasProperty(BlockStateProperties.FACING))
							state = state.setValue(BlockStateProperties.FACING, Direction.DOWN);
						previewList.add(NbtUtils.writeBlockState(shape.applyTo(state)));
					} else {
						previewList.add(new CompoundTag());
					}
				}
				tag.put("Preview", previewList);
			}
			InvalidCastingError.write(tag, this.invalidCastingError);
			tag.putInt("CastingDelay", this.castDelay);
		} else {
			tag.put("Controller", NbtUtils.writeBlockPos(this.controllerPos));
		}

		super.write(tag, clientPacket);

		if (!clientPacket) return;
		if (this.forceFluidLevelUpdate) tag.putBoolean("ForceFluidLevel", true);
		if (this.forceCastLevelUpdate) tag.putBoolean("ForceCastLevel", true);
		if (this.queuedSync) tag.putBoolean("LazySync", true);

		this.forceFluidLevelUpdate = false;
		this.forceCastLevelUpdate = false;
	}

	protected abstract void writeFluidToTag(CompoundTag tag);

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);

		Registry<CannonCastShape> shapeRegistry = CBCRegistries.cannonCastShapes();

		BlockPos controllerBefore = this.controllerPos;
		int prevHeight = this.getControllerBE() == null ? 0 : this.getControllerBE().height;

		this.castShape = tag.contains("Size") ? shapeRegistry.get(new ResourceLocation(tag.getString("Size"))) : null;
		if (tag.contains("LastKnownPos")) this.lastKnownPos = NbtUtils.readBlockPos(tag.getCompound("LastKnownPos"));

		this.structure.clear();
		if (tag.contains("Structure")) {
			ListTag list = tag.getList("Structure", Tag.TAG_STRING);
			for (int i = 0; i < list.size(); ++i) {
				CannonCastShape shape = shapeRegistry.get(new ResourceLocation(list.getString(i)));
				this.structure.add(shape == null ? CannonCastShape.VERY_SMALL : shape);
			}
			this.height = tag.getInt("Height");
			this.updateFluids(tag);
			this.castingTime = Math.max(tag.getInt("CastingTime"), 0);
			this.startCastingTime = Math.max(tag.getInt("StartCastingTime"), 1);
			this.updateRecipes = tag.contains("UpdateRecipes");

			this.resultPreview.clear();
			ListTag preview = tag.getList("Preview", Tag.TAG_COMPOUND);
			for (int i = 0; i < preview.size(); ++i) {
				this.resultPreview.add(NbtUtils.readBlockState(this.blockHolderGetter(), preview.getCompound(i)));
			}
			this.invalidCastingError = InvalidCastingError.read(tag);
			this.castDelay = tag.getInt("CastingDelay");

			this.controllerPos = null;
		} else if (tag.contains("Controller")) {
			this.controllerPos = NbtUtils.readBlockPos(tag.getCompound("Controller"));
		}

		if (tag.contains("ForceFluidLevel") || this.fluidLevel == null) {
			this.fluidLevel = LerpedFloat.linear().startWithValue(this.getFillState());
		}
		if (tag.contains("ForceCastLevel") || this.castLevel == null) {
			this.castLevel = LerpedFloat.linear().startWithValue(this.getCastingState());
		}

		if (!clientPacket) return;
		boolean changeOfController = !Objects.equals(controllerBefore, this.controllerPos);
		if (changeOfController || this.getControllerBE() != null && prevHeight != this.getControllerBE().height) {
			if (this.hasLevel()) this.getLevel().sendBlockUpdated(this.getBlockPos(), getBlockState(), getBlockState(), 16);
			if (this.isController()) this.updateFluidClient();
			this.invalidateRenderBoundingBox();
		}
		if (this.isController()) {
			float fillState = this.getFillState();
			if (tag.contains("ForceFluidLevel") || this.fluidLevel == null) {
				this.fluidLevel = LerpedFloat.linear().startWithValue(fillState);
			}
			this.fluidLevel.chase(fillState, 0.5f, Chaser.EXP);

			float castState = this.getCastingState();
			if (tag.contains("ForceCastLevel") || this.castLevel == null) {
				this.castLevel = LerpedFloat.linear().startWithValue(castState);
			}
			this.castLevel.chase(castState, 0.5f, Chaser.EXP);
		}
		if (tag.contains("LazySync")) {
			this.fluidLevel.chase(this.fluidLevel.getChaseTarget(), 0.125f, Chaser.EXP);
			this.castLevel.chase(this.castLevel.getChaseTarget(), 0.125f, Chaser.EXP);
		}
	}

	protected abstract void updateFluids(CompoundTag tag);

	protected abstract void updateFluidClient();

	@Override
	public void tick() {
		super.tick();
		this.invalidateRenderBoundingBox();
		if (this.syncCooldown > 0) {
			this.syncCooldown--;
			if (this.syncCooldown == 0 && this.queuedSync) {
				this.sendData();
			}
		}

		if (this.lastKnownPos == null) {
			this.lastKnownPos = this.worldPosition;
		} else if (!this.lastKnownPos.equals(this.worldPosition) && this.worldPosition != null) {
			this.onPositionChanged();
			return;
		}

		if (this.fluidLevel != null) this.fluidLevel.tickChaser();
		if (this.castLevel != null) this.castLevel.tickChaser();
		if (this.isController()) {
			this.tickCastingBehavior();
		}
	}

	private void onPositionChanged() {
		this.removeController(true);
		this.lastKnownPos = this.worldPosition;
	}

	protected void tickCastingBehavior() {
		if (this.getLevel().isClientSide) return;
		if (this.castDelay > 0) {
			this.castDelay--;
			this.notifyUpdate();
		}
		if (this.getLevel().getBlockState(this.worldPosition.below()).canBeReplaced()) {
			this.leakContents();
		} else if (this.canStartCasting() && this.castDelay <= 0) {
			if (this.updateRecipes) {
				this.invalidCastingError = null;
				this.updateRecipes();
				if (this.invalidCastingError == null) {
					int oldStartCastingTime = this.startCastingTime;
					this.startCastingTime = this.calculateCastingTime();
					if (this.startCastingTime != oldStartCastingTime) {
						this.castingTime = this.startCastingTime;
					}
					this.updateRecipes = false;
				} else {
					this.startCastingTime = 1;
					this.castingTime = 0;
					this.castDelay = 8;
					this.recipes.clear();
					this.notifyUpdate();
					return;
				}
			}
			this.castingTime--;
			this.notifyUpdate();
			if (this.castingTime <= 0) {
				this.finishCasting();
				return;
			}
		} else {
			this.startCastingTime = 1;
			this.castingTime = 0;
			this.updateRecipes = true;
		}
	}

	protected abstract void leakContents();

	protected abstract boolean canStartCasting();

	protected void updateRecipes() {
		this.recipes.clear();
		List<BlockRecipe> list = BlockRecipeFinder.get(CASTING_RECIPES_KEY, this.getLevel(), this::matchingRecipeCache);
		list.stream()
			.map(CannonCastingRecipe.class::cast)
			.filter(r -> r.matches(this.getLevel(), this.worldPosition))
			.forEach(r -> this.recipes.put(r.shape(), r));
		for (ListIterator<CannonCastShape> iter = this.structure.listIterator(); iter.hasNext(); ) {
			int i = iter.nextIndex();
			CannonCastShape shape = iter.next();
			if (this.recipes.containsKey(shape)) continue;
			this.invalidCastingError = new InvalidCastingError(this.worldPosition.above(i), this.getFluid(), shape);
			return;
		}
	}

	protected boolean matchingRecipeCache(BlockRecipe recipe) {
		return recipe instanceof CannonCastingRecipe;
	}

	protected abstract net.minecraft.world.level.material.Fluid getFluid();

	protected int calculateCastingTime() {
		return this.structure.stream()
			.map(this.recipes::get)
			.map(r -> r == null ? 1200 : r.castingTime())
			.reduce(Integer::sum)
			.orElse(0);
	}

	protected void finishCasting() {
		if (!this.isController() || this.structure.isEmpty()) return;
		for (int y = 0; y < this.height; ++y) {
			if (this.structure.size() <= y) continue;
			CannonCastingRecipe recipe = this.recipes.get(this.structure.get(y));
			if (recipe == null) break;

			BlockPos pos = this.worldPosition.above(y);
			if (!(this.getLevel().getBlockEntity(pos) instanceof AbstractCannonCastBlockEntity cast)) break;
			BlockPos corner = pos.offset(-1, 0, -1);
			if (cast.getRenderedSize().isLarge()) {
				BlockPos.betweenClosedStream(corner, pos.offset(1, 0, 1)).forEach(pos1 -> {
					if (pos.equals(pos1) || !(this.getLevel().getBlockEntity(pos1) instanceof AbstractCannonCastBlockEntity cast1))
						return;
					cast1.setRemoved();
					this.getLevel().setBlock(pos1, CBCBlocks.FINISHED_CANNON_CAST.getDefaultState(), 11);
					if (!(this.getLevel().getBlockEntity(pos1) instanceof FinishedCannonCastBlockEntity fCast)) return;
					if (pos1.equals(corner)) {
						fCast.setRenderedShape(cast.castShape);
						fCast.setHeight(this.height);
						fCast.setRootBlock(this.worldPosition.offset(-1, 0, -1));
					} else {
						fCast.setCentralBlock(corner);
					}
				});
			}
			recipe.assembleInWorld(this.getLevel(), pos);

			if (y > 0 && this.getLevel().getBlockEntity(pos) instanceof ICannonBlockEntity<?> cbe && this.getLevel().getBlockEntity(pos.below()) instanceof ICannonBlockEntity<?> cbe1) {
				cbe.cannonBehavior().setConnectedFace(Direction.DOWN, true);
				cbe1.cannonBehavior().setConnectedFace(Direction.UP, true);
			}
		}
		SoundEvent sound = AllSoundEvents.STEAM.getMainEvent();
		this.getLevel().playSound(null, this.getBlockPos(), sound, SoundSource.BLOCKS, 1.0f, 1.0f);
	}

	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		if (!this.getLevel().isClientSide) {
			AbstractCannonCastBlockEntity controller = this.getControllerBE();
			if (controller != null) controller.castingTime = 0;
		}
		return InteractionResult.sidedSuccess(this.getLevel().isClientSide);
	}

	public void initializeCastMultiblock(CannonCastShape size) {
		this.castShape = size;
		if (this.castShape == null) return;
		if (this.getLevel().getBlockEntity(this.worldPosition.below()) instanceof AbstractCannonCastBlockEntity otherCast
			&& otherCast.getType() == this.getType()
			&& otherCast.getHeight() < getMaxHeight()) {
			this.controllerPos = otherCast.getController();
			AbstractCannonCastBlockEntity controller = otherCast.getControllerBE();
			if (controller != null) {
				this.addStructureCapacityToController(controller);
				controller.height += 1;
				controller.structure.add(this.castShape);
				controller.notifyUpdate();
			}
		} else {
			this.reInitTank();
			this.structure.add(this.castShape);
		}
		if (this.getLevel().getBlockEntity(this.worldPosition.above()) instanceof AbstractCannonCastBlockEntity otherCast && otherCast.isController()) {
			AbstractCannonCastBlockEntity controller = this.getControllerBE();
			if (controller != null && controller.height + otherCast.height <= getMaxHeight()) {
				controller.height += otherCast.height;
				controller.structure.addAll(otherCast.structure);
				otherCast.height = 1;
				otherCast.structure = new ArrayList<>();
				this.mergeControllerAndOtherFluids(controller, otherCast);
				controller.updatePotentialCastsAbove();
				controller.notifyUpdate();
			}
		}

		if (size.isLarge()) {
			for (Iterator<BlockPos> iter = BlockPos.betweenClosed(this.worldPosition.offset(-1, 0, -1), this.worldPosition.offset(1, 0, 1)).iterator(); iter.hasNext(); ) {
				BlockPos pos = iter.next();
				if (pos.equals(this.worldPosition)) continue;
				this.getLevel().setBlock(pos, CBCBlocks.CANNON_CAST.getDefaultState(), 11);
				if (this.getLevel().getBlockEntity(pos) instanceof AbstractCannonCastBlockEntity childCast && childCast.getType() == this.getType()) {
					childCast.controllerPos = this.getController();
					childCast.notifyUpdate();
				}
			}
		}
		AbstractCannonCastBlockEntity controller = this.getControllerBE();
		if (controller != null) {
			controller.updateRecipes = true;
			controller.forceFluidLevelUpdate = true;
			controller.forceCastLevelUpdate = true;
			controller.notifyUpdate();
		}
		this.notifyUpdate();
	}

	protected abstract void addStructureCapacityToController(AbstractCannonCastBlockEntity controller);

	protected abstract void reInitTank();

	protected abstract void mergeControllerAndOtherFluids(AbstractCannonCastBlockEntity controller, AbstractCannonCastBlockEntity other);

	public void destroyCastMultiblockAtLayer() {
		if (this.canRenderCastModel() && this.castShape != null) {
			this.onDestroyCenterCast();
		} else if (this.getLevel().getBlockEntity(this.getCenterBlock()) instanceof AbstractCannonCastBlockEntity otherCast) {
			otherCast.destroyCastMultiblockAtLayer();
		}
	}

	protected abstract void onDestroyCenterCast();

	public static List<CannonCastShape> getStructureFromPoint(Level level, BlockPos pos, int height) {
		List<CannonCastShape> structure = new ArrayList<>();
		if (!(level.getBlockEntity(pos) instanceof AbstractCannonCastBlockEntity start)) return structure;
		pos = start.getCenterBlock();
		for (int i = 0; i < height; ++i) {
			if (!(level.getBlockEntity(pos) instanceof AbstractCannonCastBlockEntity cast)) break;
			structure.add(cast.castShape);
		}
		return structure;
	}

	protected void updatePotentialCastsAbove() {
		if (!this.isController()) return;
		for (int y = 0; y < this.height; ++y) {
			if (!(this.getLevel().getBlockEntity(this.worldPosition.above(y)) instanceof AbstractCannonCastBlockEntity cast))
				break;
			if (y != 0) cast.setController(this.worldPosition);
			if (cast.getRenderedSize().isLarge()) {
				for (int x = -1; x < 2; ++x) {
					for (int z = -1; z < 2; ++z) {
						if ((x != 0 || z != 0) && this.getLevel().getBlockEntity(this.worldPosition.offset(x, y, z)) instanceof AbstractCannonCastBlockEntity cast1) {
							cast1.setController(this.worldPosition);
							cast1.setChanged();
						}
					}
				}
			}
		}
	}

	public int calculateCapacityFromStructure() {
		return this.structure.stream().map(CannonCastShape::fluidSize).reduce(Integer::sum).orElse(0);
	}

	public BlockPos getCenterBlock() {
		return this.isController() ? this.worldPosition : new BlockPos(this.controllerPos.getX(), this.worldPosition.getY(), this.controllerPos.getZ());
	}

	public boolean canRenderCastModel() {
		return this.isController() || this.controllerPos.getX() == this.worldPosition.getX() && this.controllerPos.getZ() == this.worldPosition.getZ();
	}

	public abstract float getFillState();

	public LerpedFloat getFluidLevel() {
		return this.fluidLevel;
	}

	public void setFluidLevel(LerpedFloat level) {
		this.fluidLevel = level;
	}

	public float getCastingState() {
		return this.startCastingTime <= 1 ? 0.0f : 1.0f - (float) this.castingTime / (float) this.startCastingTime;
	}

	public LerpedFloat getCastingLevel() {
		return this.castLevel;
	}

	@Override
	public BlockPos getController() {
		return this.isController() ? this.worldPosition : this.controllerPos;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public AbstractCannonCastBlockEntity getControllerBE() {
		return this.isController() ? this : this.getLevel().getBlockEntity(this.controllerPos) instanceof AbstractCannonCastBlockEntity cast ? cast : null;
	}

	@Override
	public boolean isController() {
		return this.controllerPos == null || this.worldPosition.equals(this.controllerPos);
	}

	protected abstract void refreshCap();

	@Override
	public void setController(BlockPos pos) {
		if (this.getLevel().isClientSide) this.invalidateRenderBoundingBox();
		if (this.getLevel().isClientSide && !this.isVirtual() || pos.equals(this.controllerPos)) return;
		this.controllerPos = pos;
		this.refreshCap();
		this.notifyUpdate();
	}

	public static int getMaxHeight() {
		return CBCConfigs.SERVER.crafting.maxCannonCastHeight.get();
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return 3;
	}

	@Override
	protected AABB createRenderBoundingBox() {
		if (this.isController()) {
			return super.createRenderBoundingBox().move(-1, 0, -1).expandTowards(2, this.height - 1, 2);
		}
		if (this.canRenderCastModel() && this.getControllerBE() != null) {
			return this.getControllerBE().createRenderBoundingBox();
		}
		return super.createRenderBoundingBox();
	}

	public CannonCastShape getRenderedSize() {
		return this.castShape;
	}

	@Override
	public void removeController(boolean keepContents) {
		this.refreshCap();
		this.notifyUpdate();
	}

	@Override
	public BlockPos getLastKnownPos() {
		return this.lastKnownPos;
	}

	@Override
	public void preventConnectivityUpdate() {
	}

	@Override
	public void notifyMultiUpdated() {

	}

	@Override
	public Axis getMainConnectionAxis() {
		return Axis.Y;
	}

	@Override
	public int getMaxLength(Axis longAxis, int width) {
		return longAxis == Axis.Y ? CBCConfigs.SERVER.crafting.maxCannonCastHeight.get() : 3;
	}

	@Override
	public int getMaxWidth() {
		return 3;
	}

	@Override
	public void setWidth(int width) {
	}

	public boolean matchesRecipe(CannonCastingRecipe recipe) {
		return this.getControllerBE() != null && this.getControllerBE().structure.contains(recipe.shape()) && this.testWithFluid(recipe);
	}

	protected abstract boolean testWithFluid(CannonCastingRecipe recipe);

	public abstract boolean tryEmptyItemIntoBE(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem, Direction side);

	public abstract boolean tryFillItemFromBE(Level world, Player player, InteractionHand handIn, ItemStack heldItem, Direction side);

	@Override
	public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		AbstractCannonCastBlockEntity controller = this.getControllerBE();
		if (controller == null || controller.invalidCastingError == null) return false;
		Component errorMsg = controller.invalidCastingError.getMessage();
		List<Component> cutErrorLines = TooltipHelper.cutTextComponent(errorMsg, TooltipHelper.Palette.GRAY_AND_WHITE);
		for (Component cline : cutErrorLines) {
			Lang.builder().add(cline.copy()).forGoggles(tooltip);
		}
		return true;
	}

}
