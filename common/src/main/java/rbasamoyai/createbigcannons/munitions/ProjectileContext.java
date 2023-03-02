package rbasamoyai.createbigcannons.munitions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectileContext {

	private BlockState lastState = Blocks.AIR.defaultBlockState();
	private final CollisionContext collisionContext;
	private final Vec3 start;
	private final Vec3 end;
	private final List<Entity> hitEntities;
	private final CBCCfgMunitions.GriefState griefState;
	private final Map<BlockPos, Float> queuedExplosions = new HashMap<>();

	public ProjectileContext(AbstractCannonProjectile projectile, Vec3 start, Vec3 end, List<Entity> hitEntities, CBCCfgMunitions.GriefState griefState) {
		this.collisionContext = CollisionContext.of(projectile);
		this.start = start;
		this.end = end;
		this.hitEntities = hitEntities;
		this.griefState = griefState;
	}

	public void setLastState(BlockState state) { this.lastState = state; }
	public BlockState getLastState() { return this.lastState; }
	public CollisionContext collisionContext() { return this.collisionContext; }
	public Vec3 start() { return this.start; }
	public Vec3 end() { return this.end; }
	public List<Entity> hitEntities() { return this.hitEntities; }
	public CBCCfgMunitions.GriefState griefState() { return this.griefState; }
	public void queueExplosion(BlockPos pos, float power) { this.queuedExplosions.put(pos, power); }
	public Map<BlockPos, Float> getQueuedExplosions() { return this.queuedExplosions; }

}
