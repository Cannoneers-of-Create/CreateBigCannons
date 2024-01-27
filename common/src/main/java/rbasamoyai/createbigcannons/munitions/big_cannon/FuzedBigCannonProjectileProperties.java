package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class FuzedBigCannonProjectileProperties extends BigCannonProjectileProperties {

	private final boolean baseFuze;

	public FuzedBigCannonProjectileProperties(float entityDamage, float durabilityMass, boolean rendersInvulnerable,
											  boolean ignoresEntityArmor, double gravity, double drag, float knockback,
											  int addedChargePower, float minimumChargePower, boolean canSquib, float addedRecoil,
											  boolean baseFuze) {
		super(entityDamage, durabilityMass, rendersInvulnerable, ignoresEntityArmor, gravity, drag, knockback, addedChargePower, minimumChargePower, canSquib, addedRecoil);
		this.baseFuze = baseFuze;
	}

	public FuzedBigCannonProjectileProperties(String id, JsonObject obj) {
		super(id, obj);
		this.baseFuze = GsonHelper.getAsBoolean(obj, "base_fuze", false);
	}

	public FuzedBigCannonProjectileProperties(FriendlyByteBuf buf) {
		super(buf);
		this.baseFuze = buf.readBoolean();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeBoolean(this.baseFuze);
	}

	public boolean baseFuze() { return this.baseFuze; }

}
