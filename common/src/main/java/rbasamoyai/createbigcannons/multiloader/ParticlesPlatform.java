package rbasamoyai.createbigcannons.multiloader;

import com.simibubi.create.content.contraptions.particle.ICustomParticleData;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ParticlesPlatform {
    @ExpectPlatform
    public static <T extends ParticleOptions> void register(ICustomParticleData<T> iCustomParticleData, ParticleType<T> object, ParticleEngine particles) {
        throw new AssertionError();
    }
}
