package rbasamoyai.createbigcannons.index.fluid_utils;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidEntry<T extends CBCFlowingFluid> extends RegistryEntry<T> {

	private final @Nullable BlockEntry<? extends Block> block;

	public FluidEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
		super(owner, delegate);
		BlockEntry<? extends Block> block = null;
		try {
			block = BlockEntry.cast(getSibling(Registry.BLOCK));
		} catch (IllegalArgumentException e) {} // TODO add way to get entry optionally
		this.block = block;
	}

	@Override
	public <R> boolean is(R entry) {
		return get().isSame((Fluid) entry);
	}

	@SuppressWarnings("unchecked")
	<S extends CBCFlowingFluid> S getSource() {
		return (S) get().getSource();
	}

	@SuppressWarnings({ "unchecked", "null" })
	<B extends Block> Optional<B> getBlock() {
		return (Optional<B>) Optional.ofNullable(block).map(RegistryEntry::get);
	}

	@SuppressWarnings({ "unchecked", "null" })
	<I extends Item> Optional<I> getBucket() {
		return Optional.ofNullable((I) get().getBucket());
	}
}
