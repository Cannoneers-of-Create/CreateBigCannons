package rbasamoyai.createbigcannons.cannonmount;

import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.NonStationaryLighter;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;

import java.util.Map;

public abstract class AbstractMountedCannonContraption extends Contraption  {

	protected Direction initialOrientation = Direction.NORTH;
	protected BlockPos startPos = BlockPos.ZERO;

	private final float maxDepress;
	private final float maxElevate;

	protected AbstractMountedCannonContraption(float maxDepress, float maxElevate) {
		this.maxDepress = maxDepress;
		this.maxElevate = maxElevate;
	}

	public float maximumDepression() { return this.maxDepress; }
	public float maximumElevation() { return this.maxElevate; }

	public LazyOptional<IItemHandler> getItemOptional() { return LazyOptional.empty(); }

	public Direction initialOrientation() { return this.initialOrientation; }

	public abstract void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower);
	public abstract void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity);

	public abstract float getWeightForStress();

	public void tick(Level level, PitchOrientedContraptionEntity entity) {}

	public void animate() {}

	@Override
	public CompoundTag writeNBT(boolean spawnPacket) {
		for (Map.Entry<BlockPos, BlockEntity> entry : this.presentTileEntities.entrySet()) {
			StructureBlockInfo info = this.blocks.get(entry.getKey());
			if (info == null) continue;
			CompoundTag nbt = entry.getValue().saveWithFullMetadata();
			nbt.remove("x");
			nbt.remove("y");
			nbt.remove("z");
			this.blocks.put(entry.getKey(), new StructureBlockInfo(info.pos, info.state, nbt));
		}

		CompoundTag tag = super.writeNBT(spawnPacket);
		if (this.initialOrientation != null) {
			tag.putString("InitialOrientation", this.initialOrientation.getSerializedName());
		}
		tag.putLong("LocalStartingPos", this.startPos == null ? 0L : this.startPos.asLong());
		return tag;
	}

	@Override
	public void readNBT(Level world, CompoundTag tag, boolean spawnData) {
		super.readNBT(world, tag, spawnData);
		this.initialOrientation = tag.contains("InitialOrientation", Tag.TAG_STRING) ? Direction.byName(tag.getString("InitialOrientation")) : Direction.NORTH;
		this.startPos = BlockPos.of(tag.getLong("LocalStartingPos"));

		if (world.isClientSide) return;
		for (Map.Entry<BlockPos, StructureBlockInfo> entry : this.blocks.entrySet()) {
			StructureBlockInfo info = this.blocks.get(entry.getKey());
			if (info == null || info.nbt == null) continue;

			info.nbt.putInt("x", info.pos.getX());
			info.nbt.putInt("y", info.pos.getY());
			info.nbt.putInt("z", info.pos.getZ());

			BlockEntity be = BlockEntity.loadStatic(info.pos, info.state, info.nbt);
			if (be == null) continue;
			be.setLevel(world);
			this.presentTileEntities.put(info.pos, be);
		}
	}

	@Override public boolean canBeStabilized(Direction direction, BlockPos pos) { return true; }

	@OnlyIn(Dist.CLIENT)
	@Override public ContraptionLighter<?> makeLighter() { return new NonStationaryLighter<>(this); }

	public static int getMaxCannonLength() {
		return CBCConfigs.SERVER.cannons.maxCannonLength.get();
	}

	public static AssemblyException cannonTooLarge() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonTooLarge", getMaxCannonLength()));
	}

	public static AssemblyException invalidCannon() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.invalidCannon"));
	}

	public static AssemblyException cannonLoaderInsideDuringAssembly(BlockPos pos) {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonLoaderInsideDuringAssembly", pos.getX(), pos.getY(), pos.getZ()));
	}

	public static AssemblyException hasIncompleteCannonBlocks(BlockPos pos) {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.hasIncompleteCannonBlocks", pos.getX(), pos.getY(), pos.getZ()));
	}

}
