package rbasamoyai.createbigcannons.cannon_control.cannon_types;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public enum CBCCannonContraptionTypes implements ICannonContraptionType {
	BIG_CANNON,
	DROP_MORTAR,
	AUTOCANNON,
	HANDLE_AUTOCANNON;

	private static final Map<ResourceLocation, CBCCannonContraptionTypes> BY_ID =
		Arrays.stream(values()).collect(Collectors.toMap(CBCCannonContraptionTypes::getId, Function.identity()));

	private final ResourceLocation id = CreateBigCannons.resource(this.name().toLowerCase(Locale.ROOT));

	CBCCannonContraptionTypes() {
		CannonContraptionTypeRegistry.register(this.id, this);
	}

	@Override public ResourceLocation getId() { return this.id; }

	@Nullable public static CBCCannonContraptionTypes byId(ResourceLocation loc) { return BY_ID.get(loc); }

	public static void register() {
	}

}
