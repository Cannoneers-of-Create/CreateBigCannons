package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record BlockHitEffect(List<ParticleOptions> impactParticles, List<ParticleOptions> deflectParticles, HitSound impactSound, HitSound deflectSound) {

	public static BlockHitEffect fromJson(JsonObject obj) throws CommandSyntaxException, JsonParseException {
		List<ParticleOptions> impactParticles = new ArrayList<>();
		if (GsonHelper.isStringValue(obj, "impact_particle")) {
			String particle = GsonHelper.getAsString(obj, "impact_particle");
			ParticleOptions options = ParticleArgument.readParticle(new StringReader(particle));
			impactParticles.add(options);
		} else if (GsonHelper.isArrayNode(obj, "impact_particles")) {
			JsonArray arr = GsonHelper.getAsJsonArray(obj, "impact_particles");
			for (JsonElement el : arr) {
				String particle = el.getAsString();
				ParticleOptions options = ParticleArgument.readParticle(new StringReader(particle));
				impactParticles.add(options);
			}
		} else {
			throw new JsonSyntaxException("Impact particles should either be specified as string \"impact_particle\" or string array \"impact_particles\"");
		}
		List<ParticleOptions> deflectParticles = new ArrayList<>();
		if (GsonHelper.isStringValue(obj, "deflect_particle")) {
			String particle = GsonHelper.getAsString(obj, "deflect_particle");
			ParticleOptions options = ParticleArgument.readParticle(new StringReader(particle));
			deflectParticles.add(options);
		} else if (GsonHelper.isArrayNode(obj, "deflect_particles")) {
			JsonArray arr = GsonHelper.getAsJsonArray(obj, "deflect_particles");
			for (JsonElement el : arr) {
				String particle = el.getAsString();
				ParticleOptions options = ParticleArgument.readParticle(new StringReader(particle));
				deflectParticles.add(options);
			}
		} else {
			throw new JsonSyntaxException("Deflect particles should either be specified as string \"deflect_particle\" or string array \"deflect_particles\"");
		}
		HitSound impactSound = HitSound.fromJson(obj.getAsJsonObject("impact_sound"));
		HitSound deflectSound = HitSound.fromJson(obj.getAsJsonObject("deflect_sound"));
		return new BlockHitEffect(impactParticles, deflectParticles, impactSound, deflectSound);
	}

	public void playEffect(Level level, boolean deflect, boolean forceDisplay, double x, double y, double z, double dx,
						   double dy, double dz, EntityType<?> entityType, BlockState blockState, ProjectileHitEffect projectileEffect) {
		List<ParticleOptions> particles = deflect ? this.deflectParticles : this.impactParticles;
		for (ParticleOptions option : particles) {
			option = ProjectileParticleEffectModifiers.applyEffects(option, entityType, blockState, projectileEffect);
			level.addParticle(option, forceDisplay, x, y, z, dx, dy, dz);
		}
		BlockHitEffect.HitSound sound = deflect ? this.deflectSound : this.impactSound;
		sound.playSound(level, x, y, z, dx, dy, dz, projectileEffect);
	}

	public record HitSound(ResourceLocation location, SoundSource source, float basePitch, float pitchVariation) {

		public static HitSound fromJson(JsonObject obj) throws JsonParseException {
			ResourceLocation id = CBCUtils.location(GsonHelper.getAsString(obj, "sound"));
			String sourceName = GsonHelper.getAsString(obj, "source", SoundSource.BLOCKS.getName());
			SoundSource source = CBCUtils.soundSourceFromName(sourceName);
			if (source == null) {
				String types = '\'' + String.join("', '", CBCUtils.getSoundSourceNames()) + '\'';
				throw new JsonParseException("Invalid sound type '" + sourceName + "', should either be absent or one of " + types);
			}
			float pitch = GsonHelper.getAsFloat(obj, "pitch", 1);
			float pitchVariation = GsonHelper.getAsFloat(obj, "pitch_variation", 0);
			return new HitSound(id, source, pitch, pitchVariation);
		}

		public void playSound(Level level, double x, double y, double z, double dx, double dy, double dz, ProjectileHitEffect projectileEffect) {
			EnvExecute.executeOnClient(() -> () -> CBCClientCommon.playCustomSound(this, level, x, y, z, dx, dy, dz, projectileEffect));
		}
	}

}
