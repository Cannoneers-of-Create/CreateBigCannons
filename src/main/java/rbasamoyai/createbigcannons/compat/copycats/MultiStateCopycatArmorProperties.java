package rbasamoyai.createbigcannons.compat.copycats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.AllBlocks;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesSerializer;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public abstract class MultiStateCopycatArmorProperties implements BlockArmorPropertiesProvider {

    private final MimickingBlockArmorUnit defaultTotalMultiplier;
    private final Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount;

    protected MultiStateCopycatArmorProperties(MimickingBlockArmorUnit defaultTotalMultiplier, Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount) {
        this.defaultTotalMultiplier = defaultTotalMultiplier;
        this.totalMultiplierByCount = totalMultiplierByCount;
    }

    @Override
    public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
        if (!recurse || !(level.getBlockEntity(pos) instanceof IMultiStateCopycatBlockEntity msbe))
            return 1;
        int count = Math.max(this.getElementCount(level, state, pos), 1);
        MimickingBlockArmorUnit multiplier = this.totalMultiplierByCount.getOrDefault(count, this.defaultTotalMultiplier);
        int unfilled = count;
        float sumHardness = 0;
        Map<String, BlockState> materials = msbe.getMaterialItemStorage().getMaterialMap();
        for (BlockState ccstate : materials.values()) {
            if (ccstate.getDestroySpeed(level, pos) == -1)
                return 1;
            if (ccstate.isAir() || AllBlocks.COPYCAT_BASE.has(ccstate))
                continue;
            sumHardness += (float) BlockArmorPropertiesHandler.getProperties(ccstate).hardness(level, ccstate, pos, false);
            if (--unfilled <= 0)
                break;
        }
        return (sumHardness + unfilled) * multiplier.materialHardnessMultiplier() / count;
    }

    @Override
    public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
        if (!recurse || !(level.getBlockEntity(pos) instanceof IMultiStateCopycatBlockEntity msbe))
            return 1;
        int count = Math.max(this.getElementCount(level, state, pos), 1);
        MimickingBlockArmorUnit multiplier = this.totalMultiplierByCount.getOrDefault(count, this.defaultTotalMultiplier);
        int unfilled = count;
        float sumToughness = 0;
        float unbreakableToughness = 0;
        Map<String, BlockState> materials = msbe.getMaterialItemStorage().getMaterialMap();
        for (BlockState ccstate : materials.values()) {
            if (ccstate.getDestroySpeed(level, pos) == -1)
                unbreakableToughness = Math.max(unbreakableToughness, ccstate.getBlock().getExplosionResistance());
            if (ccstate.isAir() || AllBlocks.COPYCAT_BASE.has(ccstate))
                continue;
            sumToughness += (float) BlockArmorPropertiesHandler.getProperties(ccstate).toughness(level, ccstate, pos, false);
            if (--unfilled <= 0)
                break;
        }
        return unbreakableToughness > 0 ? unbreakableToughness : (sumToughness + unfilled) * multiplier.materialToughnessMultiplier() / count;
    }

    @Override
    public List<BlockState> containedBlockStates(Level level, BlockState state, BlockPos pos, boolean recurse) {
        return recurse && level.getBlockEntity(pos) instanceof IMultiStateCopycatBlockEntity msbe
            ? new ArrayList<>(msbe.getMaterialItemStorage().getAllMaterials()) : List.of();
    }

    protected abstract int getElementCount(Level level, BlockState state, BlockPos pos);

    public MimickingBlockArmorUnit getDefaultTotalMultiplier() { return this.defaultTotalMultiplier; }
    public Map<Integer, MimickingBlockArmorUnit> getTotalMultiplierByCount() { return this.totalMultiplierByCount; }

    public static <T extends MultiStateCopycatArmorProperties> BlockArmorPropertiesSerializer<T> createMultistateSerializer(Factory<T> fac) {
        return new BlockArmorPropertiesSerializer<>() {
            @Override
            public T loadBlockArmorPropertiesFromJson(Block block, JsonObject obj) {
                MimickingBlockArmorUnit defaultProperties = MimickingBlockArmorUnit.fromJson(obj);
                Map<Integer, MimickingBlockArmorUnit> propertiesByState = new Int2ObjectOpenHashMap<>();
                if (obj.has("variants") && obj.get("variants").isJsonObject()) {
                    for (Map.Entry<String, JsonElement> entry : obj.getAsJsonObject("variants").entrySet()) {
                        int count;
                        try {
                            count = Integer.parseInt(entry.getKey());
                        } catch (NumberFormatException e) {
                            CreateBigCannons.LOGGER.warn("Multistate copycat variant for block {} must be an integer (was {})", block, entry.getKey());
                            continue;
                        }
                        if (!entry.getValue().isJsonObject()) {
                            CreateBigCannons.LOGGER.warn("Multistate copycat variant for block {} must be specified by a JSON object (variant {})", block, entry.getKey());
                            continue;
                        }
                        MimickingBlockArmorUnit totalMultiplier = MimickingBlockArmorUnit.fromJson(entry.getValue().getAsJsonObject());
                        propertiesByState.put(count, totalMultiplier);
                    }
                }
                return fac.apply(defaultProperties, propertiesByState);
            }

            @Override
            public void toNetwork(T properties, FriendlyByteBuf buf) {
                properties.getDefaultTotalMultiplier().toNetwork(buf);
                Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount = properties.getTotalMultiplierByCount();
                buf.writeVarInt(totalMultiplierByCount.size());
                for (Map.Entry<Integer, MimickingBlockArmorUnit> entry : totalMultiplierByCount.entrySet()) {
                    buf.writeVarInt(entry.getKey());
                    entry.getValue().toNetwork(buf);
                }
            }

            @Override
            public T fromNetwork(FriendlyByteBuf buf) {
                MimickingBlockArmorUnit defaultProperties = MimickingBlockArmorUnit.fromNetwork(buf);
                Map<Integer, MimickingBlockArmorUnit> propertiesByState = new Int2ObjectOpenHashMap<>();
                int sz = buf.readVarInt();
                for (int i = 0; i < sz; ++i)
                    propertiesByState.put(buf.readVarInt(), MimickingBlockArmorUnit.fromNetwork(buf));
                return fac.apply(defaultProperties, propertiesByState);
            }
        };
    }

    @FunctionalInterface
    public interface Factory<T extends MultiStateCopycatArmorProperties>
        extends BiFunction<MimickingBlockArmorUnit, Map<Integer, MimickingBlockArmorUnit>, T> {
    }

}
