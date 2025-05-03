package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;
import rbasamoyai.createbigcannons.forge.mixin.compat.FramedAdjustableDoubleBlockEntityAccessor;
import xfacthd.framedblocks.common.blockentity.doubled.FramedAdjustableDoubleBlockEntity;

public class FramedAdjustableDoubleBlockArmorProperties extends FramedDoubleBlockArmorProperties {

	public FramedAdjustableDoubleBlockArmorProperties(MimickingBlockArmorUnit defaultPropertiesPrimary,
													  MimickingBlockArmorUnit defaultPropertiesSecondary,
													  Map<BlockState, MimickingBlockArmorUnit> primaryPropertiesByState,
													  Map<BlockState, MimickingBlockArmorUnit> secondaryPropertiesByState) {
		super(defaultPropertiesPrimary, defaultPropertiesSecondary, primaryPropertiesByState, secondaryPropertiesByState);
	}

	@Override
	public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		MimickingBlockArmorUnit primary = this.primaryPropertiesByState.getOrDefault(state, this.defaultPropertiesPrimary);
		MimickingBlockArmorUnit secondary = this.secondaryPropertiesByState.getOrDefault(state, this.defaultPropertiesSecondary);
		BlockState primaryState = Blocks.AIR.defaultBlockState();
		BlockState secondaryState = Blocks.AIR.defaultBlockState();
		double primaryMultiplier = 0.5;
		if (level.getBlockEntity(pos) instanceof FramedAdjustableDoubleBlockEntity fbe) {
			primaryState = fbe.getCamo().getState();
			secondaryState = fbe.getCamoTwo().getState();
			int primaryOffset = ((FramedAdjustableDoubleBlockEntityAccessor) fbe).getFirstHeight();
			primaryMultiplier = primaryOffset / 16d;
		}

		boolean secondaryUnbreakable = secondaryState.getDestroySpeed(level, pos) == -1;
		if (primaryState.getDestroySpeed(level, pos) == -1) {
			double primaryResistance = primaryState.getBlock().getExplosionResistance();
			return secondaryUnbreakable ? Math.max(primaryResistance, secondaryState.getBlock().getExplosionResistance()) : primaryResistance;
		}
		if (secondaryUnbreakable)
			return secondaryState.getBlock().getExplosionResistance();

		double primaryPart = !recurse || primaryState.isAir() ? primary.emptyToughness()
			: BlockArmorPropertiesHandler.getProperties(primaryState).toughness(level, primaryState, pos, false) * primary.materialToughnessMultiplier();
		double secondaryPart = !recurse || secondaryState.isAir() ? secondary.emptyToughness()
			: BlockArmorPropertiesHandler.getProperties(secondaryState).toughness(level, secondaryState, pos, false) * secondary.materialToughnessMultiplier();
		double secondaryMultiplier = 1 - primaryMultiplier;
		return primaryPart * primaryMultiplier + secondaryPart * secondaryMultiplier;
	}

}
