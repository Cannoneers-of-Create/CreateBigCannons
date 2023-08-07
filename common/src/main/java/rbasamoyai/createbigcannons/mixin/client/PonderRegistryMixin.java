package rbasamoyai.createbigcannons.mixin.client;

import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.ArrayList;
import java.util.List;

@Mixin(PonderRegistry.class)
public class PonderRegistryMixin {

	@Inject(method = "compile(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void compile(ResourceLocation id, CallbackInfoReturnable<List<PonderScene>> cir, List<PonderStoryBoardEntry> list) {
		if (id.getNamespace().equals(CreateBigCannons.MOD_ID)) return;
		List<PonderStoryBoardEntry> modified = new ArrayList<>();
		for (PonderStoryBoardEntry ponder : list) {
			if (ponder.getNamespace().equals(CreateBigCannons.MOD_ID)) modified.add(ponder);
		}
		list.removeAll(modified);
		list.addAll(modified);
	}

}
