package rbasamoyai.createbigcannons.cannons;

public enum CannonMaterials implements CannonMaterial {
	CAST_IRON("cast_iron", 1d / 1d, 2, FailureMode.FRAGMENT),
	BRONZE("bronze", 3d / 2d, 4, FailureMode.RUPTURE),
	STEEL("steel", 2d / 1d, 6, FailureMode.FRAGMENT);

	private final String name;
	private final double squibRatio;
	private final int maxCharges;
	private final FailureMode failureMode;
	
	private CannonMaterials(String name, double squibRatio, int maxCharges, FailureMode failureMode) {
		this.name = name;
		this.squibRatio = squibRatio;
		this.maxCharges = maxCharges;
		this.failureMode = failureMode;
	}
	
	@Override public final String getName() { return this.name; }
	@Override public double squibRatio() { return this.squibRatio; }
	@Override public int maxCharges() { return this.maxCharges; }
	@Override public FailureMode failureMode() { return this.failureMode; }
	
}
