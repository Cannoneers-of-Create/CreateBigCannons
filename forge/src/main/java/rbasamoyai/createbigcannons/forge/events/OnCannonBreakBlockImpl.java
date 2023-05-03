package rbasamoyai.createbigcannons.forge.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class OnCannonBreakBlockImpl extends Event implements OnCannonBreakBlock {
    private BlockPos blockPos;
    private BlockState blockState;
    private ResourceLocation resourceLocation;

    public OnCannonBreakBlockImpl(BlockPos blockPos, BlockState blockState, ResourceLocation resourceLocation) {
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.resourceLocation = resourceLocation;
    }

    public BlockPos getAffectedBlockPos() {
        return this.blockPos;
    }

    public BlockState getAffectedBlockState() {
        return this.blockState;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }
}
