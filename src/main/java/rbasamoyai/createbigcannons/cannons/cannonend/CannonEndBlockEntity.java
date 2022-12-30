package rbasamoyai.createbigcannons.cannons.cannonend;

import java.util.List;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.crafting.boring.TransformableByBoring;

public class CannonEndBlockEntity extends SmartTileEntity implements ICannonBlockEntity, WandActionable {

	private CannonBehavior cannonBehavior;
	
	public CannonEndBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		behaviours.add(this.cannonBehavior = new CannonBehavior(this, this::canLoadBlock));
	}
	
	@Override public boolean canLoadBlock(StructureBlockInfo blockInfo) { return false; }

	@Override public CannonBehavior cannonBehavior() { return this.cannonBehavior; }
	
	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		if (!(this.getBlockState().getBlock() instanceof TransformableByBoring unbored)) return InteractionResult.PASS;
		if (!this.level.isClientSide) {
			CompoundTag loadTag = this.saveWithFullMetadata();
			loadTag.putBoolean("JustBored", true);
			BlockState boredState = unbored.getBoredBlockState(this.getBlockState());
			this.setRemoved();
			this.level.setBlock(this.worldPosition, boredState, 11);
			BlockEntity newBE = this.level.getBlockEntity(this.worldPosition);
			if (newBE != null) newBE.load(loadTag);
		}
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

}
