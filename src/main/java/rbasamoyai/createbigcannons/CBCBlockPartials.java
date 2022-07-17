package rbasamoyai.createbigcannons;

import com.jozufozu.flywheel.core.PartialModel;

public class CBCBlockPartials {

	public static final PartialModel
		CAST_IRON_SLIDING_BREECHBLOCK = block("sliding_breechblock/cast_iron_sliding_breechblock"),
		ROTATING_MOUNT = block("cannon_mount/rotating_mount"),
		YAW_SHAFT = block("cannon_mount/yaw_axis");
	
	private static PartialModel block(String path) {
		return new PartialModel(CreateBigCannons.resource("block/" + path));
	}
	
	public static void init() {}
	
}