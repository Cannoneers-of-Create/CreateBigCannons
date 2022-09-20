package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;

public class CannonKineticsScenes {

	public static void slidingBreech(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/sliding_breech", "Using a Sliding Breech");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		scene.markAsFinished();
	}
	
	public static void screwBreech(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/screw_breech", "Using a Sliding Breech");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		scene.markAsFinished();
	}
	
}
