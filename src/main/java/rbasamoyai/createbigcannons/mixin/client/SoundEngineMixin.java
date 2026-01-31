package rbasamoyai.createbigcannons.mixin.client;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.audio.Channel;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import rbasamoyai.createbigcannons.remix.EffectsRemix;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

	@ModifyArg(method = "play",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/ChannelAccess$ChannelHandle;execute(Ljava/util/function/Consumer;)V"))
	private Consumer<Channel> createbigcannons$play(Consumer<Channel> soundConsumer, @Local(argsOnly = true) SoundInstance sound) {
		return soundConsumer.andThen(source -> {
			EffectsRemix.applyCustomSoundInstanceEffects(sound, ((Blaze3DAudioChannelAccessor) source).getSource());
		});
	}

}
