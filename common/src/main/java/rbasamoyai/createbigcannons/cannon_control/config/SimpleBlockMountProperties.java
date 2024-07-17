package rbasamoyai.createbigcannons.cannon_control.config;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import rbasamoyai.createbigcannons.base.BlockStatePredicateHelper;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public record SimpleBlockMountProperties(GeneralMountProperties defaultProperties, Map<BlockState, GeneralMountProperties> propertiesByState)
	implements CannonMountBlockPropertiesProvider {

	@Override
	public float maximumElevation(Level level, BlockState state, BlockPos pos) {
		return this.propertiesByState.getOrDefault(state, this.defaultProperties).maximumElevation();
	}

	@Override
	public float maximumDepression(Level level, BlockState state, BlockPos pos) {
		return this.propertiesByState.getOrDefault(state, this.defaultProperties).maximumDepression();
	}

	public static class Serializer implements CannonMountBlockPropertiesSerializer<SimpleBlockMountProperties> {
		@Override
		public SimpleBlockMountProperties fromJson(BlockEntityType<?> beType, ICannonContraptionType contraptionType, JsonObject obj) {
			Map<BlockState, GeneralMountProperties> propertiesByState = new Reference2ObjectOpenHashMap<>();

			for (Block block : beType.validBlocks) {
				ResourceLocation blockLoc = CBCRegistryUtils.getBlockLocation(block);
				String blockKey = blockLoc.toString();
				if (!obj.has(blockKey) && !obj.get(blockKey).isJsonObject()) continue;

				JsonObject variantsOfBlock = obj.getAsJsonObject(blockKey);
				StateDefinition<Block, BlockState> definition = block.getStateDefinition();
				Set<BlockState> blockStates = new HashSet<>(definition.getPossibleStates());

				for (String variantKey : variantsOfBlock.keySet()) {
					Predicate<BlockState> pred = BlockStatePredicateHelper.variantPredicate(definition, variantKey);
					JsonElement variantEl = variantsOfBlock.get(variantKey);
					if (!variantEl.isJsonObject()) {
						throw new JsonSyntaxException("Invalid info for variant '" + variantKey + "''");
					}
					JsonObject variantInfo = variantEl.getAsJsonObject();
					GeneralMountProperties properties = GeneralMountProperties.fromJson(variantInfo);
					for (Iterator<BlockState> stateIter = blockStates.iterator(); stateIter.hasNext(); ) {
						BlockState state = stateIter.next();
						if (pred.test(state)) {
							propertiesByState.put(state, properties);
							stateIter.remove();
						}
					}
				}
			}
			GeneralMountProperties defaultProperties = GeneralMountProperties.fromJson(obj);
			return new SimpleBlockMountProperties(defaultProperties, propertiesByState);
		}

		@Override
		public void toNetwork(SimpleBlockMountProperties properties, FriendlyByteBuf buf) {
			properties.defaultProperties.toNetwork(buf);
			buf.writeVarInt(properties.propertiesByState.size());
			for (Map.Entry<BlockState, GeneralMountProperties> entry : properties.propertiesByState.entrySet()) {
				buf.writeVarInt(Block.getId(entry.getKey()));
				entry.getValue().toNetwork(buf);
			}
		}

		@Override
		public SimpleBlockMountProperties fromNetwork(FriendlyByteBuf buf) {
			GeneralMountProperties defaultProperties = GeneralMountProperties.fromNetwork(buf);
			int sz = buf.readVarInt();
			Map<BlockState, GeneralMountProperties> propertiesByState = new Reference2ObjectOpenHashMap<>();
			for (int i = 0; i < sz; ++i) {
				BlockState state = Block.stateById(buf.readVarInt());
				GeneralMountProperties properties = GeneralMountProperties.fromNetwork(buf);
				propertiesByState.put(state, properties);
			}
			return new SimpleBlockMountProperties(defaultProperties, propertiesByState);
		}
	}

}
