package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockPartials;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;

public class CannonCarriageRenderer extends EntityRenderer<CannonCarriageEntity> {

    public CannonCarriageRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CannonCarriageEntity carriage, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light) {
        VertexConsumer vcons = buffers.getBuffer(RenderType.cutoutMipped());

        BlockState state = CBCBlocks.CANNON_CARRIAGE.getDefaultState();

        SuperByteBuffer carriageBuf = CachedBufferer.partial(CBCBlockPartials.CANNON_CARRIAGE, state);
        SuperByteBuffer axleBuf = CachedBufferer.partial(CBCBlockPartials.CANNON_CARRIAGE_AXLE, state);
        SuperByteBuffer wheelBuf = CachedBufferer.partial(CBCBlockPartials.CANNON_CARRIAGE_WHEEL, state);

        float yawRad = Mth.PI - entityYaw * Mth.DEG_TO_RAD;

        stack.pushPose();

        stack.mulPose(Vector3f.YP.rotationDegrees(180 - entityYaw));

        carriageBuf.translate(-0.5, 0, -0.5)
                .renderInto(stack, vcons);

        axleBuf.translate(-0.5, 27 / 32f, -0.5)
                .rotateCentered(Direction.EAST, carriage.getXRot() * Mth.DEG_TO_RAD)
                .renderInto(stack, vcons);

        wheelBuf.translate(-11 / 16f, 0.25, -5 / 32f)
                .rotateX(90)
                .rotateZ(90)
                .renderInto(stack, vcons);

        wheelBuf.translate(-11 / 16f, 0.25, 37 / 32f)
                .rotateX(90)
                .rotateZ(90)
                .renderInto(stack, vcons);

        wheelBuf.translate(11 / 16f, 0.25, -5 / 32f)
                .rotateX(90)
                .rotateZ(-90)
                .renderInto(stack, vcons);

        wheelBuf.translate(11 / 16f, 0.25, 37 / 32f)
                .rotateX(90)
                .rotateZ(-90)
                .renderInto(stack, vcons);

        stack.popPose();

        super.render(carriage, entityYaw, partialTicks, stack, buffers, light);
    }

    @Override public ResourceLocation getTextureLocation(CannonCarriageEntity entity) { return null; }

}
