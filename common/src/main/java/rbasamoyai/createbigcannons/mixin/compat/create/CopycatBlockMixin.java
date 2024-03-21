package rbasamoyai.createbigcannons.mixin.compat.create;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import rbasamoyai.createbigcannons.base.BlockStatePredicateHelper;
import rbasamoyai.createbigcannons.block_terminal_properties.HasSpecialTerminalBallisticsBlockProperties;
import rbasamoyai.createbigcannons.block_terminal_properties.MimickingBlockTerminalBallisticsProperties;
import rbasamoyai.createbigcannons.block_terminal_properties.TerminalBallisticsBlockPropertiesHandler;

@Mixin(CopycatBlock.class)
public abstract class CopycatBlockMixin extends Block implements HasSpecialTerminalBallisticsBlockProperties {

	@Unique private static final MimickingBlockTerminalBallisticsProperties FALLBACK = new MimickingBlockTerminalBallisticsProperties(1, 1);

	@Unique private MimickingBlockTerminalBallisticsProperties createbigcannons$defaultMimicProperties = FALLBACK;
	@Unique private final Map<BlockState, MimickingBlockTerminalBallisticsProperties> createbigcannons$mimicPropertiesByState = new HashMap<>();

	CopycatBlockMixin(Properties properties) { super(properties); }

	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		MimickingBlockTerminalBallisticsProperties properties = this.createbigcannons$mimicPropertiesByState.getOrDefault(state, this.createbigcannons$defaultMimicProperties);
		BlockState copiedState = CopycatBlock.getMaterial(level, pos);
		return !recurse || copiedState.isAir() || AllBlocks.COPYCAT_BASE.has(copiedState) ? properties.emptyHardness()
			: TerminalBallisticsBlockPropertiesHandler.getProperties(copiedState).hardness(level, state, pos, false) * properties.emptyHardness();
	}

	@Override
	public void loadTerminalBallisticsBlockPropertiesFromJson(String id, JsonObject obj) {
		this.createbigcannons$mimicPropertiesByState.clear();

		StateDefinition<Block, BlockState> definition = this.getStateDefinition();
		Set<BlockState> states = new HashSet<>(definition.getPossibleStates());
		if (obj.has("variants") && obj.get("variants").isJsonObject()) {
			JsonObject variants = obj.getAsJsonObject("variants");
			for (String key : variants.keySet()) {
				Predicate<BlockState> pred = BlockStatePredicateHelper.variantPredicate(definition, key);
				JsonElement el = variants.get(key);
				if (!el.isJsonObject()) {
					throw new JsonSyntaxException("Invalid info for variant '" + key + "''");
				}
				JsonObject variantInfo = el.getAsJsonObject();
				MimickingBlockTerminalBallisticsProperties properties = MimickingBlockTerminalBallisticsProperties.fromJson(variantInfo);
				for (Iterator<BlockState> stateIter = states.iterator(); stateIter.hasNext(); ) {
					BlockState state = stateIter.next();
					if (pred.test(state)) {
						this.createbigcannons$mimicPropertiesByState.put(state, properties);
						stateIter.remove();
					}
				}
			}
		}
		this.createbigcannons$defaultMimicProperties = MimickingBlockTerminalBallisticsProperties.fromJson(obj);
	}

}
