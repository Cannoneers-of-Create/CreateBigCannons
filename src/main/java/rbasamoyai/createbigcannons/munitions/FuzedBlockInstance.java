package rbasamoyai.createbigcannons.munitions;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class FuzedBlockInstance extends BlockEntityInstance<FuzedBlockEntity> implements DynamicInstance {

	private final OrientedData fuze;
	private final Direction facing;
	
	public FuzedBlockInstance(MaterialManager materialManager, FuzedBlockEntity blockEntity) {
		super(materialManager, blockEntity);
		
		this.facing = this.blockState.getValue(BlockStateProperties.FACING);		
		this.fuze = materialManager.defaultCutout()
				.material(Materials.ORIENTED)
				.getModel(CBCBlockPartials.FUZE, this.blockState, this.facing)
				.createInstance();
		this.fuze.setPosition(this.instancePos);
	}

	@Override public BlockPos getWorldPosition() { return this.blockEntity.getBlockPos(); }
	
	@Override
	public void beginFrame() {
		boolean hasFuze = this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.facing)
				.map(h -> !h.getStackInSlot(0).isEmpty())
				.orElse(false);
		this.fuze.setColor((byte) 255, (byte) 255, (byte) 255, hasFuze ? (byte) 255 : (byte) 0);
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

}
