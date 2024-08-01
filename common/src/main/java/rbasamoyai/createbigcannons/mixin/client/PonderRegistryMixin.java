package rbasamoyai.createbigcannons.mixin.client;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

@Mixin(PonderRegistry.class)
public class PonderRegistryMixin {

	@Inject(method = "compile(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/ponder/PonderRegistry;compile(Ljava/util/List;)Ljava/util/List;"))
	private static void createbigcannons$compile(ResourceLocation id, CallbackInfoReturnable<List<PonderScene>> cir,
												 @Local List<PonderStoryBoardEntry> list) {
		if (id.getNamespace().equals(CreateBigCannons.MOD_ID)) return;
		List<PonderStoryBoardEntry> modified = new ArrayList<>();
		for (PonderStoryBoardEntry ponder : list) {
			if (ponder.getNamespace().equals(CreateBigCannons.MOD_ID)) modified.add(ponder);
		}
		list.removeAll(modified);
		list.addAll(modified);
	}

}
