package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class FuzedBlockInstance extends BlockEntityInstance<FuzedBlockEntity> implements DynamicInstance {

	private OrientedData fuze;
	private boolean oldBaseFuze;

	public FuzedBlockInstance(MaterialManager materialManager, FuzedBlockEntity blockEntity) {
		super(materialManager, blockEntity);
	}

	@Override
	public void init() {
		super.init();

		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
		this.oldBaseFuze = this.isBaseFuze();
		if (this.oldBaseFuze) facing = facing.getOpposite();
		this.fuze = this.materialManager.defaultCutout()
				.material(Materials.ORIENTED)
				.getModel(CBCBlockPartials.FUZE, this.blockState, facing)
				.createInstance();
		this.fuze.setPosition(this.instancePos);
	}

	@Override public BlockPos getWorldPosition() { return this.blockEntity.getBlockPos(); }

	@Override
	public void beginFrame() {
		this.fuze.setColor((byte) 255, (byte) 255, (byte) 255, this.blockEntity.hasFuze() ? (byte) 255 : (byte) 0);
		if (this.oldBaseFuze != this.isBaseFuze()) {
			this.remove();
			this.init();
			this.updateLight();
		}
	}

	@Override
	public void remove() {
		this.fuze.delete();
	}

	@Override
	public void updateLight() {
		super.updateLight();
		this.fuze.updateLight(this.world, this.pos);
	}

	private boolean isBaseFuze() {
		return this.blockState.getBlock() instanceof FuzedProjectileBlock<?, ?> fuzed && fuzed.isBaseFuze();
	}

}
