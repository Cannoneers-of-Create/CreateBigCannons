package rbasamoyai.createbigcannons.crafting.casting;

import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class CannonCastBlock extends Block implements IBE<AbstractCannonCastBlockEntity> {

	public CannonCastBlock(Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean dropContents) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			this.withBlockEntityDo(level, pos, AbstractCannonCastBlockEntity::destroyCastMultiblockAtLayer);
		}
		super.onRemove(state, level, pos, newState, dropContents);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return CBCBlocks.CASTING_SAND.asStack();
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return false;
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return 0.8f;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public Class<AbstractCannonCastBlockEntity> getBlockEntityClass() {
		return AbstractCannonCastBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AbstractCannonCastBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_CAST.get();
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return this.getBlockEntityOptional(level, pos)
			.map(AbstractCannonCastBlockEntity::getControllerBE)
			.map(AbstractCannonCastBlockEntity::getFillState)
			.map(CannonCastBlock::castFractionToRedstoneLevel)
			.orElse(0);
	}

	public static int castFractionToRedstoneLevel(float frac) {
		return Mth.floor(Mth.clamp(frac * 13 + (frac > 0 ? 1 : 0), 0, 14));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (hit.getDirection() != Direction.UP) return InteractionResult.PASS;

		return this.onBlockEntityUse(level, pos, cast -> {
			AbstractCannonCastBlockEntity controller = cast.getControllerBE();
			if (controller == null || stack.isEmpty()) return InteractionResult.PASS;
			if (controller.tryEmptyItemIntoBE(level, player, hand, stack, Direction.UP))
				return InteractionResult.SUCCESS;
			if (controller.tryFillItemFromBE(level, player, hand, stack, Direction.UP))
				return InteractionResult.SUCCESS;
			return InteractionResult.PASS;
		});
	}

}
