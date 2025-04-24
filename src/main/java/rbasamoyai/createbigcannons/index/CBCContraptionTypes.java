package rbasamoyai.createbigcannons.index;

import com.simibubi.create.AllContraptionTypes;
import com.simibubi.create.Create;
import com.simibubi.create.api.contraption.ContraptionType;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoadingContraption;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillingContraption;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuildingContraption;

import java.util.function.Supplier;

import static com.simibubi.create.AllContraptionTypes.BY_LEGACY_NAME;

public class CBCContraptionTypes {

	public static final Holder.Reference<ContraptionType>
		CANNON_LOADER = register(CreateBigCannons.resource("cannon_loader").toString(), CannonLoadingContraption::new),
        MOUNTED_CANNON = register(CreateBigCannons.resource("mounted_cannon").toString(), MountedBigCannonContraption::new),
        MOUNTED_AUTOCANNON = register(CreateBigCannons.resource("mounted_autocannon").toString(), MountedAutocannonContraption::new),
        CANNON_DRILL = register(CreateBigCannons.resource("cannon_drill").toString(), CannonDrillingContraption::new),
        CANNON_BUILDER = register(CreateBigCannons.resource("cannon_builder").toString(), CannonBuildingContraption::new);

    private static Holder.Reference<ContraptionType> register(String name, Supplier<? extends Contraption> factory) {
        ContraptionType type = new ContraptionType(factory);
        BY_LEGACY_NAME.put(name, type);

        return Registry.registerForHolder(CreateBuiltInRegistries.CONTRAPTION_TYPE, CreateBigCannons.resource(name), type);
    }

    public static void init() {
    }

}
