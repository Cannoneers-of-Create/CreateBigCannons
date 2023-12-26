package rbasamoyai.createbigcannons.crafting.builtup;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ContraptionCollider;
import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.TranslatingContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCBlocks;

import java.util.Map;

public class CannonBuilderCollider {

	public static boolean collideBlocks(AbstractContraptionEntity contraptionEntity) {
		if (!(contraptionEntity.getContraption() instanceof CannonBuildingContraption contraption)) {
			return ContraptionCollider.collideBlocks(contraptionEntity);
		}

		Level level = contraptionEntity.getCommandSenderWorld();
		Vec3 motion = contraptionEntity.getDeltaMovement();
		AABB bounds = contraptionEntity.getBoundingBox();
		Vec3 position = contraptionEntity.position();
		BlockPos gridPos = BlockPos.containing(position);

		if (contraption == null) return false;
		if (bounds == null) return false;
		if (motion.equals(Vec3.ZERO)) return false;

		Direction movementDirection = Direction.getNearest(motion.x, motion.y, motion.z);
		if (movementDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
			gridPos = gridPos.relative(movementDirection);
		}
		if (isCollidingWithWorld(level, contraption, gridPos, movementDirection)) return true;

		for (ControlledContraptionEntity otherContraptionEntity : level.getEntitiesOfClass(
			ControlledContraptionEntity.class, bounds.inflate(1), e -> !e.equals(contraptionEntity))) {

			if (!otherContraptionEntity.supportsTerrainCollision()) continue;

			Vec3 otherMotion = otherContraptionEntity.getDeltaMovement();
			TranslatingContraption otherContraption = (TranslatingContraption) otherContraptionEntity.getContraption();
			AABB otherBounds = otherContraptionEntity.getBoundingBox();
			Vec3 otherPosition = otherContraptionEntity.position();

			if (otherContraption == null) return false;
			if (otherBounds == null) return false;
			if (!bounds.move(motion).intersects(otherBounds.move(otherMotion))) continue;

			for (BlockPos colliderPos : contraption.getOrCreateColliders(level, movementDirection)) {
				colliderPos = colliderPos.offset(gridPos).subtract(BlockPos.containing(otherPosition));
				if (!otherContraption.getBlocks().containsKey(colliderPos)) {
					continue;
				}
				return true;
			}
		}

		return false;
	}

	public static boolean isCollidingWithWorld(Level level, CannonBuildingContraption contraption, BlockPos anchor, Direction movementDirection) {
		Map<BlockPos, StructureBlockInfo> blocks = contraption.getBlocks();
		for (BlockPos pos : contraption.getOrCreateColliders(level, movementDirection)) {
			BlockPos colliderPos = pos.offset(anchor);
			if (!level.isLoaded(colliderPos)) return true;

			BlockState collidedState = level.getBlockState(colliderPos);
			StructureBlockInfo blockInfo = blocks.get(pos);
			boolean emptyCollider = collidedState.getCollisionShape(level, pos).isEmpty();

			BlockEntity be = level.getBlockEntity(colliderPos);

			if (contraption.isActivated) {
				if (be instanceof LayeredBigCannonBlockEntity layered) {
					if (CBCBlocks.CANNON_BUILDER_HEAD.has(blockInfo.state())) {
						if (contraption.entity == null && blocks.containsKey(pos))
							continue;
						return true;
					}
					BlockEntity be1 = contraption.presentBlockEntities.get(pos);
					if (be1 instanceof LayeredBigCannonBlockEntity layered1) {
						if (contraption.entity == null && blocks.containsKey(pos.relative(movementDirection))) continue;
						if (layered.isCollidingWith(blockInfo, layered1, movementDirection)) return true;
						continue;
					}
				}
			}

			if (!collidedState.canBeReplaced() && !emptyCollider) {
				if (!contraption.isActivated || contraption.entity != null || !blocks.containsKey(pos.relative(movementDirection)))
					return true;
			}
		}

		return false;
	}

}
