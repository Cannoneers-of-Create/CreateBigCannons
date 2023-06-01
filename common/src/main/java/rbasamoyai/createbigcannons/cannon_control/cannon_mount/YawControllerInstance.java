package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class YawControllerInstance extends KineticBlockEntityInstance<YawControllerBlockEntity> implements DynamicInstance {

	private RotatingData inputShaft;
	private RotatingData outputShaft;

	public YawControllerInstance(MaterialManager dispatcher, YawControllerBlockEntity tile) {
		super(dispatcher, tile);
	}

	@Override
	public void init() {
		super.init();

		int blockLight = this.world.getBrightness(LightLayer.BLOCK, this.pos);
		int skyLight = this.world.getBrightness(LightLayer.SKY, this.pos);

		Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();
		BlockState blockState = this.blockEntity.getBlockState();

		this.inputShaft = rotatingMaterial.getModel(AllPartialModels.SHAFT_HALF, blockState, Direction.DOWN).createInstance();
		this.inputShaft
			.setRotationAxis(Axis.Y)
			.setRotationOffset(this.getRotationOffset(Axis.Y))
			.setColor(this.blockEntity)
			.setPosition(this.getInstancePosition())
			.setBlockLight(blockLight)
			.setSkyLight(skyLight);

		this.outputShaft = rotatingMaterial.getModel(CBCBlockPartials.YAW_SHAFT, blockState, Direction.UP).createInstance();
		this.outputShaft
			.setRotationAxis(Axis.Y)
			.setRotationOffset(this.getRotationOffset(Axis.Y))
			.setColor(this.blockEntity)
			.setPosition(this.getInstancePosition())
			.setBlockLight(blockLight)
			.setSkyLight(skyLight);

		this.transformModels();
	}

	private void transformModels() {
		this.updateRotation(this.inputShaft, Axis.Y, this.getBlockEntitySpeed());
		this.updateRotation(this.outputShaft, Axis.Y, this.getBlockEntitySpeed());
	}

	@Override
	public void remove() {
		this.inputShaft.delete();
		this.outputShaft.delete();
	}

	@Override
	public void beginFrame() {
		this.transformModels();
	}

}
