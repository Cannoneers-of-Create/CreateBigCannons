package rbasamoyai.createbigcannons.cannons.autocannon.material;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record AutocannonMaterialProperties(int maxBarrelLength, float weight, float baseSpread,
										   float spreadReductionPerBarrel,
										   float baseSpeed, float speedIncreasePerBarrel, int maxSpeedIncreases,
										   int projectileLifetime, float baseRecoil, boolean connectsInSurvival,
										   boolean isWeldable, int weldDamage, int weldStressPenalty) {

	public static AutocannonMaterialProperties fromJson(JsonObject obj) {
		int maxBarrelLength = Math.max(1, GsonHelper.getAsInt(obj, "maximum_barrel_length"));
		float weight = Math.max(0, GsonHelper.getAsFloat(obj, "weight", 2));
		float baseSpread = Math.max(0.01f, GsonHelper.getAsFloat(obj, "base_spread", 3));
		float spreadReductionPerBarrel = Math.max(0, GsonHelper.getAsFloat(obj, "spread_reduction_per_barrel", 0.5f));
		float baseSpeed = Math.max(0.1f, GsonHelper.getAsFloat(obj, "base_speed", 1));
		float speedIncreasePerBarrel = Math.max(0, GsonHelper.getAsFloat(obj, "speed_increase_per_barrel", 0.5f));
		int maxSpeedIncreases = Math.max(0, GsonHelper.getAsInt(obj, "max_speed_increases", 2));
		int projectileLifetime = Math.max(1, GsonHelper.getAsInt(obj, "projectile_lifetime"));
		float baseRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "base_recoil", 1));
		boolean connectsInSurvival = GsonHelper.getAsBoolean(obj, "connects_in_survival", false);
		boolean isWeldable = GsonHelper.getAsBoolean(obj, "is_weldable", false);
		int weldDamage = Math.max(GsonHelper.getAsInt(obj, "weld_damage", 0), 0);
		int weldStressPenalty = Math.max(GsonHelper.getAsInt(obj, "weld_stress_penalty", 0), 0);
		return new AutocannonMaterialProperties(maxBarrelLength, weight, baseSpread, spreadReductionPerBarrel, baseSpeed,
			speedIncreasePerBarrel, maxSpeedIncreases, projectileLifetime, baseRecoil, connectsInSurvival, isWeldable,
			weldDamage, weldStressPenalty);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("maximum_barrel_length", this.maxBarrelLength);
		obj.addProperty("weight", this.weight);
		obj.addProperty("base_spread", this.baseSpread);
		obj.addProperty("spread_reduction_per_barrel", this.spreadReductionPerBarrel);
		obj.addProperty("base_speed", this.baseSpeed);
		obj.addProperty("speed_increase_per_barrel", this.speedIncreasePerBarrel);
		obj.addProperty("max_speed_increases", this.maxSpeedIncreases);
		obj.addProperty("projectile_lifetime", this.projectileLifetime);
		obj.addProperty("base_recoil", this.baseRecoil);
		obj.addProperty("connects_in_survival", this.connectsInSurvival);
		obj.addProperty("is_weldable", this.isWeldable);
		obj.addProperty("weld_damage", this.weldDamage);
		obj.addProperty("weld_stress_penalty", this.weldStressPenalty);
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(this.maxBarrelLength);
		buf.writeFloat(this.weight)
			.writeFloat(this.baseSpread)
			.writeFloat(this.spreadReductionPerBarrel)
			.writeFloat(this.baseSpeed)
			.writeFloat(this.speedIncreasePerBarrel);
		buf.writeVarInt(this.maxSpeedIncreases)
			.writeVarInt(this.projectileLifetime)
			.writeFloat(this.baseRecoil)
			.writeBoolean(this.connectsInSurvival)
			.writeBoolean(this.isWeldable);
		buf.writeVarInt(this.weldDamage)
			.writeVarInt(this.weldStressPenalty);
	}

	public static AutocannonMaterialProperties fromBuf(FriendlyByteBuf buf) {
		int maxBarrelLength = buf.readVarInt();
		float weight = buf.readFloat();
		float baseSpread = buf.readFloat();
		float spreadReductionPerBarrel = buf.readFloat();
		float baseSpeed = buf.readFloat();
		float speedIncreasePerBarrel = buf.readFloat();
		int maxSpeedIncreases = buf.readVarInt();
		int projectileLifetime = buf.readVarInt();
		float baseRecoil = buf.readFloat();
		boolean connectsInSurvival = buf.readBoolean();
		boolean isWeldable = buf.readBoolean();
		int weldDamage = buf.readVarInt();
		int weldStressPenalty = buf.readVarInt();
		return new AutocannonMaterialProperties(maxBarrelLength, weight, baseSpread, spreadReductionPerBarrel, baseSpeed,
			speedIncreasePerBarrel, maxSpeedIncreases, projectileLifetime, baseRecoil, connectsInSurvival, isWeldable,
			weldDamage, weldStressPenalty);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private int maxBarrelLength;
		private float weight;
		private float baseSpread;
		private float spreadReductionPerBarrel;
		private float baseSpeed;
		private float speedIncreasePerBarrel;
		private int maxSpeedIncreases;
		private int projectileLifetime;
		private float baseRecoil;
		private boolean connectsInSurvival;
		private boolean isWeldable;
		private int weldDamage;
		private int weldStressPenalty;

		private Builder() {
		}

		public Builder maxBarrelLength(int maxBarrelLength) {
			this.maxBarrelLength = maxBarrelLength;
			return this;
		}

		public Builder weight(float weight) {
			this.weight = weight;
			return this;
		}

		public Builder baseSpread(float baseSpread) {
			this.baseSpread = baseSpread;
			return this;
		}

		public Builder spreadReductionPerBarrel(float spreadReductionPerBarrel) {
			this.spreadReductionPerBarrel = spreadReductionPerBarrel;
			return this;
		}

		public Builder baseSpeed(float baseSpeed) {
			this.baseSpeed = baseSpeed;
			return this;
		}

		public Builder speedIncreasePerBarrel(float speedIncreasePerBarrel) {
			this.speedIncreasePerBarrel = speedIncreasePerBarrel;
			return this;
		}

		public Builder maxSpeedIncreases(int maxSpeedIncreases) {
			this.maxSpeedIncreases = maxSpeedIncreases;
			return this;
		}

		public Builder projectileLifetime(int projectileLifetime) {
			this.projectileLifetime = projectileLifetime;
			return this;
		}

		public Builder baseRecoil(float baseRecoil) {
			this.baseRecoil = baseRecoil;
			return this;
		}

		public Builder connectsInSurvival(boolean connectsInSurvival) {
			this.connectsInSurvival = connectsInSurvival;
			return this;
		}

		public Builder isWeldable(boolean isWeldable) {
			this.isWeldable = isWeldable;
			return this;
		}

		public Builder weldDamage(int weldDamage) {
			this.weldDamage = weldDamage;
			return this;
		}

		public Builder weldStressPenalty(int weldStressPenalty) {
			this.weldStressPenalty = weldStressPenalty;
			return this;
		}

		public AutocannonMaterialProperties build() {
			return new AutocannonMaterialProperties(this.maxBarrelLength, this.weight, this.baseSpread,
				this.spreadReductionPerBarrel, this.baseSpeed, this.speedIncreasePerBarrel,
				this.maxSpeedIncreases, this.projectileLifetime, this.baseRecoil, this.connectsInSurvival,
				this.isWeldable, this.weldDamage, this.weldStressPenalty);
		}
	}

}
