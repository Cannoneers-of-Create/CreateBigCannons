package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import java.util.List;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

public class TrafficConeBlock extends ProjectileBlock<BigCannonProjectileProperties> {

	public TrafficConeBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile<?> getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		return CBCEntityTypes.TRAFFIC_CONE.create(level);
	}

	@Override
	protected VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(3, 0, 3, 13, 2, 13), box(5, 2, 5, 11, 16, 11));
		return new AllShapes.Builder(base).forDirectional();
	}

	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends BigCannonProjectileProperties>> getAssociatedEntityType() {
		return CBCEntityTypes.TRAFFIC_CONE.get();
	}

}
