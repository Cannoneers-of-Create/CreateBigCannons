package rbasamoyai.createbigcannons.forge.mixin.client;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @WrapMethod(method = "getOverlayBlock")
    private static @Nullable Pair<BlockState, BlockPos> createbigcannons$getViewBlockingState(Player player, Operation<Pair<BlockState, BlockPos>> original) {
        if (player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null)
            return null;
        return original.call(player);
    }

}
