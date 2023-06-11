package rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class AutocannonRecoilSpringBlock extends AutocannonBaseBlock implements IBE<AutocannonRecoilSpringBlockEntity> {

	public AutocannonRecoilSpringBlock(Properties properties, AutocannonMaterial material) {
		super(properties, material);
	}

	@Override
	public Class<AutocannonRecoilSpringBlockEntity> getBlockEntityClass() {
		return AutocannonRecoilSpringBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AutocannonRecoilSpringBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.AUTOCANNON_RECOIL_SPRING.get();
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.AUTOCANNON_RECOIL_SPRING;
	}

	@Override
	public boolean isBreechMechanism(BlockState state) {
		return false;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AllShapes.SIX_VOXEL_POLE.get(this.getFacing(state).getAxis());
	}

}
