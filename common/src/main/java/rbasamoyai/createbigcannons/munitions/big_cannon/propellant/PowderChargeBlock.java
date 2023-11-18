package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;

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
	public boolean canBeLoaded(BlockState state, Direction.Axis axis) {
		return axis == state.getValue(AXIS);
	}

	@Override
	public void consumePropellant(BigCannonBehavior behavior) {
		behavior.removeBlock();
	}

	@Override
	public BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation) {
		if (oldState.hasProperty(BlockStateProperties.AXIS)) {
			Direction.Axis axis = oldState.getValue(BlockStateProperties.AXIS);
			if (axis == rotationAxis) return oldState;
			for (int i = 0; i < rotation.ordinal(); ++i) {
				axis = switch (axis) {
					case X -> rotationAxis == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y;
					case Y -> rotationAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
					case Z -> rotationAxis == Direction.Axis.Y ? Direction.Axis.X : Direction.Axis.Y;
				};
			}
			return oldState.setValue(BlockStateProperties.AXIS, axis);
		}
		return oldState;
	}

	@Override
	public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation) {
		BlockState state = this.defaultBlockState().setValue(AXIS, cannonOrientation.getAxis());
		return new StructureBlockInfo(localPos, state, null);
	}

	@Override
	public ItemStack getExtractedItem(StructureBlockInfo info) {
		return new ItemStack(this);
	}

}
