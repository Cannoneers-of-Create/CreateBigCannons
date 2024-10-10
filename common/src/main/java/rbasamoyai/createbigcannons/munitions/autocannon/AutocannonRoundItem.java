package rbasamoyai.createbigcannons.munitions.autocannon;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

public abstract class AutocannonRoundItem extends Item {

    protected AutocannonRoundItem(Properties properties) {
        super(properties);
    }

    public abstract AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);
	public abstract EntityType<?> getEntityType(ItemStack stack);

	@Nonnull public abstract AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack);

    public ItemStack getCreativeTabCartridgeItem() {
        ItemStack stack = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
        AutocannonCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        return stack;
    }

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		if (stack.getOrCreateTag().getBoolean("Tracer")) {
			Lang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
		}
	}

}
