package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Axis;

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

public class AutocannonBreechInstance extends BlockEntityInstance<AbstractAutocannonBreechBlockEntity> implements DynamicInstance {

    private OrientedData ejector;
	private OrientedData seat;
	private OrientedData ammoContainer;
	private DyeColor seatColor;
    //private final OrientedData shell;

    private Direction facing;
	private boolean isFilled = false;
	private Item magazineItem = null;

    public AutocannonBreechInstance(MaterialManager manager, AbstractAutocannonBreechBlockEntity blockEntity) {
        super(manager, blockEntity);
    }

    @Override
    public void init() {
        super.init();

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        Quaternionf q = Axis.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);

        this.ejector = this.materialManager.defaultCutout()
                .material(Materials.ORIENTED)
                .getModel(this.getPartialModelForState(), this.blockState, this.facing)
                .createInstance();
        this.ejector.setRotation(q);

		this.seatColor = this.blockEntity.getSeatColor();

        this.seat = this.materialManager.defaultCutout()
                .material(Materials.ORIENTED)
                .getModel(CBCBlockPartials.autocannonSeatFor(this.seatColor), this.blockState, this.facing)
                .createInstance();
        this.seat.setRotation(q).setPosition(this.getInstancePosition());

		this.ammoContainer = this.materialManager.defaultCutout()
			.material(Materials.ORIENTED)
			.getModel(this.getAmmoContainerModel())
			.createInstance();
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
		this.ammoContainer.setRotation(q1).setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z());
		this.isFilled = this.isFilled();
		this.magazineItem = this.getMagazineItem();

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        if (this.blockState.getValue(AutocannonBreechBlock.HANDLE)) {
            this.ejector.setColor((byte) 255, (byte) 255, (byte) 255, (byte) 0);
            this.seat.setColor((byte) 255, (byte) 255, (byte) 255, (byte) (this.seatColor == null ? 0 : 255));
        } else {
            this.seat.setColor((byte) 255, (byte) 255, (byte) 255, (byte) 0);

            float offset = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks()) * 0.5f;
            Vector3f normal = this.facing.getOpposite().step();
            normal.mul(offset);
            this.ejector.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z()).setColor((byte) 255, (byte) 255, (byte) 255, (byte) 255);
        }

		ItemStack container = this.blockEntity.getMagazine();
		this.ammoContainer.setColor((byte) 255, (byte) 255, (byte) 255, (byte)(container.getItem() instanceof AutocannonAmmoContainerItem ? 255 : 0));
		if (this.isFilled != this.isFilled() || this.magazineItem != this.getMagazineItem() || this.seatColor != this.blockEntity.getSeatColor()) {
			this.remove();
			this.init();
			this.updateLight();
		}
    }

    @Override
    public void updateLight() {
        super.updateLight();
        this.relight(this.pos, this.ejector);
        this.relight(this.pos, this.seat);
		this.relight(this.pos, this.ammoContainer);
    }

    @Override
    protected void remove() {
		this.ejector.delete();
		this.seat.delete();
		this.ammoContainer.delete();
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

	private boolean isFilled() { return AutocannonAmmoContainerItem.getTotalAmmoCount(this.blockEntity.getMagazine()) > 0; }

	private Item getMagazineItem() {
		ItemStack stack = this.blockEntity.getMagazine();
		return stack == null || stack.isEmpty() ? null : stack.getItem();
	}

}
