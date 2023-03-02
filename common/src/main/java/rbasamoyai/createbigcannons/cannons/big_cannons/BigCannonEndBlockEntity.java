package rbasamoyai.createbigcannons.cannons.big_cannons;

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
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;

import java.util.List;

public class BigCannonEndBlockEntity extends SmartTileEntity implements IBigCannonBlockEntity, WandActionable {

	private BigCannonBehavior cannonBehavior;
	
	public BigCannonEndBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		behaviours.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}
	
	@Override public boolean canLoadBlock(StructureBlockInfo blockInfo) { return false; }

	@Override public BigCannonBehavior cannonBehavior() { return this.cannonBehavior; }
	
	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		BlockState state = this.getBlockState();
		DrillBoringBlockRecipe recipe = CannonDrillBlockEntity.getBlockRecipe(state);
		if (recipe == null) return InteractionResult.PASS;
		if (!this.level.isClientSide) {
			CompoundTag loadTag = this.saveWithFullMetadata();
			loadTag.putBoolean("JustBored", true);
			BlockState boredState = recipe.getResultState(state);
			this.setRemoved();
			this.level.setBlock(this.worldPosition, boredState, 11);
			BlockEntity newBE = this.level.getBlockEntity(this.worldPosition);
			if (newBE != null) newBE.load(loadTag);
		}
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

}
