package rbasamoyai.createbigcannons.base;

import com.tterrag.registrate.util.nullness.NonNullSupplier;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class LazySupplier<T> implements NonNullSupplier<T> {

	private Supplier<T> sup;
	private T value = null;

	public LazySupplier(@Nonnull Supplier<T> sup) {
		this.sup = sup;
	}

	@Override
	public T get() {
		Supplier<T> sup = this.sup;
		if (sup != null) {
			this.value = sup.get();
			this.sup = null;
		}
		return this.value;
	}

}
