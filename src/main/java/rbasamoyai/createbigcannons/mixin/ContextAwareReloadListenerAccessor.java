package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.google.gson.JsonElement;

import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.resource.ContextAwareReloadListener;

@Mixin(ContextAwareReloadListener.class)
public interface ContextAwareReloadListenerAccessor {

    @Invoker(value = "makeConditionalOps", remap = false)
    ConditionalOps<JsonElement> callMakeConditionalOps();

}
