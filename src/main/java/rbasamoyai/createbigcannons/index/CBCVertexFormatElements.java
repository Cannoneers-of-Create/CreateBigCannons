package rbasamoyai.createbigcannons.index;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public enum CBCVertexFormatElements {

    BLOCK_UV0(VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2),
    BLOCK_UV1(VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 2);

    public final VertexFormatElement element;

    CBCVertexFormatElements(VertexFormatElement.Type type, VertexFormatElement.Usage usage, int count) {
        this.element = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, type, usage, count);
    }

}
