package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.function.Supplier;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonLayerBlock extends CannonBaseBlock implements ITE<LayeredCannonBlockEntity> {

	private final Supplier<CannonCastShape> shape;
	
	public CannonLayerBlock(Properties properties, CannonMaterial material, Supplier<CannonCastShape> shape) {
		super(properties, material);
		this.shape = shape;
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean dropItems) {
		if (level.getBlockEntity(pos) instanceof LayeredCannonBlockEntity layered) {
			layered.setBaseMaterial(this.getCannonMaterial());
			layered.setLayer(this.shape.get(), this);
		}
	}
	
	@Override public CannonCastShape getCannonShape() { return this.shape.get(); }
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }
	@Override public boolean isComplete(BlockState state) { return false; }
	
	@Override public Class<LayeredCannonBlockEntity> getTileEntityClass() { return LayeredCannonBlockEntity.class; }
	@Override public BlockEntityType<? extends LayeredCannonBlockEntity> getTileEntityType() { return CBCBlockEntities.LAYERED_CANNON.get(); }

}
