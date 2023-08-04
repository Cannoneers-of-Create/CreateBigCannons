package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

public class AutocannonBreechInstance extends BlockEntityInstance<AbstractAutocannonBreechBlockEntity> implements DynamicInstance {

    private OrientedData ejector;
	private OrientedData seat;
	private OrientedData ammoContainer;
    //private final OrientedData shell;

    private Direction facing;
	private boolean isFilled = false;

    public AutocannonBreechInstance(MaterialManager manager, AbstractAutocannonBreechBlockEntity blockEntity) {
        super(manager, blockEntity);
    }

    @Override
    public void init() {
        super.init();

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        Quaternionf q = Vector3f.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);

        this.ejector = this.materialManager.defaultCutout()
                .material(Materials.ORIENTED)
                .getModel(this.getPartialModelForState(), this.blockState, this.facing)
                .createInstance();
        this.ejector.setRotation(q);

        this.seat = this.materialManager.defaultCutout()
                .material(Materials.ORIENTED)
                .getModel(CBCBlockPartials.autocannonSeatFor(this.blockEntity.getSeatColor()), this.blockState, this.facing)
                .createInstance();
        this.seat.setRotation(q).setPosition(this.getInstancePosition());

		this.ammoContainer = this.materialManager.defaultCutout()
			.material(Materials.ORIENTED)
			.getModel(this.getAmmoContainerModel(), this.blockState, this.facing)
			.createInstance();
		boolean flag = this.facing.getAxis().isVertical();
		Quaternionf q1;
		if (flag) {
			q1 = Vector3f.ZP.rotationDegrees(180);
			q1.mul(Vector3f.YP.rotationDegrees(180));
		} else {
			q1 = Vector3f.YP.rotationDegrees(180);
		}
		Direction offset = flag
			? this.facing.getCounterClockWise(Direction.Axis.Z)
			: this.facing.getClockWise(Direction.Axis.Y);
		Vector3f normal = this.facing == Direction.UP ? offset.getOpposite().step() : offset.step();
		normal.mul(10 / 16f);
		this.ammoContainer.setRotation(q1).setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z());
		this.isFilled = this.isFilled();

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        if (this.blockState.getValue(AutocannonBreechBlock.HANDLE)) {
            this.ejector.setColor((byte) 255, (byte) 255, (byte) 255, (byte) 0);
            this.seat.setColor((byte) 255, (byte) 255, (byte) 255, (byte) (this.blockEntity.getSeatColor() == null ? 0 : 255));
        } else {
            this.seat.setColor((byte) 255, (byte) 255, (byte) 255, (byte) 0);

            float offset = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks()) * 0.5f;
            Vector3f normal = this.facing.getOpposite().step();
            normal.mul(offset);
            this.ejector.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z()).setColor((byte) 255, (byte) 255, (byte) 255, (byte) 255);
        }

		ItemStack container = this.blockEntity.getMagazine();
		this.ammoContainer.setColor((byte) 255, (byte) 255, (byte) 255, (byte)(container.getItem() instanceof AutocannonAmmoContainerItem ? 255 : 0));
		if (this.isFilled != this.isFilled()) {
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

	private PartialModel getAmmoContainerModel() {
		ItemStack container = this.blockEntity.getMagazine();
		return container.getItem() instanceof AutocannonAmmoContainerItem && AutocannonAmmoContainerItem.getTotalAmmoCount(container) > 0
			? CBCBlockPartials.AUTOCANNON_AMMO_CONTAINER_FILLED
			: CBCBlockPartials.AUTOCANNON_AMMO_CONTAINER_EMPTY;
	}

	private boolean isFilled() { return AutocannonAmmoContainerItem.getTotalAmmoCount(this.blockEntity.getMagazine()) > 0; }

}
