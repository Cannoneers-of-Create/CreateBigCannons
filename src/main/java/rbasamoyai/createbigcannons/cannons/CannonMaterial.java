package rbasamoyai.createbigcannons.cannons;

public interface CannonMaterial {

	String getName();
	
	/**
	 * The squib ratio describes the maximum ratio of <b>cannon barrel</b> to
	 * <b>cannon chamber</b> that cannons of this material can safely operate
	 * at. If the squib ratio exceeds this value
	 * 
	 * @return A double representing the squib ratio of the material
	 */
	double squibRatio();
	
	int maxCharges();
	FailureMode failureMode();
	
	public static enum FailureMode {
		RUPTURE,
		FRAGMENT
	}
	
}
