package rbasamoyai.createbigcannons.effects.sounds;

import javax.annotation.Nullable;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public abstract class SoundInstanceWrapper implements SoundInstance {

	private final SoundInstance delegate;

	public SoundInstanceWrapper(SoundInstance delegate) {
		this.delegate = delegate;
	}

	@Override public ResourceLocation getLocation() { return this.delegate.getLocation(); }

	@Nullable
	@Override
	public WeighedSoundEvents resolve(SoundManager manager) {
		return this.delegate.resolve(manager);
	}

	@Override public Sound getSound() { return this.delegate.getSound(); }
	@Override public SoundSource getSource() { return this.delegate.getSource(); }
	@Override public boolean isLooping() { return this.delegate.isLooping(); }
	@Override public boolean isRelative() { return this.delegate.isRelative(); }
	@Override public int getDelay() { return this.delegate.getDelay(); }
	@Override public float getVolume() { return this.delegate.getVolume(); }
	@Override public float getPitch() { return this.delegate.getPitch(); }
	@Override public double getX() { return this.delegate.getX(); }
	@Override public double getY() { return this.delegate.getY(); }
	@Override public double getZ() { return this.delegate.getZ(); }
	@Override public Attenuation getAttenuation() { return this.delegate.getAttenuation(); }

}
