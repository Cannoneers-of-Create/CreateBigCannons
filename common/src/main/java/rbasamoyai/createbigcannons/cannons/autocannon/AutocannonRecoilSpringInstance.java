package rbasamoyai.createbigcannons.cannons.autocannon;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

import java.util.HashMap;
import java.util.Map;

public class AutocannonRecoilSpringInstance extends BlockEntityInstance<AutocannonRecoilSpringBlockEntity> implements DynamicInstance {

    private final ModelData spring;
    private final Map<BlockPos, OrientedData> blocks = new HashMap<>();

    private final Direction facing;

    public AutocannonRecoilSpringInstance(MaterialManager manager, AutocannonRecoilSpringBlockEntity blockEntity) {
        super(manager, blockEntity);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.spring = manager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(this.getPartialModelForState(), this.blockState, this.facing)
                .createInstance();

        for (Map.Entry<BlockPos, BlockState> entry : this.blockEntity.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            this.blocks.put(entry.getKey(), manager.defaultCutout()
                    .material(Materials.ORIENTED)
                    .getModel(entry.getValue())
                    .createInstance());
        }

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        boolean flag = this.facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
        BlockPos pos = this.instancePos.relative(this.facing.getOpposite(), flag ? 1 : 0);
        Vec3 pivot = Vec3.atLowerCornerOf(pos);
        float scale = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks());
        float f1 = scale * 0.5f + 0.5f;
        Direction.Axis axis = this.facing.getAxis();

        float fx = axis == Direction.Axis.X ? f1 : 1;
        float fy = axis == Direction.Axis.Y ? f1 : 1;
        float fz = axis == Direction.Axis.Z ? f1 : 1;

        this.spring.loadIdentity().translate(pivot);
        if (flag) {
            this.spring.centre()
                    .rotate(axis.isVertical() ? Direction.EAST : Direction.UP, Mth.PI)
                    .unCentre()
                    .translate(this.facing.getOpposite().step());
        }
        this.spring.scale(fx, fy, fz);

        Vector3f offs = this.facing.step();
        offs.mul((1 - scale) * -0.5f);
        offs.add(this.instancePos.getX(), this.instancePos.getY(), this.instancePos.getZ());

        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks.entrySet()) {
            BlockPos pos1 = entry.getKey();
            entry.getValue().setPosition(offs).nudge(pos1.getX(), pos1.getY(), pos1.getZ());
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, this.spring);
        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
    }

    @Override
    protected void remove() {
        this.spring.delete();
    }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof AutocannonBlock cBlock
                ? CBCBlockPartials.autocannonSpringFor(cBlock.getAutocannonMaterial())
                : CBCBlockPartials.CAST_IRON_AUTOCANNON_SPRING;
    }

}
