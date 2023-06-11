package rbasamoyai.createbigcannons.multiloader.fabric;

import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ParticlesPlatformImpl {
    public static <T extends ParticleOptions> void register(ICustomParticleData<T> iCustomParticleData, ParticleType<T> object, ParticleEngine particles) {
        iCustomParticleData
                .register(object, particles);
    }
}
