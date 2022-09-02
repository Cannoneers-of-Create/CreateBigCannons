package rbasamoyai.createbigcannons.ponder;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction.Emitter;
import com.simibubi.create.foundation.ponder.instruction.FadeOutOfSceneInstruction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannonmount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticleData;

public class CannonMountScenes {

	public static void assemblyAndUse(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/assembly_and_use", "Assembly and Use");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		Selection cannonMount = util.select.position(2, 3, 2);
		
		List<ElementLink<WorldSectionElement>> setupBlocks = new ArrayList<>();
		
		Vec3 down = util.vector.of(0, -1, 0);
		
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("CannonYaw", Direction.WEST.toYRot());
			tag.putFloat("CannonPitch", 0);
		});
		ElementLink<WorldSectionElement> cannonSetup = scene.world.showIndependentSection(util.select.fromTo(2, 2, 2, 2, 3, 2), Direction.DOWN);
		scene.world.moveSection(cannonSetup, down, 0);
		setupBlocks.add(cannonSetup);
		scene.idle(15);
		
		List<ElementLink<WorldSectionElement>> cannonPlacement = new ArrayList<>();
		BlockPos cannonStart = util.grid.at(4, 5, 2);
		for (int i = 0; i < 5; ++i) {
			ElementLink<WorldSectionElement> block = scene.world.showIndependentSection(util.select.position(cannonStart.west(i)), Direction.EAST);
			scene.world.moveSection(block, down, 0);
			scene.idle(5);
			cannonPlacement.add(block);
		}
		scene.idle(20);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Big cannons need to be placed on cannon mounts to be assembled.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.NORTH));
		scene.idle(90);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("To assemble a cannon, power the hammer face with a redstone signal.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.WEST));
		scene.idle(90);
		
		BlockPos leverPos = util.grid.at(1, 3, 2);
		ElementLink<WorldSectionElement> lever = scene.world.showIndependentSectionImmediately(util.select.position(leverPos));
		scene.world.moveSection(lever, down, 0);
		scene.idle(15);
		setupBlocks.add(lever);
		
		scene.world.modifyBlock(leverPos, state -> state.setValue(LeverBlock.POWERED, true), false);
		scene.effects.createRedstoneParticles(leverPos.below(), 0xFF0000, 10);
		Selection cannonSel = util.select.fromTo(0, 5, 2, 4, 5, 2);
		ElementLink<WorldSectionElement> cannon = scene.world.showIndependentSectionImmediately(cannonSel);
		scene.world.moveSection(cannon, down, 0);
		setupBlocks.add(cannon);
		for (ElementLink<WorldSectionElement> block : cannonPlacement) {
			scene.addInstruction(new FadeOutOfSceneInstruction<>(1, null, block));
		}
		scene.idle(20);
		
		Vec3 up = util.vector.of(0, 1, 0);
		for (ElementLink<WorldSectionElement> block : setupBlocks) {
			scene.world.moveSection(block, up, 15);
		}
		scene.idle(20);
		
		scene.world.showSection(util.select.fromTo(1, 1, 1, 2, 1, 2), Direction.UP);
		scene.world.showSection(util.select.position(1, 2, 1), Direction.UP);
		scene.idle(10);
		
		scene.rotateCameraY(-90.0f);
		scene.idle(10);
		
		scene.world.showSection(util.select.fromTo(1, 2, 3, 2, 3, 3), Direction.NORTH);
		scene.world.showSection(util.select.position(1, 2, 2), Direction.NORTH);
		scene.idle(20);
		
		scene.rotateCameraY(90.0f);
		scene.idle(20);
		
		scene.world.setKineticSpeed(util.select.position(1, 2, 2), 16.0f);
		scene.world.setKineticSpeed(util.select.position(1, 2, 3), 16.0f);
		scene.world.setKineticSpeed(util.select.position(2, 3, 3), -8.0f);
		scene.world.setKineticSpeed(cannonMount, -8.0f);
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 8.0f);
		});
		scene.world.rotateSection(cannon, 0, 0, -30, 40);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountPitch(util.grid.at(2, 3, 2), -30, 40));
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Power the Cannon Mount to aim the cannon up and down.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 3, 2), Direction.SOUTH));
		scene.idle(40);
		scene.world.setKineticSpeed(util.select.position(1, 2, 2), 0.0f);
		scene.world.setKineticSpeed(util.select.position(1, 2, 3), 0.0f);
		scene.world.setKineticSpeed(util.select.position(2, 3, 3), 0.0f);
		scene.world.setKineticSpeed(cannonMount, 0.0f);
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 0.0f);
		});
		
		scene.idle(50);
		
		scene.world.setKineticSpeed(util.select.position(1, 1, 1), 16.0f);
		scene.world.setKineticSpeed(util.select.position(2, 1, 2), -8.0f);
		scene.world.setKineticSpeed(util.select.position(1, 2, 1), 16.0f);
		
		scene.world.setKineticSpeed(util.select.position(2, 2, 2), -8.0f);
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("YawSpeed", -8.0f);
		});
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountYaw(util.grid.at(2, 3, 2), -360, 200));
		
		scene.world.rotateSection(cannon, -30, 90, 30, 50); // W -> S
		scene.idle(20);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("A Yaw Controller is needed to aim the cannon left and right.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.NORTH));
		
		scene.idle(30);
		scene.world.rotateSection(cannon, 30, 90, 30, 50); // S -> E
		scene.idle(50);
		scene.world.rotateSection(cannon, 30, 90, -30, 50); // E -> N
		scene.idle(50);
		scene.world.rotateSection(cannon, -30, 90, -30, 50); // N -> W
		scene.idle(50);
		
		scene.world.setKineticSpeed(util.select.layers(1, 2), 0.0f);
		scene.world.setKineticSpeed(util.select.position(2, 2, 2), 0.0f);
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("YawSpeed", 0.0f);
		});
		
		scene.markAsFinished();
	}
	
	public static void firingBigCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/firing_big_cannons", "Firing Big Cannons");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		BlockPos fireLeverPos = util.grid.at(3, 2, 2);
		
		scene.world.modifyBlock(util.grid.at(1, 2, 2), state -> state.setValue(LeverBlock.POWERED, true), false);
		Selection showSelection = util.select.fromTo(0, 1, 0, 4, 2, 4).substract(util.select.position(fireLeverPos));
		scene.world.showSection(showSelection, Direction.DOWN);
		scene.idle(40);
		ElementLink<WorldSectionElement> cannon = scene.world.showIndependentSection(util.select.fromTo(0, 4, 2, 4, 4, 2), Direction.WEST);
		scene.idle(30);
		
		Selection cannonMount = util.select.position(2, 2, 2);
		
		scene.world.setKineticSpeed(util.select.position(1, 1, 2), 16.0f);
		scene.world.setKineticSpeed(util.select.position(1, 1, 3), 16.0f);
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), -8.0f);
		scene.world.setKineticSpeed(cannonMount, -8.0f);
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 8.0f);
			tag.putFloat("CannonYaw", Direction.WEST.toYRot());
			tag.putFloat("CannonPitch", 0);
		});
		scene.world.rotateSection(cannon, 0, 0, -30, 40);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountPitch(util.grid.at(2, 2, 2), -30, 40));
		scene.idle(40);
		scene.world.setKineticSpeed(util.select.position(1, 1, 2), 0.0f);
		scene.world.setKineticSpeed(util.select.position(1, 1, 3), 0.0f);
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), 0.0f);
		scene.world.setKineticSpeed(cannonMount, 0.0f);
		scene.world.modifyTileNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 0.0f);
		});
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("After loading, assembling, and aiming the cannon, big cannons are ready to fire.");
		scene.idle(80);
		scene.rotateCameraY(180.0f);
		scene.idle(40);
		scene.overlay.showText(80)
			.text("Power the lit cannon face on the cannon mount to fire the mounted cannon.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.EAST));
		scene.idle(90);
		
		scene.world.showIndependentSectionImmediately(util.select.position(fireLeverPos));
		scene.idle(30);
		scene.world.modifyBlock(fireLeverPos, state -> state.setValue(LeverBlock.POWERED, true), false);
		scene.effects.createRedstoneParticles(fireLeverPos, 0xFF0000, 10);
		scene.effects.emitParticles(util.vector.of(-0.2d, 6.25d, 2.5), Emitter.withinBlockSpace(new CannonPlumeParticleData(2), util.vector.of(-0.87d, 0.5d, 0.0d)), 1, 10);
		scene.idle(60);
		
		scene.rotateCameraY(180.0f);
		scene.idle(40);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("To disassemble the cannon to prepare it for the next shot, unpower the hammer face.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.WEST));
		scene.idle(90);
		
		scene.world.modifyBlock(util.grid.at(1, 2, 2), state -> state.setValue(LeverBlock.POWERED, false), false);
		scene.world.rotateSection(cannon, 0, 0, 30, 0);
		scene.idle(30);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("The cannon instantly snaps back to the position it was assembled at when disassembled.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 4, 2), Direction.WEST));
		scene.idle(100);
		
		scene.markAsFinished();
	}
	
}
