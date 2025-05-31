package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class BigCannonLayerBlock extends BigCannonBaseBlock implements IBE<LayeredBigCannonBlockEntity> {

	private final Supplier<CannonCastShape> cannonShape;
    private final MapCodec<? extends DirectionalBlock> codec;

	public BigCannonLayerBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> shape) {
		super(properties, material);
		this.cannonShape = shape;
        this.codec = simpleCodec(this::fromSelf);
	}

    private BigCannonLayerBlock fromSelf(Properties properties) {
        return new BigCannonLayerBlock(properties, this.getCannonMaterial(), this.cannonShape);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    @Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean dropItems) {
		if (level.getBlockEntity(pos) instanceof LayeredBigCannonBlockEntity layered) {
			layered.setBaseMaterial(this.getCannonMaterial());
			layered.setLayer(this.cannonShape.get(), this);
		}
	}

	@Override
	public CannonCastShape getCannonShape() {
		return this.cannonShape.get();
	}

	@Override public BigCannonEnd getDefaultOpeningType() { return BigCannonEnd.OPEN; }

	@Override
	public boolean isComplete(BlockState state) {
		return false;
	}

	@Override
	public Class<LayeredBigCannonBlockEntity> getBlockEntityClass() {
		return LayeredBigCannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends LayeredBigCannonBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.LAYERED_CANNON.get();
	}

}
