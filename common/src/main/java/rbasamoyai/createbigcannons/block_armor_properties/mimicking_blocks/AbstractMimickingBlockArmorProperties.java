package rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks;

import java.util.Map;
import java.util.function.BiFunction;

import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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
		if (copiedState.getDestroySpeed(level, pos) == -1)
			return 1;
		return !recurse || this.isEmptyState(level, copiedState, state, pos) ? properties.emptyHardness()
			: BlockArmorPropertiesHandler.getProperties(copiedState).hardness(level, copiedState, pos, false) * properties.materialHardnessMultiplier();
	}

	@Override
	public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		MimickingBlockArmorUnit properties = this.unitsByState.getOrDefault(state, this.defaultUnit);
		BlockState copiedState = this.getCopiedState(level, state, pos);
		if (copiedState.getDestroySpeed(level, pos) == -1)
			return copiedState.getBlock().getExplosionResistance();
		return !recurse || this.isEmptyState(level, copiedState, state, pos) ? properties.emptyToughness()
			: BlockArmorPropertiesHandler.getProperties(copiedState).toughness(level, copiedState, pos, false) * properties.materialToughnessMultiplier();
	}

	protected abstract BlockState getCopiedState(Level level, BlockState state, BlockPos pos);

	protected boolean isEmptyState(Level level, BlockState copiedState, BlockState state, BlockPos pos) {
		return copiedState == null || copiedState.isAir();
	}

	public MimickingBlockArmorUnit getDefaultProperties() { return this.defaultUnit; }
	public Map<BlockState, MimickingBlockArmorUnit> getPropertiesByState() { return this.unitsByState; }

	public static <T extends AbstractMimickingBlockArmorProperties> BlockArmorPropertiesSerializer<T> createMimicrySerializer(Factory<T> fac) {
		return new BlockArmorPropertiesSerializer<>() {
			@Override
			public T loadBlockArmorPropertiesFromJson(Block block, JsonObject obj) {
				MimickingBlockArmorUnit defaultProperties = MimickingBlockArmorUnit.fromJson(obj);
				Map<BlockState, MimickingBlockArmorUnit> propertiesByState = new Reference2ObjectOpenHashMap<>();
				if (obj.has("variants") && obj.get("variants").isJsonObject()) {
					propertiesByState.putAll(MimickingBlockArmorUnit.readAllProperties(block, obj.getAsJsonObject("variants")));
				}
				return fac.apply(defaultProperties, propertiesByState);
			}

			@Override
			public void toNetwork(T properties, FriendlyByteBuf buf) {
				properties.getDefaultProperties().toNetwork(buf);
				MimickingBlockArmorUnit.writePropertiesToBuf(properties.getPropertiesByState(), buf);
			}

			@Override
			public T fromNetwork(FriendlyByteBuf buf) {
				MimickingBlockArmorUnit defaultProperties = MimickingBlockArmorUnit.fromNetwork(buf);
				Map<BlockState, MimickingBlockArmorUnit> propertiesByState = MimickingBlockArmorUnit.readPropertiesFromBuf(buf);
				return fac.apply(defaultProperties, propertiesByState);
			}
		};
	}

	@FunctionalInterface
	public interface Factory<T extends AbstractMimickingBlockArmorProperties>
		extends BiFunction<MimickingBlockArmorUnit, Map<BlockState, MimickingBlockArmorUnit>, T> {
	}

}
