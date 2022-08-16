package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionCollider;
import com.simibubi.create.content.contraptions.components.structureMovement.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.TranslatingContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CannonDrillCollider {

	public static boolean collideBlocks(AbstractContraptionEntity contraptionEntity) {
		if (!(contraptionEntity.getContraption() instanceof CannonDrillingContraption contraption)) {
			return ContraptionCollider.collideBlocks(contraptionEntity);
		}
		
		Level level = contraptionEntity.getCommandSenderWorld();
		Vec3 motion = contraptionEntity.getDeltaMovement();
		AABB bounds = contraptionEntity.getBoundingBox();
		Vec3 position = contraptionEntity.position();
		BlockPos gridPos = new BlockPos(position);
		
		if (contraption == null) return false;
		if (bounds == null) return false;
		if (motion.equals(Vec3.ZERO)) return false;
		
		Direction movementDirection = Direction.getNearest(motion.x, motion.y, motion.z);
		if (movementDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
			gridPos = gridPos.relative(movementDirection);
		}
		if (ContraptionCollider.isCollidingWithWorld(level, contraption, gridPos, movementDirection)) return true;
		
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
			
			for (BlockPos colliderPos : contraption.getColliders(level, movementDirection)) {
				colliderPos = colliderPos.offset(gridPos).subtract(new BlockPos(otherPosition));
				if (!otherContraption.getBlocks().containsKey(colliderPos)) {
					continue;
				}
				return true;
			}
		}
		
		return false;
	}
	
}
