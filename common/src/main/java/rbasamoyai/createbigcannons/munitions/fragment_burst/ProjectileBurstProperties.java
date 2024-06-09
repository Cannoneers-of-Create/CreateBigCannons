package rbasamoyai.createbigcannons.munitions.fragment_burst;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import rbasamoyai.createbigcannons.munitions.BaseProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

public class ProjectileBurstProperties extends BaseProjectileProperties {

	private final int lifetime;

	public ProjectileBurstProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable, boolean ignoresEntityArmor,
									 double gravity, double drag, boolean isQuadraticDrag, float knockback, int lifetime) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, isQuadraticDrag, knockback);
		this.lifetime = lifetime;
	}

	public ProjectileBurstProperties(String id, JsonObject obj) {
		super(id, obj);
		this.lifetime = Math.max(1, GsonHelper.getAsInt(obj, "lifetime", 1));
	}

	public ProjectileBurstProperties(FriendlyByteBuf buf) {
		super(buf);
		this.lifetime = buf.readVarInt();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeVarInt(this.lifetime);
	}

	public int lifetime() { return this.lifetime; }

	public static class Serializer implements MunitionPropertiesSerializer<ProjectileBurstProperties> {
		@Override
		public ProjectileBurstProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new ProjectileBurstProperties(loc.toString(), obj);
		}

		@Override
		public ProjectileBurstProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new ProjectileBurstProperties(buf);
		}
	}

}
