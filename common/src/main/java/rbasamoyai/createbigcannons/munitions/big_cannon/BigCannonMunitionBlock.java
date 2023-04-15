package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public interface BigCannonMunitionBlock {

	BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation);
	boolean canBeLoaded(BlockState state, Direction.Axis facing);
	StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation);
	default ItemStack getExtractedItem(StructureBlockInfo info) {
		if (!(this instanceof ItemLike item)) return ItemStack.EMPTY;
		ItemStack stack = new ItemStack(item);
		if (info.nbt != null && !info.nbt.isEmpty()) {
			stack.getOrCreateTag().put("BlockEntityTag", info.nbt);
		}
		return stack;
	}

}
