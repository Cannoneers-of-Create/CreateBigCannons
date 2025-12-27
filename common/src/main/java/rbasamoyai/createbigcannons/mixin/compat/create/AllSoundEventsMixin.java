package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllSoundEvents;

import rbasamoyai.createbigcannons.index.CBCSoundEvents;

@Mixin(AllSoundEvents.SoundEntryBuilder.class)
public abstract class AllSoundEventsMixin {

	@Inject(method = "build",
			at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
			cancellable = true,
			remap = false)
	private void createbigcannons$build(CallbackInfoReturnable<AllSoundEvents.SoundEntry> cir, @Local AllSoundEvents.SoundEntry entry) {
		if (((Object) this) instanceof CBCSoundEvents.CBCSoundEntryBuilder) cir.setReturnValue(entry);
	}

}
