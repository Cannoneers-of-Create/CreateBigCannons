package rbasamoyai.createbigcannons.cannon_loading;

import com.simibubi.create.AllContraptionTypes;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.content.contraptions.Contraption;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

public class CBCModifiedContraptionRegistry {

	private static final ReferenceOpenHashSet<ContraptionType> CANNON_LOADER_TYPES = new ReferenceOpenHashSet<>();
	private static final ReferenceOpenHashSet<ContraptionType> FRAGILE_TYPES = new ReferenceOpenHashSet<>();

	public static void registerCannonLoaderType(ContraptionType type) {
		if (CANNON_LOADER_TYPES.contains(type))
			throw new IllegalStateException("Already registered big cannon loader contraption type");
		CANNON_LOADER_TYPES.add(type);
	}

	public static void registerFragileType(ContraptionType type) {
		if (FRAGILE_TYPES.contains(type))
			throw new IllegalStateException("Already registered fragile contraption type");
		FRAGILE_TYPES.add(type);
	}

	public static boolean canLoadBigCannon(Contraption contraption) {
		return CANNON_LOADER_TYPES.contains(contraption.getType()) && contraption instanceof CanLoadBigCannon;
	}

	public static boolean isFragileContraption(Contraption contraption) {
		return FRAGILE_TYPES.contains(contraption.getType()) && contraption instanceof HasFragileContraption;
	}

	public static void registerDefaults() {
		registerCannonLoaderType(CBCContraptionTypes.CANNON_LOADER.value());
		registerCannonLoaderType(AllContraptionTypes.PISTON.value());
		registerCannonLoaderType(AllContraptionTypes.GANTRY.value());
		registerCannonLoaderType(AllContraptionTypes.PULLEY.value());
		registerFragileType(CBCContraptionTypes.CANNON_LOADER.value());
		registerFragileType(AllContraptionTypes.PISTON.value());
		registerFragileType(AllContraptionTypes.GANTRY.value());
		registerFragileType(AllContraptionTypes.PULLEY.value());
	}

}
