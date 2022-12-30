package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
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
			.text("Sliding Breeches offer a quick way to load cannons from the breech, at the cost of cannon strength.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(100);
		
		scene.world.showSection(util.select.fromTo(3, 1, 2, 3, 2, 3), Direction.WEST);
		scene.world.showSection(util.select.position(2, 2, 3), Direction.WEST);
		scene.idle(30);
		
		scene.addKeyframe();
		
		Selection gearUp = util.select.fromTo(2, 2, 3, 3, 2, 3);
		Selection gearDown = util.select.fromTo(breechPos, util.grid.at(3, 1, 2));
		scene.world.setKineticSpeed(gearUp, -16);
		scene.world.setKineticSpeed(gearDown, 8);
		scene.idle(40);
		
		scene.overlay.showText(60)
			.text("Use rotational force to open up sliding breeches.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(40);
		
		scene.world.setKineticSpeed(gearUp, 0);
		scene.world.setKineticSpeed(gearDown, 0);
		scene.idle(20);
		
		scene.addKeyframe();
		
		BlockPos munitionPos = util.grid.at(2, 1, 1);
		ElementLink<WorldSectionElement> munitionBlock = scene.world.showIndependentSection(util.select.position(munitionPos), Direction.SOUTH);
		scene.idle(15);
		
		scene.overlay.showText(80)
			.text("Once the sliding breech is fully opened, cannon munitions can be loaded through the breech.")
			.pointAt(util.vector.centerOf(munitionPos))
			.colored(PonderPalette.GREEN);
		scene.idle(15);
		scene.world.moveSection(munitionBlock, util.vector.of(0.0d, 0.0d, 2.0d), 40);
		scene.idle(80);
		
		scene.addKeyframe();
		
		scene.world.setKineticSpeed(gearUp, 16);
		scene.world.setKineticSpeed(gearDown, -8);
		scene.idle(40);
		
		scene.overlay.showText(80)
			.text("The sliding breech must be not obstructed by munitions or loader contraptions for it to close.")
			.pointAt(util.vector.blockSurface(breechPos, Direction.NORTH, 0))
			.colored(PonderPalette.RED);
		scene.idle(100);
		
		scene.world.setKineticSpeed(gearUp, 0);
		scene.world.setKineticSpeed(gearDown, 0);
		
		scene.markAsFinished();
	}
	
	public static void screwBreech(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/screw_breech", "Using a Screw Breech");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		BlockPos breechPos = util.grid.at(2, 1, 2);
		ElementLink<WorldSectionElement> breech = scene.world.showIndependentSection(util.select.position(breechPos), Direction.DOWN);
		scene.world.showSection(util.select.fromTo(util.grid.at(3, 1, 2), util.grid.at(4, 1, 2)), Direction.DOWN);
		scene.idle(20);
		
		scene.overlay.showText(80)
			.text("Screw Breeches are strong breech blocks that do not suffer strength penalties unlike Sliding Breeches.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(100);
		
		scene.idle(20);
		
		scene.addKeyframe();
		
		Selection breechGearUp = util.select.fromTo(0, 2, 3, 1, 2, 3);
		Selection breechGearDown = util.select.position(1, 1, 2);
		Selection breechGearDownAll = util.select.fromTo(util.grid.at(1, 1, 2), breechPos);
		
		scene.world.showSection(breechGearUp, Direction.EAST);
		scene.world.showSection(breechGearDown, Direction.EAST);
		scene.idle(30);
		
		scene.world.setKineticSpeed(breechGearUp, -16);
		scene.world.setKineticSpeed(breechGearDownAll, 8);
		scene.idle(20);
		
		scene.overlay.showText(60)
			.text("Use rotational force to unlock screw breeches.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(40);
		
		scene.world.setKineticSpeed(breechGearUp, 0);
		scene.world.setKineticSpeed(breechGearDownAll, 0);
		scene.idle(20);
		
		scene.world.hideSection(breechGearUp, Direction.WEST);
		scene.world.hideSection(breechGearDown, Direction.WEST);
		scene.idle(15);
		
		scene.addKeyframe();
		
		Selection breechKinetics = util.select.fromTo(1, 3, 2, 2, 3, 2);
		scene.world.showSection(breechKinetics, Direction.DOWN);
		ElementLink<WorldSectionElement> piston = scene.world.showIndependentSection(util.select.fromTo(2, 2, 3, 2, 3, 3), Direction.DOWN);
		scene.world.moveSection(piston, util.vector.of(0, 0, -1), 0);
		scene.idle(30);
		
		scene.world.setKineticSpeed(breechKinetics, -16);
		scene.world.moveSection(piston, util.vector.of(0, 1, 0), 60);
		scene.world.moveSection(breech, util.vector.of(0, 1, 0), 60);
		scene.idle(20);
		
		scene.overlay.showText(80)
			.text("Once the screw breech is fully unlocked, use kinetic contraptions to move it out of the way for loading.")
			.colored(PonderPalette.GREEN)
			.pointAt(util.vector.centerOf(2, 3, 2));
		scene.idle(40);
		scene.world.setKineticSpeed(breechKinetics, 0);
		scene.idle(60);
		
		ElementLink<WorldSectionElement> munition = scene.world.showIndependentSection(util.select.position(0, 1, 2), Direction.EAST);
		scene.idle(20);
		scene.world.moveSection(munition, util.vector.of(3, 0, 0), 40);
		scene.idle(60);
		
		scene.world.setKineticSpeed(breechKinetics, 16);
		scene.world.moveSection(piston, util.vector.of(0, -1, 0), 60);
		scene.world.moveSection(breech, util.vector.of(0, -1, 0), 60);
		scene.idle(60);
		scene.world.setKineticSpeed(breechKinetics, 0);
		scene.idle(20);
		scene.world.hideSection(breechKinetics, Direction.UP);
		scene.world.hideIndependentSection(piston, Direction.UP);
		
		scene.addKeyframe();
		
		scene.world.showSection(breechGearUp, Direction.EAST);
		scene.world.showSection(breechGearDown, Direction.EAST);
		scene.idle(30);
		
		scene.world.setKineticSpeed(breechGearUp, 16);
		scene.world.setKineticSpeed(breechGearDownAll, -8);
		scene.idle(20);
		
		scene.overlay.showText(60)
			.text("Once loading is finished, close the screw breech to close the cannon.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(40);
		
		scene.world.setKineticSpeed(breechGearUp, 0);
		scene.world.setKineticSpeed(breechGearDownAll, 0);
		scene.idle(10);
		
		scene.world.hideSection(breechGearUp, Direction.WEST);
		scene.world.hideSection(breechGearDown, Direction.WEST);
		scene.idle(15);
		
		scene.markAsFinished();
	}
	
}
