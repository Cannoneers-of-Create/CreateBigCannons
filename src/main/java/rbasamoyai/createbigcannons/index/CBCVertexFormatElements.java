package rbasamoyai.createbigcannons.index;

import com.mojang.blaze3d.vertex.VertexFormatElement;

public enum CBCVertexFormatElements {

    BLOCK_UV(VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.UV, 4);

    public final VertexFormatElement element;

    CBCVertexFormatElements(VertexFormatElement.Type type, VertexFormatElement.Usage usage, int count) {
        this.element = VertexFormatElement.register(VertexFormatElement.findNextId(), 0, type, usage, count);
    }

}
