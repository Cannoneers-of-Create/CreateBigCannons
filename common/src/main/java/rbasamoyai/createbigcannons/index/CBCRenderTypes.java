package rbasamoyai.createbigcannons.index;

import java.io.IOException;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import rbasamoyai.createbigcannons.CreateBigCannons;

public enum CBCRenderTypes {
	CANNON_SMOKE_PARTICLE(CBCRenderingParts.CANNON_SMOKE_PARTICLE_INPUT, VertexFormat.Mode.QUADS, CBCRenderingParts.CANNON_SMOKE_PARTICLE_STATE),
	SPLINTER_PARTICLE(type -> RenderType.create(type.id.toString(), CBCRenderingParts.SPLINTER_PARTICLE_INPUT, VertexFormat.Mode.QUADS,
		512, false, false, CBCRenderingParts.SPLINTER_PARTICLE_STATE.apply(type)));

	private final ResourceLocation id = CreateBigCannons.resource(this.name().toLowerCase(Locale.ROOT));
	private final RenderType renderType;
	@Nullable private ShaderInstance shaderInstance;

	CBCRenderTypes(Function<CBCRenderTypes, RenderType> renderType) {
		this.renderType = renderType.apply(this);
	}

	CBCRenderTypes(VertexFormat format, VertexFormat.Mode mode, Function<CBCRenderTypes, RenderType.CompositeState> state) {
		this(type -> RenderType.create(type.id.toString(), format, mode, 256, false, false, state.apply(type)));
	}

	public ResourceLocation id() { return this.id; }
	public RenderType renderType() { return this.renderType; }
	@Nullable public ShaderInstance getShaderInstance() { return this.shaderInstance; }
	public void setShaderInstance(ShaderInstance shaderInstance) { this.shaderInstance = shaderInstance; }

	public void setRenderTypeForBuilder(BufferBuilder builder) {
		RenderSystem.setShader(this::getShaderInstance);
		builder.begin(this.renderType.mode(), this.renderType.format());
	}

	public static void registerAllShaders(BiConsumer<ShaderInstance, Consumer<ShaderInstance>> cons,
										  CreateShaderInstance shaderCreator, ResourceManager resourceManager) throws IOException {
		for (CBCRenderTypes renderType : values())
			cons.accept(shaderCreator.create(resourceManager, renderType.id, renderType.renderType().format()), renderType::setShaderInstance);
	}

	public interface CreateShaderInstance {
		ShaderInstance create(ResourceManager manager, ResourceLocation location, VertexFormat format) throws IOException;
	}

}
