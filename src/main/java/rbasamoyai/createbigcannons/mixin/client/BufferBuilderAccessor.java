package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;

@Mixin(BufferBuilder.class)
public interface BufferBuilderAccessor {

    @Invoker long callBeginElement(VertexFormatElement element);

}
