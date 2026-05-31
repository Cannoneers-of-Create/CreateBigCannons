package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import static rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity.getFluidShellCapacity;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class FluidShellBlock extends FuzedProjectileBlock<AbstractFluidShellBlockEntity, FluidShellProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(FluidShellBlock::new);

	public FluidShellBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

	@Override
	public Class<AbstractFluidShellBlockEntity> getBlockEntityClass() {
		return AbstractFluidShellBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AbstractFluidShellBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.FLUID_SHELL.get();
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		FluidShellProjectile projectile = CBCEntityTypes.FLUID_SHELL.create(level);
		projectile.setFuze(getFuzeFromBlocks(projectileBlocks, level.registryAccess()));
		projectile.setTracer(getTracerFromBlocks(projectileBlocks, level.registryAccess()));
		if (!projectileBlocks.isEmpty()) {
			StructureBlockInfo info = projectileBlocks.get(0);
			if (info.nbt() != null) { // jank alert
				CompoundTag component = info.nbt().getCompound("FluidContent");
                FluidTank tank = new FluidTank(getFluidShellCapacity()).readFromNBT(level.registryAccess(), component); // It's cheaper than recreating the entire BE.
                FluidStack fstack = tank.getFluid();
                projectile.setFluidStack(fstack.isEmpty() ? EndFluidStack.EMPTY : new EndFluidStack(fstack.getFluid(), fstack.getAmount(), fstack.getComponentsPatch()));
			}
		}
		return projectile;
	}

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, ItemStack itemStack) {
		FluidShellProjectile projectile = CBCEntityTypes.FLUID_SHELL.create(level);
		projectile.setFuze(getFuzeFromItemStack(itemStack));
		projectile.setTracer(getTracerFromItemStack(itemStack));
		CompoundTag fluidTag = itemStack.getOrDefault(CBCDataComponents.FLUID_CONTENT, CustomData.EMPTY).copyTag();
		projectile.setFluidStack(EndFluidStack.readTag(fluidTag, level.registryAccess()));
		return projectile;
    }

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
		FluidShellProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromBlock(level, pos, state));
		projectile.setFuze(getFuzeFromBlock(level, pos, state));
		if (level.getBlockEntity(pos) instanceof AbstractFluidShellBlockEntity fluidShell)
			fluidShell.setFluidShellStack(projectile);
		return projectile;
	}

    @Override
    public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation, HolderLookup.Provider registries) {
        StructureBlockInfo info = super.getHandloadingInfo(stack, localPos, cannonOrientation, registries);
        if (info.nbt() != null) { // More jank
            CompoundTag fluidTag = stack.getOrDefault(CBCDataComponents.FLUID_CONTENT, CustomData.EMPTY).copyTag();
            FluidTank tank = new FluidTank(getFluidShellCapacity()).readFromNBT(registries, fluidTag);
            info.nbt().put("FluidContent", tank.writeToNBT(registries, new CompoundTag()));
        }
        return info;
    }

    @Override
    public ItemStack getExtractedItem(StructureBlockInfo info, HolderLookup.Provider registries) {
        ItemStack stack = super.getExtractedItem(info, registries);
        if (info.nbt() != null) { // More jank
            CompoundTag component = info.nbt().getCompound("FluidContent");
            FluidTank tank = new FluidTank(getFluidShellCapacity()).readFromNBT(registries, component);
            stack.set(CBCDataComponents.FLUID_CONTENT, CustomData.of(tank.writeToNBT(registries, new CompoundTag())));
        }
        return stack;
    }

    @Override
	public EntityType<? extends FluidShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.FLUID_SHELL.get();
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand == InteractionHand.OFF_HAND)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		Direction facing = hit.getDirection();
		Direction targetDir = this.isBaseFuze() ? state.getValue(FACING).getOpposite() : state.getValue(FACING);
		boolean correctOrientation = facing == targetDir;

		return this.onBlockEntityUseItemOn(level, pos, shell -> {
			if (!stack.isEmpty() && correctOrientation) {
				if (shell.tryEmptyItemIntoTE(level, player, hand, stack, facing)) return ItemInteractionResult.SUCCESS;
				if (shell.tryFillItemFromTE(level, player, hand, stack, facing)) return ItemInteractionResult.SUCCESS;
			}
			return super.useItemOn(stack, state, level, pos, player, hand, hit);
		});
	}

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return this.onBlockEntityUse(level, pos, shell -> super.useWithoutItem(state, level, pos, player, hit));
    }

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.FLUID_SHELL.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        // Because fluid content does not have a good direct block entity data-item component mapping.
        List<ItemStack> items = super.getDrops(state, params);
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof AbstractFluidShellBlockEntity be) {
            HolderLookup.Provider prov = be.getLevel().registryAccess();
            for (ItemStack item : items) {
                if (this.asItem() == item.getItem())
                    be.setFluidShellItemFluidData(item, prov);
            }
        }
        return items;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack item = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof AbstractFluidShellBlockEntity be) {
            be.setFluidShellItemFluidData(item, level.registryAccess());
        }
        return item;
    }

    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> serverType) {
        return createTickerHelper(serverType, CBCBlockEntities.FLUID_SHELL.get(), FuzedProjectileBlock::tickBlockEntity);
    }

}
