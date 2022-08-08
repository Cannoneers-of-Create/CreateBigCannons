package rbasamoyai.createbigcannons.cannons.cannonend;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.HalfShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlockPartials;
import rbasamoyai.createbigcannons.cannons.CannonBlock;

public class ScrewBreechInstance extends HalfShaftInstance implements DynamicInstance {

	private final ScrewBreechBlockEntity breech;
	private final OrientedData screwLock;
	private final Direction facing;
	
	public ScrewBreechInstance(MaterialManager modelManager, ScrewBreechBlockEntity tile) {
		super(modelManager, tile);
		this.breech = tile;
		this.facing = this.blockState.getValue(BlockStateProperties.FACING);
		this.screwLock = modelManager.defaultSolid()
				.material(Materials.ORIENTED)
				.getModel(this.getPartialModelForState(this.blockState))
				.createInstance();
		this.transformModels();
	}
	
	private void transformModels() {
		float renderedScrewLockOffset = this.breech.getRenderedBlockOffset(AnimationTickHolder.getPartialTicks());
		float heightOffset = renderedScrewLockOffset * 0.25f;
		float rotationOffset = renderedScrewLockOffset * (this.facing.getAxisDirection() == AxisDirection.POSITIVE ? 360.0f : -360.0f);
		Vector3f normal = this.facing.step();
		Vector3f height = normal.copy();
		height.mul(heightOffset);
		
		boolean isY = this.facing.getAxis() == Direction.Axis.Y;
		Quaternion q = Vector3f.XP.rotationDegrees(isY ? this.facing == Direction.DOWN ? 180.0f : 0.0f : 90.0f);
		Quaternion q1 = Vector3f.YP.rotationDegrees(isY ? 0.0f : -this.facing.toYRot());
		Quaternion q2 = normal.rotationDegrees(rotationOffset);
		q1.mul(q);
		q2.mul(q1);
		
		this.screwLock.setPosition(this.getInstancePosition()).nudge(height.x(), height.y(), height.z()).setRotation(q2);
	}

	@Override
	public void beginFrame() {
		this.transformModels();
	}
	
	@Override
	public void remove() {
		super.remove();
		this.screwLock.delete();
	}
	
	@Override
	public void updateLight() {
		super.updateLight();
		this.screwLock.updateLight(this.world, this.instancePos);
	}
	
	private PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof CannonBlock ? CBCBlockPartials.screwLockFor(((CannonBlock) state.getBlock()).getCannonMaterial()) : CBCBlockPartials.STEEL_SCREW_LOCK;
	}
	
}
