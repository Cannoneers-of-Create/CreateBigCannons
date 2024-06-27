package rbasamoyai.createbigcannons.munitions;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;

public class ProjectileContext {

	private final CollisionContext collisionContext;
	private final Set<Entity> hitEntities = new LinkedHashSet<>();
	private final CBCCfgMunitions.GriefState griefState;
	private final Map<BlockPos, Float> queuedExplosions = new HashMap<>();
	private final List<ClientboundPlayBlockHitEffectPacket> effects = new LinkedList<>();

	public ProjectileContext(AbstractCannonProjectile projectile, CBCCfgMunitions.GriefState griefState) {
		this.collisionContext = CollisionContext.of(projectile);
		this.griefState = griefState;
	}

	public CollisionContext collisionContext() { return this.collisionContext; }
	public CBCCfgMunitions.GriefState griefState() { return this.griefState; }

	public boolean hasHitEntity(Entity entity) { return this.hitEntities.contains(entity); }
	public void addEntity(Entity entity) { this.hitEntities.add(entity); }
	public Set<Entity> hitEntities() { return this.hitEntities; }

	public void queueExplosion(BlockPos pos, float power) { this.queuedExplosions.put(pos, power); }
	public Map<BlockPos, Float> getQueuedExplosions() { return this.queuedExplosions; }

	public void addPlayedEffect(ClientboundPlayBlockHitEffectPacket packet) { this.effects.add(packet); }
	public List<ClientboundPlayBlockHitEffectPacket> getPlayedEffects() { return this.effects; }

}
