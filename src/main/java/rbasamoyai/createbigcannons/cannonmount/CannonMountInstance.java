package rbasamoyai.createbigcannons.cannonmount;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Constants;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class CannonMountInstance extends KineticTileInstance<CannonMountBlockEntity> implements DynamicInstance {

	private final OrientedData rotatingMount;
	private final OrientedData rotatingMountShaft1;
	private final OrientedData rotatingMountShaft2;
	private final RotatingData pitchShaft;
	private final RotatingData yawShaft;
	
	private final CannonMountBlockEntity cannonMount;
	
	public CannonMountInstance(MaterialManager dispatcher, CannonMountBlockEntity tile) {
		super(dispatcher, tile);
		this.cannonMount = tile;
		
		int blockLight = this.world.getBrightness(LightLayer.BLOCK, this.pos);
		int skyLight = this.world.getBrightness(LightLayer.SKY, this.pos);
		
		BlockState blockState = this.cannonMount.getBlockState();
		Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis pitchAxis = facing.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
		
		Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();
		Instancer<RotatingData> shaftInstance = rotatingMaterial.getModel(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, pitchAxis));
		
		this.rotatingMount = dispatcher.defaultCutout()
				.material(Materials.ORIENTED)
				.getModel(CBCBlockPartials.ROTATING_MOUNT, blockState)
				.createInstance();
		this.rotatingMount.setPosition(this.getInstancePosition().above());
		
		this.rotatingMountShaft1 = dispatcher.defaultCutout()
				.material(Materials.ORIENTED)
				.getModel(AllBlockPartials.SHAFT_HALF, blockState, Direction.EAST)
				.createInstance();
		
		this.rotatingMountShaft1.setPosition(this.getInstancePosition().above(2));
		this.rotatingMountShaft1.nudge(-0.25f, 0.0f, 0.0f);
		
		this.rotatingMountShaft2 = dispatcher.defaultCutout()
				.material(Materials.ORIENTED)
				.getModel(AllBlockPartials.SHAFT_HALF, blockState, Direction.WEST)
				.createInstance();
		this.rotatingMountShaft2.setPosition(this.getInstancePosition().above(2));
		this.rotatingMountShaft2.nudge(0.25f, 0.0f, 0.0f);
				
		
		this.pitchShaft = shaftInstance.createInstance();
		this.pitchShaft
		.setRotationAxis(pitchAxis)
		.setRotationOffset(this.getRotationOffset(pitchAxis))
		.setColor(tile)
		.setPosition(this.getInstancePosition())
		.setBlockLight(blockLight)
		.setSkyLight(skyLight);
		
		this.yawShaft = rotatingMaterial.getModel(CBCBlockPartials.YAW_SHAFT, blockState, Direction.DOWN).createInstance();
		this.yawShaft
		.setRotationAxis(Direction.Axis.Y)
		.setRotationOffset(this.getRotationOffset(Direction.Axis.Y))
		.setColor(tile)
		.setPosition(this.getInstancePosition())
		.setBlockLight(blockLight)
		.setSkyLight(skyLight);
		
		this.transformModels();
	}

	@Override
	public void remove() {
		this.rotatingMount.delete();
		this.rotatingMountShaft1.delete();
		this.rotatingMountShaft2.delete();
		this.pitchShaft.delete();
		this.yawShaft.delete();
	}
	
	private void transformModels() {
		BlockState blockState = this.cannonMount.getBlockState();
		Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis pitchAxis = facing.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
		
		this.updateRotation(this.pitchShaft, pitchAxis, this.getTileSpeed());
		this.updateRotation(this.yawShaft, Direction.Axis.Y, this.cannonMount.getYawSpeed());
	}

	@Override
	public void beginFrame() {
		this.transformModels();
		float partialTicks = AnimationTickHolder.getPartialTicks();
		
		float yaw = this.cannonMount.getYawOffset(partialTicks);
		Quaternion qyaw = Vector3f.YN.rotationDegrees(yaw);
		this.rotatingMount.setRotation(qyaw);
		float pitch = this.cannonMount.getPitchOffset(partialTicks);
		Direction facing = this.cannonMount.getContraptionDirection();
		boolean flag = (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (facing.getAxis() == Direction.Axis.X);
		Quaternion qpitch = Vector3f.XP.rotationDegrees(flag ? -pitch : pitch);
		Quaternion qyaw1 = qyaw.copy();
		qyaw1.mul(qpitch);
		this.rotatingMountShaft1.setRotation(qyaw1);
		this.rotatingMountShaft2.setRotation(qyaw1);
		
		BlockPos hub = this.getInstancePosition().above(2);
		
		float nx = Mth.cos(Constants.DEG_TO_RAD * yaw);
		float nz = Mth.sin(Constants.DEG_TO_RAD * yaw);
		this.rotatingMountShaft1.setPosition(hub);
		this.rotatingMountShaft1.nudge(nx * 0.25f, 0.0f, nz * 0.25f);
		
		this.rotatingMountShaft2.setPosition(hub);
		this.rotatingMountShaft2.nudge(nx * -0.25f, 0.0f, nz * -0.25f);
	}
	
	@Override
	public void updateLight() {
		super.updateLight();
		this.relight(this.pos, this.rotatingMount);
		this.relight(this.pos, this.rotatingMountShaft1);
		this.relight(this.pos, this.rotatingMountShaft2);
		this.relight(this.pos, this.pitchShaft);
		this.relight(this.pos, this.yawShaft);
	}

}
