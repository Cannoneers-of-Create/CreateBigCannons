package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.simibubi.create.content.kinetics.base.HalfShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class ScrewBreechInstance extends HalfShaftInstance<ScrewBreechBlockEntity> implements DynamicInstance {

	private final ScrewBreechBlockEntity breech;
	private OrientedData screwLock;
	private Direction facing;

	public ScrewBreechInstance(MaterialManager modelManager, ScrewBreechBlockEntity tile) {
		super(modelManager, tile);
		this.breech = tile;
	}

	@Override
	public void init() {
		super.init();

		this.facing = this.blockState.getValue(BlockStateProperties.FACING);
		this.screwLock = this.materialManager.defaultSolid()
			.material(Materials.ORIENTED)
			.getModel(CBCClientCommon.getScrewBreechForState(this.blockState), this.blockState, this.facing)
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

		Quaternionf q = normal.rotationDegrees(rotationOffset);

		this.screwLock.setPosition(this.getInstancePosition()).nudge(height.x(), height.y(), height.z()).setRotation(q);
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
		this.relight(this.pos, this.screwLock);
	}

}
