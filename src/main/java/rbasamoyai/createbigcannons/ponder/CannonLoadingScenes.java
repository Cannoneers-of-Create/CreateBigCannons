package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity.Phase;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction.Emitter;
import com.simibubi.create.foundation.utility.Pointing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntity;
import rbasamoyai.createbigcannons.effects.particles.plumes.BigCannonPlumeParticleData;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

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
			.pointAt(util.vector.centerOf(headScenePos))
			.colored(PonderPalette.GREEN);
		scene.idle(100);

		scene.world.modifyBlock(headPos, state -> {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			return CBCBlocks.WORM_HEAD.getDefaultState().setValue(BlockStateProperties.FACING, facing);
		}, true);
		scene.overlay.showText(80)
			.text("However, the Worm Head can be attached if the cannon needs to be unjammed.")
			.pointAt(util.vector.centerOf(headScenePos))
			.colored(PonderPalette.BLUE);
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

	public static void baseContraptionLoadingBigCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_loader/base_contraption_loading", "Loading Big Cannons with base Create contraptions");
		scene.configureBasePlate(0, 0, 9);
		scene.showBasePlate();

		scene.world.showSection(util.select.fromTo(1, 1, 6, 1, 1, 8), Direction.UP);
		scene.world.showSection(util.select.fromTo(4, 1, 6, 4, 1, 8), Direction.UP);
		scene.world.showSection(util.select.fromTo(7, 1, 4, 7, 3, 4), Direction.UP);
		scene.idle(30);

		Selection pistonSelection = util.select.fromTo(0, 1, 3, 1, 1, 3);
		scene.world.showSection(pistonSelection, Direction.WEST);
		ElementLink<WorldSectionElement> pistonElement = scene.world.showIndependentSection(util.select.fromTo(1, 2, 1, 1, 2, 3), Direction.WEST);
		scene.world.moveSection(pistonElement, util.vector.of(0, -1, 0), 0);
		scene.idle(5);
		ElementLink<WorldSectionElement> pistonMunitionsElement = scene.world.showIndependentSection(util.select.fromTo(1, 2, 4, 1, 2, 5), null);
		scene.world.moveSection(pistonMunitionsElement, util.vector.of(0, -1, 0), 0);
		scene.idle(5);

		Selection gantryShaftSelection = util.select.fromTo(5, 1, 0, 5, 1, 5);
		scene.world.showSection(gantryShaftSelection, Direction.WEST);
		ElementLink<WorldSectionElement> gantryElement = scene.world.showIndependentSection(util.select.position(4, 1, 1), Direction.WEST);
		scene.idle(5);
		ElementLink<WorldSectionElement> gantryMunitionsElement = scene.world.showIndependentSection(util.select.fromTo(4, 1, 2, 4, 1, 3), null);
		scene.idle(5);

		Selection pulleySelection = util.select.fromTo(6, 6, 4, 7, 6, 4);
		scene.world.showSection(pulleySelection, Direction.DOWN);
		scene.idle(5);
		ElementLink<WorldSectionElement> pulleyMunitionsElement = scene.world.showIndependentSection(util.select.position(7, 4, 4), null);
		ElementLink<WorldSectionElement> pulleyMunitionsElementExtract = scene.world.showIndependentSection(util.select.position(7, 5, 4), null);
		scene.idle(20);

		scene.overlay.showText(60)
			.text("Mechanical Pistons, Gantries, and Rope Pulleys can also load big cannons.")
			.attachKeyFrame();
		scene.idle(20);

		scene.world.setKineticSpeed(pistonSelection, 16);
		scene.world.moveSection(pistonElement, util.vector.of(0, 0, 2), 40);
		scene.world.moveSection(pistonMunitionsElement, util.vector.of(0, 0, 2), 40);
		scene.idle(10);
		scene.world.setKineticSpeed(gantryShaftSelection, -16);
		scene.world.moveSection(gantryElement, util.vector.of(0, 0, 4), 80);
		scene.world.moveSection(gantryMunitionsElement, util.vector.of(0, 0, 4), 80);
		scene.idle(10);
		scene.world.setKineticSpeed(pulleySelection, 16);
		scene.world.movePulley(util.grid.at(7, 6, 4), 2, 40);
		scene.world.moveSection(pulleyMunitionsElement, util.vector.of(0, -2, 0), 40);
		scene.world.moveSection(pulleyMunitionsElementExtract, util.vector.of(0, -2, 0), 40);
		scene.idle(20);

		scene.world.setKineticSpeed(pistonSelection, 0);
		scene.world.hideIndependentSectionImmediately(pistonMunitionsElement);
		scene.idle(20);
		scene.world.setKineticSpeed(pulleySelection, 0);
		scene.world.hideIndependentSectionImmediately(pulleyMunitionsElement);
		scene.idle(30);
		scene.world.setKineticSpeed(gantryShaftSelection, 0);
		scene.world.hideIndependentSectionImmediately(gantryMunitionsElement);
		scene.idle(15);

		scene.overlay.showText(50)
			.text("Munition blocks can be pulled out of big cannons depending on the connectivity of certain blocks.")
			.colored(PonderPalette.BLUE)
			.attachKeyFrame();
		scene.idle(15);

		scene.world.setKineticSpeed(pistonSelection, -16);
		scene.world.moveSection(pistonElement, util.vector.of(0, 0, -2), 40);
		scene.world.setKineticSpeed(gantryShaftSelection, 16);
		scene.world.moveSection(gantryElement, util.vector.of(0, 0, -4), 80);
		scene.world.setKineticSpeed(pulleySelection, -16);
		scene.world.movePulley(util.grid.at(7, 6, 4), -2, 40);
		scene.world.moveSection(pulleyMunitionsElementExtract, util.vector.of(0, 2, 0), 40);
		scene.idle(10);

		scene.overlay.showOutline(PonderPalette.RED, new Object(), util.select.fromTo(7, 3, 4, 7, 6, 4), 30);
		scene.idle(30);
		scene.world.setKineticSpeed(pistonSelection, 0);
		scene.world.setKineticSpeed(pulleySelection, 0);
		scene.idle(40);
		scene.world.setKineticSpeed(gantryShaftSelection, 0);
		scene.idle(30);

		scene.overlay.showText(50)
			.text("Unlike the Cannon Loader, other blocks can still be attached to the contraption.")
			.attachKeyFrame();
		scene.world.setBlock(util.grid.at(1, 2, 3), AllBlocks.MECHANICAL_PISTON_HEAD.getDefaultState()
			.setValue(BlockStateProperties.FACING, Direction.SOUTH).setValue(PistonHeadBlock.TYPE, PistonType.STICKY), false);
		scene.effects.emitParticles(util.vector.blockSurface(util.grid.at(1, 1, 3), Direction.SOUTH), Emitter.simple(ParticleTypes.ITEM_SLIME, Vec3.ZERO), 8, 1);
		scene.world.setBlock(util.grid.at(1, 2, 4), Blocks.OAK_PLANKS.defaultBlockState(), false);
		scene.idle(20);

		scene.overlay.showOutline(PonderPalette.WHITE, new Object(), util.select.fromTo(1, 1, 3, 1, 1, 5), 20);
		pistonMunitionsElement = scene.world.showIndependentSectionImmediately(util.select.position(1, 2, 5));
		ElementLink<WorldSectionElement> pistonBlockElement = scene.world.showIndependentSectionImmediately(util.select.position(1, 2, 4));
		scene.world.moveSection(pistonMunitionsElement, util.vector.of(0, -1, 0), 0);
		scene.world.moveSection(pistonBlockElement, util.vector.of(0, -1, 0), 0);
		scene.idle(50);

		scene.overlay.showText(50)
			.text("This does not affect cannon loading in any way, although only aligned munition blocks can be inserted into big cannons.");
		scene.idle(15);
		scene.world.setKineticSpeed(pistonSelection, 16);
		scene.world.moveSection(pistonElement, util.vector.of(0, 0, 1), 20);
		scene.world.moveSection(pistonBlockElement, util.vector.of(0, 0, 1), 20);
		scene.world.moveSection(pistonMunitionsElement, util.vector.of(0, 0, 1), 20);
		scene.idle(20);
		scene.world.setKineticSpeed(pistonSelection, 0);
		scene.world.hideIndependentSectionImmediately(pistonMunitionsElement);
		scene.idle(10);
		scene.world.setKineticSpeed(pistonSelection, -16);
		scene.world.moveSection(pistonElement, util.vector.of(0, 0, -1), 20);
		scene.world.moveSection(pistonBlockElement, util.vector.of(0, 0, -1), 20);
		scene.idle(20);
		scene.world.setKineticSpeed(pistonSelection, 0);

		scene.idle(20);
		scene.markAsFinished();
	}

	public static void cannonLoads(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/cannon_loads", "Cannon Loads");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection cannon = util.select.fromTo(0, 1, 2, 4, 1, 2);
		scene.world.showSection(cannon, Direction.DOWN);
		scene.idle(40);
		scene.overlay.showText(100).text("When loading a cannon, care must be taken to ensure that cannon loads are safe and effective.");
		scene.idle(110);

		scene.overlay.showText(80).text("A cannon's material has two main factors that can cause the cannon to fail; its minimum projectile velocity per barrel and its strength.").colored(PonderPalette.RED);
		scene.idle(90);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("The minimum projectile velocity per barrel determines how much power a projectile should have to not get stuck in a cannon.");
		scene.idle(90);

		Selection safeLoad = util.select.fromTo(0, 2, 2, 3, 2, 2);
		scene.world.showSection(safeLoad, Direction.UP);
		scene.overlay.showText(80).text("For example, the default minimum projectile velocity of cast iron is equivalent to 1 Powder Charge for every barrel.");
		scene.idle(20);

		AABB bb1 = new AABB(util.grid.at(3, 2, 2));
		AABB bb2 = new AABB(util.grid.at(1, 1, 2));
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb1, bb1, 20);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb2, bb2, 20);
		scene.idle(20);

		AABB bb3 = bb1.move(util.vector.of(-1, 0, 0));
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb1, bb3, 20);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb2, bb2.move(util.vector.of(-1, 0, 0)), 20);

		scene.idle(40);

		scene.overlay.showText(80).text("The barrel that the loaded projectile is in is also counted towards the barrels travelled.");
		scene.idle(120);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("The strength of a cannon determines the maximum amount of Powder Charges that can be loaded, after which the cannon has a chance to burst when firing.");
		scene.idle(90);

		scene.overlay.showText(80)
			.text("For example, cast iron is strong enough to safely handle 2 Powder Charges.");
		scene.idle(20);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb1, bb1, 20);
		scene.idle(20);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, bb1, bb3, 20);
		scene.idle(70);
		scene.world.hideSection(safeLoad, Direction.UP);
		scene.idle(20);

		scene.overlay.showSelectionWithText(util.select.position(4, 1, 2), 60)
			.text("The strength of a cannon can be affected by its breech.")
			.colored(PonderPalette.RED)
			.placeNearTarget()
			.pointAt(util.vector.centerOf(4, 1, 2));
		scene.idle(90);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Unsafe loads in a cannon...")
			.colored(PonderPalette.RED);
		scene.idle(90);

		scene.overlay.showText(80)
			.text("...such as not loading enough Powder Charges...");
		ElementLink<WorldSectionElement> notEnoughCharges = scene.world.showIndependentSection(util.select.fromTo(2, 2, 1, 3, 2, 1), Direction.DOWN);
		scene.world.moveSection(notEnoughCharges, util.vector.of(0, 0, 1), 0);
		scene.idle(70);
		scene.world.hideIndependentSection(notEnoughCharges, Direction.UP);
		scene.idle(20);

		scene.overlay.showText(80)
			.text("...loading too many Powder Charges...");
		ElementLink<WorldSectionElement> tooManyCharges = scene.world.showIndependentSection(util.select.fromTo(0, 2, 4, 3, 2, 4), Direction.DOWN);
		scene.world.moveSection(tooManyCharges, util.vector.of(0, 0, -2), 0);
		scene.idle(70);
		scene.world.hideIndependentSection(tooManyCharges, Direction.UP);
		scene.idle(20);

		scene.overlay.showText(80)
			.text("...allowing a fired projectile to collide with another object in the barrel...");
		ElementLink<WorldSectionElement> obstructedLoad = scene.world.showIndependentSection(util.select.fromTo(0, 2, 3, 3, 2, 3), Direction.DOWN);
		scene.world.moveSection(obstructedLoad, util.vector.of(0, 0, -1), 0);
		scene.idle(70);
		scene.world.hideIndependentSection(obstructedLoad, Direction.DOWN);
		scene.idle(20);

		scene.overlay.showText(80).text("...can cause catastrophic failure and pose a major threat to the surrounding environment.").colored(PonderPalette.RED);
		scene.idle(20);
		scene.world.hideSection(cannon, null);
		scene.effects.emitParticles(util.vector.centerOf(2, 1, 2), Emitter.simple(ParticleTypes.EXPLOSION_EMITTER, util.vector.of(0, 0, 0)), 1, 10);
		scene.idle(80);

		scene.markAsFinished();
	}

	public static void wetAmmoStorage(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/wet_ammo_storage", "Protecting Munitions with Waterlogging, and its Side Effects");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection sel = util.select.fromTo(1, 1, 2, 3, 1, 2);
		scene.idle(15);
		scene.world.showSection(sel, Direction.DOWN);
		scene.idle(30);
		scene.overlay.showText(50)
			.text("Propellant blocks, as well as projectiles, are vulnerable to exploding from fire and other explosions.");
		scene.idle(65);
		scene.overlay.showText(70)
			.text("Munition blocks can be protected from fire and explosions by waterlogging them.")
			.pointAt(util.vector.centerOf(2, 1, 2));
		scene.idle(30);
		scene.world.modifyBlocks(sel, state -> state.setValue(BigCannonMunitionBlock.WATERLOGGED, true)
			.setValue(BigCannonMunitionBlock.DAMP, true), false);
		Emitter splashEmitter = Emitter.simple(ParticleTypes.SPLASH, util.vector.of(0, 0.05, 0));
		Vec3 blockSurface1 = util.vector.centerOf(util.grid.at(1, 1, 2));
		Vec3 blockSurface2 = util.vector.centerOf(util.grid.at(2, 1, 2));
		Vec3 blockSurface3 = util.vector.centerOf(util.grid.at(3, 1, 2));
		scene.effects.emitParticles(blockSurface1.add(0, 0.5, 0), splashEmitter, 5, 2);
		scene.effects.emitParticles(blockSurface2.add(0, 0.5, 0), splashEmitter, 5, 2);
		scene.effects.emitParticles(blockSurface3.add(0, 0.5, 0), splashEmitter, 5, 2);
		scene.idle(55);

		scene.overlay.showOutline(PonderPalette.RED, new Object(), sel, 80);
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("However, waterlogging causes propellant blocks to become damp.")
			.colored(PonderPalette.RED)
			.pointAt(util.vector.centerOf(2, 1, 2));

		Emitter moisture = Emitter.withinBlockSpace(ParticleTypes.DRIPPING_WATER, util.vector.of(0, 1e-5, 0));
		for (int i = 0; i < 3; ++i) {
			scene.idle(30);
			if (i == 0)
				scene.world.modifyBlocks(sel, state -> state.setValue(BigCannonMunitionBlock.WATERLOGGED, false), false);
			scene.effects.emitParticles(blockSurface1, moisture, 3, 2);
			scene.effects.emitParticles(blockSurface2, moisture, 3, 2);
			scene.effects.emitParticles(blockSurface3, moisture, 3, 2);
		}

		scene.idle(5);
		scene.overlay.showText(50)
			.attachKeyFrame()
			.text("Damp propellant is weaker and also fails to ignite if it is placed as the first propellant block in a big cannon.")
			.colored(PonderPalette.RED);
		scene.idle(30);
		scene.effects.emitParticles(blockSurface1, moisture, 3, 2);
		scene.effects.emitParticles(blockSurface2, moisture, 3, 2);
		scene.effects.emitParticles(blockSurface3, moisture, 3, 2);
		scene.idle(35);
		scene.overlay.showText(50)
			.text("To fix this, damp propellant must be left to dry, and cannot be dried in a furnace like wet sponges.")
			.colored(PonderPalette.BLUE);
		scene.idle(30);
		scene.effects.emitParticles(blockSurface1, moisture, 3, 2);
		scene.effects.emitParticles(blockSurface2, moisture, 3, 2);
		scene.effects.emitParticles(blockSurface3, moisture, 3, 2);
		scene.idle(35);

		scene.world.modifyBlocks(util.select.fromTo(0, 0, 0, 4, 0, 4), state -> {
			if (state.getBlock() == Blocks.SNOW_BLOCK)
				return Blocks.NETHERRACK.defaultBlockState();
			if (state.getBlock() == Blocks.WHITE_CONCRETE)
				return Blocks.BLACKSTONE.defaultBlockState();
			return state;
		}, true);
		scene.idle(30);
		scene.overlay.showText(50)
			.attachKeyFrame()
			.text("However, propellant placed in hot places like the Nether will instantly dry, similar to wet sponges.")
			.colored(PonderPalette.GREEN);
		scene.idle(65);

		scene.markAsFinished();
	}

	public static void fuzingMunitions(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/fuzing_munitions", "Fuzing Munitions");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos munitionPos = util.grid.at(2, 1, 3);
		Selection munitionSel = util.select.position(munitionPos);
		scene.idle(20);
		scene.world.showSection(munitionSel, Direction.NORTH);
		scene.idle(30);

		scene.overlay.showText(80)
			.text("Fuzes can be attached to certain projectiles to detonate them under certain conditions.")
			.pointAt(util.vector.centerOf(2, 1, 3));
		scene.idle(40);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(munitionPos, Direction.NORTH), Pointing.DOWN)
			.rightClick()
			.withItem(CBCItems.IMPACT_FUZE.asStack()), 60);
		scene.idle(20);
		scene.world.modifyBlockEntityNBT(munitionSel, FuzedBlockEntity.class, tag -> tag.put("Fuze", CBCItems.IMPACT_FUZE.asStack().save(new CompoundTag())));
		scene.idle(50);

		scene.overlay.showText(100)
			.attachKeyFrame()
			.text("Right-click the projectile head with an empty hand to remove any fuzes present. Some shells are fuzed on the base instead.")
			.pointAt(util.vector.centerOf(2, 1, 3));
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(munitionPos, Direction.NORTH), Pointing.DOWN).rightClick(), 60);
		scene.idle(20);
		scene.world.modifyBlockEntityNBT(munitionSel, FuzedBlockEntity.class, tag -> tag.remove("Fuze"));
		scene.idle(80);

		Selection kineticSel = util.select.fromTo(2, 1, 1, 5, 1, 1);
		Selection largeCog = util.select.position(5, 0, 2);
		scene.world.showSection(kineticSel, Direction.WEST);
		scene.world.showSection(largeCog, Direction.WEST);

		BlockPos deployerPos = util.grid.at(2, 1, 1);
		scene.world.modifyBlockEntityNBT(util.select.position(deployerPos), DeployerBlockEntity.class, tag -> tag.put("HeldItem", CBCItems.TIMED_FUZE.asStack().save(new CompoundTag())));

		scene.world.setKineticSpeed(kineticSel, 32.0f);
		scene.world.setKineticSpeed(largeCog, -16.0f);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Fuzing projectiles can be automated with Deployers.")
			.pointAt(util.vector.centerOf(deployerPos));
		scene.idle(90);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(26);
		scene.world.modifyBlockEntityNBT(util.select.position(deployerPos), DeployerBlockEntity.class, tag -> tag.put("HeldItem", ItemStack.EMPTY.save(new CompoundTag())));
		scene.world.modifyBlockEntityNBT(munitionSel, FuzedBlockEntity.class, tag -> tag.put("Fuze", CBCItems.TIMED_FUZE.asStack().save(new CompoundTag())));
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(46);

		scene.markAsFinished();
	}

	public static void handloadingTools(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_loader/handloading_tools", "Handloading Tools");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.showSection(util.select.fromTo(2, 1, 2, 2, 1, 4), Direction.UP);
		scene.idle(30);

		scene.overlay.showText(100)
			.text("Handloading tools are convenient for loading big cannons.");
		scene.idle(40);

		scene.addKeyframe();

		ElementLink<WorldSectionElement> munition = scene.world.showIndependentSection(util.select.position(2, 1, 1), Direction.SOUTH);
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 1, 1), Direction.NORTH), Pointing.RIGHT)
			.withItem(CBCItems.RAM_ROD.asStack()), 30);
		scene.idle(40);
		scene.world.moveSection(munition, util.vector.of(0, 0, 1.2), 20);
		scene.idle(40);

		scene.addKeyframe();

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.NORTH), Pointing.RIGHT)
			.withItem(CBCItems.WORM.asStack()), 30);
		scene.idle(40);
		scene.world.moveSection(munition, util.vector.of(0, 0, -1.2), 20);
		scene.idle(40);
		scene.world.hideIndependentSection(munition, Direction.UP);

		scene.overlay.showText(60)
			.text("Munitions can also be inserted directly into assembled big cannons.")
			.colored(PonderPalette.BLUE)
			.attachKeyFrame();
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.NORTH), Pointing.RIGHT)
			.withItem(CBCBlocks.POWDER_CHARGE.asStack()), 30);
		scene.idle(40);

		scene.overlay.showText(60)
			.text("Handloading tools can also interact with assembled big cannons.")
			.colored(PonderPalette.GREEN)
			.attachKeyFrame();
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.NORTH), Pointing.RIGHT)
			.withItem(CBCItems.RAM_ROD.asStack()), 30);
		scene.idle(50);

		scene.markAsFinished();
	}

	public static void quickFiringBreech(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_kinetics/quick_firing_breech", "Loading a Quick-Firing Breech");
		scene.configureBasePlate(1, 0, 7);
		scene.showBasePlate();
		scene.idle(20);

		BlockPos breechPos = util.grid.at(4, 3, 2);
		Selection breechSel = util.select.position(breechPos);
		scene.world.modifyBlockEntityNBT(breechSel, QuickfiringBreechBlockEntity.class, compoundTag -> {
			compoundTag.putBoolean("InPonder", true);
		});

		scene.world.showSection(util.select.fromTo(breechPos, util.grid.at(4, 1, 4)), Direction.DOWN);
		scene.idle(20);

		scene.overlay.showText(60)
			.text("The Quick-Firing Breech allows fast reloading of assembled big cannons.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(80);

		BlockPos assembleLeverPos = util.grid.at(4, 1, 2);
		BlockPos fireLeverPos = util.grid.at(4, 1, 4);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(assembleLeverPos.west(), Direction.EAST), Pointing.LEFT)
			.rightClick(), 40);

		scene.world.modifyBlock(assembleLeverPos, state -> state.setValue(LeverBlock.POWERED, true), false);
		scene.effects.createRedstoneParticles(assembleLeverPos, 0xFF0000, 10);

		scene.idle(20);

		scene.overlay.showText(60)
			.text("The Quick-Firing Breech can be opened and closed by right clicking the side of the breech.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos.east(), Direction.WEST), Pointing.RIGHT)
			.rightClick(), 40);
		scene.idle(40);
		animateQFBHack(scene, breechSel, false);
		scene.idle(35);

		scene.overlay.showText(60)
			.text("First, load the projectile by right clicking the open side of the breech.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(80);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos.west(), Direction.EAST), Pointing.LEFT)
			.rightClick()
			.withItem(CBCBlocks.SOLID_SHOT.asStack()), 40);
		scene.idle(60);

		scene.overlay.showText(60)
			.text("Then load the propellant and close the breech.")
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(80);

		scene.overlay.showText(60)
			.text("Big Cartridges are recommended for faster reloading.")
			.colored(PonderPalette.BLUE)
			.pointAt(util.vector.centerOf(breechPos));
		scene.idle(30);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos.west(), Direction.EAST), Pointing.LEFT)
			.rightClick()
			.withItem(BigCartridgeBlockItem.getWithPower(4)), 30);
		scene.idle(40);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos.east(), Direction.WEST), Pointing.RIGHT)
			.rightClick(), 20);
		animateQFBHack(scene, breechSel, true);
		scene.idle(35);

		scene.overlay.showText(40)
			.attachKeyFrame()
			.text("The big cannon can then be fired.");
		scene.idle(60);

		scene.rotateCameraY(180.0f);
		scene.idle(20);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(fireLeverPos, Direction.WEST), Pointing.RIGHT)
			.rightClick(), 40);
		scene.idle(60);

		scene.world.modifyBlock(fireLeverPos, state -> state.setValue(LeverBlock.POWERED, true), false);
		scene.effects.createRedstoneParticles(fireLeverPos, 0xFF0000, 10);
		scene.effects.emitParticles(util.vector.of(4, 2.5, 5.1), Emitter.withinBlockSpace(new BigCannonPlumeParticleData(1), util.vector.of(0d, 0d, 1d)), 1, 10);
		scene.idle(60);

		scene.rotateCameraY(180.0f);
		scene.idle(20);

		scene.overlay.showText(60)
			.text("Remaining munitions such as spent Big Cartridge cases will be automatically ejected.")
			.colored(PonderPalette.GREEN)
			.pointAt(util.vector.centerOf(breechPos))
			.attachKeyFrame();

		animateQFBHack(scene, breechSel, false);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos.east(), Direction.EAST), Pointing.RIGHT)
			.rightClick(), 40);
		scene.idle(60);
		animateQFBHack(scene, breechSel, true);
		scene.idle(40);

		scene.markAsFinished();
	}

	public static void automatingQuickFiringBreeches(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_kinetics/automating_quick_firing_breeches", "Automating Quick-Firing Breeches");
		scene.configureBasePlate(1, 0, 7);
		scene.showBasePlate();
		scene.idle(20);

		BlockPos breechPos = util.grid.at(4, 3, 2);
		Selection breechSel = util.select.position(breechPos);
		scene.world.modifyBlockEntityNBT(breechSel, QuickfiringBreechBlockEntity.class, compoundTag -> {
			compoundTag.putBoolean("InPonder", true);
		});

		scene.world.showSection(util.select.fromTo(breechPos, util.grid.at(4, 1, 4)), Direction.DOWN);
		scene.idle(20);

		Selection cannonMount = util.select.position(4, 1, 3);
		BlockPos cannonMountPos = util.grid.at(4, 1, 3);

		Selection largeGear = util.select.position(0, 1, 4);
		Selection smallGear = util.select.position(1, 1, 3);
		BlockPos mechanicalArmPos = util.grid.at(2, 1, 3);
		Selection mechanicalArm = util.select.position(mechanicalArmPos);
		Selection depots = util.select.fromTo(1, 1, 6, 2, 2, 6);

		scene.world.showSection(largeGear, Direction.DOWN);
		scene.idle(5);
		scene.world.showSection(smallGear, Direction.DOWN);
		scene.world.showSection(mechanicalArm, Direction.DOWN);
		scene.idle(5);
		scene.world.showSection(depots, Direction.DOWN);
		scene.idle(15);
		scene.world.setKineticSpeed(largeGear, -16);
		scene.world.setKineticSpeed(smallGear, 32);
		scene.world.setKineticSpeed(mechanicalArm, -32);
		scene.idle(25);

		scene.idle(20);

		scene.overlay.showSelectionWithText(mechanicalArm, 80)
			.attachKeyFrame()
			.colored(PonderPalette.RED)
			.text("Mechanical Arms can be used to reload the Quick-Firing Breech.")
			.placeNearTarget();
		scene.idle(100);

		scene.overlay.showSelectionWithText(cannonMount, 60)
			.colored(PonderPalette.OUTPUT)
			.text("Right-click the Cannon Mount to set the arm's output.")
			.placeNearTarget();
		scene.idle(80);

		Selection shotDepot = util.select.position(1, 1, 6);
		BlockPos shotDepotPos = util.grid.at(1, 1, 6);
		ItemStack shot = new ItemStack(CBCBlocks.SOLID_SHOT.get());
		scene.world.createItemOnBeltLike(shotDepotPos, Direction.SOUTH, shot);
		scene.overlay.showOutline(PonderPalette.INPUT, null, shotDepot, 40);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(1, 1, 6), Direction.UP), Pointing.DOWN)
			.withItem(CBCBlocks.SOLID_SHOT.asStack()), 40);

		scene.idle(60);

		Selection cartridgeDepot = util.select.position(2, 1, 6);
		BlockPos cartridgeDepotPos = util.grid.at(2, 1, 6);
		ItemStack cartridge = BigCartridgeBlockItem.getWithPower(4);
		scene.world.createItemOnBeltLike(cartridgeDepotPos, Direction.SOUTH, cartridge);
		scene.overlay.showOutline(PonderPalette.INPUT, null, cartridgeDepot, 40);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 1, 6), Direction.UP), Pointing.DOWN)
			.withItem(cartridge), 40);

		scene.idle(60);

		scene.overlay.showText(50)
			.text("The Quick-Firing Breech must be closed in order for the arm to operate.")
			.colored(PonderPalette.RED)
			.attachKeyFrame();
		scene.idle(75);
		scene.overlay.showText(50)
			.text("The arm will not load the big cannon if the breech is open.")
			.colored(PonderPalette.RED);
		scene.idle(75);

		scene.world.instructArm(mechanicalArmPos, Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
		scene.idle(30);
		scene.world.removeItemsFromBelt(shotDepotPos);
		scene.world.instructArm(mechanicalArmPos, Phase.SEARCH_OUTPUTS, shot, -1);
		scene.idle(20);
		scene.world.instructArm(mechanicalArmPos, Phase.MOVE_TO_OUTPUT, shot, 0);
		scene.idle(30);
		scene.world.createItemOnBeltLike(cannonMountPos, Direction.UP, shot);
		scene.world.instructArm(mechanicalArmPos, Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
		scene.idle(70);

		scene.world.instructArm(mechanicalArmPos, Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 1);
		scene.idle(30);
		scene.world.removeItemsFromBelt(cartridgeDepotPos);
		scene.world.instructArm(mechanicalArmPos, Phase.SEARCH_OUTPUTS, cartridge, -1);
		scene.idle(20);
		scene.world.instructArm(mechanicalArmPos, Phase.MOVE_TO_OUTPUT, cartridge, 0);
		scene.idle(30);
		scene.world.createItemOnBeltLike(cannonMountPos, Direction.UP, cartridge);
		scene.world.instructArm(mechanicalArmPos, Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
		scene.idle(30);

		scene.overlay.showText(60)
			.text("Once the cannon fires, the arm will automatically extract empty cartridges when provided a deposit area.")
			.pointAt(util.vector.centerOf(3, 2, 6))
			.attachKeyFrame();
		scene.world.showSection(util.select.fromTo(3, 1, 6, 3, 2, 6), Direction.DOWN);
		scene.idle(30);
		scene.world.instructArm(mechanicalArmPos, Phase.MOVE_TO_OUTPUT, ItemStack.EMPTY, 0);
		scene.idle(45);

		scene.overlay.showText(60)
			.text("Deposit areas should have an empty Big Cartridge filter to avoid item mismanagement.")
			.colored(PonderPalette.BLUE);
		ItemStack emptyCartridge = BigCartridgeBlockItem.getWithPower(0);
		scene.world.instructArm(mechanicalArmPos, Phase.SEARCH_OUTPUTS, emptyCartridge, -1);
		scene.idle(20);
		scene.world.instructArm(mechanicalArmPos, Phase.MOVE_TO_OUTPUT, emptyCartridge, 1);
		scene.idle(30);
		scene.world.instructArm(mechanicalArmPos, Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
		scene.idle(45);

		scene.markAsFinished();
	}

	// I hate this so much, but it had to be done.
	private static void animateQFBHack(SceneBuilder scene, Selection breechSel, boolean close) {
		for (int i = 0; i < 5; i++) {
			int finalI = i;
			scene.world.modifyBlockEntityNBT(breechSel, QuickfiringBreechBlockEntity.class, compoundTag -> {
				compoundTag.putInt("OpenDirection", close ? -1 : 1);
				compoundTag.putInt("OpenProgress", close ? 5 - finalI : finalI);
			});

			scene.idle(1);
		}
		scene.world.modifyBlockEntityNBT(breechSel, QuickfiringBreechBlockEntity.class, compoundTag -> {
			compoundTag.putInt("OpenDirection", 0);
			compoundTag.putInt("OpenProgress", close ? 0 : 5);
		});
	}

	public static void usingAutocannonAmmoContainer(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/using_autocannon_ammo_container", "Using the Autocannon Ammo Container");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		Selection autocannon = util.select.fromTo(2, 1, 1, 2, 3, 4);
		Selection lever = util.select.position(2, 1, 1);
		BlockPos breechPos = util.grid.at(2, 3, 1);
		Selection breechSel = util.select.position(breechPos);

		ItemStack filledContainer = CBCBlocks.AUTOCANNON_AMMO_CONTAINER.asStack();
		filledContainer.getOrCreateTag().put("Ammo", CBCItems.MACHINE_GUN_ROUND.asStack(64).save(new CompoundTag()));
		ItemStack emptyContainer = CBCBlocks.AUTOCANNON_AMMO_CONTAINER.asStack();

		scene.world.showSection(autocannon, Direction.DOWN);
		scene.idle(30);

		scene.overlay.showText(60)
			.text("Autocannon Ammo Containers are a convenient way to store autocannon ammo and to load autocannons.")
			.pointAt(util.vector.centerOf(1, 3, 1));
		scene.idle(75);

		scene.overlay.showText(80)
			.text("Right-click a container on an Autocannon Breech to load it.");
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos, Direction.UP), Pointing.DOWN)
			.rightClick().withItem(filledContainer), 30);
		scene.idle(20);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.idle(55);

		scene.overlay.showText(80)
			.text("To remove a container, right-click the breech with an empty hand.");
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos, Direction.UP), Pointing.DOWN)
			.rightClick(), 30);
		scene.idle(20);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.remove("Magazine");
		});
		scene.idle(55);

		scene.addKeyframe();
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", emptyContainer.save(new CompoundTag()));
		});
		scene.idle(15);
		scene.overlay.showText(80)
			.text("You can also swap out the container by right-clicking the breech with another container.")
			.colored(PonderPalette.BLUE);
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos, Direction.UP), Pointing.DOWN)
			.rightClick().withItem(filledContainer), 30);
		scene.idle(20);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.idle(55);

		scene.addKeyframe();
		scene.world.toggleRedstonePower(lever);
		scene.effects.createRedstoneParticles(util.grid.at(2, 1, 1), 0xFF0000, 10);
		scene.idle(15);
		scene.overlay.showText(60)
			.text("You can also load an assembled autocannon with a container.");
		scene.idle(75);
		scene.overlay.showText(190)
			.text("However, you can only remove a present container on an assembled autocannon if it is empty, or by swapping or shift right-clicking it.")
			.colored(PonderPalette.BLUE);
		scene.idle(15);

		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", emptyContainer.save(new CompoundTag()));
		});
		scene.idle(15);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos, Direction.UP), Pointing.DOWN)
			.rightClick(), 30);
		scene.idle(45);

		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.idle(15);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos, Direction.UP), Pointing.DOWN)
			.rightClick().withItem(filledContainer), 30);
		scene.idle(45);

		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.idle(15);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(breechPos, Direction.UP), Pointing.DOWN)
			.rightClick().whileSneaking(), 30);
		scene.idle(55);

		scene.markAsFinished();
	}

	public static void fillingAutocannonAmmoContainer(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/filling_autocannon_ammo_container", "Filling the Autocannon Ammo Container");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos depot = util.grid.at(2, 1, 2);
		scene.world.showSection(util.select.position(depot), Direction.UP);
		scene.idle(30);

		scene.overlay.showText(60)
			.text("Autocannon Ammo Containers can be filled in-menu.");
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(depot, Direction.UP), Pointing.DOWN)
			.rightClick(), 30);
		scene.idle(55);
		scene.overlay.showText(60)
			.text("You can also configure tracer spacing in the menu.");
		scene.idle(75);
		scene.overlay.showText(60)
			.text("Different ammo types have different capacities.");
		scene.idle(75);

		Selection largePowerCog = util.select.position(5, 0, 3);
		Selection smallCog = util.select.position(5, 1, 2);
		Selection oppositeSmallCog = util.select.position(5, 2, 2);
		Selection deployerKinetics = util.select.fromTo(5, 3, 2, 3, 3, 2);
		BlockPos deployer = util.grid.at(2, 3, 2);
		Selection deployerSel = util.select.position(deployer);
		Selection deployerGroup = smallCog.add(deployerKinetics).add(deployerSel);

		scene.world.setKineticSpeed(largePowerCog, 16);
		scene.world.setKineticSpeed(deployerGroup, -32);
		scene.world.setKineticSpeed(oppositeSmallCog, 32);

		ItemStack ammo = CBCItems.AP_AUTOCANNON_ROUND.get().getCreativeTabCartridgeItem();

		scene.world.modifyBlockEntityNBT(deployerSel, DeployerBlockEntity.class, tag -> {
			tag.put("HeldItem", ammo.save(new CompoundTag()));
		});

		scene.world.showSection(largePowerCog, Direction.WEST);
		scene.idle(10);
		scene.world.showSection(smallCog, Direction.WEST);
		scene.idle(10);
		scene.world.showSection(oppositeSmallCog, Direction.WEST);
		scene.idle(10);
		scene.world.showSection(deployerKinetics, Direction.EAST);
		scene.idle(10);
		scene.world.showSection(deployerSel, Direction.EAST);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("The container can also be filled with deployers.")
			.pointAt(util.vector.topOf(deployer));
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(deployer, Direction.WEST), Pointing.LEFT)
			.withItem(ammo), 30);

		scene.world.moveDeployer(deployer, 1, 25);
		scene.idle(30);
		scene.world.moveDeployer(deployer, -1, 25);
		scene.idle(30);

		scene.world.moveDeployer(deployer, 1, 25);
		scene.idle(10);
		scene.overlay.showText(60)
			.text("Ammo with tracers will go in the tracer slot, otherwise it will go in the main slot.")
			.colored(PonderPalette.BLUE);
		scene.idle(15);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(deployer.below(), Direction.WEST), Pointing.LEFT)
			.withItem(CBCItems.TRACER_TIP.asStack()), 30);
		scene.idle(5);
		scene.world.moveDeployer(deployer, -1, 25);
		scene.idle(30);

		scene.world.moveDeployer(deployer, 1, 25);
		scene.idle(30);
		scene.world.moveDeployer(deployer, -1, 25);
		scene.idle(30);

		scene.markAsFinished();

		for (int i = 0; i < 4; ++i) {
			scene.world.moveDeployer(deployer, 1, 25);
			scene.idle(30);
			scene.world.moveDeployer(deployer, -1, 25);
			scene.idle(30);
		}
	}

	public static void automatingAutocannonAmmoContainer(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/automating_autocannon_ammo_container", "Automating loading the Autocannon Ammo Container");
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();

		Selection autocannon = util.select.fromTo(3, 1, 3, 3, 3, 6);
		BlockPos breech = util.grid.at(3, 3, 3);
		Selection breechSel = util.select.position(breech);
		scene.world.toggleRedstonePower(util.select.position(3, 1, 3));
		scene.world.showSection(autocannon, Direction.DOWN);
		scene.idle(30);

		Selection powerLargeCog = util.select.position(7, 0, 3);
		Selection oppositeLargeCog = util.select.position(6, 1, 3);
		Selection smallCog = util.select.position(5, 1, 2);
		BlockPos arm = util.grid.at(4, 1, 2);
		Selection armSel = util.select.position(arm);
		Selection depots = util.select.fromTo(6, 1, 0, 7, 2, 1);
		BlockPos takeDepot = util.grid.at(6, 1, 0);
		BlockPos takeFunnel = util.grid.at(6, 2, 0);
		BlockPos depositFunnel = util.grid.at(6, 2, 1);

		ItemStack emptyContainer = CBCBlocks.AUTOCANNON_AMMO_CONTAINER.asStack();
		ItemStack filledContainer = emptyContainer.copy();
		filledContainer.getOrCreateTag().put("Ammo", CBCItems.MACHINE_GUN_ROUND.asStack(64).save(new CompoundTag()));

		scene.world.showSection(powerLargeCog, Direction.WEST);
		scene.world.setKineticSpeed(powerLargeCog, 16);
		scene.idle(10);
		scene.world.setKineticSpeed(oppositeLargeCog, -16);
		scene.world.showSection(oppositeLargeCog, Direction.DOWN);
		scene.idle(10);
		scene.world.setKineticSpeed(smallCog, 32);
		scene.world.showSection(smallCog, Direction.DOWN);
		scene.idle(10);
		scene.world.setKineticSpeed(armSel, -32);
		scene.world.showSection(armSel, Direction.DOWN);
		scene.idle(10);
		scene.world.createItemOnBeltLike(takeDepot, Direction.EAST, filledContainer);
		scene.world.showSection(depots, Direction.WEST);
		scene.idle(30);

		scene.world.instructArm(arm, Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
		scene.idle(35);
		scene.world.removeItemsFromBelt(takeDepot);
		scene.world.instructArm(arm, Phase.SEARCH_OUTPUTS, filledContainer, -1);
		scene.idle(5);
		scene.world.flapFunnel(takeFunnel, true);
		scene.world.createItemOnBeltLike(takeDepot, Direction.EAST, filledContainer);
		scene.idle(30);
		scene.world.instructArm(arm, Phase.MOVE_TO_OUTPUT, filledContainer, 0);
		scene.overlay.showSelectionWithText(armSel, 120)
			.text("Mechanical Arms can load assembled autocannons with Autocannon Ammo Containers.")
			.colored(PonderPalette.GREEN);
		scene.idle(35);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.world.instructArm(arm, Phase.SEARCH_OUTPUTS, emptyContainer, -1);
		scene.idle(35);
		scene.world.instructArm(arm, Phase.MOVE_TO_OUTPUT, emptyContainer, 1);
		scene.idle(35);
		scene.world.instructArm(arm, Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
		scene.idle(35);

		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", emptyContainer.save(new CompoundTag()));
		});
		scene.world.instructArm(arm, Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
		scene.idle(35);
		scene.world.removeItemsFromBelt(takeDepot);
		scene.world.instructArm(arm, Phase.SEARCH_OUTPUTS, filledContainer, -1);
		scene.idle(5);
		scene.world.flapFunnel(takeFunnel, true);
		scene.world.createItemOnBeltLike(takeDepot, Direction.EAST, filledContainer);
		scene.idle(30);
		scene.world.instructArm(arm, Phase.MOVE_TO_OUTPUT, filledContainer, 0);
		scene.overlay.showSelectionWithText(util.select.position(3, 1, 4), 80)
			.text("Set a Cannon Mount with an autocannon as one of the Mechanical Arm's deposits.")
			.colored(PonderPalette.OUTPUT);
		scene.idle(35);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.world.instructArm(arm, Phase.SEARCH_OUTPUTS, emptyContainer, -1);
		scene.idle(35);
		scene.world.instructArm(arm, Phase.MOVE_TO_OUTPUT, emptyContainer, 1);
		scene.idle(35);
		scene.world.instructArm(arm, Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
		scene.idle(35);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", emptyContainer.save(new CompoundTag()));
		});

		scene.world.instructArm(arm, Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
		scene.idle(35);
		scene.world.removeItemsFromBelt(takeDepot);
		scene.world.instructArm(arm, Phase.SEARCH_OUTPUTS, filledContainer, -1);
		scene.idle(5);
		scene.world.flapFunnel(takeFunnel, true);
		scene.world.createItemOnBeltLike(takeDepot, Direction.EAST, filledContainer);
		scene.idle(30);
		scene.world.instructArm(arm, Phase.MOVE_TO_OUTPUT, filledContainer, 0);

		Vec3 filter = util.vector.blockSurface(depositFunnel, Direction.WEST);
		scene.overlay.showText(80)
			.attachKeyFrame()
			.pointAt(filter)
			.text("You can dispose of empty containers by setting a deposit filter slot to an empty container.");
		scene.idle(5);
		filter = filter.add(0, -5 / 16f, 0);
		scene.overlay.showControls(new InputWindowElement(filter, Pointing.LEFT).rightClick().withItem(emptyContainer), 30);
		scene.idle(30);

		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", filledContainer.save(new CompoundTag()));
		});
		scene.world.instructArm(arm, Phase.SEARCH_OUTPUTS, emptyContainer, -1);
		scene.idle(35);
		scene.world.instructArm(arm, Phase.MOVE_TO_OUTPUT, emptyContainer, 1);
		scene.idle(35);
		scene.world.instructArm(arm, Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
		scene.idle(30);
		scene.world.modifyBlockEntityNBT(breechSel, AbstractAutocannonBreechBlockEntity.class, tag -> {
			tag.put("Magazine", emptyContainer.save(new CompoundTag()));
		});

		scene.markAsFinished();
	}

	public static void addingTracers(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/adding_tracers", "Adding tracers to big cannon projectiles");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos munitionPos = util.grid.at(2, 1, 3);
		Selection munitionSel = util.select.position(munitionPos);
		scene.idle(20);
		scene.world.showSection(munitionSel, Direction.NORTH);
		scene.idle(30);

		scene.overlay.showText(80)
			.text("In addition to autocannon munitions, tracers can also be added to big cannon projectiles.")
			.pointAt(util.vector.centerOf(2, 1, 3));
		scene.idle(40);

		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(munitionPos, Direction.NORTH), Pointing.DOWN)
			.rightClick()
			.withItem(CBCItems.TRACER_TIP.asStack()), 60);
		scene.idle(20);
		// Not actually visible for now
		scene.world.modifyBlockEntityNBT(munitionSel, BigCannonProjectileBlockEntity.class, tag -> tag
			.put("Tracer", CBCItems.TRACER_TIP.asStack().save(new CompoundTag())));
		scene.idle(55);

		scene.overlay.showText(50)
			.text("Unlike fuzing, tracer tips can be applied to any side of the projectile block.")
			.colored(PonderPalette.BLUE);
		scene.idle(65);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Right-click the projectile with an empty hand to remove any tracers present.")
			.pointAt(util.vector.centerOf(2, 1, 3));
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(munitionPos, Direction.NORTH), Pointing.DOWN).rightClick(), 60);
		scene.idle(20);
		scene.world.modifyBlockEntityNBT(munitionSel, BigCannonProjectileBlockEntity.class, tag -> tag.remove("Tracer"));
		scene.idle(60);

		Selection kineticSel = util.select.fromTo(2, 1, 1, 5, 1, 1);
		Selection largeCog = util.select.position(5, 0, 2);
		scene.world.showSection(kineticSel, Direction.WEST);
		scene.world.showSection(largeCog, Direction.WEST);

		BlockPos deployerPos = util.grid.at(2, 1, 1);
		scene.world.modifyBlockEntityNBT(util.select.position(deployerPos), DeployerBlockEntity.class, tag -> tag.put("HeldItem", CBCItems.TRACER_TIP.asStack().save(new CompoundTag())));

		scene.world.setKineticSpeed(kineticSel, 32.0f);
		scene.world.setKineticSpeed(largeCog, -16.0f);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("As with fuzing, tracer application can be automated with Deployers.")
			.pointAt(util.vector.centerOf(deployerPos));
		scene.idle(90);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(26);
		scene.world.modifyBlockEntityNBT(util.select.position(deployerPos), DeployerBlockEntity.class, tag -> tag.put("HeldItem", ItemStack.EMPTY.save(new CompoundTag())));
		scene.world.modifyBlockEntityNBT(munitionSel, BigCannonProjectileBlockEntity.class, tag -> tag.put("Tracer", CBCItems.TRACER_TIP.asStack().save(new CompoundTag())));
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(46);

		scene.markAsFinished();
	}

}
