package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.content.contraptions.components.deployer.DeployerTileEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntity;

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
	
	public static void cannonLoads(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("munitions/cannon_loads", "Cannon Loads");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		
		Selection cannon = util.select.fromTo(0, 1, 2, 4, 1, 2);
		scene.world.showSection(cannon, Direction.DOWN);
		scene.idle(40);
		scene.overlay.showText(100).text("When loading a cannon, care must be taken to ensure that cannon loads are safe and effective.");
		scene.idle(110);
		
		scene.overlay.showText(80).text("A cannon's material has two factors that can cause the cannon to fail; its squib ratio and its strength.").colored(PonderPalette.RED);
		scene.idle(90);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("The squib ratio determines the minimum length of barrel per Powder Charge a projectile can safely travel through before possibly getting stuck.");
		scene.idle(90);
		
		Selection safeLoad = util.select.fromTo(0, 2, 2, 3, 2, 2);
		scene.world.showSection(safeLoad, Direction.UP);
		scene.overlay.showText(80).text("For example, the squib ratio of cast iron is 1 barrel to 1 Powder Charge.");
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
		
		scene.overlay.showText(80).text("The barrel that the loaded projectile is in is also counted towards the distance travelled.");
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
		scene.world.modifyTileNBT(munitionSel, FuzedBlockEntity.class, tag -> tag.put("Fuze", CBCItems.IMPACT_FUZE.asStack().serializeNBT()));
		scene.idle(50);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Right-click the projectile head with an empty hand to remove any fuzes present.")
			.pointAt(util.vector.centerOf(2, 1, 3));
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(munitionPos, Direction.NORTH), Pointing.DOWN).rightClick(), 60);
		scene.idle(20);
		scene.world.modifyTileNBT(munitionSel, FuzedBlockEntity.class, tag -> tag.remove("Fuze"));
		scene.idle(60);
		
		Selection kineticSel = util.select.fromTo(2, 1, 1, 5, 1, 1);
		Selection largeCog = util.select.position(5, 0, 2);
		scene.world.showSection(kineticSel, Direction.WEST);
		scene.world.showSection(largeCog, Direction.WEST);
		
		BlockPos deployerPos = util.grid.at(2, 1, 1);
		scene.world.modifyTileNBT(util.select.position(deployerPos), DeployerTileEntity.class, tag -> tag.put("HeldItem", CBCItems.TIMED_FUZE.asStack().serializeNBT()));
		
		scene.world.setKineticSpeed(kineticSel, 32.0f);
		scene.world.setKineticSpeed(largeCog, -16.0f);
		
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Fuzing projectiles can be automated with Deployers.")
			.pointAt(util.vector.centerOf(deployerPos));
		scene.idle(90);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(26);
		scene.world.modifyTileNBT(util.select.position(deployerPos), DeployerTileEntity.class, tag -> tag.put("HeldItem", ItemStack.EMPTY.serializeNBT()));
		scene.world.modifyTileNBT(munitionSel, FuzedBlockEntity.class, tag -> tag.put("Fuze", CBCItems.TIMED_FUZE.asStack().serializeNBT()));
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(46);
		
		scene.markAsFinished();
	}
	
}
