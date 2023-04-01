package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class PowderChargeBlock extends RotatedPillarBlock implements IWrenchable, BigCannonPropellantBlock {

	private static final EnumProperty<Axis> AXIS = RotatedPillarBlock.AXIS;
	
	private final VoxelShaper shapes;
	
	public PowderChargeBlock(Properties properties) {
		super(properties);
		this.shapes = this.makeShapes();
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Block.box(3, 0, 3, 13, 16, 13);
		return new AllShapes.Builder(base).forAxis();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(AXIS));
	}

	@Override
	public float getChargePower(StructureBlockInfo data) {
		return CBCConfigs.SERVER.munitions.powderChargeStrength.getF();
	}

	@Override
	public float getStressOnCannon(StructureBlockInfo data) {
		return getPowderChargeStress();
	}

	@Override
	public float getSpread(StructureBlockInfo data) {
		return CBCConfigs.SERVER.munitions.powderChargeSpread.getF();
	}

	@Override
	public boolean canBeLoaded(BlockState state, Direction.Axis axis) {
		return axis == state.getValue(AXIS);
	}

	@Override
	public void consumePropellant(BigCannonBehavior behavior) {
		behavior.removeBlock();
	}

	public static float getPowderChargeEquivalent(float baseStress) {
		return baseStress / getPowderChargeStress();
	}

	public static float getPowderChargeStress() {
		return CBCConfigs.SERVER.munitions.powderChargeStress.getF();
	}

}
