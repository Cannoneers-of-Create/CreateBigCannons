package rbasamoyai.createbigcannons.cannons;

public interface CannonMaterial {

	String getName();
	double squibRatio();
	int maxCharges();
	FailureMode failureMode();
	
	public static enum FailureMode {
		RUPTURE,
		FRAGMENT
	}
	
}
