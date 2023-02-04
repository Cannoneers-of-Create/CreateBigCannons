package rbasamoyai.createbigcannons.crafting.incomplete;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractIncompleteAutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IncompleteAutocannonBlock extends AbstractIncompleteAutocannonBlock implements IncompleteWithItemsCannonBlock {

	private final VoxelShaper shapes;
	private final Supplier<CannonCastShape> cannonShape;
	private final NonNullSupplier<? extends ItemLike> itemSupplier;
	private ItemLike item;
	private List<ItemLike> requiredItems = null;

	private final NonNullSupplier<? extends Block> resultBlockSup;
	private Block resultBlock;

	public IncompleteAutocannonBlock(Properties properties, AutocannonMaterial material, VoxelShape shape,
									 Supplier<CannonCastShape> castShape, NonNullSupplier<? extends Block> completeBlockSup,
									 NonNullSupplier<? extends ItemLike> item) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(shape).forDirectional();
		this.cannonShape = castShape;
		this.itemSupplier = item;
		this.resultBlockSup = completeBlockSup;
	}

	public static IncompleteAutocannonBlock breech(Properties properties, AutocannonMaterial material,
												   NonNullSupplier<? extends Block> completeBlock, NonNullSupplier<? extends ItemLike> item) {
		return new IncompleteAutocannonBlock(properties, material, box(4, 0, 4, 12, 16, 12),
				CannonCastShape.AUTOCANNON_BREECH, completeBlock, item);
	}

	public static IncompleteAutocannonBlock recoilSpring(Properties properties, AutocannonMaterial material,
														 NonNullSupplier<? extends Block> completeBlock, NonNullSupplier<? extends ItemLike> item) {
		return new IncompleteAutocannonBlock(properties, material, box(5, 0, 5, 11, 16, 11),
				CannonCastShape.AUTOCANNON_RECOIL_SPRING, completeBlock, item);
	}

	private ItemLike resolveItem() {
		if (this.item == null) this.item = this.itemSupplier.get();
		return this.item;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.get(this.getFacing(state));
	}

	@Override public CannonCastShape getCannonShape() { return this.cannonShape.get(); }

	@Override
	public List<ItemLike> requiredItems() {
		if (this.requiredItems == null) {
			this.requiredItems = new ArrayList<>(1);
			this.requiredItems.add(this.resolveItem());
		}
		return this.requiredItems;
	}

	@Override public int progress(BlockState state) { return 0; }

	protected Block getResultBlock() {
		if (this.resultBlock == null) this.resultBlock = this.resultBlockSup.get();
		return this.resultBlock;
	}

	@Override
	public BlockState getCompleteBlockState(BlockState state) {
		BlockState complete = this.getResultBlock().defaultBlockState();
		return complete.hasProperty(FACING) ? complete.setValue(FACING, state.getValue(FACING)) : complete;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() != this.resolveItem()) return InteractionResult.PASS;
		level.playSound(player, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		if (!level.isClientSide) {
			if (!player.isCreative()) stack.shrink(1);
			this.withTileEntityDo(level, pos, cbe -> {
				CannonBehavior behavior = cbe.cannonBehavior();
				cbe.setRemoved();

				level.setBlock(pos, this.getCompleteBlockState(state), 3 | 16);

				BlockEntity be = level.getBlockEntity(pos);
				if (!(be instanceof ICannonBlockEntity<?> cbe1)) return;
				CannonBehavior behavior1 = cbe1.cannonBehavior();
				for (Direction dir : Direction.values()) {
					boolean isConnected = behavior.isConnectedTo(dir);
					behavior1.setConnectedFace(dir, isConnected);
					if (level.getBlockEntity(pos.relative(dir)) instanceof ICannonBlockEntity<?> cbe2) {
						cbe2.cannonBehavior().setConnectedFace(dir.getOpposite(), isConnected);
					}
				}
			});
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

}
