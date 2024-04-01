package rbasamoyai.createbigcannons.datagen;

import java.util.Locale;

public enum CBCDatagenPlatform {
	FABRIC("c", 81),
	FORGE("forge", 1);

	private final String id = this.name().toLowerCase(Locale.ROOT);
	private final String tagNamespace;
	private final int fluidMultiplier;

	CBCDatagenPlatform(String tagNamespace, int fluidMultiplier) {
		this.tagNamespace = tagNamespace;
        this.fluidMultiplier = fluidMultiplier;
    }

	public String id() { return this.id; }
	public String tagNamespace() { return this.tagNamespace; }
	public int fluidMultiplier() { return this.fluidMultiplier; }

	public static CBCDatagenPlatform getPlatform(String id) {
		return switch (id) {
			case "fabric" -> FABRIC;
			case "forge" -> FORGE;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
	}

}
