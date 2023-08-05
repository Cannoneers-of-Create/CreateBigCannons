package rbasamoyai.createbigcannons.munitions;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;

public class ProjectileContext {

	private BlockState lastState = Blocks.AIR.defaultBlockState();
	private final CollisionContext collisionContext;
	private final Set<Entity> hitEntities = new LinkedHashSet<>();
	private final CBCCfgMunitions.GriefState griefState;
	private final Map<BlockPos, Float> queuedExplosions = new HashMap<>();

	public ProjectileContext(AbstractCannonProjectile projectile, CBCCfgMunitions.GriefState griefState) {
		this.collisionContext = CollisionContext.of(projectile);
		this.griefState = griefState;
	}

	public void setLastState(BlockState state) { this.lastState = state; }
	public BlockState getLastState() { return this.lastState; }
	public CollisionContext collisionContext() { return this.collisionContext; }
	public CBCCfgMunitions.GriefState griefState() { return this.griefState; }

	public boolean hasHitEntity(Entity entity) { return this.hitEntities.contains(entity); }
	public void addEntity(Entity entity) { this.hitEntities.add(entity); }
	public Set<Entity> hitEntities() { return this.hitEntities; }

	public void queueExplosion(BlockPos pos, float power) { this.queuedExplosions.put(pos, power); }
	public Map<BlockPos, Float> getQueuedExplosions() { return this.queuedExplosions; }

}
