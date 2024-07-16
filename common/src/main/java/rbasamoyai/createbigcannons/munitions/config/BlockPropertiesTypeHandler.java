package rbasamoyai.createbigcannons.munitions.config;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockPropertiesTypeHandler<PROPERTIES> extends PropertiesTypeHandler<Block, PROPERTIES> {

	@Nonnull public final PROPERTIES getPropertiesOf(BlockState blockState) { return this.getPropertiesOf(blockState.getBlock()); }

}
