package rbasamoyai.createbigcannons.mixin.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.PonderStoryBoardEntry;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

@Mixin(PonderSceneRegistry.class)
public class PonderSceneRegistryMixin {

	@Inject(method = "compile(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;",
		at = @At(value = "INVOKE", target = "Lnet/createmod/ponder/foundation/registration/PonderSceneRegistry;compile(Ljava/util/Collection;)Ljava/util/List;"))
	private void createbigcannons$compile(ResourceLocation id, CallbackInfoReturnable<List<PonderScene>> cir,
												 @Local Collection<PonderStoryBoardEntry> scenes) {
		if (id.getNamespace().equals(CreateBigCannons.MOD_ID)) return;
		List<PonderStoryBoardEntry> modified = new ArrayList<>();
		for (PonderStoryBoardEntry ponder : scenes) {
			if (ponder.getNamespace().equals(CreateBigCannons.MOD_ID)) modified.add(ponder);
		}
		scenes.removeAll(modified);
		scenes.addAll(modified);
	}

}
