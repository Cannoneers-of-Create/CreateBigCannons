package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Axis;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlock;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

public class AutocannonBreechVisual extends AbstractBlockEntityVisual<AbstractAutocannonBreechBlockEntity> implements SimpleDynamicVisual {

	private final OrientedInstance ejector;
	private final OrientedInstance seat;
	private final OrientedInstance ammoContainer;
	private DyeColor seatColor;
	//private final OrientedData shell;

	private final Direction facing;
	private boolean isFilled = false;
	private Item magazineItem = null;

	public AutocannonBreechVisual(VisualizationContext ctx, AbstractAutocannonBreechBlockEntity blockEntity, float partialTick) {
		super(ctx, blockEntity, partialTick);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        Quaternionf q = Axis.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);
        this.ejector = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPartialModelForState(), this.facing))
            .createInstance()
            .rotation(q);

        this.seatColor = this.blockEntity.getSeatColor();

        this.seat = this.seatInstancer()
            .createInstance()
            .rotation(q)
            .position(this.getVisualPosition());

        this.ammoContainer = this.containerInstancer().createInstance();
        boolean flag = this.facing.getAxis().isVertical();
        Quaternionf q1;
        if (flag) {
            float f = this.facing == Direction.UP ? 90 : -90;
            q1 = Axis.ZP.rotationDegrees(f);
            q1.mul(Axis.XP.rotationDegrees(f));
        } else {
            q1 = Axis.YP.rotationDegrees(-90 - this.facing.toYRot());
        }
        Direction offset = flag
            ? this.facing.getCounterClockWise(Direction.Axis.Z)
            : this.facing.getClockWise(Direction.Axis.Y);
        Vector3f normal = this.facing == Direction.UP ? offset.getOpposite().step() : offset.step();
        normal.mul(10 / 16f);
        this.ammoContainer.rotation(q1).position(this.getVisualPosition()).translatePosition(normal.x(), normal.y(), normal.z());
        this.isFilled = this.isFilled();
        this.magazineItem = this.getMagazineItem();

        this.updateTransforms(partialTick);
	}

    protected void refreshSeat() {
        this.seatColor = this.blockEntity.getSeatColor();
        this.seatInstancer().stealInstance(this.seat);
        this.seat.setVisible(this.blockState.getValue(AutocannonBreechBlock.HANDLE) && this.seatColor != null);
    }

    protected Instancer<OrientedInstance> seatInstancer() {
        return this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCBlockPartials.autocannonSeatFor(this.seatColor), this.facing));
    }

    protected void refreshContainer() {
        this.isFilled = this.isFilled();
        this.magazineItem = this.getMagazineItem();
        this.containerInstancer().stealInstance(this.ammoContainer);
    }

    protected Instancer<OrientedInstance> containerInstancer() {
        return this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(this.getAmmoContainerModel()));
    }

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.updateTransforms(ctx.partialTick());
	}

	private void updateTransforms(float partialTick) {
		if (this.blockState.getValue(AutocannonBreechBlock.HANDLE)) {
			this.ejector.setVisible(false);
			this.seat.setVisible(this.seatColor != null);
		} else {
            this.ejector.setVisible(true);
			this.seat.setVisible(false);

			float offset = this.blockEntity.getAnimateOffset(partialTick) * 0.5f;
			Vector3f normal = this.facing.getOpposite().step();
			normal.mul(offset);
			this.ejector.position(this.getVisualPosition()).translatePosition(normal.x(), normal.y(), normal.z()).color((byte) 255, (byte) 255, (byte) 255, (byte) 255);
		}

		this.ammoContainer.setVisible(this.getMagazineItem() instanceof AutocannonAmmoContainerItem);

        if (this.seatColor != this.blockEntity.getSeatColor())
            this.refreshSeat();
        if (this.isFilled != this.isFilled() || this.magazineItem != this.getMagazineItem())
            this.refreshContainer();

        this.seat.setChanged();
        this.ejector.setChanged();
        this.ammoContainer.setChanged();
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.pos, this.ejector);
		this.relight(this.pos, this.seat);
		this.relight(this.pos, this.ammoContainer);
	}

	@Override
	protected void _delete() {
		this.ejector.delete();
		this.seat.delete();
		this.ammoContainer.delete();
	}

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(ejector);
        consumer.accept(seat);
        consumer.accept(ammoContainer);
    }

	private PartialModel getPartialModelForState() {
		return this.blockState.getBlock() instanceof AutocannonBlock cBlock
			? CBCBlockPartials.autocannonEjectorFor(cBlock.getAutocannonMaterial())
			: CBCBlockPartials.CAST_IRON_AUTOCANNON_EJECTOR;
	}

	private BlockState getAmmoContainerModel() {
		ItemStack item = this.blockEntity.getMagazine();
		if (item == null || item.isEmpty() || !(item.getItem() instanceof AutocannonAmmoContainerItem blockItem))
			return Blocks.AIR.defaultBlockState();
		BlockState state = blockItem.getBlock().defaultBlockState();
		if (state.hasProperty(AutocannonAmmoContainerBlock.CONTAINER_STATE)) {
			state = state.setValue(AutocannonAmmoContainerBlock.CONTAINER_STATE,
				AutocannonAmmoContainerBlock.State.getFromFilled(AutocannonAmmoContainerItem.getTotalAmmoCount(item) > 0));
		}
		return state;
	}

	private boolean isFilled() {
		return AutocannonAmmoContainerItem.getTotalAmmoCount(this.blockEntity.getMagazine()) > 0;
	}

	private Item getMagazineItem() {
		ItemStack stack = this.blockEntity.getMagazine();
		return stack == null || stack.isEmpty() ? null : stack.getItem();
	}

}
