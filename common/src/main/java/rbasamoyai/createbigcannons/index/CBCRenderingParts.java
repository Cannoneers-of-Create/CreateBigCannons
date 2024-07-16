package rbasamoyai.createbigcannons.index;

import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import net.minecraft.client.renderer.RenderType;

public class CBCRenderingParts extends RenderType {

	private CBCRenderingParts(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize,
							  boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	public static final Function<CBCRenderTypes, CompositeState> CANNON_SMOKE_PARTICLE_STATE = type -> CompositeState.builder()
		.setShaderState(new ShaderStateShard(type::getShaderInstance))
		.setLightmapState(LIGHTMAP)
		.createCompositeState(false);

	public static final Function<CBCRenderTypes, CompositeState> COLOR_STATE = type -> CompositeState.builder()
		.setShaderState(new ShaderStateShard(type::getShaderInstance))
		.setLightmapState(LIGHTMAP)
		.setTextureState(NO_TEXTURE)
		.createCompositeState(false);

	public static final Function<CBCRenderTypes, CompositeState> SPLINTER_PARTICLE_STATE = type -> CompositeState.builder()
		.setShaderState(new ShaderStateShard(type::getShaderInstance))
		.setLightmapState(LIGHTMAP)
		.setCullState(CULL)
		.createCompositeState(false);

	public static final VertexFormat CANNON_SMOKE_PARTICLE_INPUT = new VertexFormat(
		ImmutableMap.<String, VertexFormatElement>builder()
			.put("Position", DefaultVertexFormat.ELEMENT_POSITION)
			.put("UV0", DefaultVertexFormat.ELEMENT_UV0)
			.put("UV1", DefaultVertexFormat.ELEMENT_UV1)
			.put("Color", DefaultVertexFormat.ELEMENT_COLOR)
			.put("UV2", DefaultVertexFormat.ELEMENT_UV2).build()
	);

	public static final VertexFormat SPLINTER_PARTICLE_INPUT = new VertexFormat(
		ImmutableMap.<String, VertexFormatElement>builder()
			.put("Position", DefaultVertexFormat.ELEMENT_POSITION)
			.put("UV0", DefaultVertexFormat.ELEMENT_UV0)
			.put("UV01", DefaultVertexFormat.ELEMENT_UV0)
			.put("UV3", DefaultVertexFormat.ELEMENT_UV0)
			.put("Color", DefaultVertexFormat.ELEMENT_COLOR)
			.put("UV2", DefaultVertexFormat.ELEMENT_UV2)
			.put("Normal", DefaultVertexFormat.ELEMENT_NORMAL).build()
	);

}
