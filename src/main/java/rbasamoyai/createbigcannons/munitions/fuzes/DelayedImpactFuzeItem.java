package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile.ImpactResult;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

public class DelayedImpactFuzeItem extends FuzeItem implements MenuProvider {

	public DelayedImpactFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.has(CBCDataComponents.FUZE_TIMER)) {
				stack.set(CBCDataComponents.FUZE_TIMER, 20);
			}
			int timer = stack.get(CBCDataComponents.FUZE_TIMER);

			CBCMenuTypes.SET_DELAYED_IMPACT_FUZE.open((ServerPlayer) player, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(timer);
                ItemStack.STREAM_CODEC.encode(buf, new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		ItemStack stack = player.getMainHandItem();
		return DelayedImpactFuzeContainer.getServerMenu(windowId, playerInv, stack);
	}

	@Override
	public Component getDisplayName() {
		return this.getDescription();
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, ImpactResult impactResult, boolean baseFuze) {
		if (baseFuze || impactResult.shouldRemove() || impactResult.kinematics() == ImpactResult.KinematicOutcome.BOUNCE)
            return false;
        this.onCommonImpact(stack, projectile.level());
        return false;
	}

    @Override
    public boolean onBlockImpact(ItemStack stack, Level level, BlockPos pos, BlockState state, HitResult hitResult, ImpactResult impactResult, Vec3 impactPos) {
        boolean baseFuze = state.getBlock() instanceof FuzedProjectileBlock<?, ?> fuzedBlock && fuzedBlock.isBaseFuze();
        Direction shellFacing = state.getValue(FuzedProjectileBlock.FACING);
        Direction hitFace = baseFuze ? shellFacing.getOpposite() : shellFacing;
        Vec3 hitDir = impactPos.subtract(pos.getCenter());
        Direction closest = Direction.getNearest(hitDir.x, hitDir.y, hitDir.z);
        if (closest != hitFace)
            return false;

        if (impactResult.shouldRemove() || impactResult.kinematics() == ImpactResult.KinematicOutcome.BOUNCE)
            return false;
        this.onCommonImpact(stack, level);
        return false;
    }

    public void onCommonImpact(ItemStack stack, Level level) {
        int damage = stack.getOrDefault(CBCDataComponents.DAMAGE, this.getFuzeDurability());
        if (damage > 0 && !stack.has(CBCDataComponents.ACTIVATED)) {
            --damage;
            stack.set(CBCDataComponents.DAMAGE, damage);
            float f = this.getDetonateChance();
            if (f > 0 && level.getRandom().nextFloat() < f)
                stack.set(CBCDataComponents.ACTIVATED, true);
        }
    }

    @Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) {
        return this.onCommonTick(stack);
	}

    @Override
    public boolean onBlockTick(ItemStack stack, Level level, BlockPos pos, BlockState state) {
        return this.onCommonTick(stack);
    }

    public boolean onCommonTick(ItemStack stack) {
        if (!stack.has(CBCDataComponents.ACTIVATED)) return false;
        if (!stack.has(CBCDataComponents.FUZE_TIMER)) return true;
        int timer = stack.get(CBCDataComponents.FUZE_TIMER);
        --timer;
        stack.set(CBCDataComponents.FUZE_TIMER, timer);
        return timer <= 0;
    }

    @Override
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		CBCTooltip.appendImpactFuzeText(stack, ctx, tooltip, flag, this.getDetonateChance(), this.getFuzeDurability());

		int time = stack.getOrDefault(CBCDataComponents.FUZE_TIMER, 20);
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		tooltip.add(CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".delayed_impact_fuze.tooltip.shell_info.item", seconds, ticks)
			.component());
	}

	protected float getDetonateChance() {
		return CBCConfigs.server().munitions.impactFuzeDetonationChance.getF();
	}

	protected int getFuzeDurability() {
		return CBCConfigs.server().munitions.impactFuzeDurability.get();
	}

	@Override
	public boolean canLingerInGround(ItemStack stack, AbstractCannonProjectile projectile) {
		return stack.getOrDefault(CBCDataComponents.ACTIVATED, false);
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".delayed_impact_fuze.tooltip.shell_info.chance", (int) (this.getDetonateChance() * 100.0f))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));

		int time = stack.getOrDefault(CBCDataComponents.FUZE_TIMER, 20);
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		MutableComponent info1 = CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".delayed_impact_fuze.tooltip.shell_info", seconds, ticks)
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info1, Style.EMPTY, Style.EMPTY, 6));
	}

	public static ItemStack getCreativeTabItem(int defaultFuze) {
		ItemStack stack = CBCItems.DELAYED_IMPACT_FUZE.asStack();
		stack.set(CBCDataComponents.FUZE_TIMER, defaultFuze);
		return stack;
	}

}
