package rbasamoyai.createbigcannons.munitions.big_cannon;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

import java.util.function.Consumer;

public class FuzedBlockVisual extends AbstractBlockEntityVisual<FuzedBlockEntity> implements SimpleDynamicVisual {

	private OrientedInstance fuze;
	private boolean oldBaseFuze;

	public FuzedBlockVisual(VisualizationContext ctx, FuzedBlockEntity blockEntity, float partialTick) {
		super(ctx, blockEntity, partialTick);

        Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
        this.oldBaseFuze = this.isBaseFuze();
        if (this.oldBaseFuze) facing = facing.getOpposite();
        this.fuze = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCBlockPartials.FUZE)).createInstance();
        this.fuze.position(this.getVisualPosition());
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.fuze.color((byte) 255, (byte) 255, (byte) 255, this.blockEntity.hasFuze() ? (byte) 255 : (byte) 0);
		if (this.oldBaseFuze != this.isBaseFuze()) {
			this._delete();
			//this.init(); todo: c6 playtest
			this.updateLight(ctx.partialTick());
		}
	}

	@Override
	public void _delete() {
		this.fuze.delete();
	}

	@Override
	public void updateLight(float partialTick) {
		//this.fuze.updateLight(this.world, this.pos); fixme
	}

	private boolean isBaseFuze() {
		return this.blockState.getBlock() instanceof FuzedProjectileBlock<?, ?> fuzed && fuzed.isBaseFuze();
	}

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(fuze);
    }

}
