package rbasamoyai.createbigcannons.index;

import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.connected.CTTypeRegistry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour.CTContext;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour.ContextRequirement;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public enum CBCCTTypes implements CTType {
	CANNON(4, ContextRequirement.builder().vertical().build()) {
		@Override
		public int getTextureIndex(CTContext context) {
			int i = (context.up == context.down ? 1 : 0) + (context.up ? 0 : 2);
			return i * 4;
		}
	};

	private final ResourceLocation id;
	private final int sheetSize;
	private final ContextRequirement contextRequirement;

	CBCCTTypes(int sheetSize, ContextRequirement requirement) {
		this.id = CreateBigCannons.resource(Lang.asId(name()));
		this.sheetSize = sheetSize;
		this.contextRequirement = requirement;
		CTTypeRegistry.register(this);
	}

	@Override public ResourceLocation getId() { return this.id; }
	@Override public int getSheetSize() { return this.sheetSize; }
	@Override public ContextRequirement getContextRequirement() { return this.contextRequirement; }

}
