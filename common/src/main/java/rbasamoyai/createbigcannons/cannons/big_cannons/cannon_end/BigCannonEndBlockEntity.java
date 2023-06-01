package rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.WandActionable;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;

public class BigCannonEndBlockEntity extends SmartBlockEntity implements IBigCannonBlockEntity, WandActionable {

	private BigCannonBehavior cannonBehavior;

	public BigCannonEndBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}

	@Override
	public boolean canLoadBlock(StructureBlockInfo blockInfo) {
		return false;
	}

	@Override
	public BigCannonBehavior cannonBehavior() {
		return this.cannonBehavior;
	}

	@Override
	public InteractionResult onWandUsed(UseOnContext context) {
		BlockState state = this.getBlockState();
		Direction dir = state.getValue(BlockStateProperties.FACING);
		DrillBoringBlockRecipe recipe = AbstractCannonDrillBlockEntity.getBlockRecipe(state, dir);
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
