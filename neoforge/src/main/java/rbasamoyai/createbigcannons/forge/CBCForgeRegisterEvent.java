package rbasamoyai.createbigcannons.forge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.event.IModBusEvent;

public class CBCForgeRegisterEvent<T> extends GenericEvent<T> implements IModBusEvent {

	private final Registry<T> registry;

	public CBCForgeRegisterEvent(Class<T> clazz, Registry<T> registry) {
		super(clazz);
		this.registry = registry;
	}

	public T register(ResourceLocation id, T shape) {
		return Registry.register(this.registry, id, shape);
	}

}
