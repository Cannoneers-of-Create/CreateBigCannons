package rbasamoyai.createbigcannons.cannonmount;

import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class YawControllerInstance extends KineticTileInstance<YawControllerBlockEntity> implements DynamicInstance {
	
	private final RotatingData inputShaft;
	private final RotatingData outputShaft;
	
	public YawControllerInstance(MaterialManager dispatcher, YawControllerBlockEntity tile) {
		super(dispatcher, tile);
		
		int blockLight = this.world.getBrightness(LightLayer.BLOCK, this.pos);
		int skyLight = this.world.getBrightness(LightLayer.SKY, this.pos);
		
		Material<RotatingData> rotatingMaterial = this.getRotatingMaterial();
		BlockState blockState = tile.getBlockState();
		
		this.inputShaft = rotatingMaterial.getModel(AllBlockPartials.SHAFT_HALF, blockState, Direction.DOWN).createInstance();
		this.inputShaft
		.setRotationAxis(Axis.Y)
		.setRotationOffset(this.getRotationOffset(Axis.Y))
		.setColor(tile)
		.setPosition(this.getInstancePosition())
		.setBlockLight(blockLight)
		.setSkyLight(skyLight);
		
		this.outputShaft = rotatingMaterial.getModel(CBCBlockPartials.YAW_SHAFT, blockState, Direction.UP).createInstance();
		this.outputShaft
		.setRotationAxis(Axis.Y)
		.setRotationOffset(this.getRotationOffset(Axis.Y))
		.setColor(tile)
		.setPosition(this.getInstancePosition())
		.setBlockLight(blockLight)
		.setSkyLight(skyLight);
		
		this.transformModels();
	}
	
	private void transformModels() {
		this.updateRotation(this.inputShaft, Direction.Axis.Y, this.getTileSpeed());
		this.updateRotation(this.outputShaft, Direction.Axis.Y, this.getTileSpeed());
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
