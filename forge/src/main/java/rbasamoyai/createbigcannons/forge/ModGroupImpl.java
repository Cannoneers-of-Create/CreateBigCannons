package rbasamoyai.createbigcannons.forge;

import java.util.function.Supplier;

import com.simibubi.create.Create;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class ModGroupImpl {

	private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateBigCannons.MOD_ID);

	public static Supplier<CreativeModeTab> wrapGroup(Supplier<CreativeModeTab> sup) {
		return TAB_REGISTER.register("base", sup);
	}

	public static CreativeModeTab.Builder createBuilder() {
		return CreativeModeTab.builder().withTabsBefore(Create.asResource("palettes"));
	}

	public static void registerForge(IEventBus modBus) {
		TAB_REGISTER.register(modBus);
	}

}
