package rbasamoyai.createbigcannons.index;

import java.util.function.Function;

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

	public static final VertexFormat CANNON_SMOKE_PARTICLE_INPUT = VertexFormat.builder()
			.add("Position", VertexFormatElement.POSITION)
			.add("UV0", VertexFormatElement.UV0)
			.add("UV1", VertexFormatElement.UV1)
			.add("Color", VertexFormatElement.COLOR)
			.add("UV2", VertexFormatElement.UV2).build();

	public static final VertexFormat SPLINTER_PARTICLE_INPUT = VertexFormat.builder()
			.add("Position", VertexFormatElement.POSITION)
			.add("UV0", CBCVertexFormatElements.BLOCK_UV.element)
			.add("UV3", VertexFormatElement.UV0)
			.add("Color", VertexFormatElement.COLOR)
			.add("UV2", VertexFormatElement.UV2)
			.add("Normal", VertexFormatElement.NORMAL).build();

}
