package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class CannonKineticsScenes {

	public static void slidingBreech(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/sliding_breech", "Using a Sliding Breech");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		BlockPos breechPos = util.grid.at(2, 1, 2);
		scene.world.showSection(util.select.fromTo(breechPos, util.grid.at(2, 1, 4)), Direction.DOWN);
		scene.idle(20);
		
		scene.overlay.showText(80)
			.text("Sliding Breeches offer a quick way to load cannons from the breech.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(100);
		
		scene.world.showSection(util.select.fromTo(3, 1, 2, 3, 2, 3), Direction.WEST);
		scene.world.showSection(util.select.position(2, 2, 3), Direction.WEST);
		scene.idle(30);
		
		scene.addKeyframe();
		
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), -16);
		scene.world.setKineticSpeed(util.select.position(3, 2, 3), -16);
		scene.world.setKineticSpeed(util.select.position(3, 1, 2), 8);
		scene.world.setKineticSpeed(util.select.position(breechPos), 8);
		scene.idle(40);
		
		scene.overlay.showText(60)
			.text("Use rotational force to open up sliding breeches.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(40);
		
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), 0);
		scene.world.setKineticSpeed(util.select.position(3, 2, 3), 0);
		scene.world.setKineticSpeed(util.select.position(3, 1, 2), 0);
		scene.world.setKineticSpeed(util.select.position(breechPos), 0);
		scene.idle(20);
		
		scene.addKeyframe();
		
		BlockPos munitionPos = util.grid.at(2, 1, 1);
		ElementLink<WorldSectionElement> munitionBlock = scene.world.showIndependentSection(util.select.position(munitionPos), Direction.SOUTH);
		scene.idle(15);
		
		scene.overlay.showText(80)
			.text("Once the sliding breech is fully opened, cannon muntions can be loaded through the breech.")
			.pointAt(util.vector.centerOf(munitionPos));
		scene.idle(15);
		scene.world.moveSection(munitionBlock, util.vector.of(0.0d, 0.0d, 2.0d), 40);
		scene.idle(80);
		
		scene.addKeyframe();
		
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), 16);
		scene.world.setKineticSpeed(util.select.position(3, 2, 3), 16);
		scene.world.setKineticSpeed(util.select.position(3, 1, 2), -8);
		scene.world.setKineticSpeed(util.select.position(breechPos), -8);
		scene.idle(40);
		
		scene.overlay.showText(80)
			.text("The sliding breech must be not obstructed by munitions or loader contraptions for it to close.")
			.pointAt(util.vector.blockSurface(breechPos, Direction.NORTH, 0));
		scene.idle(100);
		
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), 0);
		scene.world.setKineticSpeed(util.select.position(3, 2, 3), 0);
		scene.world.setKineticSpeed(util.select.position(3, 1, 2), 0);
		scene.world.setKineticSpeed(util.select.position(breechPos), 0);
		
		scene.markAsFinished();
	}
	
	public static void screwBreech(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/screw_breech", "Using a Sliding Breech");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		scene.markAsFinished();
	}
	
}
