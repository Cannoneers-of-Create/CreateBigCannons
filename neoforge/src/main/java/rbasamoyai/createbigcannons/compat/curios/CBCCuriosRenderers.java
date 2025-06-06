package rbasamoyai.createbigcannons.compat.curios;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import rbasamoyai.createbigcannons.index.CBCItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// Adapted from Create's CuriosRenderers --ritchie
public class CBCCuriosRenderers {

	public static void register(IEventBus modBus, IEventBus forgeBus) {
		modBus.addListener(CBCCuriosRenderers::onClientSetup);
		modBus.addListener(CBCCuriosRenderers::onLayerRegister);
	}

	private static void onClientSetup(FMLClientSetupEvent event) {
		CuriosRendererRegistry.register(CBCItems.GAS_MASK.get(),
			() -> new GasMaskCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GasMaskCurioRenderer.LAYER)));
	}

	private static void onLayerRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(GasMaskCurioRenderer.LAYER, () -> LayerDefinition.create(GasMaskCurioRenderer.mesh(), 1, 1));
	}

}
