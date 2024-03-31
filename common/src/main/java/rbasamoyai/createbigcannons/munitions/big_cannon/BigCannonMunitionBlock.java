package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public interface BigCannonMunitionBlock {

	BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation);
	Direction.Axis getAxis(BlockState state);
	boolean canBeLoaded(BlockState state, Direction.Axis facing);
	StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation);
	ItemStack getExtractedItem(StructureBlockInfo info);

}
