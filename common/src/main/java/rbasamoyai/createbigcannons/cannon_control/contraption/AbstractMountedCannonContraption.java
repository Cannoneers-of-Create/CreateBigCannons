package rbasamoyai.createbigcannons.cannon_control.contraption;

import java.util.Map;

import javax.annotation.Nonnull;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.config.CannonMountPropertiesHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public abstract class AbstractMountedCannonContraption extends Contraption {

	protected Direction initialOrientation = Direction.NORTH;
	protected BlockPos startPos = BlockPos.ZERO;

	public float maximumDepression(@Nonnull ControlPitchContraption controller) {
		ICannonContraptionType type = this.getCannonType();
		if (controller instanceof BlockEntity beMount) {
			return CannonMountPropertiesHandler.getProperties(beMount, type).maximumDepression(beMount);
		} else if (controller instanceof Entity entityMount) {
			return CannonMountPropertiesHandler.getProperties(entityMount, type).maximumDepression(entityMount);
		} else {
			return 0;
		}
	}

	public float maximumElevation(@Nonnull ControlPitchContraption controller) {
		ICannonContraptionType type = this.getCannonType();
		if (controller instanceof BlockEntity beMount) {
			return CannonMountPropertiesHandler.getProperties(beMount, type).maximumElevation(beMount);
		} else if (controller instanceof Entity entityMount) {
			return CannonMountPropertiesHandler.getProperties(entityMount, type).maximumElevation(entityMount);
		} else {
			return 0;
		}
	}

	public Direction initialOrientation() {
		return this.initialOrientation;
	}

	public abstract void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller);

	public abstract void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity);

	public abstract float getWeightForStress();

	public void tick(Level level, PitchOrientedContraptionEntity entity) {
	}

	public void animate() {
	}

	@Override
	public CompoundTag writeNBT(boolean spawnPacket) {
		for (Map.Entry<BlockPos, BlockEntity> entry : this.presentBlockEntities.entrySet()) {
			StructureBlockInfo info = this.blocks.get(entry.getKey());
			if (info == null) continue;
			CompoundTag nbt = entry.getValue().saveWithFullMetadata();
			nbt.remove("x");
			nbt.remove("y");
			nbt.remove("z");
			this.blocks.put(entry.getKey(), new StructureBlockInfo(info.pos(), info.state(), nbt));
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
			if (info == null || info.nbt() == null) continue;
			CompoundTag infoNbt = info.nbt();
			BlockPos pos = info.pos();

			infoNbt.putInt("x", pos.getX());
			infoNbt.putInt("y", pos.getY());
			infoNbt.putInt("z", pos.getZ());

			BlockEntity be = BlockEntity.loadStatic(pos, info.state(), infoNbt);
			if (be == null) continue;
			be.setLevel(world);
			this.presentBlockEntities.put(pos, be);
		}
	}

	@Override
	public boolean canBeStabilized(Direction direction, BlockPos pos) {
		return true;
	}

	public boolean canBeTurnedByPassenger(Entity entity) {
		return false;
	}

	public boolean canBeTurnedByController(ControlPitchContraption control) {
		return true;
	}

	public boolean canBeFiredOnController(ControlPitchContraption control) {
		return true;
	}

	public BlockPos getSeatPos(Entity entity) {
		return null;
	}

	public abstract Vec3 getInteractionVec(PitchOrientedContraptionEntity poce);

	public BlockPos getStartPos() {
		return this.startPos;
	}

	public abstract ICannonContraptionType getCannonType();

	public static int getMaxCannonLength() {
		return CBCConfigs.SERVER.cannons.maxCannonLength.get();
	}

	public static AssemblyException cannonTooLarge() {
		return new AssemblyException(Component.translatable("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonTooLarge", getMaxCannonLength()));
	}

	public static AssemblyException invalidCannon() {
		return new AssemblyException(Component.translatable("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.invalidCannon"));
	}

	public static AssemblyException cannonLoaderInsideDuringAssembly(BlockPos pos) {
		return new AssemblyException(Component.translatable("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonLoaderInsideDuringAssembly", pos.getX(), pos.getY(), pos.getZ()));
	}

	public static AssemblyException hasIncompleteCannonBlocks(BlockPos pos) {
		return new AssemblyException(Component.translatable("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.hasIncompleteCannonBlocks", pos.getX(), pos.getY(), pos.getZ()));
	}

}
