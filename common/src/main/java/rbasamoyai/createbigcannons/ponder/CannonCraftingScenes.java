package rbasamoyai.createbigcannons.ponder;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction.Emitter;
import com.simibubi.create.foundation.ponder.instruction.FadeOutOfSceneInstruction;
import com.simibubi.create.foundation.ponder.instruction.HighlightValueBoxInstruction;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderHeadBlock;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteWithItemsCannonBlock;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderSelectionHandler;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.multiloader.PonderPlatform;

public class CannonCraftingScenes {

	public static void cannonCasting(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/cannon_casting", "Cannon Casting");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.modifyBlockEntityNBT(util.select.fromTo(2, 2, 2, 2, 3, 2), AbstractCannonCastBlockEntity.class, setUnfinishedCannonShape(CannonCastShape.MEDIUM));

		scene.idle(15);
		int placeDelay = 3;
		scene.world.showSection(util.select.position(3, 1, 1), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(2, 1, 1), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(1, 1, 1), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(1, 1, 2), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(1, 1, 3), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(2, 1, 3), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(3, 1, 3), Direction.DOWN);
		scene.idle(placeDelay);
		scene.world.showSection(util.select.position(3, 1, 2), Direction.DOWN);
		scene.idle(placeDelay + 15);

		Vec3 castCenter = util.vector.centerOf(2, 1, 2);
		scene.overlay.showText(60)
			.text("Cannon Casts are the first block recipe for cannon blocks.")
			.pointAt(castCenter);
		scene.idle(80);

		scene.overlay.showText(60)
			.text("Use a cannon mould to build a cast layer.")
			.pointAt(castCenter);
		scene.idle(40);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(2, 1, 2), Pointing.DOWN).withItem(CBCBlocks.MEDIUM_CAST_MOULD.asStack()), 10);
		scene.idle(15);
		scene.world.showSection(util.select.position(2, 1, 2), null);
		scene.idle(30);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(2, 1, 2), Pointing.DOWN).withItem(CBCBlocks.CASTING_SAND.asStack()), 10);
		scene.idle(15);
		scene.world.modifyBlock(util.grid.at(2, 1, 2), setStateValue(CannonCastMouldBlock.SAND, true), false);
		Emitter mouldSandEmitter = Emitter.simple(new BlockParticleOption(ParticleTypes.BLOCK, CBCBlocks.CASTING_SAND.getDefaultState()), util.vector.of(0, 1, 0));
		scene.effects.emitParticles(castCenter.add(0, 1, 0), mouldSandEmitter, 10, 1);
		scene.idle(30);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(2, 1, 2), Pointing.DOWN).rightClick(), 10);
		scene.idle(15);
		scene.world.hideSection(util.select.fromTo(1, 1, 1, 3, 1, 3), null);
		ElementLink<WorldSectionElement> cast = scene.world.showIndependentSectionImmediately(util.select.fromTo(1, 2, 1, 3, 2, 3));
		scene.world.moveSection(cast, util.vector.of(0, -1, 0), 0);
		scene.idle(30);

		Selection deployerGearDown = util.select.position(5, 0, 1);
		Selection deployerGearUp = util.select.position(5, 1, 0);
		Selection deployerGearReverse = util.select.fromTo(2, 2, 0, 5, 2, 0);
		scene.world.showSection(deployerGearDown, Direction.NORTH);
		scene.idle(5);
		scene.world.showSection(deployerGearUp, Direction.DOWN);
		scene.idle(5);
		scene.world.showSection(deployerGearReverse, Direction.DOWN);
		scene.idle(30);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Deployers can also interact with cannon casts.")
			.colored(PonderPalette.BLUE)
			.pointAt(util.vector.centerOf(2, 2, 0));
		scene.idle(20);

		ElementLink<WorldSectionElement> reusedFirstLayer = scene.world.showIndependentSection(util.select.fromTo(1, 1, 1, 3, 1, 3).substract(util.select.position(2, 1, 2)), Direction.DOWN);
		scene.world.moveSection(reusedFirstLayer, util.vector.of(0, 1, 0), 0);
		scene.idle(30);

		BlockPos deployerPos = util.grid.at(2, 2, 0);
		Selection deployer = util.select.position(deployerPos);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(2, 2, 0), Pointing.DOWN).withItem(CBCBlocks.MEDIUM_CAST_MOULD.asStack()), 10);
		scene.idle(15);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, putItemInDeployer(CBCBlocks.MEDIUM_CAST_MOULD.asStack()));
		scene.idle(10);

		scene.world.setKineticSpeed(deployerGearDown, 16);
		scene.world.setKineticSpeed(deployerGearUp, -32);
		scene.world.setKineticSpeed(deployerGearReverse, 32);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(26);
		scene.world.modifyBlock(util.grid.at(2, 1, 2), setStateValue(CannonCastMouldBlock.SAND, false), false);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, putItemInDeployer(ItemStack.EMPTY));
		ElementLink<WorldSectionElement> reusedFirstLayerCore = scene.world.showIndependentSectionImmediately(util.select.position(2, 1, 2));
		scene.world.moveSection(reusedFirstLayerCore, util.vector.of(0, 1, 0), 0);
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(36);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(2, 2, 0), Pointing.DOWN).withItem(CBCBlocks.CASTING_SAND.asStack()), 10);
		scene.idle(15);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, putItemInDeployer(CBCBlocks.CASTING_SAND.asStack()));
		scene.idle(10);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(26);
		scene.world.modifyBlock(util.grid.at(2, 1, 2), setStateValue(CannonCastMouldBlock.SAND, true), false);
		scene.effects.emitParticles(castCenter.add(0, 2, 0), mouldSandEmitter, 10, 1);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, putItemInDeployer(ItemStack.EMPTY));
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(36);

		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(26);
		scene.addInstruction(new FadeOutOfSceneInstruction<>(0, null, reusedFirstLayer));
		scene.addInstruction(new FadeOutOfSceneInstruction<>(0, null, reusedFirstLayerCore));
		ElementLink<WorldSectionElement> cast2 = scene.world.showIndependentSectionImmediately(util.select.fromTo(1, 3, 1, 3, 3, 3));
		scene.world.moveSection(cast2, util.vector.of(0, -1, 0), 0);
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(36);
		scene.world.hideSection(deployerGearDown.add(deployerGearUp).add(deployerGearReverse), Direction.EAST);
		scene.idle(20);

		scene.addKeyframe();

		scene.world.showSection(util.select.fromTo(0, 1, 2, 0, 2, 2), Direction.DOWN);
		ElementLink<WorldSectionElement> upperLayer = scene.world.showIndependentSection(util.select.fromTo(0, 4, 2, 2, 4, 2), Direction.DOWN);
		scene.world.moveSection(upperLayer, util.vector.of(0, -1, 0), 0);
		Selection pumpGear = util.select.fromTo(2, 1, 5, 2, 4, 5);
		Selection pumpGear1 = util.select.position(1, 4, 5).add(util.select.position(1, 4, 3));
		Selection pumpGear2 = util.select.position(1, 4, 4).add(util.select.position(1, 4, 2));
		scene.world.showSectionAndMerge(pumpGear, Direction.DOWN, upperLayer);
		scene.idle(5);
		scene.world.showSectionAndMerge(pumpGear1, Direction.EAST, upperLayer);
		scene.idle(5);
		scene.world.showSectionAndMerge(util.select.position(1, 4, 4), Direction.EAST, upperLayer);
		scene.idle(30);

		scene.world.setKineticSpeed(pumpGear, 16);
		scene.world.setKineticSpeed(pumpGear1, -16);
		scene.world.setKineticSpeed(pumpGear2, 16);
		scene.world.propagatePipeChange(util.grid.at(1, 4, 2));
		scene.idle(15);

		scene.overlay.showText(60)
			.text("Fill the cast with molten metal to start the casting process.")
			.colored(PonderPalette.GREEN);
		scene.idle(20);

		BlockPos tankPos = util.grid.at(0, 1, 2);
		BlockPos castPos = util.grid.at(2, 2, 2);
		for (int i = 0; i < 24; ++i) {
			scene.world.modifyBlockEntity(tankPos, FluidTankBlockEntity.class, tank ->
				PonderPlatform.drain(tank, 144, null));
			scene.world.modifyBlockEntity(castPos, AbstractCannonCastBlockEntity.class, cast1 ->
				PonderPlatform.fillWith(cast1, CBCFluids.MOLTEN_CAST_IRON.get(), 144, Direction.UP));
			scene.idle(5);
		}
		scene.idle(40);

		scene.world.hideIndependentSection(upperLayer, Direction.UP);
		scene.world.hideSection(pumpGear, Direction.UP);
		scene.world.hideSection(util.select.fromTo(0, 1, 2, 0, 2, 2), Direction.UP);
		scene.idle(30);

		scene.rotateCameraY(90);
		scene.idle(15);
		Selection comparatorSel = util.select.fromTo(4, 1, 2, 5, 1, 2).add(util.select.position(5, 0, 2));
		scene.world.showSection(comparatorSel, Direction.DOWN);
		scene.world.modifyBlock(util.grid.at(4, 1, 2), setStateValue(ComparatorBlock.POWERED, true), false);
		scene.world.modifyBlockEntityNBT(util.select.position(5, 1, 2), NixieTubeBlockEntity.class, tag -> tag.putInt("RedstoneStrength", 14));
		scene.idle(20);
		Vec3 comparator = util.vector.centerOf(4, 1, 2);
		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Cannon casts output a signal that can be read by comparators.")
			.pointAt(comparator);
		scene.idle(80);
		scene.overlay.showText(80)
			.text("They output a signal from 0 to 14 measuring how full the cast is, and 15 when the cast is finished.")
			.pointAt(comparator);
		scene.idle(100);
		scene.world.hideSection(comparatorSel, Direction.UP);
		scene.idle(30);
		scene.rotateCameraY(-90);
		scene.idle(15);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("After being completely filled up, the cannon cast takes some time to solidify.")
			.pointAt(util.vector.topOf(2, 2, 2));
		scene.idle(100);
		scene.overlay.showText(80)
			.text("The time it takes for the casting process to finish depends on the size of the cast.")
			.colored(PonderPalette.RED);
		scene.idle(120);

		Selection innerCast = util.select.fromTo(2, 2, 2, 2, 3, 2);
		scene.world.setBlocks(util.select.fromTo(1, 2, 1, 3, 3, 3).substract(innerCast), CBCBlocks.FINISHED_CANNON_CAST.getDefaultState(), false);
		scene.world.setBlocks(innerCast, CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.UP), false);
		Selection centerBlocks = util.select.fromTo(1, 2, 1, 1, 3, 1);
		scene.world.modifyBlockEntityNBT(centerBlocks, FinishedCannonCastBlockEntity.class, setFinishedCannonShape(CannonCastShape.MEDIUM));
		scene.world.modifyBlockEntityNBT(util.select.fromTo(1, 2, 1, 3, 2, 3).substract(centerBlocks), FinishedCannonCastBlockEntity.class, setCentralBlock(util.grid.at(1, 2, 1)));
		scene.world.modifyBlockEntityNBT(util.select.fromTo(1, 3, 1, 3, 3, 3).substract(centerBlocks), FinishedCannonCastBlockEntity.class, setCentralBlock(util.grid.at(1, 3, 1)));
		scene.idle(20);

		scene.overlay.showText(80)
			.text("The finished cannon cast can be removed by breaking or interacting with it.")
			.pointAt(util.vector.blockSurface(util.grid.at(1, 2, 1), Direction.WEST));
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.blockSurface(util.grid.at(2, 2, 1), Direction.NORTH), Pointing.RIGHT).leftClick(), 20);
		scene.idle(30);
		scene.world.setBlocks(util.select.fromTo(1, 3, 1, 3, 3, 3).substract(innerCast), Blocks.AIR.defaultBlockState(), true);
		scene.idle(80);

		deployerGearUp = util.select.fromTo(3, 1, 0, 5, 1, 0);
		BlockPos deployerPos1 = util.grid.at(3, 1, 0);
		scene.world.showSection(deployerGearDown, Direction.NORTH);
		scene.idle(5);
		scene.world.showSection(deployerGearUp, Direction.DOWN);
		scene.world.setKineticSpeed(deployerGearUp, -32);
		scene.idle(30);
		scene.overlay.showText(60)
			.text("It is possible to automate the removal of finished casts.")
			.colored(PonderPalette.BLUE)
			.pointAt(util.vector.centerOf(3, 1, 0));
		scene.idle(20);
		scene.world.moveDeployer(deployerPos1, 1, 25);
		scene.idle(26);
		scene.world.setBlocks(util.select.fromTo(1, 2, 1, 3, 2, 3).substract(innerCast), Blocks.AIR.defaultBlockState(), true);
		scene.world.moveDeployer(deployerPos1, -1, 25);
		scene.idle(36);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Smaller cannons such as autocannons shed the casting sand automatically.")
			.colored(PonderPalette.BLUE);
		scene.idle(80);

		scene.markAsFinished();
	}

	public static void cannonMovement(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/moving_cannons", "Moving Cannons Around");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		ElementLink<WorldSectionElement> cannon = scene.world.showIndependentSection(util.select.fromTo(2, 1, 0, 2, 1, 2), Direction.DOWN);
		scene.idle(20);

		scene.overlay.showText(80)
			.text("By default, cast cannons will turn into scrap when broken, some even with Silk Touch.")
			.colored(PonderPalette.RED);
		scene.idle(20);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(2, 1, 0), Pointing.DOWN).withItem(CBCItems.CAST_IRON_NUGGET.asStack()), 40);
		scene.idle(20);
		scene.world.destroyBlock(util.grid.at(2, 1, 0));
		scene.idle(40);
		scene.world.setBlock(util.grid.at(2, 1, 0), CBCBlocks.CAST_IRON_CANNON_BARREL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.SOUTH), false);
		scene.idle(20);

		scene.overlay.showText(100)
			.text("Create's contraptions can be used in varying ways to move cannons.")
			.colored(PonderPalette.GREEN)
			.attachKeyFrame();

		Selection bearing = util.select.fromTo(2, 2, 1, 2, 3, 1);
		scene.world.showSection(bearing, Direction.DOWN);
		scene.idle(30);
		scene.world.rotateBearing(util.grid.at(2, 2, 1), 90, 40);
		scene.world.setKineticSpeed(bearing, 16);
		scene.world.rotateSection(cannon, 0, 90, 0, 40);
		scene.idle(40);
		scene.world.setKineticSpeed(bearing, 0);
		scene.world.hideSection(util.select.fromTo(2, 2, 1, 2, 3, 1), Direction.UP);
		scene.idle(20);

		Selection pistonTe = util.select.fromTo(1, 1, 3, 2, 1, 3);
		scene.world.showSection(pistonTe, Direction.NORTH);
		ElementLink<WorldSectionElement> piston = scene.world.showIndependentSection(util.select.fromTo(2, 2, 3, 2, 2, 4), Direction.NORTH);
		scene.world.moveSection(piston, util.vector.of(0, -1, 0), 0);
		scene.idle(20);
		scene.world.setKineticSpeed(pistonTe, -16);
		scene.world.moveSection(piston, util.vector.of(0, 0, -1), 40);
		scene.idle(40);
		scene.world.setKineticSpeed(pistonTe, 0);
		scene.idle(15);
		scene.world.setKineticSpeed(pistonTe, 16);
		scene.world.moveSection(piston, util.vector.of(0, 0, 1), 40);
		scene.world.moveSection(cannon, util.vector.of(0, 0, 1), 40);
		scene.idle(40);
		scene.world.setKineticSpeed(pistonTe, 0);
		scene.idle(15);
		scene.world.hideSection(pistonTe, Direction.SOUTH);
		scene.world.hideIndependentSection(piston, Direction.SOUTH);
		scene.idle(20);

		Selection pulleyTe = util.select.fromTo(1, 3, 2, 2, 3, 2);
		scene.world.showSection(pulleyTe, Direction.DOWN);
		scene.idle(30);
		scene.world.setKineticSpeed(pulleyTe, 16);
		scene.world.movePulley(util.grid.at(2, 3, 2), 1, 40);
		scene.idle(40);
		scene.world.setKineticSpeed(pulleyTe, 0);
		scene.idle(15);
		scene.world.setKineticSpeed(pulleyTe, -16);
		scene.world.movePulley(util.grid.at(2, 3, 2), -1, 40);
		scene.world.moveSection(cannon, util.vector.of(0, 1, 0), 40);
		scene.idle(40);
		scene.world.setKineticSpeed(pulleyTe, 0);
		scene.idle(15);
		scene.world.hideSection(pulleyTe, Direction.UP);

		scene.idle(20);
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

		scene.world.modifyBlock(util.grid.at(5, 1, 1), copyPropertyTo(FACING, CBCBlocks.CAST_IRON_CANNON_BARREL.getDefaultState()), false);
		scene.idle(20);
		scene.overlay.showText(60)
			.text("Bored cannon blocks drop scrap items.")
			.pointAt(util.vector.centerOf(5, 1, 1))
			.colored(PonderPalette.GREEN);
		scene.idle(20);

		scene.overlay.showControls(new InputWindowElement(util.vector.centerOf(5, 1, 1), Pointing.DOWN).withItem(CBCItems.CAST_IRON_NUGGET.asStack()), 40);
		scene.idle(67);

		scene.world.modifyBlock(util.grid.at(6, 1, 1), copyPropertyTo(FACING, CBCBlocks.CAST_IRON_CANNON_CHAMBER.getDefaultState()), false);
		scene.idle(10);

		scene.world.setKineticSpeed(drillGearDown, -16);
		scene.world.setKineticSpeed(drillGearUp, 32);
		scene.world.moveSection(drillPiston, util.vector.of(-3, 0, 0), 24);
		scene.idle(20);

		scene.markAsFinished();
	}

	public static void cannonBuilding(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/cannon_building", "Building Built-Up Cannons");
		scene.configureBasePlate(6, 0, 3);
		scene.world.showSection(util.select.cuboid(util.grid.zero(), util.grid.at(9, 0, 3)), Direction.UP);

		Selection builderGearDown = util.select.position(4, 0, 3);
		Selection builderGearUp = util.select.fromTo(3, 1, 1, 3, 1, 3);
		scene.world.showSection(builderGearDown, Direction.UP);
		scene.world.showSection(builderGearUp, Direction.UP);
		ElementLink<WorldSectionElement> builderPiston = scene.world.showIndependentSection(util.select.fromTo(0, 2, 1, 3, 2, 1), Direction.UP);
		scene.world.moveSection(builderPiston, util.vector.of(0, -1, 0), 0);
		scene.idle(15);

		scene.world.showSection(util.select.fromTo(6, 1, 1, 8, 1, 1), Direction.DOWN);
		scene.idle(30);

		scene.overlay.showText(80)
			.text("Cannon Builders are used to put together the layers of built-up cannons.")
			.pointAt(util.vector.centerOf(3, 1, 1));
		scene.idle(60);

		ElementLink<WorldSectionElement> layer1 = scene.world.showIndependentSection(util.select.fromTo(6, 1, 2, 7, 1, 2), Direction.DOWN);
		scene.world.moveSection(layer1, util.vector.of(-2, 0, -1), 0);
		scene.idle(40);

		scene.overlay.showText(80)
			.text("Pulse the Cannon Builder with power to toggle its attachment state.")
			.pointAt(util.vector.centerOf(3, 1, 1));
		scene.idle(20);
		BlockPos leverPos = util.grid.at(3, 1, 0);
		scene.world.showSection(util.select.position(leverPos), null);
		scene.idle(10);
		scene.world.modifyBlock(leverPos, setStateValue(POWERED, true), false);
		scene.effects.createRedstoneParticles(leverPos, 0xFF0000, 10);
		BlockPos headPos = util.grid.at(3, 2, 1);
		scene.world.modifyBlock(headPos, setStateValue(ATTACHED, true), false);
		scene.idle(15);
		scene.world.modifyBlock(leverPos, setStateValue(POWERED, false), false);
		scene.idle(15);
		scene.world.setKineticSpeed(builderGearDown, 16);
		scene.world.setKineticSpeed(builderGearUp, -32);

		scene.world.moveSection(builderPiston, util.vector.of(2, 0, 0), 32);
		scene.world.moveSection(layer1, util.vector.of(2, 0, 0), 32);
		scene.idle(40);

		scene.world.modifyBlock(leverPos, setStateValue(POWERED, true), false);
		scene.effects.createRedstoneParticles(leverPos, 0xFF0000, 10);
		scene.world.modifyBlock(headPos, setStateValue(ATTACHED, false), false);
		scene.idle(15);
		scene.world.modifyBlock(leverPos, setStateValue(POWERED, false), false);
		scene.idle(15);

		scene.world.setKineticSpeed(builderGearDown, -16);
		scene.world.setKineticSpeed(builderGearUp, 32);
		scene.world.moveSection(builderPiston, util.vector.of(-2, 0, 0), 32);
		scene.idle(40);

		ElementLink<WorldSectionElement> layer2 = scene.world.showIndependentSection(util.select.position(6, 1, 0), Direction.DOWN);
		scene.world.moveSection(layer2, util.vector.of(-2, 0, 1), 0);
		scene.idle(20);
		scene.world.modifyBlock(leverPos, setStateValue(POWERED, true), false);
		scene.effects.createRedstoneParticles(leverPos, 0xFF0000, 10);
		scene.world.modifyBlock(headPos, setStateValue(ATTACHED, true), false);
		scene.idle(15);
		scene.world.modifyBlock(leverPos, setStateValue(POWERED, false), false);
		scene.idle(15);
		scene.world.setKineticSpeed(builderGearDown, 16);
		scene.world.setKineticSpeed(builderGearUp, -32);

		scene.world.moveSection(builderPiston, util.vector.of(2, 0, 0), 32);
		scene.world.moveSection(layer2, util.vector.of(2, 0, 0), 32);
		scene.idle(40);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("When grabbing layers from built-up blocks, the outermost layer will always be grabbed")
			.colored(PonderPalette.RED)
			.pointAt(util.vector.topOf(6, 1, 1));
		scene.idle(20);

		scene.world.setKineticSpeed(builderGearDown, -16);
		scene.world.setKineticSpeed(builderGearUp, 32);
		scene.world.moveSection(builderPiston, util.vector.of(-2, 0, 0), 32);
		scene.world.moveSection(layer2, util.vector.of(-2, 0, 0), 32);
		scene.idle(40);

		scene.world.setKineticSpeed(builderGearDown, 16);
		scene.world.setKineticSpeed(builderGearUp, -32);
		scene.world.moveSection(builderPiston, util.vector.of(2, 0, 0), 32);
		scene.world.moveSection(layer2, util.vector.of(2, 0, 0), 32);
		scene.idle(40);

		scene.world.modifyBlock(leverPos, setStateValue(POWERED, true), false);
		scene.effects.createRedstoneParticles(leverPos, 0xFF0000, 10);
		scene.world.modifyBlock(headPos, setStateValue(ATTACHED, false), false);
		scene.idle(15);
		scene.world.modifyBlock(leverPos, setStateValue(POWERED, false), false);
		scene.idle(15);
		scene.world.setKineticSpeed(builderGearDown, -16);
		scene.world.setKineticSpeed(builderGearUp, 32);
		scene.world.moveSection(builderPiston, util.vector.of(-2, 0, 0), 32);
		scene.idle(60);

		scene.markAsFinished();
	}

	public static void finishingBuiltUpCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/finishing_built_up_cannons", "Finishing Built-Up Cannons");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.showSection(util.select.fromTo(0, 1, 2, 2, 1, 2), Direction.UP);
		scene.idle(20);
		ElementLink<WorldSectionElement> smallLayer = scene.world.showIndependentSection(util.select.fromTo(0, 1, 3, 1, 1, 3), Direction.EAST);
		scene.world.moveSection(smallLayer, util.vector.of(0, 0, -1), 0);
		scene.idle(10);
		ElementLink<WorldSectionElement> mediumLayer = scene.world.showIndependentSection(util.select.position(0, 1, 1), Direction.EAST);
		scene.world.moveSection(mediumLayer, util.vector.of(0, 0, 1), 0);
		scene.idle(30);

		scene.world.setKineticSpeed(util.select.position(5, 0, 3), -16);
		scene.world.setKineticSpeed(util.select.position(5, 1, 2), 32);
		scene.world.setKineticSpeed(util.select.fromTo(4, 2, 2, 5, 2, 2), -32);
		scene.world.setBlock(util.grid.at(2, 2, 2), Blocks.AIR.defaultBlockState(), false);

		scene.world.showSection(util.select.fromTo(3, 2, 2, 4, 2, 2), Direction.WEST);
		scene.idle(10);
		scene.world.showSection(util.select.fromTo(5, 2, 2, 5, 0, 3), Direction.WEST);
		scene.idle(30);

		scene.overlay.showText(60)
			.text("Blasting built-up cannon layers turns them into built-up cannon blocks.")
			.pointAt(util.vector.centerOf(1, 1, 2));
		scene.idle(80);

		scene.overlay.showText(80)
			.text("The process works similar to bulk blasting of items, using a fan blowing into a lava block to produce a heating current.")
			.pointAt(util.vector.blockSurface(util.grid.at(4, 2, 2), Direction.WEST));
		scene.idle(100);

		scene.overlay.showText(60)
			.text("It takes a while for the cannon layers to transform into cannon blocks.");
		scene.idle(40);

		scene.world.setBlock(util.grid.at(0, 1, 2), CBCBlocks.STEEL_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.WEST), true);
		scene.world.setBlock(util.grid.at(1, 1, 2), CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL.getDefaultState().setValue(FACING, Direction.WEST), true);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.STEEL_CANNON_BARREL.getDefaultState().setValue(FACING, Direction.WEST), true);

		scene.idle(5);
		scene.world.setKineticSpeed(util.select.everywhere(), 0);
		scene.idle(30);

		scene.markAsFinished();
	}

	public static void incompleteCannonBlocks(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/incomplete_cannon_blocks", "Incomplete Cannon Blocks");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(20);

		scene.world.showSection(util.select.fromTo(2, 1, 1, 2, 1, 4), Direction.DOWN);
		scene.idle(20);

		BlockPos incompletePos = util.grid.at(2, 1, 2);
		scene.overlay.showText(60)
			.text("Some bored cannon blocks need additional items to be completed.")
			.pointAt(util.vector.centerOf(incompletePos));
		scene.idle(80);

		scene.overlay.showText(120)
			.text("These are usually cannon breech blocks.")
			.colored(PonderPalette.BLUE);
		scene.idle(20);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(incompletePos), Pointing.DOWN).withItem(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.asStack()), 40);
		scene.idle(60);

		scene.world.modifyBlock(util.grid.at(2, 1, 4), copyPropertyTo(FACING, CBCBlocks.STEEL_CANNON_BARREL.getDefaultState()), true);
		scene.world.modifyBlock(util.grid.at(2, 1, 3), copyPropertyTo(FACING, CBCBlocks.STEEL_CANNON_CHAMBER.getDefaultState()), true);
		scene.world.modifyBlock(incompletePos, copyPropertyTo(FACING, CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH.getDefaultState()), true);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(incompletePos), Pointing.DOWN).withItem(CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH.asStack()), 40);
		scene.idle(60);

		scene.world.modifyBlock(util.grid.at(2, 1, 4), copyPropertyTo(FACING, CBCBlocks.CAST_IRON_CANNON_BARREL.getDefaultState()), true);
		scene.world.modifyBlock(util.grid.at(2, 1, 3), copyPropertyTo(FACING, CBCBlocks.CAST_IRON_CANNON_CHAMBER.getDefaultState()), true);
		scene.world.modifyBlock(incompletePos, copyPropertyTo(FACING, CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.getDefaultState().setValue(ALONG_FIRST, true)), true);
		scene.idle(20);

		scene.addKeyframe();

		scene.overlay.showText(60)
			.text("Use the listed items on the block to complete it.")
			.pointAt(util.vector.centerOf(incompletePos))
			.colored(PonderPalette.GREEN);
		scene.idle(20);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(incompletePos), Pointing.DOWN).rightClick().withItem(AllBlocks.SHAFT.asStack()), 40);
		scene.idle(10);
		scene.world.modifyBlock(incompletePos, setStateValue(IncompleteWithItemsCannonBlock.STAGE_2, 1), false);
		scene.idle(40);

		Selection deployerGearDown = util.select.position(3, 0, 5);
		Selection deployerGearUp = util.select.fromTo(4, 1, 2, 4, 1, 5);
		scene.world.showSection(deployerGearDown, Direction.WEST);
		scene.idle(5);
		scene.world.showSection(deployerGearUp, Direction.WEST);
		scene.idle(15);

		BlockPos deployerPos = util.grid.at(4, 1, 2);
		scene.overlay.showText(80)
			.text("Deployers can also complete incomplete cannon blocks.")
			.pointAt(util.vector.centerOf(deployerPos))
			.colored(PonderPalette.BLUE);
		scene.idle(20);
		ItemStack breechblock = CBCItems.CAST_IRON_SLIDING_BREECHBLOCK.asStack();
		Selection deployer = util.select.position(deployerPos);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(deployerPos), Pointing.DOWN).withItem(breechblock), 40);
		scene.idle(30);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, tag -> tag.put("HeldItem", breechblock.save(new CompoundTag())));
		scene.idle(15);

		scene.world.setKineticSpeed(deployerGearDown, -16);
		scene.world.setKineticSpeed(deployerGearUp, 32);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(25);

		scene.world.modifyBlock(incompletePos, copyPropertyTo(FACING, CBCBlocks.CAST_IRON_SLIDING_BREECH.getDefaultState().setValue(ALONG_FIRST, true)), false);

		scene.idle(10);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, tag -> tag.put("HeldItem", ItemStack.EMPTY.save(new CompoundTag())));
		scene.world.setKineticSpeed(deployerGearDown, 16);
		scene.world.setKineticSpeed(deployerGearUp, -32);
		scene.world.moveDeployer(deployerPos, -1, 25);
		scene.idle(35);

		scene.markAsFinished();
	}

	public static void basinFoundry(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/basin_foundry", "Using the Basin Foundry Lid");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.world.showSection(util.select.fromTo(1, 1, 2, 1, 2, 2), Direction.UP);
		scene.idle(15);
		scene.world.showSection(util.select.position(1, 3, 2), Direction.DOWN);
		scene.idle(20);

		scene.overlay.showText(60)
			.text("Basin Foundry Lids are used to melt down metals for cannon casting.")
			.pointAt(util.vector.centerOf(1, 3, 2).subtract(0, -0.25, 0));
		scene.idle(80);

		scene.overlay.showText(60)
			.text("They must be heated to process melting recipes.")
			.pointAt(util.vector.centerOf(1, 1, 2));
		scene.idle(60);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(1, 2, 2), Pointing.DOWN).withItem(CBCItems.CAST_IRON_INGOT.asStack()), 10);
		scene.idle(10);
		scene.world.modifyBlock(util.grid.at(1, 1, 2), setStateValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED), false);
		RandomSource rand = RandomSource.create();
		for (int i = 0; i < 20; ++i) {
			float angle = rand.nextFloat() * 360.0f;
			Vec3 offset = new Vec3(0, 0, 0.25f);
			offset = VecHelper.rotate(offset, angle, Axis.Y);
			Vec3 target = VecHelper.rotate(offset, -25, Axis.Y).add(0, .5f, 0);
			target = VecHelper.offsetRandomly(target.subtract(offset), rand, 1 / 128f);
			Emitter foundryLava = Emitter.simple(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()), target);
			scene.effects.emitParticles(util.vector.topOf(1, 2, 2).add(0, 0.25, 0), foundryLava, 5, 1);
			scene.idle(5);
		}

		Selection pumpGearDown = util.select.position(5, 0, 2);
		Selection pumpGearUp = util.select.fromTo(5, 1, 3, 2, 1, 3);
		Selection pump = util.select.position(2, 2, 2);
		scene.world.showSection(pumpGearDown, Direction.NORTH);
		scene.idle(5);
		scene.world.showSection(pumpGearUp, Direction.NORTH);
		scene.idle(5);
		scene.world.showSection(util.select.fromTo(2, 2, 2, 3, 1, 2), Direction.DOWN);
		scene.idle(20);
		scene.world.setKineticSpeed(pumpGearDown, -16);
		scene.world.setKineticSpeed(pumpGearUp, 32);
		scene.world.setKineticSpeed(pump, -64);
		scene.world.propagatePipeChange(util.grid.at(2, 2, 2));
		scene.idle(20);

		scene.world.modifyBlockEntity(util.grid.at(3, 1, 2), FluidTankBlockEntity.class, tank ->
			PonderPlatform.fillWith(tank, CBCFluids.MOLTEN_CAST_IRON.get(), 8000, null));
		scene.idle(20);

		scene.markAsFinished();
	}

	public static void makingQuickFiringBreeches(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/making_quick_firing_breeches", "Creating a Quick-Firing Breech");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.idle(20);

		BlockPos breechPos = util.grid.at(2, 1, 2);
		scene.world.showSection(util.select.fromTo(breechPos, util.grid.at(2, 1, 4)), Direction.DOWN);
		scene.idle(20);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Use a Quick-Firing Mechanism to convert a Sliding Breech into a Quick-Firing Breech.")
			.pointAt(util.vector.centerOf(breechPos));

		scene.idle(20);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(breechPos), Pointing.DOWN).withItem(CBCBlocks.CAST_IRON_SLIDING_BREECH.asStack()), 60);
		scene.idle(80);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(breechPos), Pointing.DOWN).rightClick().withItem(CBCItems.QUICKFIRING_MECHANISM.asStack()), 60);
		scene.idle(80);
		scene.world.modifyBlock(breechPos, copyPropertyTo(FACING, CBCBlocks.CAST_IRON_QUICKFIRING_BREECH.getDefaultState()), true);
		scene.idle(60);

		scene.world.modifyBlock(breechPos, copyPropertyTo(FACING, CBCBlocks.CAST_IRON_SLIDING_BREECH.getDefaultState()), false);

		Selection deployerGearDown = util.select.position(3, 0, 5);
		Selection deployerGearUp = util.select.fromTo(4, 1, 2, 4, 1, 5);
		scene.world.showSection(deployerGearDown, Direction.WEST);
		scene.idle(5);
		scene.world.showSection(deployerGearUp, Direction.WEST);
		scene.idle(15);

		BlockPos deployerPos = util.grid.at(4, 1, 2);
		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Deployers can also be used to create Quick-Firing Breeches.")
			.pointAt(util.vector.centerOf(deployerPos))
			.colored(PonderPalette.BLUE);
		scene.idle(20);

		ItemStack mechanism = CBCItems.QUICKFIRING_MECHANISM.asStack();
		Selection deployer = util.select.position(deployerPos);
		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(deployerPos), Pointing.DOWN).withItem(mechanism), 40);
		scene.idle(30);
		scene.world.modifyBlockEntityNBT(deployer, DeployerBlockEntity.class, tag -> tag.put("HeldItem", mechanism.save(new CompoundTag())));
		scene.idle(15);

		scene.world.setKineticSpeed(deployerGearDown, -16);
		scene.world.setKineticSpeed(deployerGearUp, 32);
		scene.world.moveDeployer(deployerPos, 1, 25);
		scene.idle(25);

		scene.world.modifyBlock(breechPos, copyPropertyTo(FACING, CBCBlocks.CAST_IRON_QUICKFIRING_BREECH.getDefaultState()), true);

		scene.world.moveDeployer(deployerPos, 0, 25);
		scene.idle(25);

		scene.world.hideSection(deployerGearDown, Direction.EAST);
		scene.idle(5);
		scene.world.hideSection(deployerGearUp, Direction.EAST);
		scene.idle(15);

		scene.overlay.showText(80)
			.attachKeyFrame()
			.text("Wrench a Quick-Firing Breech to revert it back to a Sliding Breech.")
			.pointAt(util.vector.centerOf(breechPos));

		scene.idle(20);

		scene.overlay.showControls(new InputWindowElement(util.vector.topOf(breechPos), Pointing.DOWN).rightClick().withItem(AllItems.WRENCH.asStack()), 60);

		scene.idle(80);

		scene.world.modifyBlock(breechPos, copyPropertyTo(FACING, CBCBlocks.CAST_IRON_SLIDING_BREECH.getDefaultState()), true);

		scene.idle(20);

		scene.markAsFinished();
	}

	public static void weldingCannons(SceneBuilder scene, SceneBuildingUtil util) {
		scene.title("cannon_crafting/cannon_welder", "Using the Cannon Welder");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		scene.world.showSection(util.select.fromTo(1, 1, 2, 3, 1, 2), Direction.DOWN);
		scene.idle(20);
		scene.world.setBlock(util.grid.at(3, 1, 2), CBCBlocks.CAST_IRON_CANNON_END.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(5);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.CAST_IRON_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(5);
		scene.world.setBlock(util.grid.at(1, 1, 2), CBCBlocks.CAST_IRON_CANNON_BARREL.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(20);
		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Cannon blocks placed in survival will not connect to other cannon blocks.")
			.pointAt(util.vector.centerOf(2, 1, 2))
			.placeNearTarget();
		Vec3 seam1 = util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.EAST);
		Vec3 seam2 = util.vector.blockSurface(util.grid.at(2, 1, 2), Direction.WEST);
		scene.addInstruction(new HighlightValueBoxInstruction(seam1, util.vector.of(1 / 8f, 0.5, 0.5), 50));
		scene.addInstruction(new HighlightValueBoxInstruction(seam2, util.vector.of(1 / 8f, 0.5, 0.5), 50));
		scene.idle(75);

		scene.overlay.showText(50)
			.attachKeyFrame()
			.text("The Cannon Welder can quickly connect two cannon blocks at a time.");
		scene.idle(75);
		scene.overlay.showText(80)
			.text("Right-click two adjacent cannon blocks to weld them together.");
		scene.idle(20);
		AABB weld1 = new AABB(util.grid.at(3, 1, 2));
		AABB weld2 = weld1.expandTowards(-1, 0, 0);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.BLUE, weld1, weld1, 5);
		scene.idle(1);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.BLUE, weld1, weld2, 30);
		scene.idle(10);
		scene.overlay.showControls(new InputWindowElement(seam1.add(0, 0.5, 0), Pointing.DOWN).rightClick().withItem(CBCItems.CANNON_WELDER.asStack()), 20);
		scene.idle(10);
		scene.effects.emitParticles(util.vector.centerOf(3, 1, 2), particleEmitterForCannonWelder(Direction.WEST), 5, 1);
		scene.world.modifyBlockEntityNBT(util.select.position(3, 1, 2), BlockEntity.class, setWeldInTag(Direction.WEST, true), true);
		scene.world.modifyBlockEntityNBT(util.select.position(2, 1, 2), BlockEntity.class, setWeldInTag(Direction.EAST, true), true);
		scene.idle(40);

		AABB weld3 = new AABB(util.grid.at(2, 1, 2));
		AABB weld4 = weld3.expandTowards(-1, 0, 0);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.BLUE, weld3, weld3, 5);
		scene.idle(1);
		scene.overlay.chaseBoundingBoxOutline(PonderPalette.BLUE, weld3, weld4, 30);
		scene.idle(10);
		scene.overlay.showControls(new InputWindowElement(seam2.add(0, 0.5, 0), Pointing.DOWN).rightClick().withItem(CBCItems.CANNON_WELDER.asStack()), 20);
		scene.idle(10);
		scene.effects.emitParticles(util.vector.centerOf(2, 1, 2), particleEmitterForCannonWelder(Direction.WEST), 5, 1);
		scene.world.modifyBlockEntityNBT(util.select.position(2, 1, 2), BlockEntity.class, setWeldInTag(Direction.WEST, true), true);
		scene.world.modifyBlockEntityNBT(util.select.position(1, 1, 2), BlockEntity.class, setWeldInTag(Direction.EAST, true), true);
		scene.idle(40);

		scene.overlay.showText(140)
			.attachKeyFrame()
			.text("The cannon blocks must align, connect, have the same material, and have the same cannon type.")
			.colored(PonderPalette.BLUE);
		scene.idle(10);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.CAST_IRON_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.SOUTH), true);
		scene.overlay.showSelectionWithText(util.select.fromTo(2, 1, 2, 3, 1, 2), 120)
			.text("Invalid")
			.colored(PonderPalette.RED)
			.placeNearTarget();
		scene.idle(30);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.CAST_IRON_CANNON_END.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(30);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.BRONZE_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(30);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.CAST_IRON_AUTOCANNON_RECOIL_SPRING.getDefaultState().setValue(FACING, Direction.WEST), true);
		scene.idle(45);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("By default, the Cannon Welder cannot weld some cannon materials and/or types together.")
			.colored(PonderPalette.RED);
		scene.idle(30);
		scene.world.setBlock(util.grid.at(3, 1, 2), CBCBlocks.WROUGHT_IRON_CANNON_END.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(1, 1, 2), CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(5);
		scene.overlay.showOutline(PonderPalette.RED, new Object(), util.select.fromTo(1, 1, 2, 3, 1, 2), 50);
		scene.idle(30);
		scene.world.setBlock(util.grid.at(3, 1, 2), CBCBlocks.NETHERSTEEL_SCREW_BREECH.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.NETHERSTEEL_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(1, 1, 2), CBCBlocks.NETHERSTEEL_CANNON_BARREL.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.idle(15);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("The Cannon Welder can weld different types of cannon.")
			.pointAt(util.vector.centerOf(2, 1, 2))
			.placeNearTarget();
		scene.idle(20);
		scene.world.setBlock(util.grid.at(3, 1, 2), CBCBlocks.STEEL_AUTOCANNON_BREECH.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.STEEL_AUTOCANNON_RECOIL_SPRING.getDefaultState().setValue(FACING, Direction.WEST), true);
		scene.world.setBlock(util.grid.at(1, 1, 2), CBCBlocks.STEEL_AUTOCANNON_BARREL.getDefaultState().setValue(FACING, Direction.WEST), true);
		scene.world.modifyBlockEntityNBT(util.select.position(3, 1, 2), BlockEntity.class, setWeldInTag(Direction.WEST, true), true);
		scene.world.modifyBlockEntityNBT(util.select.position(2, 1, 2), BlockEntity.class, setWeldInTag(Direction.EAST, true), true);
		scene.world.modifyBlockEntityNBT(util.select.position(2, 1, 2), BlockEntity.class, setWeldInTag(Direction.WEST, true), true);
		scene.world.modifyBlockEntityNBT(util.select.position(1, 1, 2), BlockEntity.class, setWeldInTag(Direction.EAST, true), true);
		scene.idle(40);
		scene.world.setBlock(util.grid.at(3, 1, 2), CBCBlocks.CAST_IRON_CANNON_END.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(2, 1, 2), CBCBlocks.CAST_IRON_CANNON_CHAMBER.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.setBlock(util.grid.at(1, 1, 2), CBCBlocks.CAST_IRON_CANNON_BARREL.getDefaultState().setValue(FACING, Direction.EAST), true);
		scene.world.modifyBlockEntityNBT(util.select.position(3, 1, 2), BlockEntity.class, setWeldInTag(Direction.WEST, true), true);
		scene.world.modifyBlockEntityNBT(util.select.position(2, 1, 2), BlockEntity.class, setWeldInTag(Direction.EAST, true), true);
		scene.idle(40);

		scene.overlay.showText(60)
			.attachKeyFrame()
			.text("Welding cannon blocks together can weaken certain types of cannon.")
			.colored(PonderPalette.RED)
			.pointAt(seam1)
			.placeNearTarget();
		scene.idle(75);
		scene.overlay.showText(60)
			.text("It is recommended to craft and move cannon pieces in their entirety instead.")
			.colored(PonderPalette.BLUE);
		scene.idle(80);

		scene.markAsFinished();
	}

	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	private static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	private static final BooleanProperty ATTACHED = CannonBuilderHeadBlock.ATTACHED;
	private static final BooleanProperty ALONG_FIRST = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

	private static <T extends Comparable<T>> UnaryOperator<BlockState> copyPropertyTo(Property<T> property, BlockState newState) {
		return state -> state.hasProperty(property) && newState.hasProperty(property) ? newState.setValue(property, state.getValue(property)) : newState;
	}

	private static <T extends Comparable<T>> UnaryOperator<BlockState> setStateValue(Property<T> property, T value) {
		return state -> state.hasProperty(property) ? state.setValue(property, value) : state;
	}

	private static Consumer<CompoundTag> putItemInDeployer(ItemStack stack) {
		return tag -> tag.put("HeldItem", stack.save(new CompoundTag()));
	}

	private static Consumer<CompoundTag> setUnfinishedCannonShape(CannonCastShape shape) {
		return tag -> tag.putString("Size", CBCRegistries.CANNON_CAST_SHAPES.getKey(shape).toString());
	}

	private static Consumer<CompoundTag> setCentralBlock(BlockPos pos) {
		return tag -> tag.put("CentralBlock", NbtUtils.writeBlockPos(pos));
	}

	private static Consumer<CompoundTag> setFinishedCannonShape(CannonCastShape shape) {
		return tag -> tag.putString("RenderedShape", CBCRegistries.CANNON_CAST_SHAPES.getKey(shape).toString());
	}

	public static Emitter particleEmitterForCannonWelder(Direction dir) {
		return (w, x, y, z) -> CannonWelderSelectionHandler.spawnParticles(w, new BlockPos(x, y, z), dir, true);
	}

	public static Consumer<CompoundTag> setConnectionInTag(Direction dir, boolean connected) {
		String name = dir.getSerializedName();
		return nbt -> {
			ListTag weldedTowards = nbt.getList("Connections", Tag.TAG_STRING);
			weldedTowards.removeIf(t -> t.getType() == StringTag.TYPE && t.getAsString().equals(name));
			if (connected) weldedTowards.add(StringTag.valueOf(name));
		};
	}

	public static Consumer<CompoundTag> setWeldInTag(Direction dir, boolean welded) {
		String name = dir.getSerializedName();
		return nbt -> {
			ListTag weldedTowards = nbt.getList("Welds", Tag.TAG_STRING);
			weldedTowards.removeIf(t -> t.getType() == StringTag.TYPE && t.getAsString().equals(name));
			if (welded) weldedTowards.add(StringTag.valueOf(name));
		};
	}

}
