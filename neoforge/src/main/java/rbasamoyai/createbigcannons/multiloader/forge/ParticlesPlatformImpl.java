package rbasamoyai.createbigcannons.multiloader.forge;

import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

public class ParticlesPlatformImpl {
    public static <T extends ParticleOptions> void register(ICustomParticleData<T> iCustomParticleData, ParticleType<T> object, ParticleEngine particles) {
        iCustomParticleData
                .register(object, new RegisterParticleProvidersEvent(particles));
    }
}
