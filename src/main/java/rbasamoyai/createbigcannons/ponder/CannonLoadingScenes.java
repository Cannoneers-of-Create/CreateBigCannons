package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlock;

public class CannonLoadingScenes {

	public static void loadingBigCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_loader/loading_big_cannons", "Loading Big Cannons");
		scene.configureBasePlate(0, 0, 9);
		scene.scaleSceneView(0.8f);
		scene.showBasePlate();
		
		BlockPos loaderPos = util.grid.at(4, 1, 2);
		BlockPos crankPos = util.grid.at(3, 1, 2);
		scene.world.modifyBlocks(util.select.position(loaderPos), state -> state.setValue(CannonLoaderBlock.MOVING, true), false);
		scene.world.showSection(util.select.fromTo(crankPos, loaderPos), Direction.DOWN);
		ElementLink<WorldSectionElement> ramrod = scene.world.showIndependentSection(util.select.fromTo(4, 2, 0, 4, 2, 3), Direction.DOWN);
		scene.world.moveSection(ramrod, util.vector.of(0, -1, 0), 0);
		scene.idle(20);
		
		ElementLink<WorldSectionElement> cannon = scene.world.showIndependentSection(util.select.fromTo(4, 3, 6, 4, 3, 8), Direction.DOWN);
		scene.world.moveSection(cannon, util.vector.of(0, -2, 0), 0);
		scene.idle(10);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Cannon Loaders work like Mechanical Pistons, using Piston Extension Poles to extend their range.")
			.pointAt(util.vector.centerOf(loaderPos));
		scene.idle(90);
		BlockPos headPos = util.grid.at(4, 2, 3);
		BlockPos headScenePos = headPos.below();
		scene.overlay.showText(80)
			.text("They must be outfitted with a head to be useful, such as a Ram Head or a Worm Head.")
			.pointAt(util.vector.centerOf(headScenePos));
		scene.idle(90);
		scene.overlay.showText(80)
			.text("Under normal circumstances, the Ram Head should be used.")
			.pointAt(util.vector.centerOf(headScenePos));
		scene.idle(100);
		
		scene.world.modifyBlock(headPos, state -> {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			return CBCBlocks.WORM_HEAD.getDefaultState().setValue(BlockStateProperties.FACING, facing);
		}, true);
		scene.overlay.showText(80)
			.text("However, the Worm Head can be attached if the cannon needs to be unjammed.")
			.pointAt(util.vector.centerOf(headScenePos));
		scene.idle(100);
		
		scene.world.modifyBlock(headPos, state -> {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			return CBCBlocks.RAM_HEAD.getDefaultState().setValue(BlockStateProperties.FACING, facing);
		}, true);
		scene.idle(20);
		
		scene.world.moveSection(cannon, util.vector.of(0, 2, 0), 30);
		scene.idle(15);
		BlockPos leverPos = new BlockPos(4, 1, 6);
		ElementLink<WorldSectionElement> cannonMount = scene.world.showIndependentSection(util.select.fromTo(leverPos, leverPos.south()), Direction.UP);
		scene.world.modifyBlock(leverPos, state -> state.setValue(LeverBlock.POWERED, true), false);
		scene.idle(40);
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Before reloading a cannon, it must be disassembled.")
			.pointAt(util.vector.blockSurface(util.grid.at(4, 1, 7), Direction.NORTH));
		scene.idle(90);
		scene.world.modifyBlock(leverPos, state -> state.setValue(LeverBlock.POWERED, false), false);
		scene.idle(20);
		scene.world.hideIndependentSection(cannonMount, Direction.DOWN);
		scene.idle(10);
		scene.world.moveSection(cannon, util.vector.of(0, -2, 0), 30);
		scene.idle(40);
		
		scene.addKeyframe();
		ElementLink<WorldSectionElement> projectile = scene.world.showIndependentSection(util.select.position(4, 2, 4), Direction.DOWN);
		scene.world.moveSection(projectile, util.vector.of(0, -1, 0), 0);
		scene.idle(5);
		ElementLink<WorldSectionElement> powderCharge = scene.world.showIndependentSection(util.select.position(4, 2, 5), Direction.DOWN);
		scene.world.moveSection(powderCharge, util.vector.of(0, -1, 0), 0);
		scene.idle(25);
		
		AABB bb1 = new AABB(util.grid.at(4, 1, 0));
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb1, bb1, 1);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb1, bb1.expandTowards(0, 0, 8), 100);
		scene.idle(10);
		scene.overlay.showText(100)
			.text("When loading a cannon, the loader mechanism, munitions, and cannon must all be in line on the same axis.")
			.pointAt(util.vector.centerOf(4, 1, 4));
		scene.idle(110);
		
		
		scene.world.setKineticSpeed(util.select.position(loaderPos), 16.0f);
		scene.world.setKineticSpeed(util.select.position(crankPos), 16.0f);
		Vec3 loadMotion = util.vector.of(0, 0, 2);
		scene.world.moveSection(ramrod, loadMotion, 40);
		scene.world.moveSection(projectile, loadMotion, 40);
		scene.world.moveSection(powderCharge, loadMotion, 40);
		scene.idle(40);
		
		scene.world.setKineticSpeed(util.select.position(loaderPos), 0.0f);
		scene.world.setKineticSpeed(util.select.position(crankPos), 0.0f);
		scene.idle(10);
		scene.world.setKineticSpeed(util.select.position(loaderPos), -16.0f);
		scene.world.setKineticSpeed(util.select.position(crankPos), -16.0f);
		scene.world.moveSection(ramrod, loadMotion.reverse(), 40);
		scene.idle(40);
		
		scene.world.setKineticSpeed(util.select.position(loaderPos), 0.0f);
		scene.world.setKineticSpeed(util.select.position(crankPos), 0.0f);
		
		scene.idle(20);
		scene.markAsFinished();
	}
	
	public static void optimalCannonLoads(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_loader/optimal_cannon_loads", "Optimal Cannon Loads");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		
		
		scene.idle(20);
		scene.markAsFinished();
	}
	
}
