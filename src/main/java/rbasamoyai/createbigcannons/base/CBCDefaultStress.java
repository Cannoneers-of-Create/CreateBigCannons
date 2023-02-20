package rbasamoyai.createbigcannons.base;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * Avoiding concurrency issues in config from accessing BlockStressDefaults, also simplified to leave out generators
 * and capacity
 */
public class CBCDefaultStress {

	public static final Map<ResourceLocation, Double> DEFAULT_IMPACTS = new HashMap<>();

	public static void setDefaultImpact(ResourceLocation blockId, double impact) {
		DEFAULT_IMPACTS.put(blockId, impact);
	}

	public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double impact) {
		return b -> {
			setDefaultImpact(new ResourceLocation(b.getOwner()
					.getModid(), b.getName()), impact);
			return b;
		};
	}

}
