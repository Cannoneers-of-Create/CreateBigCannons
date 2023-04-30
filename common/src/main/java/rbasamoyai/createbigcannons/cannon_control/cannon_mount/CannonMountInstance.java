package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonMountInstance extends KineticTileInstance<CannonMountBlockEntity> implements DynamicInstance {

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

		Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();
		Instancer<RotatingData> shaftInstance = rotatingMaterial.getModel(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, pitchAxis));

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
				.setColor(this.blockEntity)
				.setPosition(this.getInstancePosition())
				.setBlockLight(blockLight)
				.setSkyLight(skyLight);

		this.yawShaft = rotatingMaterial.getModel(CBCBlockPartials.YAW_SHAFT, blockState, Direction.DOWN).createInstance();
		this.yawShaft
				.setRotationAxis(Direction.Axis.Y)
				.setRotationOffset(this.getRotationOffset(Direction.Axis.Y))
				.setColor(this.blockEntity)
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
		
		this.updateRotation(this.pitchShaft, pitchAxis, this.getTileSpeed());
		this.updateRotation(this.yawShaft, Direction.Axis.Y, this.blockEntity.getYawSpeed());
	}

	@Override
	public void beginFrame() {
		this.transformModels();
		float partialTicks = AnimationTickHolder.getPartialTicks();
		
		float yaw = this.blockEntity.getYawOffset(partialTicks);
		Quaternion qyaw = Vector3f.YN.rotationDegrees(yaw);
		this.rotatingMount.setRotation(qyaw);
		float pitch = this.blockEntity.getPitchOffset(partialTicks);
		Direction facing = this.blockEntity.getContraptionDirection();
		boolean flag = (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (facing.getAxis() == Direction.Axis.X);
		Quaternion qpitch = Vector3f.XP.rotationDegrees(flag ? -pitch : pitch);
		Quaternion qyaw1 = qyaw.copy();
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
