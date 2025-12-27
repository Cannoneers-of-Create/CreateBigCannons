package rbasamoyai.createbigcannons.cannon_control.cannon_types;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonContraptionTypeRegistry {

	private static final Map<ResourceLocation, ICannonContraptionType> TYPES_BY_ID = new Object2ReferenceLinkedOpenHashMap<>();
	private static final Map<ICannonContraptionType, ResourceLocation> IDS_BY_TYPE = new Reference2ObjectOpenHashMap<>();
	private static final ResourceLocation EMPTY = CreateBigCannons.resource("empty");

	public static <T extends ICannonContraptionType> T register(ResourceLocation loc, T type) {
		if (TYPES_BY_ID.containsKey(loc)) {
			throw new IllegalStateException("Cannon contraption type " + loc + " already registered");
		}
		TYPES_BY_ID.put(loc, type);
		IDS_BY_TYPE.put(type, loc);
		return type;
	}

	@Nullable public static ICannonContraptionType get(ResourceLocation loc) { return TYPES_BY_ID.get(loc); }

	public static Optional<ICannonContraptionType> getOptional(ResourceLocation loc) { return Optional.ofNullable(get(loc)); }

	public static ResourceLocation getKey(ICannonContraptionType type) { return IDS_BY_TYPE.getOrDefault(type, EMPTY); }

}
