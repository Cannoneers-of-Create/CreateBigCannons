package rbasamoyai.createbigcannons.munitions;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class ProjectileContext {

	private BlockState lastState = Blocks.AIR.defaultBlockState();
	private final CollisionContext collisionContext;

	public ProjectileContext(AbstractCannonProjectile projectile) {
		this.collisionContext = CollisionContext.of(projectile);
	}

	public void setLastState(BlockState state) { this.lastState = state; }
	public BlockState getLastState() { return this.lastState; }
	public CollisionContext collisionContext() { return this.collisionContext; }

}
