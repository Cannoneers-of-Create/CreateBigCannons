package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing.Type;
import com.simibubi.create.content.contraptions.relays.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.belt.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.belt.TransportedItemStackHandlerBehaviour.TransportedResult;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.crafting.boring.TransformableByBoring;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class LayeredCannonBlockEntity extends SmartTileEntity implements ICannonBlockEntity, WandActionable {

	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	private static final Object BUILT_UP_HEATING_RECIPES_KEY = new Object();
	
	private CannonBehavior cannonBehavior;
	private CannonMaterial baseMaterial;
	private Map<CannonCastShape, Block> layeredBlocks = new HashMap<>();
	private Multimap<Direction, CannonCastShape> layersConnectedTowards = HashMultimap.create();
	private Direction currentFacing;
	
	private TransportedItemStack clockStack = new TransportedItemStack(ItemStack.EMPTY);
	private int completionProgress;
	
	public LayeredCannonBlockEntity(BlockEntityType<? extends LayeredCannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		if (state.hasProperty(FACING)) {
			this.currentFacing = state.getValue(FACING);
		}
	}
	
	@Override public CannonBehavior cannonBehavior() { return this.cannonBehavior; }
	
	public void setBaseMaterial(CannonMaterial material) { this.baseMaterial = material; }
	public CannonMaterial getBaseMaterial() { return this.baseMaterial; }

	@Override
	public void tick() {
		super.tick();
		
		BlockState state = this.getBlockState();
		
		if (!this.level.isClientSide && this.isEmpty()) {
			this.setRemoved();
			this.level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3 | 16);
			return;
		}
		
		if (state.hasProperty(FACING)) {
			Direction previousFacing = this.currentFacing;
			this.currentFacing = state.getValue(FACING);
			if (previousFacing != null && previousFacing != this.currentFacing) {
				Direction.Axis rotationAxis = getRotationAxis(previousFacing, this.currentFacing);
				Rotation rotation = getRotationBetween(previousFacing, this.currentFacing, rotationAxis);
				
				Multimap<Direction, CannonCastShape> newLayersConnected = HashMultimap.create();
				for (Direction dir : this.layersConnectedTowards.keySet()) {
					Direction dc = dir;
					for (int i = 0; i < rotation.ordinal(); ++i) {
						dc = dc.getClockWise(rotationAxis);
					}
					newLayersConnected.putAll(dc, this.layersConnectedTowards.get(dir));
				}
				this.layersConnectedTowards = newLayersConnected;
				this.setChanged();
			}
		}
		
		if (this.clockStack.processedBy == Type.BLASTING) {
			this.clockStack.processedBy = Type.NONE;
			++this.completionProgress;
			this.sendData();
			int cap = CBCConfigs.SERVER.crafting.builtUpCannonHeatingTime.get();
			if (this.completionProgress >= cap) {
				this.completionProgress = cap;
				if (!this.level.isClientSide && !this.tryFinishHeating()) this.completionProgress = 0;
			}
		} else if (this.completionProgress > 0) {
			--this.completionProgress;
			this.sendData();
		}
	}
	
	private boolean tryFinishHeating() {
		List<BlockRecipe> recipes = BlockRecipeFinder.get(BUILT_UP_HEATING_RECIPES_KEY, this.level, this::matchingRecipeCache);
		if (recipes.isEmpty()) return false;
		Optional<BlockRecipe> recipe = recipes.stream()
				.filter(r -> r.matches(this.level, this.worldPosition))
				.findFirst();
		if (!recipe.isPresent()) return false;
		recipe.get().assembleInWorld(this.level, this.worldPosition);
		return true;
	}
	
	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		if (!this.level.isClientSide) this.tryFinishHeating();
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}
	
	private boolean matchingRecipeCache(BlockRecipe recipe) {
		return recipe instanceof BuiltUpHeatingRecipe;
	}
	
	private static Direction.Axis getRotationAxis(Direction prev, Direction current) {
		Set<Direction.Axis> axes = EnumSet.allOf(Direction.Axis.class);
		axes.remove(prev.getAxis());
		axes.remove(current.getAxis());
		return axes.stream().findFirst().orElseThrow(() -> new IllegalStateException("Failed to find the rotation axes of two different axes"));
	}
	
	private static Rotation getRotationBetween(Direction prev, Direction current, Direction.Axis axis) {
		if (prev == current) return Rotation.NONE;
		if (prev == current.getOpposite()) return Rotation.CLOCKWISE_180;
		return prev.getClockWise(axis) == current ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
	}
	
	public void setLayer(CannonCastShape layer, Block block) {
		this.layeredBlocks.put(layer, block);
	}
	public Block getLayer(CannonCastShape layer) { return this.layeredBlocks.get(layer); }
	public void removeLayer(CannonCastShape layer) {
		this.layeredBlocks.remove(layer);
		for (Direction dir : Iterate.directions) this.setLayerConnectedTo(dir, layer, false);
	}
	public boolean hasLayer(CannonCastShape layer) { return this.layeredBlocks.containsKey(layer); }
	public Map<CannonCastShape, Block> getLayers() { return this.layeredBlocks; }
	
	public CannonCastShape getTopCannonShape() {
		if (this.layeredBlocks.isEmpty()) return null;
		CannonCastShape result = null;
		for (CannonCastShape shape : this.layeredBlocks.keySet()) {
			if (result == null || result.diameter() < shape.diameter()) result = shape;
		}
		return result;
	}
	
	public CannonCastShape getTopConnectedLayer(Direction direction) {
		if (!this.layersConnectedTowards.containsKey(direction)) return null;
		CannonCastShape result = null;
		for (CannonCastShape shape : this.layersConnectedTowards.get(direction)) {
			if (result == null || result.diameter() < shape.diameter()) result = shape;
		}
		return result;
	}
	
	public LayeredCannonBlockEntity getSplitBlockEntity(Collection<CannonCastShape> layers, Direction from) {
		LayeredCannonBlockEntity newLayer = new LayeredCannonBlockEntity(CBCBlockEntities.LAYERED_CANNON.get(), this.worldPosition, this.getBlockState());
		newLayer.baseMaterial = this.baseMaterial;
		newLayer.currentFacing = this.currentFacing;
		for (CannonCastShape layer : layers) {
			if (!this.layeredBlocks.containsKey(layer) || from != null && !this.isLayerConnectedTo(from, layer)) continue;
			newLayer.setLayer(layer, this.getLayer(layer));
			for (Direction dir : Iterate.directions) {
				newLayer.setLayerConnectedTo(dir, layer, this.isLayerConnectedTo(dir, layer));
			}
		}
		for (Direction dir : Iterate.directions) {
			boolean connect = this.cannonBehavior.isConnectedTo(dir);
			newLayer.cannonBehavior.setConnectedFace(dir, connect);		
		}
		return newLayer;
	}
	
	public LayeredCannonBlockEntity getSplitBlockEntity(CannonCastShape fullShape, Direction from) {
		return this.getSplitBlockEntity(Arrays.asList(fullShape), from);
	}
	
	public void removeLayersOfOther(LayeredCannonBlockEntity other) {
		for (CannonCastShape layer : other.layeredBlocks.keySet()) {
			this.removeLayer(layer);
		}
	}
	
	public void addLayersOfOther(LayeredCannonBlockEntity other) {
		for (Map.Entry<CannonCastShape, Block> layer : other.layeredBlocks.entrySet()) {
			CannonCastShape shape = layer.getKey(); 
			this.setLayer(shape, layer.getValue());
			for (Direction dir : Iterate.directions) {
				this.setLayerConnectedTo(dir, shape, other.isLayerConnectedTo(dir, shape));
			}
		}
		this.setChanged();
	}
	
	public Block getSimplifiedBlock() {
		return this.isEmpty() ? Blocks.AIR
			: this.layeredBlocks.size() == 1 ? this.layeredBlocks.values().stream().findAny().get()
			: CBCBlocks.BUILT_UP_CANNON.get();
	}
	
	public void updateBlockstate() {
		Block block = this.getSimplifiedBlock();
		if (this.getBlockState().getBlock() != block) {
			BlockState newState = block.defaultBlockState();
			if (newState.hasProperty(FACING) && this.getBlockState().hasProperty(FACING)) {
				newState = newState.setValue(FACING, this.getBlockState().getValue(FACING));
			}
			this.setRemoved();
			this.level.setBlock(this.worldPosition, newState, 3 | 16);
			if (!this.getType().isValid(newState)) return;
			BlockEntity be = this.level.getBlockEntity(this.worldPosition);
			if (!(be instanceof LayeredCannonBlockEntity newLayered)) return;
			newLayered.layeredBlocks = this.layeredBlocks;
			newLayered.layersConnectedTowards = this.layersConnectedTowards;
			newLayered.baseMaterial = this.baseMaterial;
			newLayered.currentFacing = newState.getValue(FACING);
			newLayered.setChanged();
			
			for (Direction dir : Iterate.directions) {
				BlockPos pos1 = newLayered.worldPosition.relative(dir);
				BlockEntity be1 = newLayered.level.getBlockEntity(pos1);
				BlockState state1 = newLayered.level.getBlockState(pos1);
				if (be1 instanceof ICannonBlockEntity cbe && state1.getBlock() instanceof CannonBlock cBlock && cBlock.getCannonMaterialInLevel(newLayered.level, state1, pos1) == newLayered.baseMaterial) {
					boolean connect = this.cannonBehavior.isConnectedTo(dir);
					newLayered.cannonBehavior.setConnectedFace(dir, connect);
					cbe.cannonBehavior().setConnectedFace(dir.getOpposite(), connect);
				}
				if (be1 instanceof LayeredCannonBlockEntity layered1 && layered1.baseMaterial == this.baseMaterial) {
					for (CannonCastShape shape : this.getConnectedTo(dir)) {
						layered1.setLayerConnectedTo(dir.getOpposite(), shape, true);
					}
					layered1.setChanged();
				}
			}
		}
	}
	
	public void setLayerConnectedTo(Direction direction, CannonCastShape shape, boolean connected) {
		if (!this.layeredBlocks.containsKey(shape)) return;
		if (connected) this.layersConnectedTowards.put(direction, shape); 
		else this.layersConnectedTowards.remove(direction, shape);
	}
	
	public boolean isLayerConnectedTo(Direction direction, CannonCastShape shape) {
		return this.layersConnectedTowards.get(direction).contains(shape);
	}
	
	public boolean isEmpty() { return this.layeredBlocks.isEmpty(); }
	
	public Collection<CannonCastShape> getConnectedTo(Direction direction) {
		return this.layersConnectedTowards.get(direction);
	}
	
	public boolean isCollidingWith(StructureBlockInfo info, LayeredCannonBlockEntity other, Direction dir) {
		if (this.currentFacing == null || dir.getAxis() != this.currentFacing.getAxis()) return true;
		if (info.nbt == null || !info.nbt.contains("id")) return true;
		if (other.baseMaterial != this.baseMaterial) return true;
		Set<Integer> set = this.layeredBlocks.keySet().stream().map(CannonCastShape::diameter).collect(Collectors.toCollection(HashSet::new));
		Set<Integer> set1 = other.layeredBlocks.keySet().stream().map(CannonCastShape::diameter).collect(Collectors.toCollection(HashSet::new));
		set.retainAll(set1);
		return !set.isEmpty();
	}
	
	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		behaviours.add(this.cannonBehavior = new CannonBehavior(this, this::canLoadBlock));
		behaviours.add(new TransportedItemStackHandlerBehaviour(this, this::clockCallback));
	}
	
	private void clockCallback(float maxDistanceFromCenter, Function<TransportedItemStack, TransportedResult> func) {
		this.clockStack.processedBy = Type.NONE;
		func.apply(this.clockStack);
		this.clockStack.processingTime = -1;
	}
	
	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		if (this.baseMaterial != null) {
			tag.putString("Material", this.baseMaterial.name().toString());
		}
		ListTag layerTag = new ListTag();
		for (Map.Entry<CannonCastShape, Block> e : this.layeredBlocks.entrySet()) {
			CompoundTag entryTag = new CompoundTag();
			entryTag.putString("Shape", CBCRegistries.CANNON_CAST_SHAPES.get().getKey(e.getKey()).toString());
			entryTag.putString("Block", ForgeRegistries.BLOCKS.getKey(e.getValue()).toString());
			layerTag.add(entryTag);
		}
		tag.put("Layers", layerTag);
		CompoundTag layerConnectionTag = new CompoundTag();
		for (Direction dir : Iterate.directions) {
			if (!this.layersConnectedTowards.containsKey(dir)) continue;
			layerConnectionTag.put(dir.getSerializedName(),
				this.layersConnectedTowards.get(dir).stream()
				.map(CBCRegistries.CANNON_CAST_SHAPES.get()::getKey)
				.map(ResourceLocation::toString)
				.map(StringTag::valueOf)
				.collect(Collectors.toCollection(ListTag::new)));
		}
		tag.put("LayerConnections", layerConnectionTag);
		if (this.currentFacing != null) {
			tag.putString("Facing", this.currentFacing.getSerializedName());
		}
		if (this.completionProgress > 0) tag.putInt("Progress", this.completionProgress);
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		boolean justBored = TransformableByBoring.wasJustBored(tag);		
		
		this.layersConnectedTowards.clear();
		CompoundTag layerConnectionTag = tag.getCompound("LayerConnections");
		for (Direction dir : Iterate.directions) {
			if (!layerConnectionTag.contains(dir.getSerializedName())) continue;
			ListTag connections = layerConnectionTag.getList(dir.getSerializedName(), Tag.TAG_STRING);
			for (int i = 0; i < connections.size(); ++i) {
				CannonCastShape shape = CBCRegistries.CANNON_CAST_SHAPES.get().getValue(new ResourceLocation(connections.getString(i)));
				if (shape != null) this.layersConnectedTowards.put(dir, shape);
			}
		}		
		
		if (justBored) {
			tag.remove("JustBored");
			return;
		}
		
		this.baseMaterial = tag.contains("Material") ? CannonMaterial.fromName(new ResourceLocation(tag.getString("Material"))) : null;
		this.layeredBlocks.clear();
		ListTag layers = tag.getList("Layers", Tag.TAG_COMPOUND);
		for (int i = 0; i < layers.size(); ++i) {
			CompoundTag entry = layers.getCompound(i);
			this.layeredBlocks.put(CBCRegistries.CANNON_CAST_SHAPES.get().getValue(new ResourceLocation(entry.getString("Shape"))), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(entry.getString("Block"))));
		}
		this.currentFacing = tag.contains("Facing") ? Direction.byName(tag.getString("Facing")) : null;
		this.completionProgress = tag.getInt("Progress");
	}
	
	@Override public boolean canLoadBlock(StructureBlockInfo blockInfo) { return false; }

}
