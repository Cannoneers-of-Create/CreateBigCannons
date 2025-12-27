package rbasamoyai.createbigcannons.base.tag_utils;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class FluidTypeDataHolder<VALUE> extends TypeAndTagDataHolder<Fluid, VALUE> {

	private final Map<Fluid, VALUE> cachedData = new Reference2ObjectOpenHashMap<>();
	private final Set<Fluid> noData = new ReferenceOpenHashSet<>();

	public FluidTypeDataHolder() { super(CBCRegistryUtils.getFluidRegistry()); }

	@Override
	public void cleanUp() {
		this.cachedData.clear();
		this.noData.clear();
		super.cleanUp();
	}

	@Override
	public void cleanUpTags() {
		this.cachedData.clear();
		this.noData.clear();
		super.cleanUpTags();
	}

	@Nullable
	@Override
	public VALUE getData(Fluid fluid) {
		if (this.noData.contains(fluid))
			return null;
		if (this.cachedData.containsKey(fluid))
			return this.cachedData.get(fluid);
		VALUE result = super.getData(fluid);
		if (result != null)
			return result;
		for (Map.Entry<Fluid, VALUE> entry : this.typeData.entrySet()) {
			if (entry.getKey().isSame(fluid)) {
				VALUE found = entry.getValue();
				this.cachedData.put(fluid, found);
				return found;
			}
		}
		for (Map.Entry<Fluid, VALUE> entry : this.tagData.entrySet()) {
			if (entry.getKey().isSame(fluid)) {
				VALUE found = entry.getValue();
				this.cachedData.put(fluid, found);
				return found;
			}
		}
		this.noData.add(fluid);
		return null;
	}

}
