package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstParentPropertiesComponent;

public class GrapeshotBagPropertiesHandler extends EntityPropertiesTypeHandler<GrapeshotBagProperties> {

	private static final GrapeshotBagProperties DEFAULT = new GrapeshotBagProperties(BigCannonProjectilePropertiesComponent.DEFAULT,
		ProjectileBurstParentPropertiesComponent.DEFAULT);

	@Override
	protected GrapeshotBagProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromJson(id, obj);
		ProjectileBurstParentPropertiesComponent grapeshotBurst = ProjectileBurstParentPropertiesComponent.fromJson(id, "grapeshot_", obj);
		return new GrapeshotBagProperties(bigCannonProperties, grapeshotBurst);
	}

	@Override
	protected GrapeshotBagProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BigCannonProjectilePropertiesComponent bigCannonProperties = BigCannonProjectilePropertiesComponent.fromNetwork(buf);
		ProjectileBurstParentPropertiesComponent grapeshotBurst = ProjectileBurstParentPropertiesComponent.fromNetwork(buf);
		return new GrapeshotBagProperties(bigCannonProperties, grapeshotBurst);
	}

	@Override
	protected void writePropertiesToNetwork(GrapeshotBagProperties properties, FriendlyByteBuf buf) {
		properties.bigCannonProperties().toNetwork(buf);
		properties.grapeshotBurst().toNetwork(buf);
	}

	@Override protected GrapeshotBagProperties getNoPropertiesValue() { return DEFAULT; }

}
