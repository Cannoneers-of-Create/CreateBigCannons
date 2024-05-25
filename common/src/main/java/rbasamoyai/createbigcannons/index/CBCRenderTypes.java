package rbasamoyai.createbigcannons.index;

import java.util.Locale;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import rbasamoyai.createbigcannons.CreateBigCannons;

public enum CBCRenderTypes {
	CANNON_SMOKE_PARTICLE(type -> RenderType.create(type.id, CBCRenderingParts.CANNON_SMOKE_PARTICLE_INPUT, VertexFormat.Mode.QUADS,
		256, false, false, CBCRenderingParts.CANNON_SMOKE_PARTICLE_STATE.apply(type)));

	private final String id = CreateBigCannons.MOD_ID + "_" + this.name().toLowerCase(Locale.ROOT);
	private final RenderType renderType;
	@Nullable private ShaderInstance shaderInstance;

	CBCRenderTypes(Function<CBCRenderTypes, RenderType> renderType) {
		this.renderType = renderType.apply(this);
	}

	public String id() { return this.id; }
	public RenderType renderType() { return this.renderType; }
	@Nullable public ShaderInstance getShaderInstance() { return this.shaderInstance; }
	public void setShaderInstance(ShaderInstance shaderInstance) { this.shaderInstance = shaderInstance; }

}
