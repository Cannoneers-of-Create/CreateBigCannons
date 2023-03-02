package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.components.actors.SeatBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class AutocannonBreechBlock extends AutocannonBaseBlock implements ITE<AutocannonBreechBlockEntity>, IWrenchable {

    public static final BooleanProperty HANDLE = BooleanProperty.create("handle");

    public AutocannonBreechBlock(Properties properties, AutocannonMaterial material) {
        super(properties, material);
        this.registerDefaultState(this.defaultBlockState().setValue(HANDLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HANDLE);
    }

    @Override public Class<AutocannonBreechBlockEntity> getTileEntityClass() { return AutocannonBreechBlockEntity.class; }
    @Override public BlockEntityType<? extends AutocannonBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.AUTOCANNON_BREECH.get(); }

    @Override public CannonCastShape getCannonShape() { return CannonCastShape.AUTOCANNON_BREECH.get(); }

    @Override
    public boolean canConnectToSide(LevelAccessor level, BlockState state, BlockPos pos, Direction face) {
        return this.getFacing(state) == face;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
    }

    @Override public boolean isBreechMechanism(BlockState state) { return true; }
    @Override public boolean isComplete(BlockState state) { return true; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.EIGHT_VOXEL_POLE.get(this.getFacing(state).getAxis());
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        AutocannonBlockEntity autocannon = this.getTileEntity(level, pos);
        Direction facing = this.getFacing(state);

        BlockState newState = state.cycle(HANDLE);
        level.setBlock(pos, newState, 3);
        AutocannonBreechBlockEntity autocannon1 = this.getTileEntity(level, pos);
        if (autocannon1 != null) {
            boolean previouslyConnected = autocannon.cannonBehavior().isConnectedTo(facing);
            autocannon1.cannonBehavior().setConnectedFace(facing, previouslyConnected);
            if (level.getBlockEntity(pos.relative(facing)) instanceof AutocannonBlockEntity autocannon2) {
                autocannon2.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
            }

            if (!newState.getValue(HANDLE) ) {
                DyeColor seat = autocannon1.getSeatColor();
                if (seat != null) {
                    ItemStack drop = AllBlocks.SEATS.get(seat).asStack();
                    Player player = context.getPlayer();
                    if (!player.addItem(drop) && !player.isCreative()) {
                        Vec3 spawnLoc = Vec3.atCenterOf(pos);
                        ItemEntity dropEntity = new ItemEntity(level, spawnLoc.x, spawnLoc.y, spawnLoc.z, drop);
                        level.addFreshEntity(dropEntity);
                    }
                }
                autocannon1.setSeatColor(null);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) { return InteractionResult.PASS; }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof AutocannonBreechBlockEntity breech) {
            if (breech.getSeatColor() == null
                    && state.getValue(HANDLE)
                    && stack.getItem() instanceof BlockItem blockItem
                    && blockItem.getBlock() instanceof SeatBlock seat) {
                if (!level.isClientSide) {
                    breech.setSeatColor(seat.getColor());
                    SoundType soundType = seat.defaultBlockState().getSoundType();
                    level.playSound(null, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                    if (!player.isCreative()) stack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (stack.isEmpty() && breech.getSeatColor() != null) {
                if (!level.isClientSide) {
                    DyeColor seat = breech.getSeatColor();
                    if (seat != null) {
                        ItemStack drop = AllBlocks.SEATS.get(seat).asStack();
                        if (!player.addItem(drop) && !player.isCreative()) {
                            Vec3 spawnLoc = Vec3.atCenterOf(pos);
                            ItemEntity dropEntity = new ItemEntity(level, spawnLoc.x, spawnLoc.y, spawnLoc.z, drop);
                            level.addFreshEntity(dropEntity);
                        }
                    }
                    breech.setSeatColor(null);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.use(state, level, pos, player, hand, result);
    }

}
