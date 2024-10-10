package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.SmokeShellSmokeParticleData;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

public class SmokeEmitterEntity extends Entity {

	private static final EntityDataAccessor<Float> SMOKE_SIZE_X = SynchedEntityData.defineId(SmokeEmitterEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> SMOKE_SIZE_Y = SynchedEntityData.defineId(SmokeEmitterEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> SMOKE_SIZE_Z = SynchedEntityData.defineId(SmokeEmitterEntity.class, EntityDataSerializers.FLOAT);

	protected int duration;
	protected int age;

	public SmokeEmitterEntity(EntityType<? extends SmokeEmitterEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(SMOKE_SIZE_X, 0f);
		this.entityData.define(SMOKE_SIZE_Y, 0f);
		this.entityData.define(SMOKE_SIZE_Z, 0f);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putInt("Duration", this.duration);
		tag.putInt("Age", this.age);
		tag.put("Size", this.newDoubleList(this.getSizeX(), this.getSizeY(), this.getSizeZ()));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.duration = tag.getInt("Duration");
		ListTag sizeDims = tag.getList("Size", Tag.TAG_DOUBLE);
		this.setSizeX((float) sizeDims.getDouble(0));
		this.setSizeY((float) sizeDims.getDouble(1));
		this.setSizeZ((float) sizeDims.getDouble(2));
	}

	public void setDuration(int duration) { this.duration = duration; }

	/**
	 * Set the size of all three dimensions. This forms a cube.
	 * @param size the size of the smoke shell
	 */
	public void setSize(float size) {
		this.entityData.set(SMOKE_SIZE_X, Math.abs(size));
		this.entityData.set(SMOKE_SIZE_Y, Math.abs(size));
		this.entityData.set(SMOKE_SIZE_Z, Math.abs(size));
		this.setBoundingBox(this.makeBoundingBox());
	}

	public float getMeanSize() {
		float sizeX = this.getSizeX();
		float sizeY = this.getSizeY();
		float sizeZ = this.getSizeZ();
		return (sizeX + sizeY + sizeZ) / 3f;
	}

	public float getVolume() { return this.getSizeX() * this.getSizeY() * this.getSizeZ(); }

	public void setSizeX(float sizeX) {
		this.entityData.set(SMOKE_SIZE_X, Math.abs(sizeX));
		this.setBoundingBox(this.makeBoundingBox());
	}

	public float getSizeX() { return this.entityData.get(SMOKE_SIZE_X); }

	public void setSizeY(float sizeY) {
		this.entityData.set(SMOKE_SIZE_Y, Math.abs(sizeY));
		this.setBoundingBox(this.makeBoundingBox());
	}

	public float getSizeY() { return this.entityData.get(SMOKE_SIZE_Y); }

	public void setSizeZ(float sizeZ) {
		this.entityData.set(SMOKE_SIZE_Z, Math.abs(sizeZ));
		this.setBoundingBox(this.makeBoundingBox());
	}

	public float getSizeZ() { return this.entityData.get(SMOKE_SIZE_Z); }

	@Override
	public void tick() {
		boolean doStuff = this.canDoStuff();
		if (this.level().isClientSide) {
			if (doStuff) {
				double sizeX = this.getSizeX();
				double sizeY = this.getSizeY();
				double sizeZ = this.getSizeZ();
				double count = Math.ceil(this.getMeanSize());
				ParticleOptions particle = this.getParticle();
				double baseX = this.getX();
				double baseY = this.getY();
				double baseZ = this.getZ();
				for (int i = 0; i < count; ++i) {
					double rx = this.random.nextDouble() * sizeX - sizeX * 0.5 + baseX;
					double ry = this.random.nextDouble() * sizeY - sizeY * 0.5 + baseY;
					double rz = this.random.nextDouble() * sizeZ - sizeZ * 0.5 + baseZ;
					double dx = this.random.nextGaussian() * 0.05;
					double dy = this.random.nextGaussian() * 0.02;
					double dz = this.random.nextGaussian() * 0.05;
					this.level().addParticle(particle, true, rx, ry, rz, dx, dy, dz);
				}
			}
		} else {
			if (doStuff) {
				double sizeX = this.getSizeX();
				double sizeY = this.getSizeY();
				double sizeZ = this.getSizeZ();
				AABB bounding = this.getBoundingBox();
				AABB queryBounding = bounding.inflate(8);
				for (SmokeEmitterEntity other : this.level().getEntitiesOfClass(SmokeEmitterEntity.class, queryBounding, this::canMergeWithOther)) {
					double otherSizeX = other.getSizeX();
					double otherSizeY = other.getSizeY();
					double otherSizeZ = other.getSizeZ();
					Vec3 disp = other.position().subtract(this.position());
					Vec3 totalDims = new Vec3(sizeX + otherSizeX, sizeY + otherSizeY, sizeZ + otherSizeZ);
					Vec3 maxSpacing = totalDims.scale(0.55);
					if (Math.abs(disp.x) > maxSpacing.x || Math.abs(disp.y) > maxSpacing.y || Math.abs(disp.z) > maxSpacing.z)
						continue;
					AABB otherBounding = other.getBoundingBox();
					bounding = new AABB(Math.min(bounding.minX, otherBounding.minX),
						Math.min(bounding.minY, otherBounding.minY),
						Math.min(bounding.minZ, otherBounding.minZ),
						Math.max(bounding.maxX, otherBounding.maxX),
						Math.max(bounding.maxY, otherBounding.maxY),
						Math.max(bounding.maxZ, otherBounding.maxZ));
					this.mergeWith(other);
					other.discard();
				}
				this.setSizeX((float) bounding.getXsize());
				this.setSizeY((float) bounding.getYsize());
				this.setSizeZ((float) bounding.getZsize());
				this.setPos(bounding.getCenter());
			}
			++this.age;
			if (this.age >= this.getLifetime()) {
				this.discard();
				return;
			}
			if (this.level() instanceof ServerLevel slevel && !this.isRemoved()) {
				if (this.canChunkLoad()) {
					ChunkPos cpos = new ChunkPos(this.blockPosition());
					RitchiesProjectileLib.queueForceLoad(slevel, cpos.x, cpos.z);
				}
			}
		}
		super.tick();
	}

	protected boolean canDoStuff() { return this.firstTick || (this.level().getGameTime() + this.getId()) % 5 == 0; }

	protected int getLifetime() { return this.duration; }

	protected boolean canChunkLoad() { return CBCConfigs.SERVER.munitions.smokeCloudsCanChunkload.get(); }

	public boolean canMergeWithOther(SmokeEmitterEntity other) {
		return this != other && other.getType() == this.getType();
	}

	public void mergeWith(SmokeEmitterEntity other) {
		this.duration = Mth.ceil((this.duration + other.duration) / 2d) + 10;
		this.age = Math.max(Mth.ceil((this.age + other.age) / 2d) - 10, 0);
	}

	@Override
	protected AABB makeBoundingBox() {
		double sizeX = this.getSizeX() * 0.5;
		double sizeY = this.getSizeY() * 0.5;
		double sizeZ = this.getSizeZ() * 0.5;
		return new AABB(this.getX() + sizeX, this.getY() + sizeY, this.getZ() + sizeZ, this.getX() - sizeX, this.getY() - sizeY, this.getZ() - sizeZ);
	}

	protected ParticleOptions getParticle() {
		return new SmokeShellSmokeParticleData(4);
	}

	@Override public boolean isPickable() { return false; }

}
