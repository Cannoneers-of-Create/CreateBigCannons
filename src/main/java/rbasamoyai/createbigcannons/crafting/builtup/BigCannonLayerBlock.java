package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.function.Supplier;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonEnd;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class BigCannonLayerBlock extends BigCannonBaseBlock implements ITE<LayeredBigCannonBlockEntity> {

	private final Supplier<CannonCastShape> shape;
	
	public BigCannonLayerBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> shape) {
		super(properties, material);
		this.shape = shape;
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean dropItems) {
		if (level.getBlockEntity(pos) instanceof LayeredBigCannonBlockEntity layered) {
			layered.setBaseMaterial(this.getCannonMaterial());
			layered.setLayer(this.shape.get(), this);
		}
	}
	
	@Override public CannonCastShape getCannonShape() { return this.shape.get(); }
	@Override public BigCannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return BigCannonEnd.CLOSED; }
	@Override public boolean isComplete(BlockState state) { return false; }
	
	@Override public Class<LayeredBigCannonBlockEntity> getTileEntityClass() { return LayeredBigCannonBlockEntity.class; }
	@Override public BlockEntityType<? extends LayeredBigCannonBlockEntity> getTileEntityType() { return CBCBlockEntities.LAYERED_CANNON.get(); }

}
