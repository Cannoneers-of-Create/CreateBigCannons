package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.remix.CBCExplodableBlock;

public interface BigCannonMunitionBlock extends CBCExplodableBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final BooleanProperty DAMP = BooleanProperty.create("damp");

	BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation);
	Direction.Axis getAxis(BlockState state);
	boolean canBeLoaded(BlockState state, Direction.Axis facing);
	StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation);
	ItemStack getExtractedItem(StructureBlockInfo info);

	/**
	 * Only use for blocks that have the waterlogged and damp properties
	 */
	static boolean canDry(BlockState state) { return !state.getValue(WATERLOGGED) && state.getValue(DAMP); }

	/**
	 * Only use for blocks that have the waterlogged and damp properties
	 */
	static boolean doesntIgnite(BlockState state) {
		return state.getValue(WATERLOGGED) || state.getValue(DAMP) || !CBCConfigs.SERVER.munitions.munitionBlocksCanExplode.get();
	}

}
