package rbasamoyai.createbigcannons.forge;

import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.data.event.GatherDataEvent;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.CBCDatagenRoot;

@Mod.EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CBCDataForge {

	@SubscribeEvent
	public static void onDatagen(GatherDataEvent evt) {
		Tags.init();
		CBCDatagenRoot.register(evt.getGenerator(), evt.getExistingFileHelper(), evt.includeClient(), evt.includeServer());
	}

}
