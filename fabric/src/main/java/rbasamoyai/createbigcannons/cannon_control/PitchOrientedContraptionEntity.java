package rbasamoyai.createbigcannons.cannon_control;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;

public class PitchOrientedContraptionEntity extends AbstractPitchOrientedContraptionEntity implements ItemTransferable {

	public PitchOrientedContraptionEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
		return this.contraption instanceof ItemTransferable it ? it.getItemStorage(face) : null;
	}

}
