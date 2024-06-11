package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

public class FluidShellProjectile extends FuzedBigCannonProjectile {

	private EndFluidStack fluidStack;

	public FluidShellProjectile(EntityType<? extends FluidShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Fluid", this.fluidStack.writeTag(new CompoundTag()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.fluidStack = EndFluidStack.readTag(tag.getCompound("Fluid"));
	}

	public void setFluidStack(EndFluidStack fstack) { this.fluidStack = fstack; }

	@Override
	protected void detonate() {
		Vec3 oldDelta = this.getDeltaMovement();
		FluidExplosion explosion = new FluidExplosion(this.level, null, this.indirectArtilleryFire(), this.getX(),
			this.getY(), this.getZ(), this.getAllProperties().explosion().explosivePower(),
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction(), this.fluidStack.fluid());
		CreateBigCannons.handleCustomExplosion(this.level, explosion);

		if (!this.fluidStack.isEmpty()) {
			FluidShellProperties properties = this.getAllProperties();
			int mbPerBlob = properties.mBPerFluidBlob();
			byte blobSize = (byte)(mbPerBlob / (double) properties.mBPerAoeRadius()); // No conversion because ratio would be same
			int convertCount = IndexPlatform.convertFluid(mbPerBlob);
			int count = (int) Math.ceil(this.fluidStack.amount() / (double) convertCount);
			FluidBlobBurst burst = CBCProjectileBurst.spawnConeBurst(this.level, CBCEntityTypes.FLUID_BLOB_BURST.get(),
				this.position(), oldDelta, count, properties.fluidBlobSpread());
			burst.setFluidStack(this.fluidStack.copy(convertCount));
			burst.setBlobSize(blobSize);
		}
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.FLUID_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected FluidShellProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.FLUID_SHELL.getPropertiesOf(this);
	}

}
