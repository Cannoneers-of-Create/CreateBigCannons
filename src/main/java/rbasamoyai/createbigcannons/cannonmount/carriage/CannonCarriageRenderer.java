package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockPartials;
import rbasamoyai.createbigcannons.CBCBlocks;

public class CannonCarriageRenderer extends EntityRenderer<CannonCarriageEntity> {

    public CannonCarriageRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CannonCarriageEntity carriage, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffers, int light) {
        VertexConsumer vcons = buffers.getBuffer(RenderType.cutoutMipped());

        boolean cannonRider = carriage.isCannonRider();
        BlockState state = CBCBlocks.CANNON_CARRIAGE.getDefaultState().setValue(CannonCarriageBlock.SADDLED, cannonRider);

        SuperByteBuffer carriageBuf = CachedBufferer.partial(cannonRider ? CBCBlockPartials.CANNON_CARRIAGE_SADDLE : CBCBlockPartials.CANNON_CARRIAGE, state);
        SuperByteBuffer axleBuf = CachedBufferer.partial(CBCBlockPartials.CANNON_CARRIAGE_AXLE, state);
        SuperByteBuffer wheelBuf = CachedBufferer.partial(CBCBlockPartials.CANNON_CARRIAGE_WHEEL, state);

        float yawRad = Mth.PI - entityYaw * Mth.DEG_TO_RAD;

        stack.pushPose();

        stack.mulPose(Vector3f.YP.rotationDegrees(180 - entityYaw));

        carriageBuf.translate(-0.5, 0, -0.5)
                .light(light)
                .renderInto(stack, vcons);

        axleBuf.translate(-0.5, 27 / 32f, -0.5)
                .rotateCentered(Direction.EAST, carriage.getXRot() * Mth.DEG_TO_RAD)
                .light(light)
                .renderInto(stack, vcons);

        Vector4f oldWheel = carriage.previousWheelState;
        Vector4f newWheel = carriage.getWheelState();

        wheelBuf.translate(-11 / 16f, 0.25, -5 / 32f)
                .rotateX(90)
                .rotateZ(90)
                .rotateY(Mth.lerp(partialTicks, oldWheel.x(), newWheel.x()))
                .light(light)
                .renderInto(stack, vcons);

        wheelBuf.translate(-11 / 16f, 0.25, 37 / 32f)
                .rotateX(90)
                .rotateZ(90)
                .rotateY(Mth.lerp(partialTicks, oldWheel.y(), newWheel.y()))
                .light(light)
                .renderInto(stack, vcons);

        wheelBuf.translate(11 / 16f, 0.25, -5 / 32f)
                .rotateX(90)
                .rotateZ(-90)
                .rotateY(Mth.lerp(partialTicks, oldWheel.z(), newWheel.z()))
                .light(light)
                .renderInto(stack, vcons);

        wheelBuf.translate(11 / 16f, 0.25, 37 / 32f)
                .rotateX(90)
                .rotateZ(-90)
                .rotateY(Mth.lerp(partialTicks, oldWheel.w(), newWheel.w()))
                .light(light)
                .renderInto(stack, vcons);

        stack.popPose();

        super.render(carriage, entityYaw, partialTicks, stack, buffers, light);
    }

    @Override public ResourceLocation getTextureLocation(CannonCarriageEntity entity) { return null; }

}
