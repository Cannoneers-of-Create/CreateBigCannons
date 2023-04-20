package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import rbasamoyai.createbigcannons.CBCClientCommon;

public class SlidingBreechInstance extends ShaftInstance implements DynamicInstance {

	private final SlidingBreechBlockEntity breech;
	private OrientedData breechblock;
	private Direction blockRotation;

	public SlidingBreechInstance(MaterialManager dispatcher, SlidingBreechBlockEntity tile) {
		super(dispatcher, tile);
		this.breech = tile;
	}

	@Override
	public void init() {
		super.init();

		Axis axis = CBCClientCommon.getRotationAxis(this.blockState);
		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
		this.blockRotation = facing.getCounterClockWise(axis);
		if (this.blockRotation == Direction.DOWN) this.blockRotation = Direction.UP;

		this.breechblock = this.materialManager.defaultSolid()
				.material(Materials.ORIENTED)
				.getModel(CBCClientCommon.getBreechblockForState(this.blockState), this.blockState, this.blockRotation)
				.createInstance();

		boolean alongFirst = this.blockState.getValue(QuickfiringBreechBlock.AXIS);
		if (facing.getAxis().isHorizontal() && !alongFirst) {
			Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
			Quaternion q = rotDir.step().rotationDegrees(90f);
			this.breechblock.setRotation(q);
		}
		if (facing.getAxis() == Axis.X && alongFirst) {
			this.breechblock.setRotation(this.blockRotation.step().rotationDegrees(90f));
		}

		this.transformModels();
	}

	@Override
	public void beginFrame() {
		this.transformModels();
	}
	
	private void transformModels() {
		float renderedBreechblockOffset = this.breech.getRenderedBlockOffset(AnimationTickHolder.getPartialTicks());
		renderedBreechblockOffset = renderedBreechblockOffset / 16.0f * 13.0f;
		Vector3f normal = this.blockRotation.step();
		normal.mul(renderedBreechblockOffset);
		this.breechblock.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z());
	}
	
	@Override
	public void updateLight() {
		super.updateLight();
		this.relight(this.pos, this.breechblock);
	}
	
	@Override
	public void remove() {
		super.remove();
		this.breechblock.delete();
	}

}
