package rbasamoyai.createbigcannons.munitions.fragment_burst;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class ProjectileBurstPropertiesHandler extends EntityPropertiesTypeHandler<ProjectileBurstProperties> {

	private static final ProjectileBurstProperties DEFAULT = new ProjectileBurstProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, 1);

	@Override
	protected ProjectileBurstProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		int lifetime = Math.max(1, GsonHelper.getAsInt(obj, "lifetime", 1));
		return new ProjectileBurstProperties(ballistics, damage, lifetime);
	}

	@Override
	protected ProjectileBurstProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		int lifetime = buf.readVarInt();
		return new ProjectileBurstProperties(ballistics, damage, lifetime);
	}

	@Override
	protected void writePropertiesToNetwork(ProjectileBurstProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		buf.writeVarInt(properties.lifetime());
	}

	@Override protected ProjectileBurstProperties getNoPropertiesValue() { return DEFAULT; }

}
