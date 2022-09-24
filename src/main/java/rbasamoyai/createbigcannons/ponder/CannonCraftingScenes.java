package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCItems;

public class CannonCraftingScenes {

	public static void cannonCasting(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/cannon_casting", "Cannon Casting");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		scene.markAsFinished();
	}
	
	public static void cannonBoring(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/cannon_boring", "Boring Holes in Cast Cannons");
		scene.configureBasePlate(6, 0, 3);
		scene.world.showSection(util.select.cuboid(util.grid.zero(), util.grid.at(9, 0, 3)), Direction.UP);
		
		Selection latheGearDown = util.select.position(9, 0, 0);
		Selection latheGearUp = util.select.fromTo(9, 1, 1, 8, 1, 1);
		scene.world.showSection(latheGearDown, Direction.UP);
		scene.world.showSection(latheGearUp, Direction.UP);
		ElementLink<WorldSectionElement> cannonPiece = scene.world.showIndependentSection(util.select.fromTo(7, 1, 1, 5, 1, 1), Direction.UP);
		
		int rpm = -32 * 360;
		scene.world.setKineticSpeed(latheGearDown, 16);
		scene.world.setKineticSpeed(latheGearUp, -32);
		scene.world.rotateBearing(util.grid.at(8, 1, 1), rpm, 1200);
		scene.world.rotateSection(cannonPiece, rpm, 0, 0, 1200);
		scene.idle(10);
		
		Selection drillGearDown = util.select.position(4, 0, 3);
		Selection drillGearUp = util.select.fromTo(3, 1, 0, 3, 1, 3).add(util.select.position(3, 2, 2));
		scene.world.showSection(drillGearDown, Direction.NORTH);
		scene.world.showSection(drillGearUp, Direction.NORTH);
		ElementLink<WorldSectionElement> drillPiston = scene.world.showIndependentSection(util.select.fromTo(0, 3, 1, 4, 3, 1), Direction.NORTH);
		scene.world.moveSection(drillPiston, util.vector.of(0, -2, 0), 0);
		
		scene.idle(30);
		
		scene.overlay.showText(60)
			.text("Cannon Drills are used to bore out cast cannons.")
			.pointAt(util.vector.centerOf(3, 1, 1));
		scene.idle(60);
		
		drillGearUp.add(util.select.position(2, 2, 2));
		Selection pump = util.select.position(2, 2, 1);
		scene.world.showSection(util.select.fromTo(0, 1, 1, 2, 2, 2), Direction.DOWN);
		scene.world.showSection(util.select.position(3, 2, 1), Direction.DOWN);
		scene.idle(20);
		scene.world.setKineticSpeed(drillGearUp, -32);
		scene.world.setKineticSpeed(pump, 32);
		scene.idle(10);
		scene.overlay.showText(80)
			.text("In addition to rotational force, they require water to operate, and consume more as they speed up.")
			.pointAt(util.vector.topOf(0, 1, 2))
			.colored(PonderPalette.BLUE);
		scene.idle(80);
		
		scene.addKeyframe();
		
		scene.world.setKineticSpeed(drillGearDown, 16);
		scene.world.setKineticSpeed(drillGearUp, -32);
		scene.world.moveSection(drillPiston, util.vector.of(1, 0, 0), 8);
		scene.idle(8);
		
		scene.world.moveSection(drillPiston, util.vector.of(2, 0, 0), 213);
		scene.world.propagatePipeChange(util.grid.at(2, 2, 1));
		scene.idle(20);
		
		scene.overlay.showText(60)
			.text("The speed of the drill must match the speed of the lathe to do work.")
			.pointAt(util.vector.blockSurface(util.grid.at(5, 1, 1), Direction.WEST))
			.colored(PonderPalette.RED);
		scene.idle(87);
		
		scene.addKeyframe();
		
		scene.world.modifyBlock(util.grid.at(5, 1, 1), state -> boreBlock(state, CBCBlocks.CAST_IRON_CANNON_BARREL.getDefaultState()), false);
		scene.idle(20);
		scene.overlay.showText(60)
			.text("Bored cannon blocks drop scrap items.")
			.pointAt(util.vector.centerOf(5, 1, 1))
			.colored(PonderPalette.GREEN);
		scene.idle(20);
		
		scene.overlay.showControls(new InputWindowElement(util.vector.centerOf(5, 1, 1), Pointing.DOWN).withItem(CBCItems.CAST_IRON_NUGGET.asStack()), 40);
		scene.idle(67);
		
		scene.world.modifyBlock(util.grid.at(6, 1, 1), state -> boreBlock(state, CBCBlocks.CAST_IRON_CANNON_CHAMBER.getDefaultState()), false);
		scene.idle(10);
		
		scene.world.setKineticSpeed(drillGearDown, -16);
		scene.world.setKineticSpeed(drillGearUp, 32);
		scene.world.moveSection(drillPiston, util.vector.of(-3, 0, 0), 24);
		scene.idle(20);
		
		scene.markAsFinished();
	}
	
	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	private static BlockState boreBlock(BlockState state, BlockState newState) {
		return state.hasProperty(FACING) && newState.hasProperty(FACING) ? newState.setValue(FACING, state.getValue(FACING)) : newState;
	}
	
	public static void cannonBuilding(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/cannon_building", "Building Built-Up Cannons");
		scene.configureBasePlate(6, 0, 3);
		scene.world.showSection(util.select.cuboid(util.grid.zero(), util.grid.at(9, 0, 3)), Direction.UP);
		
		
		scene.markAsFinished();
	}
	
	public static void finishingBuiltUpCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/finishing_built_up_cannons", "Finishing Built-Up Cannons");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		scene.markAsFinished();
	}
	
}
