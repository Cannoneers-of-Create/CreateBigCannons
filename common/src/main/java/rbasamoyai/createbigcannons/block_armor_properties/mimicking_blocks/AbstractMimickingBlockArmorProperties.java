package rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks;

import java.util.Map;
import java.util.function.BiFunction;

import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesSerializer;

public abstract class AbstractMimickingBlockArmorProperties implements BlockArmorPropertiesProvider {

	private final MimickingBlockArmorUnit defaultUnit;
	private final Map<BlockState, MimickingBlockArmorUnit> unitsByState;

    protected AbstractMimickingBlockArmorProperties(MimickingBlockArmorUnit defaultUnit, Map<BlockState, MimickingBlockArmorUnit> unitsByState) {
        this.defaultUnit = defaultUnit;
        this.unitsByState = unitsByState;
    }

    @Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		MimickingBlockArmorUnit properties = this.unitsByState.getOrDefault(state, this.defaultUnit);
		BlockState copiedState = this.getCopiedState(level, state, pos);
		return !recurse || this.isEmptyState(level, copiedState, state, pos) ? properties.emptyHardness()
			: BlockArmorPropertiesHandler.getProperties(copiedState).hardness(level, copiedState, pos, false) * properties.materialHardnessMultiplier();
	}

	protected abstract BlockState getCopiedState(Level level, BlockState state, BlockPos pos);

	protected boolean isEmptyState(Level level, BlockState copiedState, BlockState state, BlockPos pos) {
		return copiedState == null || copiedState.isAir();
	}

	public static BlockArmorPropertiesSerializer createMimicrySerializer(Factory fac) {
		return new BlockArmorPropertiesSerializer() {
			@Override
			public BlockArmorPropertiesProvider loadBlockArmorPropertiesFromJson(Block block, String id, JsonObject obj) {
				MimickingBlockArmorUnit defaultProperties = MimickingBlockArmorUnit.fromJson(obj);
				Map<BlockState, MimickingBlockArmorUnit> propertiesByState = new Reference2ObjectOpenHashMap<>();
				if (obj.has("variants") && obj.get("variants").isJsonObject()) {
					propertiesByState.putAll(MimickingBlockArmorUnit.readAllProperties(block, obj.getAsJsonObject("variants")));
				}
				return fac.apply(defaultProperties, propertiesByState);
			}
		};
	}

	@FunctionalInterface
	public interface Factory extends BiFunction<MimickingBlockArmorUnit, Map<BlockState, MimickingBlockArmorUnit>, AbstractMimickingBlockArmorProperties> {
	}

}
