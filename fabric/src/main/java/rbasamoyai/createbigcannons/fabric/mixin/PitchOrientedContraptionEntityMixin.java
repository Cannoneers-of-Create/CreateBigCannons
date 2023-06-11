package rbasamoyai.createbigcannons.fabric.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.contraptions.OrientedContraptionEntity;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.fabric.mixin_interface.GetItemStorage;

@Mixin(PitchOrientedContraptionEntity.class)
public class PitchOrientedContraptionEntityMixin extends OrientedContraptionEntity implements ItemTransferable {

	PitchOrientedContraptionEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
		return this.contraption instanceof GetItemStorage storage ? storage.getItemStorage() : null;
	}

}
