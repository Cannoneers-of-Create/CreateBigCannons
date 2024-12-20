package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import org.joml.Quaternionf;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.AllMaterialSpecs;

import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonMountInstance extends BlockEntityInstance<CannonMountBlockEntity> implements DynamicInstance {

	private OrientedData rotatingMount;
	private OrientedData rotatingMountShaft;
	private RotatingData pitchShaft;
	private RotatingData yawShaft;

	public CannonMountInstance(MaterialManager dispatcher, CannonMountBlockEntity tile) {
		super(dispatcher, tile);
	}

	@Override
	public void init() {
		super.init();

		int blockLight = this.world.getBrightness(LightLayer.BLOCK, this.pos);
		int skyLight = this.world.getBrightness(LightLayer.SKY, this.pos);

		Direction facing = this.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis pitchAxis = facing.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

		Material<RotatingData> rotatingMaterial = this.materialManager.defaultSolid().material(AllMaterialSpecs.ROTATING);
		Instancer<RotatingData> shaftInstance = rotatingMaterial.getModel(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, pitchAxis));
		// TODO: upside down mount
		Instancer<RotatingData> halfShaftInstance = rotatingMaterial.getModel(AllPartialModels.SHAFT_HALF, this.blockState, Direction.DOWN);

		this.rotatingMount = this.materialManager.defaultCutout()
			.material(Materials.ORIENTED)
			.getModel(CBCBlockPartials.ROTATING_MOUNT, this.blockState)
			.createInstance();
		this.rotatingMount.setPosition(this.getInstancePosition().above());

		this.rotatingMountShaft = this.materialManager.defaultCutout()
			.material(Materials.ORIENTED)
			.getModel(CBCBlockPartials.CANNON_CARRIAGE_AXLE, this.blockState, Direction.NORTH)
			.createInstance();

		this.rotatingMountShaft.setPosition(this.getInstancePosition().above(2));

		this.pitchShaft = shaftInstance.createInstance();
		this.pitchShaft
			.setRotationAxis(pitchAxis)
			.setRotationOffset(this.getRotationOffset(pitchAxis))
			.setColor(this.blockEntity.getPitchInterface())
			.setPosition(this.getInstancePosition())
			.setBlockLight(blockLight)
			.setSkyLight(skyLight);

		this.yawShaft = halfShaftInstance.createInstance();
		this.yawShaft
			.setRotationAxis(Direction.Axis.Y)
			.setRotationOffset(this.getRotationOffset(Direction.Axis.Y))
			.setColor(this.blockEntity.getYawInterface())
			.setPosition(this.getInstancePosition())
			.setBlockLight(blockLight)
			.setSkyLight(skyLight);

		this.transformModels();
	}

	@Override
	public void remove() {
		this.rotatingMount.delete();
		this.rotatingMountShaft.delete();
		this.pitchShaft.delete();
		this.yawShaft.delete();
	}

	private void transformModels() {
		Direction facing = this.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis pitchAxis = facing.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

		this.updateRotation(this.pitchShaft, pitchAxis, this.blockEntity.getPitchSpeed(), true);
		this.updateRotation(this.yawShaft, Direction.Axis.Y, this.blockEntity.getYawSpeed(), false);
	}

	// Copied from KineticBlockEntityInstance
	protected void updateRotation(RotatingData instance, Direction.Axis axis, float speed, boolean pitch) {
		instance.setRotationAxis(axis)
			.setRotationOffset(getRotationOffset(axis))
			.setRotationalSpeed(speed)
			.setColor(pitch ? this.blockEntity.getPitchInterface() : this.blockEntity.getYawInterface());
	}

	// Copied from KineticBlockEntityInstance
	protected float getRotationOffset(final Direction.Axis axis) {
		float offset = ICogWheel.isLargeCog(blockState) ? 11.25f : 0;
		double d = (((axis == Direction.Axis.X) ? 0 : pos.getX()) + ((axis == Direction.Axis.Y) ? 0 : pos.getY())
			+ ((axis == Direction.Axis.Z) ? 0 : pos.getZ())) % 2;
		if (d == 0) {
			offset = 22.5f;
		}
		return offset;
	}


	@Override
	public void beginFrame() {
		this.transformModels();
		float partialTicks = AnimationTickHolder.getPartialTicks();

		float yaw = this.blockEntity.getYawOffset(partialTicks);
		Quaternionf qyaw = Axis.YN.rotationDegrees(yaw);
		this.rotatingMount.setRotation(qyaw);
		float pitch = this.blockEntity.getPitchOffset(partialTicks);
		Quaternionf qpitch = Axis.XP.rotationDegrees(-pitch);
		Quaternionf qyaw1 = new Quaternionf(qyaw);
		qyaw1.mul(qpitch);
		this.rotatingMountShaft.setRotation(qyaw1);
	}

	@Override
	public void updateLight() {
		super.updateLight();
		this.relight(this.pos.relative(Direction.UP), this.rotatingMount);
		this.relight(this.pos.relative(Direction.UP, 2), this.rotatingMountShaft);
		this.relight(this.pos, this.pitchShaft);
		this.relight(this.pos, this.yawShaft);
	}

}
