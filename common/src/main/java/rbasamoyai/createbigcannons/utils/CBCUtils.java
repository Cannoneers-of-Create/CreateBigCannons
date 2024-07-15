package rbasamoyai.createbigcannons.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.serialization.Codec;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.mixin.Matrix3fAccessor;
import rbasamoyai.createbigcannons.mixin.Matrix4fAccessor;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundBlastSoundPacket;
import rbasamoyai.createbigcannons.network.ClientboundSendCustomBreakProgressPacket;

public class CBCUtils {

	private static final Map<String, SoundSource> SOURCE_BY_NAME = Arrays.stream(SoundSource.values())
		.collect(Collectors.toMap(SoundSource::getName, Function.identity()));

	@Nullable public static SoundSource soundSourceFromName(String name) { return SOURCE_BY_NAME.get(name); }
	public static Set<String> getSoundSourceNames() { return SOURCE_BY_NAME.keySet(); }

	public static final Codec<SoundSource> SOUND_SOURCE_CODEC =
		CBCUtils.fromEnumWithStringFunction(SoundSource::values, SoundSource::getName, CBCUtils::soundSourceFromName);

	/**
	 * Alias method for easier porting to 1.21+.
	 *
	 * @param namespace the namespace
	 * @param path the path
	 * @return Resource location of form {@code <namespace>:<path>}
	 */
	public static ResourceLocation location(String namespace, String path) {
		return new ResourceLocation(namespace, path);
	}

	/**
	 * Alias method for easier porting to 1.21+.
	 *
	 * @param location String resource location of form {@code "<namespace>:<path>"}
	 * @return Resource location of form {@code <namespace>:<path>}
	 */
	public static ResourceLocation location(String location) {
		return new ResourceLocation(location);
	}

	/**
	 * Constructor for {@link Matrix3f} with the ability to set individual values.
	 */
	public static Matrix3f mat3x3f(float m00, float m01, float m02,
								   float m10, float m11, float m12,
								   float m20, float m21, float m22) {
		Matrix3f mat3x3f = new Matrix3f();
		Matrix3fAccessor acc = (Matrix3fAccessor) (Object) mat3x3f;
		acc.setM00(m00);
		acc.setM01(m01);
		acc.setM02(m02);
		acc.setM10(m10);
		acc.setM11(m11);
		acc.setM12(m12);
		acc.setM20(m20);
		acc.setM21(m21);
		acc.setM22(m22);
		return mat3x3f;
	}

	/**
	 * Constructor for {@link Matrix4f} with the ability to set individual values.
	 */
	public static Matrix4f mat4x4f(float m00, float m01, float m02, float m03,
								   float m10, float m11, float m12, float m13,
								   float m20, float m21, float m22, float m23,
								   float m30, float m31, float m32, float m33) {
		Matrix4f mat4x4f = new Matrix4f();
		Matrix4fAccessor acc = (Matrix4fAccessor) (Object) mat4x4f;
		acc.setM00(m00);
		acc.setM01(m01);
		acc.setM02(m02);
		acc.setM03(m03);
		acc.setM10(m10);
		acc.setM11(m11);
		acc.setM12(m12);
		acc.setM13(m13);
		acc.setM20(m20);
		acc.setM21(m21);
		acc.setM22(m22);
		acc.setM23(m23);
		acc.setM30(m30);
		acc.setM31(m31);
		acc.setM32(m32);
		acc.setM33(m33);
		return mat4x4f;
	}

	public static Matrix4f mat4x4f(@Nonnull Matrix3f mat3x3f) {
		Matrix3fAccessor m = (Matrix3fAccessor) (Object) mat3x3f;
		return mat4x4f(m.getM00(), m.getM01(), m.getM02(), 0,
			           m.getM10(), m.getM11(), m.getM12(), 0,
			           m.getM20(), m.getM21(), m.getM22(), 0,
			           0,          0,          0,          1);
	}

	/**
	 * Adapted from Inigo Quilez: <a href="https://iquilezles.org/articles/noacos/">https://iquilezles.org/articles/noacos/</a>
	 * <br>This returns the rotation matrix which orients (0, 0, 1) with the input vector.
	 *
	 * @param normalized Normalized vector pointing in a direction
	 * @return {@link Matrix3f} rotating vectors to orient in the specified direction
	 */
	public static Matrix3f mat3x3fFacing(Vec3 normalized) {
		// (0, 0, 1) x norm
		float cx = (float) -normalized.y;
		float cy = (float) normalized.x;
		float cos = (float) normalized.z; // (0, 0, 1) . norm
		float k = 1f / (1f + cos);
		if (Float.isFinite(k))
			return mat3x3f( cx*cx*k+cos, cy*cx*k,      cy,
			                cx*cy*k,     cy*cy*k+cos, -cx,
			               -cy,          cx,           cos);
		return mat3x3f( 1,  0,  0,
			            0, -1,  0,
			            0,  0, -1);
	}

	/**
	 * Adapted from Inigo Quilez: <a href="https://iquilezles.org/articles/noacos/">https://iquilezles.org/articles/noacos/</a>
	 * <br>This returns the rotation matrix which orients the start vector with the input vector.
	 *
	 * @param dest Normalized vector pointing in a direction
	 * @param source Normalized vector pointing in a direction
	 * @return {@link Matrix4f} rotating vectors to orient in the specified direction
	 */
	public static Matrix3f mat3x3fFacing(Vec3 dest, Vec3 source) {
		Vec3 c = source.cross(dest);
		float cx = (float) c.x;
		float cy = (float) c.y;
		float cz = (float) c.z;
		float cos = (float) source.dot(dest);
		float k = 1f / (1f + cos);
		if (Float.isFinite(k))
			return mat3x3f(cx*cx*k+cos, cy*cx*k-cz,  cz*cx*k+cy,
				           cx*cy*k+cz,  cy*cy*k+cos, cz*cy*k-cx,
				           cx*cz*k-cy,  cy*cz*k+cx,  cz*cz*k+cos);
		if (Math.abs(1 - source.dot(new Vec3(0, 0, 1))) < 1e-4d)
			return mat3x3f( 1,  0,  0,
				            0, -1,  0,
				            0,  0, -1);
		Matrix3f first = mat3x3fFacing(source); // (0, 0, 1) -> source
		first.transpose(); // source -> (0, 0, 1)
		Matrix3f second = mat3x3fFacing(dest); // (0, 0, 1) -> dest
		second.mul(first);
		return second;
	}

	/**
	 * {@link #mat3x3fFacing(Vec3)} but padded for {@link Matrix4f}
	 * <br>This returns the rotation matrix which orients (0, 0, 1) with the input vector.
	 *
	 * @param normalized Normalized vector pointing in a direction
	 * @return {@link Matrix4f} rotating vectors to orient in the specified direction
	 */
	public static Matrix4f mat4x4fFacing(Vec3 normalized) {
		// (0, 0, 1) x norm
		float cx = (float) -normalized.y;
		float cy = (float) normalized.x;
		float cos = (float) normalized.z; // (0, 0, 1) . norm
		float k = 1f / (1f + cos);
		if (Float.isFinite(k))
			return mat4x4f( cx*cx*k+cos, cy*cx*k,      cy,  0,
				            cx*cy*k,     cy*cy*k+cos, -cx,  0,
				           -cy,          cx,           cos, 0,
				            0,           0,            0,   1);
		return mat4x4f( 1,  0,  0,  0,
				        0, -1,  0,  0,
		                0,  0, -1,  0,
		                0,  0,  0,  1);
	}

	/**
	 * {@link #mat3x3fFacing(Vec3, Vec3)} but padded for {@link Matrix4f}
	 * <br>This returns the rotation matrix which orients the start vector with the input vector.
	 *
	 * @param dest Normalized vector pointing in a direction
	 * @param source Normalized vector pointing in a direction
	 * @return {@link Matrix4f} rotating vectors to orient in the specified direction
	 */
	public static Matrix4f mat4x4fFacing(Vec3 dest, Vec3 source) {
		Vec3 c = source.cross(dest);
		float cx = (float) c.x;
		float cy = (float) c.y;
		float cz = (float) c.z;
		float cos = (float) source.dot(dest);
		float k = 1f / (1f + cos);
		if (Float.isFinite(k))
			return mat4x4f(cx*cx*k+cos, cy*cx*k-cz,  cz*cx*k+cy,  0,
			               cx*cy*k+cz,  cy*cy*k+cos, cz*cy*k-cx,  0,
			               cx*cz*k-cy,  cy*cz*k+cx,  cz*cz*k+cos, 0,
			               0,           0,           0,           1);
		if (Math.abs(1 - source.dot(new Vec3(0, 0, 1))) < 1e-4d)
			return mat4x4f( 1,  0,  0,  0,
				            0, -1,  0,  0,
				            0,  0, -1,  0,
				            0,  0,  0,  1);
		Matrix4f first = mat4x4fFacing(source); // (0, 0, 1) -> source
		first.transpose(); // source -> (0, 0, 1)
		Matrix4f second = mat4x4fFacing(dest); // (0, 0, 1) -> dest
		second.multiply(first);
		return second;
	}

	public static void sendCustomBlockDamage(Level level, BlockPos pos, int damage) {
		if (!(level instanceof ServerLevel slevel))
			return;

		ClientboundSendCustomBreakProgressPacket pkt = new ClientboundSendCustomBreakProgressPacket(pos, damage);
		for (ServerPlayer splayer : slevel.players()) {
			double d = (double) pos.getX() - splayer.getX();
			double e = (double) pos.getY() - splayer.getY();
			double f = (double) pos.getZ() - splayer.getZ();
			if (d * d + e * e + f * f < 1024.0)
				NetworkPlatform.sendToClientPlayer(pkt, splayer);
		}
	}

	/**
	 * Mixin point for mods such as Valkyrien Skies and Landlord to transform impact vectors. Used for penetration
	 * calculations.
	 *
	 * @param level
	 * @param hitPos
	 * @param normal the untransformed normal
	 * @return the transformed normal vector
	 */
	public static Vec3 getSurfaceNormalVector(Level level, BlockPos hitPos, Vec3 normal) {
		return normal;
	}

	/**
	 * Version of {@link #getSurfaceNormalVector(Level, BlockPos, Vec3)} that takes in a {@link net.minecraft.world.phys.BlockHitResult}.
	 * This calls {@link #getSurfaceNormalVector(Level, BlockPos, Vec3)}; apply mixins to that method instead.
	 */
	public static Vec3 getSurfaceNormalVector(Level level, BlockHitResult hitResult) {
		Direction dir = hitResult.getDirection();
		return getSurfaceNormalVector(level, hitResult.getBlockPos(), new Vec3(dir.getStepX(), dir.getStepY(), dir.getStepZ()));
	}

	public static void playBlastLikeSoundOnServer(ServerLevel level, double x, double y, double z, SoundEvent soundEvent,
												  SoundSource soundSource, float volume, float pitch, float airAbsorption) {
		double radius = volume > 1 ? 16 * volume : 16;
		double radSqr = radius * radius;
		ClientboundBlastSoundPacket packet = new ClientboundBlastSoundPacket(soundEvent, soundSource, x, y, z, volume, pitch, airAbsorption);
		for (ServerPlayer player : level.players()) {
			if (player.distanceToSqr(x, y, z) < radSqr)
				NetworkPlatform.sendToClientPlayer(packet, player);
		}
	}

	/**
	 * Adapted from {@link StringRepresentable#fromEnum(Supplier)}
	 *
	 * @param elementSupplier
	 * @param strFunc
	 * @param namingFunction
	 * @return
	 * @param <E>
	 */
	public static <E extends Enum<E>> Codec<E> fromEnumWithStringFunction(Supplier<E[]> elementSupplier, Function<E, String> strFunc,
																		  Function<String, E> namingFunction) {
		E[] enums = elementSupplier.get();
		return ExtraCodecs.orCompressed(
			ExtraCodecs.stringResolverCodec(strFunc, namingFunction),
			ExtraCodecs.idResolverCodec(Enum::ordinal, i -> i >= 0 && i < enums.length ? enums[i] : null, -1)
		);
	}

	/**
	 * Alias method for easier porting to 1.19+.
	 *
	 * @param reader the string reader
	 * @return the block state represented by the string reader
	 * @throws CommandSyntaxException thrown by BlockStateParser
	 */
	public static BlockState parseBlockState(StringReader reader) throws CommandSyntaxException {
		return BlockStateParser.parseForBlock(CBCRegistryUtils.getBlockRegistry(), reader, false).blockState();
	}

	private CBCUtils() {}

}
