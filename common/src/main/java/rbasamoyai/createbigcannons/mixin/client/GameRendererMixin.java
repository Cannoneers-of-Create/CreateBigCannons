package rbasamoyai.createbigcannons.mixin.client;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.shaders.Program;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method = "reloadShaders",
			at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 53),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void reloadShaders(ResourceProvider resourceProvider, CallbackInfo ci, List<Program> programs,
							   List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderInstances) throws IOException {
		for (CBCRenderTypes renderType : CBCRenderTypes.values())
			shaderInstances.add(Pair.of(new ShaderInstance(resourceProvider, renderType.id(), renderType.renderType().format()),
				renderType::setShaderInstance));
	}

}
