package rbasamoyai.createbigcannons.ponder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.redstone.analogLever.AnalogLeverBlockEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction.Emitter;
import com.simibubi.create.foundation.ponder.instruction.FadeOutOfSceneInstruction;
import com.simibubi.create.foundation.utility.Pointing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringBlockEntity;
import rbasamoyai.createbigcannons.effects.particles.plumes.AutocannonPlumeParticleData;
import rbasamoyai.createbigcannons.effects.particles.plumes.BigCannonPlumeParticleData;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CannonMountScenes {

	public static void assemblyAndUse(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/assembly_and_use", "Assembly and Use");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection cannonMount = util.select.position(2, 3, 2);

		List<ElementLink<WorldSectionElement>> setupBlocks = new ArrayList<>();

		Vec3 down = util.vector.of(0, -1, 0);

		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
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
		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 8.0f);
		});
		scene.world.rotateSection(cannon, 0, 0, -30, 40);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountPitch(util.grid.at(2, 3, 2), 30, 40));

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Power the Cannon Mount to aim the cannon up and down...")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 3, 2), Direction.SOUTH));
		scene.idle(40);
		scene.world.setKineticSpeed(util.select.position(1, 2, 2), 0.0f);
		scene.world.setKineticSpeed(util.select.position(1, 2, 3), 0.0f);
		scene.world.setKineticSpeed(util.select.position(2, 3, 3), 0.0f);
		scene.world.setKineticSpeed(cannonMount, 0.0f);
		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 0.0f);
		});

		scene.idle(50);

		scene.world.setKineticSpeed(util.select.position(1, 1, 1), -16.0f);
		scene.world.setKineticSpeed(util.select.position(2, 1, 2), 8.0f);
		scene.world.setKineticSpeed(util.select.position(1, 2, 1), -16.0f);

		scene.world.setKineticSpeed(util.select.position(2, 2, 2), 8.0f);
		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("YawSpeed", -8.0f);
		});
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountYaw(util.grid.at(2, 3, 2), -360, 200));

		scene.world.rotateSection(cannon, -30, 90, 30, 50); // W -> S
		scene.idle(20);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("...or side to side.")
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
		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("YawSpeed", 0.0f);
		});

		scene.markAsFinished();
	}

	public static void firingBigCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/firing_big_cannons", "Firing Big Cannons");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos fireLeverPos = util.grid.at(3, 2, 2);

		scene.world.modifyBlock(util.grid.at(1, 2, 2), setStateValue(LeverBlock.POWERED, true), false);
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
		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
			tag.putFloat("PitchSpeed", 8.0f);
			tag.putFloat("CannonYaw", Direction.WEST.toYRot());
			tag.putFloat("CannonPitch", 0);
		});
		scene.world.rotateSection(cannon, 0, 0, -30, 40);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountPitch(util.grid.at(2, 2, 2), 30, 40));
		scene.idle(40);
		scene.world.setKineticSpeed(util.select.position(1, 1, 2), 0.0f);
		scene.world.setKineticSpeed(util.select.position(1, 1, 3), 0.0f);
		scene.world.setKineticSpeed(util.select.position(2, 2, 3), 0.0f);
		scene.world.setKineticSpeed(cannonMount, 0.0f);
		scene.world.modifyBlockEntityNBT(cannonMount, CannonMountBlockEntity.class, tag -> {
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
		scene.world.modifyBlock(fireLeverPos, setStateValue(LeverBlock.POWERED, true), false);
		scene.effects.createRedstoneParticles(fireLeverPos, 0xFF0000, 10);
		scene.effects.emitParticles(util.vector.of(-0.2d, 6.25d, 2.5),
			Emitter.withinBlockSpace(new BigCannonPlumeParticleData(4), util.vector.of(-0.87d, 0.5d, 0.0d).scale(0.5)), 1, 1);
		scene.idle(60);

		scene.rotateCameraY(180.0f);
		scene.idle(40);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("To disassemble the cannon to prepare it for the next shot, unpower the hammer face.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.WEST));
		scene.idle(90);

		scene.world.modifyBlock(util.grid.at(1, 2, 2), setStateValue(LeverBlock.POWERED, false), false);
		scene.world.rotateSection(cannon, 0, 0, 30, 0);
		scene.idle(30);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("The cannon instantly snaps back to the position it was assembled at when disassembled.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 4, 2), Direction.WEST));
		scene.idle(100);

		scene.markAsFinished();
	}

	public static void upsideDownCannonMounts(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/upside_down_cannon_mounts", "Placing Cannon Mounts upside down");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.modifyBlockEntityNBT(util.select.position(2, 3, 2), CannonMountBlockEntity.class, tag -> {
			tag.putFloat("CannonYaw", Direction.WEST.toYRot());
			tag.putFloat("CannonPitch", 0);
		});

		scene.idle(20);
		scene.world.showSection(util.select.position(2, 3, 2), Direction.DOWN);
		scene.idle(30);
		scene.overlay.showText(80)
			.text("Cannon Mounts can be rotated to face downwards by clicking their sides with a wrench.");
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 3, 2), Direction.WEST), Pointing.LEFT)
			.withItem(AllItems.WRENCH.asStack()).rightClick(), 40);
		scene.idle(30);
		scene.world.modifyBlock(util.grid.at(2, 3, 2), setStateValue(BlockStateProperties.VERTICAL_DIRECTION, Direction.UP), false);
		scene.idle(30);
		scene.world.showSection(util.select.fromTo(1, 1, 2, 3, 1, 2), Direction.WEST);
		scene.overlay.showText(50)
			.text("Cannon Mounts can also be placed upside-down on the bottom of blocks.")
			.colored(PonderPalette.BLUE);

		scene.idle(30);
		scene.markAsFinished();
	}

	public static void usingExtensions(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/using_extensions", "Using Cannon Mount Extensions");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.showSection(util.select.fromTo(0, 3, 2, 3, 5, 2), Direction.DOWN);
		scene.idle(30);
		scene.world.showSection(util.select.fromTo(2, 1, 2, 4, 2, 3), Direction.WEST);
		scene.world.showSection(util.select.position(5, 0, 2), Direction.WEST);
		scene.idle(10);
		scene.world.showSection(util.select.fromTo(2, 3, 3, 2, 3, 4), Direction.WEST);
		scene.world.showSection(util.select.fromTo(2, 3, 5, 3, 0, 5), Direction.WEST);
		scene.idle(30);

		scene.overlay.showText(120)
			.text("Cannon Mount Extensions provide extra interface areas for blocks.");
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 2, 2), Direction.WEST), Pointing.LEFT)
			.withItem(AllBlocks.DISPLAY_LINK.asStack()), 40);
		scene.idle(65);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 3, 3), Direction.WEST), Pointing.LEFT)
			.withItem(AllBlocks.BRASS_FUNNEL.asStack()), 40);
		scene.idle(65);
		scene.overlay.showText(50)
			.attachKeyFrame()
			.text("They extend the mount they point into, as indicated by the arrow on their sides.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 3, 3), Direction.WEST));

		scene.idle(65);
		scene.markAsFinished();
	}

	public static void usingAutocannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/using_autocannons", "Using Autocannons");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.idle(20);

		scene.world.showSection(util.select.fromTo(1, 1, 1, 1, 2, 1).add(util.select.position(2, 1, 2)), Direction.DOWN);
		scene.idle(10);
		scene.world.showSection(util.select.fromTo(2, 2, 2, 2, 3, 2), Direction.DOWN);
		BlockPos assemblyLeverPos = util.grid.at(1, 3, 2);
		scene.world.showSection(util.select.position(assemblyLeverPos), Direction.DOWN);
		scene.idle(10);
		scene.world.showSection(util.select.fromTo(1, 2, 2, 1, 2, 3).add(util.select.position(2, 3, 3)), Direction.NORTH);
		scene.idle(10);

		scene.world.showSection(util.select.position(2, 4, 2), Direction.DOWN);
		scene.idle(10);
		scene.world.showSection(util.select.fromTo(2, 5, 2, 3, 5, 2), Direction.WEST);
		ElementLink<WorldSectionElement> autocannonBarrel = scene.world.showIndependentSection(util.select.fromTo(0, 5, 2, 1, 5, 2), Direction.WEST);
		scene.idle(20);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Autocannons fire at a fast rate.")
			.pointAt(util.vector.centerOf(2, 5, 2));
		scene.idle(95);
		scene.overlay.showText(100)
			.attachKeyFrame()
			.text("They take in item munitions as input.");
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 4, 2), Direction.WEST), Pointing.LEFT)
			.withItem(CBCItems.AUTOCANNON_CARTRIDGE.asStack()), 70);
		scene.idle(85);
		scene.overlay.showText(60)
			.text("Spent ammunition is ejected from the breech.")
			.colored(PonderPalette.RED)
			.pointAt(util.vector.blockSurface(util.grid.at(3, 4, 2), Direction.UP));
		scene.idle(75);

		scene.addKeyframe();
		scene.world.modifyBlock(assemblyLeverPos, setStateValue(LeverBlock.POWERED, true), false);
		scene.effects.createRedstoneParticles(assemblyLeverPos, 0xFF0000, 10);
		scene.idle(20);
		scene.rotateCameraY(180);
		scene.idle(20);
		BlockPos fireRateLever = util.grid.at(3, 3, 2);
		scene.world.showSection(util.select.position(fireRateLever), Direction.WEST);
		scene.idle(20);

		scene.overlay.showText(100)
			.text("Power the firing face on the Cannon Mount to make the autocannon shoot.")
			.pointAt(util.vector.blockSurface(fireRateLever, Direction.EAST));
		scene.idle(20);

		for (int i = 0; i < 8; ++i) {
			final int j = i + 1;
			scene.world.modifyBlockEntityNBT(util.select.position(fireRateLever), AnalogLeverBlockEntity.class, tag -> tag.putInt("State", j));
			scene.effects.createRedstoneParticles(assemblyLeverPos, 0xFF0000, 10);
			scene.idle(2);
		}

		Selection breech = util.select.position(3, 5, 2);
		Selection spring = util.select.position(2, 5, 2);

		Vec3 emitPos = util.vector.of(-0.2d, 5.5d, 2.5);
		Emitter emitter = Emitter.withinBlockSpace(new AutocannonPlumeParticleData(1f), util.vector.of(-1d, 0.0d, 0.0d));

		for (int i = 0; i < 5; ++i) {
			scene.effects.emitParticles(emitPos, emitter, 1, 1);
			scene.world.moveSection(autocannonBarrel, util.vector.of(0.5, 0, 0), 1);
			scene.world.modifyBlockEntityNBT(breech, AbstractAutocannonBreechBlockEntity.class, tag -> tag.putInt("AnimateTicks", 0));
			scene.world.modifyBlockEntityNBT(spring, AutocannonRecoilSpringBlockEntity.class, tag -> tag.putInt("AnimateTicks", 0));
			scene.addInstruction(CBCAnimateBlockEntityInstruction.autocannon(util.grid.at(2, 5, 2), 5));
			scene.addInstruction(CBCAnimateBlockEntityInstruction.autocannon(util.grid.at(3, 5, 2), 5));
			scene.idle(1);
			scene.world.moveSection(autocannonBarrel, util.vector.of(-0.5, 0, 0), 3);
			scene.idle(19);
		}

		scene.overlay.showText(100)
			.attachKeyFrame()
			.text("Increasing the power on the firing face increases the fire rate of the autocannon.")
			.pointAt(util.vector.blockSurface(fireRateLever, Direction.EAST));
		scene.idle(20);

		for (int i = 0; i < 7; ++i) {
			final int j = i + 9;
			scene.world.modifyBlockEntityNBT(util.select.position(fireRateLever), AnalogLeverBlockEntity.class, tag -> tag.putInt("State", j));
			scene.effects.createRedstoneParticles(assemblyLeverPos, 0xFF0000, 10);
			scene.idle(2);
		}

		for (int i = 0; i < 20; ++i) {
			scene.effects.emitParticles(emitPos, emitter, 1, 1);
			scene.world.moveSection(autocannonBarrel, util.vector.of(0.5, 0, 0), 1);
			scene.world.modifyBlockEntityNBT(breech, AbstractAutocannonBreechBlockEntity.class, tag -> tag.putInt("AnimateTicks", 0));
			scene.world.modifyBlockEntityNBT(spring, AutocannonRecoilSpringBlockEntity.class, tag -> tag.putInt("AnimateTicks", 0));
			scene.addInstruction(CBCAnimateBlockEntityInstruction.autocannon(util.grid.at(2, 5, 2), 5));
			scene.addInstruction(CBCAnimateBlockEntityInstruction.autocannon(util.grid.at(3, 5, 2), 5));
			scene.idle(1);
			scene.world.moveSection(autocannonBarrel, util.vector.of(-0.5, 0, 0), 3);
			scene.idle(3);
		}

		scene.idle(20);
		scene.markAsFinished();
	}

	public static void customizingAutocannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/customizing_autocannons", "Customizing Autocannons");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.idle(20);
		scene.world.showSection(util.select.position(2, 1, 2), Direction.DOWN);
		scene.idle(20);
		ElementLink<WorldSectionElement> autocannon = scene.world.showIndependentSection(util.select.fromTo(0, 3, 2, 4, 3, 2), Direction.WEST);
		scene.idle(20);

		scene.overlay.showText(80)
			.text("Autocannons can be customized in a few ways, both cosmetic and functional.");
		scene.idle(95);

		BlockPos barrelEnd = util.grid.at(0, 3, 2);
		scene.overlay.showText(100)
			.attachKeyFrame()
			.text("Wrenching the end of an autocannon will change the type of barrel end.")
			.pointAt(util.vector.centerOf(barrelEnd));
		scene.idle(20);
		InputWindowElement barrelWrench = new InputWindowElement(util.vector.topOf(barrelEnd), Pointing.DOWN).withWrench();
		scene.overlay.showControls(barrelWrench, 40);
		scene.idle(30);
		scene.world.modifyBlock(barrelEnd, setStateValue(AutocannonBarrelBlock.BARREL_END, AutocannonBarrelBlock.AutocannonBarrelEnd.FLANGED), false);
		scene.idle(25);
		scene.overlay.showControls(barrelWrench, 40);
		scene.idle(30);
		scene.world.modifyBlock(barrelEnd, setStateValue(AutocannonBarrelBlock.BARREL_END, AutocannonBarrelBlock.AutocannonBarrelEnd.NOTHING), false);
		scene.idle(25);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Recoil Springs make the barrel push back when firing.")
			.pointAt(util.vector.centerOf(2, 3, 2));
		scene.idle(90);

		scene.rotateCameraY(180);
		BlockPos breechPos = util.grid.at(3, 3, 2);
		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Wrenching the autocannon breech will add handles.")
			.pointAt(util.vector.topOf(breechPos));
		scene.idle(20);
		InputWindowElement breechWrench = new InputWindowElement(util.vector.topOf(breechPos), Pointing.DOWN).withWrench();
		scene.overlay.showControls(breechWrench, 40);
		scene.idle(30);
		scene.world.modifyBlock(breechPos, setStateValue(AutocannonBreechBlock.HANDLE, true), false);
		scene.idle(30);
		scene.overlay.showText(100)
			.text("Autocannons with handles are controlled by the player instead of by kinetics.");

		BlockPos mountPos = util.grid.at(2, 1, 2);

		scene.world.rotateSection(autocannon, 0, 0, -60, 40);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountPitch(mountPos, 60, 40));
		scene.idle(60);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountPitch(mountPos, -60, 40));
		scene.world.rotateSection(autocannon, 0, 0, 60, 40);
		scene.idle(60);

		scene.world.rotateSection(autocannon, 0, -30, 0, 20);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountYaw(mountPos, 30, 20));
		scene.idle(40);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountYaw(mountPos, -60, 40));
		scene.world.rotateSection(autocannon, 0, 60, 0, 40);
		scene.idle(60);
		scene.addInstruction(CBCAnimateBlockEntityInstruction.cannonMountYaw(mountPos, 30, 20));
		scene.world.rotateSection(autocannon, 0, -30, 0, 20);
		scene.idle(40);

		scene.overlay.showText(100)
			.attachKeyFrame()
			.text("You can place a seat on the handle breech.")
			.pointAt(util.vector.topOf(breechPos));
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(breechPos), Pointing.DOWN).rightClick(), 40);
		scene.idle(30);
		scene.world.modifyBlockEntityNBT(util.select.position(breechPos), AbstractAutocannonBreechBlockEntity.class, tag -> tag.putString("Seat", DyeColor.RED.getSerializedName()));
		scene.idle(70);
		scene.overlay.showText(60)
			.text("Seats are purely cosmetic.")
			.colored(PonderPalette.BLUE);
		scene.idle(80);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Wrenching the autocannon breech again will remove handles.")
			.pointAt(util.vector.topOf(breechPos));
		scene.idle(20);
		scene.overlay.showControls(breechWrench, 40);
		scene.idle(30);
		scene.world.modifyBlock(breechPos, setStateValue(AutocannonBreechBlock.HANDLE, false), false);
		scene.idle(30);
		scene.overlay.showText(60)
			.text("Any placed seats will be returned to the player or popped off.")
			.colored(PonderPalette.BLUE);
		scene.idle(30);

		scene.markAsFinished();
	}

	public static void usingFixedCannonMounts(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_mount/using_fixed_cannon_mounts", "Using Fixed Cannon Mounts");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.showSection(util.select.position(2, 1, 2), Direction.DOWN);
		ElementLink<WorldSectionElement> cannon = scene.world.showIndependentSection(util.select.fromTo(0, 2, 2, 4, 2, 2), Direction.DOWN);
		scene.idle(30);
		scene.overlay.showText(50)
			.text("Fixed Cannon Mounts can be used for aiming cannons at the same spot.");
		scene.idle(65);
		scene.overlay.showText(50)
			.text("They do not use kinetic input and must be manually set using the side interfaces.")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.NORTH));
		scene.overlay.showScrollInput(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.NORTH).add(-0.25, 0, 0), Direction.NORTH, 50);
		scene.overlay.showScrollInput(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.NORTH).add(0.25, 0, 0), Direction.NORTH, 50);
		scene.idle(30);
		scene.world.rotateSection(cannon, 0, 0, -15, 0);
		scene.idle(35);

		scene.overlay.showText(160)
			.attachKeyFrame()
			.text("Interfacing with a Fixed Cannon Mount is similar to a regular Cannon Mount, with the same control faces.");
		scene.idle(40);
		scene.overlay.showText(50)
			.text("Cannon assembly")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST))
			.colored(PonderPalette.GREEN)
			.placeNearTarget();
		scene.idle(55);
		scene.rotateCameraY(180);
		scene.overlay.showText(50)
			.text("Fire cannon")
			.pointAt(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.EAST))
			.colored(PonderPalette.RED)
			.placeNearTarget();
		scene.idle(55);
		scene.rotateCameraY(-180);
		scene.idle(25);

		scene.overlay.showText(50)
			.attachKeyFrame()
			.text("The Fixed Cannon Mount cannot be extended.")
			.colored(PonderPalette.RED);
		scene.idle(65);
		scene.overlay.showText(50)
			.text("Use a wrench to rotate the Fixed Cannon Mount.");
		scene.idle(65);

		scene.markAsFinished();
	}

	private static <T extends Comparable<T>> UnaryOperator<BlockState> setStateValue(Property<T> property, T value) {
		return state -> state.hasProperty(property) ? state.setValue(property, value) : state;
	}

}
