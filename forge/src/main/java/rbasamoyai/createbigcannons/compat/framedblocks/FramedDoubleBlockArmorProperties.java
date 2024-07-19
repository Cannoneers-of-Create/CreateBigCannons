package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;
import xfacthd.framedblocks.common.blockentity.FramedDoubleBlockEntity;

public class FramedDoubleBlockArmorProperties implements BlockArmorPropertiesProvider {

	protected final MimickingBlockArmorUnit defaultPropertiesPrimary;
	protected final MimickingBlockArmorUnit defaultPropertiesSecondary;
	protected final Map<BlockState, MimickingBlockArmorUnit> primaryPropertiesByState;
	protected final Map<BlockState, MimickingBlockArmorUnit> secondaryPropertiesByState;

    public FramedDoubleBlockArmorProperties(MimickingBlockArmorUnit defaultPropertiesPrimary, MimickingBlockArmorUnit defaultPropertiesSecondary,
											Map<BlockState, MimickingBlockArmorUnit> primaryPropertiesByState,
											Map<BlockState, MimickingBlockArmorUnit> secondaryPropertiesByState) {
        this.defaultPropertiesPrimary = defaultPropertiesPrimary;
        this.defaultPropertiesSecondary = defaultPropertiesSecondary;
        this.primaryPropertiesByState = primaryPropertiesByState;
        this.secondaryPropertiesByState = secondaryPropertiesByState;
    }

	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		MimickingBlockArmorUnit primary = this.primaryPropertiesByState.getOrDefault(state, this.defaultPropertiesPrimary);
		MimickingBlockArmorUnit secondary = this.secondaryPropertiesByState.getOrDefault(state, this.defaultPropertiesSecondary);
		BlockState primaryState = Blocks.AIR.defaultBlockState();
		BlockState secondaryState = Blocks.AIR.defaultBlockState();
		if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity fbe) {
			primaryState = fbe.getCamo().getState();
			secondaryState = fbe.getCamoTwo().getState();
		}

		if (primaryState.getDestroySpeed(level, pos) == -1 || secondaryState.getDestroySpeed(level, pos) == -1)
            return 1;

		double primaryPart = !recurse || primaryState.isAir() ? primary.emptyHardness()
			: BlockArmorPropertiesHandler.getProperties(primaryState).hardness(level, primaryState, pos, false) * primary.materialHardnessMultiplier();
		double secondaryPart = !recurse || secondaryState.isAir() ? secondary.emptyHardness()
			: BlockArmorPropertiesHandler.getProperties(secondaryState).hardness(level, secondaryState, pos, false) * secondary.materialHardnessMultiplier();
		return primaryPart + secondaryPart;
	}

	@Override
	public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		MimickingBlockArmorUnit primary = this.primaryPropertiesByState.getOrDefault(state, this.defaultPropertiesPrimary);
		MimickingBlockArmorUnit secondary = this.secondaryPropertiesByState.getOrDefault(state, this.defaultPropertiesSecondary);
		BlockState primaryState = Blocks.AIR.defaultBlockState();
		BlockState secondaryState = Blocks.AIR.defaultBlockState();
		if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity fbe) {
			primaryState = fbe.getCamo().getState();
			secondaryState = fbe.getCamoTwo().getState();
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
		return primaryPart + secondaryPart;
	}

    @Override
    public List<BlockState> containedBlockStates(Level level, BlockState state, BlockPos pos, boolean recurse) {
		BlockState primaryState = Blocks.AIR.defaultBlockState();
		BlockState secondaryState = Blocks.AIR.defaultBlockState();
		if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity fbe) {
			primaryState = fbe.getCamo().getState();
			secondaryState = fbe.getCamoTwo().getState();
		}
        return Lists.newArrayList(primaryState, secondaryState);
    }

    public MimickingBlockArmorUnit getPrimaryDefaultProperties() { return this.defaultPropertiesPrimary; }
	public MimickingBlockArmorUnit getSecondaryDefaultProperties() { return this.defaultPropertiesSecondary; }

	public Map<BlockState, MimickingBlockArmorUnit> getPrimaryPropertiesByState() { return this.primaryPropertiesByState; }
	public Map<BlockState, MimickingBlockArmorUnit> getSecondaryPropertiesByState() { return this.secondaryPropertiesByState; }

}
