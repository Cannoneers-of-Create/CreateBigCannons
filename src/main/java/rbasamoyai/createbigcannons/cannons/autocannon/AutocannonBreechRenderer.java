package rbasamoyai.createbigcannons.cannons.autocannon;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class AutocannonBreechRenderer extends SmartTileEntityRenderer<AutocannonBreechBlockEntity> {

    public AutocannonBreechRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(AutocannonBreechBlockEntity breech, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(breech, partialTicks, ms, buffer, light, overlay);
        if (Backend.canUseInstancing(breech.getLevel())) return;

        BlockState state = breech.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.FACING);
        SuperByteBuffer ejectorBuf = CachedBufferer.partialFacing(getPartialModelForState(state), state, facing);

        Quaternion q = Vector3f.YP.rotationDegrees(facing.getAxis().isVertical() ? 180 : 0);

        ms.pushPose();

        Vector3f normal = facing.step();
        normal.mul(breech.getAnimateOffset(partialTicks) * -0.5f);
        ejectorBuf.translate(normal)
                //.rotateCentered(q)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));

        ms.popPose();
    }

    private static PartialModel getPartialModelForState(BlockState state) {
        return state.getBlock() instanceof AutocannonBlock cBlock
                ? CBCBlockPartials.autocannonEjectorFor(cBlock.getAutocannonMaterial())
                : CBCBlockPartials.CAST_IRON_AUTOCANNON_EJECTOR;
    }

}
