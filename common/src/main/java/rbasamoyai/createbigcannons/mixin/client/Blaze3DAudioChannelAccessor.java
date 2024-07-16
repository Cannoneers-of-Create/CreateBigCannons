package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.audio.Channel;

@Mixin(Channel.class)
public interface Blaze3DAudioChannelAccessor {

	@Accessor("source") int getSource();

}
