package rbasamoyai.createbigcannons.crafting.foundry;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.content.contraptions.processing.BasinOperatingTileEntity;
import com.simibubi.create.content.contraptions.processing.BasinTileEntity;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.crafting.CBCRecipeTypes;

public class BasinFoundryBlockEntity extends BasinOperatingTileEntity {

	public int meltingTime;
	public int recipeCooldown;
	public boolean running; 
	
	public BasinFoundryBlockEntity(BlockEntityType<? extends BasinFoundryBlockEntity> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}
	
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("MeltingTime", this.meltingTime);
		compound.putBoolean("Running", this.running);
	}
	
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		this.meltingTime = compound.getInt("MeltingTime");
		this.running = compound.getBoolean("Running");
	}

	@Override protected boolean isRunning() { return this.running; }

	@Override
	protected void onBasinRemoved() {
		if (!this.running) return;
		this.meltingTime = 0;
		this.currentRecipe = null;
		this.running = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide && (this.currentRecipe == null || this.meltingTime == -1)) {
			--this.recipeCooldown;
			if (this.recipeCooldown <= 0) {
				this.running = false;
				this.meltingTime = -1;
				this.basinChecker.scheduleUpdate();
				this.recipeCooldown = 20;
			}
		} else {
			this.recipeCooldown = 0;
		}
		
		if (this.running && this.level != null) {
			if (this.level.isClientSide && this.meltingTime > 0) {
				this.renderParticles();
			}
			if (!this.level.isClientSide && this.meltingTime <= 0) {
				this.meltingTime = -1;
				this.applyBasinRecipe();
				this.sendData();
			}
			
			if (this.meltingTime > 0) --this.meltingTime;
		}
	}
	
	private void renderParticles() {
		// Code sheepishly stolen from MechanicalMixerTileEntity#spillParticle with modifications
		float angle = this.level.random.nextFloat() * 360.0f;
		Vec3 offset = new Vec3(0, 0, 0.25f);
		offset = VecHelper.rotate(offset, angle, Axis.Y);
		Vec3 target = VecHelper.rotate(offset, -25, Axis.Y).add(0, .5f, 0);
		Vec3 center = offset.add(Vec3.atBottomCenterOf(this.worldPosition));
		target = VecHelper.offsetRandomly(target.subtract(offset), this.level.random, 1 / 128f);
		this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()), center.x, center.y + 0.3, center.z, target.x, target.y, target.z);
	}
	
	@Override
	protected boolean updateBasin() {
		if (this.running) return true;
		if (this.level == null || this.level.isClientSide) return true;
		if (!this.getBasin().filter(BasinTileEntity::canContinueProcessing).isPresent()) return true;
		
		List<Recipe<?>> recipes = this.getMatchingRecipes();
		if (recipes.isEmpty()) return true;
		this.currentRecipe = recipes.get(0);
		this.startProcessingBasin();
		this.sendData();
		return true;
	}
	
	@Override
	public void startProcessingBasin() {
		if (this.running && this.meltingTime > 0) return;
		super.startProcessingBasin();
		this.running = true;
		this.meltingTime = this.currentRecipe instanceof ProcessingRecipe<?> processed ? processed.getProcessingDuration() : 20;
	}

	@Override
	protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
		return recipe.getType() == CBCRecipeTypes.MELTING.getType();
	}

	private static final Object BASIN_MELTING_RECIPE_KEY = new Object();
	@Override protected Object getRecipeCacheKey() { return BASIN_MELTING_RECIPE_KEY; }
	
	@Override
	protected Optional<BasinTileEntity> getBasin() {
		return this.level != null && this.level.getBlockEntity(this.worldPosition.below()) instanceof BasinTileEntity basin ? Optional.of(basin) : Optional.empty();
	}
	
}
