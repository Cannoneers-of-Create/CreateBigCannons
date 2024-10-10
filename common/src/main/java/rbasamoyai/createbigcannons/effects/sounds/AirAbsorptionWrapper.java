package rbasamoyai.createbigcannons.effects.sounds;

import net.minecraft.client.resources.sounds.SoundInstance;

public class AirAbsorptionWrapper extends SoundInstanceWrapper implements HasAirAbsorptionSound {

	private final float airAbsorption;

	public AirAbsorptionWrapper(SoundInstance delegate, float airAbsorption) {
		super(delegate);
		this.airAbsorption = airAbsorption;
	}

	@Override public float getAirAbsorption() { return this.airAbsorption; }

}
