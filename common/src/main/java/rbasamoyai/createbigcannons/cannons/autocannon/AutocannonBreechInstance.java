package rbasamoyai.createbigcannons.cannons.autocannon;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class AutocannonBreechInstance extends BlockEntityInstance<AbstractAutocannonBreechBlockEntity> implements DynamicInstance {

    private final OrientedData ejector;
    private final OrientedData seat;
    //private final OrientedData shell;

    private final Direction facing;

    public AutocannonBreechInstance(MaterialManager manager, AbstractAutocannonBreechBlockEntity blockEntity) {
        super(manager, blockEntity);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        Quaternion q = Vector3f.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);

        this.ejector = manager.defaultCutout()
                .material(Materials.ORIENTED)
                .getModel(this.getPartialModelForState(), this.blockState, this.facing)
                .createInstance();
        this.ejector.setRotation(q);

        this.seat = manager.defaultCutout()
                .material(Materials.ORIENTED)
                .getModel(CBCBlockPartials.autocannonSeatFor(blockEntity.getSeatColor()), this.blockState, this.facing)
                .createInstance();
        this.seat.setRotation(q);

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        if (this.blockState.getValue(AutocannonBreechBlock.HANDLE)) {
            this.ejector.setColor((byte) 255, (byte) 255, (byte) 255, (byte) 0);

            this.seat.setPosition(this.getInstancePosition())
                    .setColor((byte) 255, (byte) 255, (byte) 255, (byte) (this.blockEntity.getSeatColor() == null ? 0 : 255));
        } else {
            this.seat.setColor((byte) 255, (byte) 255, (byte) 255, (byte) 0);

            float offset = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks()) * 0.5f;
            Vector3f normal = this.facing.getOpposite().step();
            normal.mul(offset);
            this.ejector.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z()).setColor((byte) 255, (byte) 255, (byte) 255, (byte) 255);
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, this.ejector);
        this.relight(this.pos, this.seat);
    }

    @Override
    protected void remove() {
        this.ejector.delete();
        this.seat.delete();
    }

    @Override public boolean shouldReset() { return super.shouldReset() || this.blockEntity.shouldUpdateInstance(); }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof AutocannonBlock cBlock
                ? CBCBlockPartials.autocannonEjectorFor(cBlock.getAutocannonMaterial())
                : CBCBlockPartials.CAST_IRON_AUTOCANNON_EJECTOR;
    }

}
