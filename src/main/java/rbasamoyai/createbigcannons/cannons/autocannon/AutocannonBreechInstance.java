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
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class AutocannonBreechInstance extends BlockEntityInstance<AutocannonBreechBlockEntity> implements DynamicInstance {

    private final OrientedData ejector;
    //private final OrientedData shell;

    private final Direction facing;

    public AutocannonBreechInstance(MaterialManager manager, AutocannonBreechBlockEntity blockEntity) {
        super(manager, blockEntity);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.ejector = manager.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(this.getPartialModelForState(), this.blockState, this.facing)
                .createInstance();

        Quaternion q = Vector3f.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);
        this.ejector.setRotation(q);

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        float offset = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks()) * 0.5f;
        Vector3f normal = this.facing.getOpposite().step();
        normal.mul(offset);
        this.ejector.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z());
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, this.ejector);
    }

    @Override
    protected void remove() {
        this.ejector.delete();
    }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof AutocannonBlock cBlock
                ? CBCBlockPartials.autocannonEjectorFor(cBlock.getAutocannonMaterial())
                : CBCBlockPartials.CAST_IRON_AUTOCANNON_EJECTOR;
    }

}
