package rbasamoyai.createbigcannons.neoforge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.contraptions.OrientedContraptionEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.neoforge.mixin_interface.GetItemStorage;
import rbasamoyai.createbigcannons.neoforge.remix.CBCHasIItemHandlerEntity;

@Mixin(PitchOrientedContraptionEntity.class)
public class PitchOrientedContraptionEntityMixin extends OrientedContraptionEntity implements CBCHasIItemHandlerEntity {

	PitchOrientedContraptionEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

    @Nullable
    @Override
    public IItemHandler getItemHandler() {
        return this.contraption instanceof GetItemStorage storage ? storage.getItemStorage() : null;
    }

    // TODO: address invalidation?

}
