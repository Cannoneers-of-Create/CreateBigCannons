package rbasamoyai.createbigcannons.forge.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class OnCannonBreakBlockImpl extends Event implements OnCannonBreakBlock {
    private BlockPos blockPos;
    private BlockState blockState;
    private ResourceLocation resourceLocation;
    private BlockEntity blockEntity;

    public OnCannonBreakBlockImpl(BlockPos blockPos, BlockState blockState, ResourceLocation resourceLocation, BlockEntity blockEntity) {
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.resourceLocation = resourceLocation;
        this.blockEntity = blockEntity;
    }

    public BlockPos getAffectedBlockPos() {
        return this.blockPos;
    }

    public BlockState getAffectedBlockState() {
        return this.blockState;
    }

    public BlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
}
