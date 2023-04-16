package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class QuickfiringBreechInstance extends BlockEntityInstance<QuickfiringBreechBlockEntity> implements DynamicInstance {

	private final OrientedData breechblock;
	private final Direction facing;
	private final boolean alongFirst;
	private final Direction.Axis axis;

	public QuickfiringBreechInstance(MaterialManager materialManager, QuickfiringBreechBlockEntity blockEntity) {
		super(materialManager, blockEntity);

		this.breechblock = materialManager.defaultSolid()
				.material(Materials.ORIENTED)
				.getModel(this.getPartialModelForState(this.blockState))
				.createInstance();

		this.facing = this.blockState.getValue(QuickfiringBreechBlock.FACING);
		this.alongFirst = this.blockState.getValue(QuickfiringBreechBlock.AXIS);

		boolean horizontal = this.facing.getAxis().isHorizontal();

		if (horizontal && (this.facing.getAxis() == Direction.Axis.X) != this.alongFirst) {
			Quaternion q = Direction.UP.step().rotationDegrees(AngleHelper.horizontalAngle(this.facing));
			Quaternion q1 = Direction.EAST.step().rotationDegrees(90.0f);
			q.mul(q1);
			this.breechblock.setRotation(q);
			this.axis = Direction.Axis.Y;
		} else if (horizontal) {
			Quaternion q = Direction.UP.step().rotationDegrees(AngleHelper.horizontalAngle(this.facing) + 90.0f);
			Quaternion q1 = Direction.SOUTH.step().rotationDegrees(90.0f);
			q.mul(q1);
			this.breechblock.setRotation(q);
			this.axis = this.alongFirst ? Direction.Axis.Z : Direction.Axis.X;
		} else {
			Quaternion q = Direction.UP.step().rotationDegrees(AngleHelper.horizontalAngle(this.facing) - (this.alongFirst ? 0.0f : 90.0f));
			this.breechblock.setRotation(q);
			this.axis = this.alongFirst ? Direction.Axis.Z : Direction.Axis.X;
		}

		this.transformModels();
	}

	@Override
	public void beginFrame() {
		this.transformModels();
	}

	private void transformModels() {
		float renderedBreechblockOffset = this.blockEntity.getOpenProgress(AnimationTickHolder.getPartialTicks());
		renderedBreechblockOffset = renderedBreechblockOffset / 16.0f * 13.0f;
		Vector3f normal = Direction.fromAxisAndDirection(this.axis, this.axis == Direction.Axis.Y ? Direction.AxisDirection.POSITIVE : this.facing.getAxisDirection()).step();
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
		this.breechblock.delete();
	}

	private PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof BigCannonBlock ? CBCBlockPartials.breechblockFor(((BigCannonBlock) state.getBlock()).getCannonMaterial()) : CBCBlockPartials.CAST_IRON_SLIDING_BREECHBLOCK;
	}

}
