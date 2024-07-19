package rbasamoyai.createbigcannons.index;

import java.util.Locale;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import rbasamoyai.createbigcannons.CreateBigCannons;

public enum CBCRenderTypes {
	CANNON_SMOKE_PARTICLE(CBCRenderingParts.CANNON_SMOKE_PARTICLE_INPUT, VertexFormat.Mode.QUADS, CBCRenderingParts.CANNON_SMOKE_PARTICLE_STATE),
	SPLINTER_PARTICLE(type -> RenderType.create(type.id, CBCRenderingParts.SPLINTER_PARTICLE_INPUT, VertexFormat.Mode.QUADS,
		512, false, false, CBCRenderingParts.SPLINTER_PARTICLE_STATE.apply(type)));

	private final String id = CreateBigCannons.MOD_ID + "_" + this.name().toLowerCase(Locale.ROOT);
	private final RenderType renderType;
	@Nullable private ShaderInstance shaderInstance;

	CBCRenderTypes(Function<CBCRenderTypes, RenderType> renderType) {
		this.renderType = renderType.apply(this);
	}

	CBCRenderTypes(VertexFormat format, VertexFormat.Mode mode, Function<CBCRenderTypes, RenderType.CompositeState> state) {
		this(type -> RenderType.create(type.id, format, mode, 256, false, false, state.apply(type)));
	}

	public String id() { return this.id; }
	public RenderType renderType() { return this.renderType; }
	@Nullable public ShaderInstance getShaderInstance() { return this.shaderInstance; }
	public void setShaderInstance(ShaderInstance shaderInstance) { this.shaderInstance = shaderInstance; }

	public void setRenderTypeForBuilder(BufferBuilder builder) {
		RenderSystem.setShader(this::getShaderInstance);
		builder.begin(this.renderType.mode(), this.renderType.format());
	}

}
