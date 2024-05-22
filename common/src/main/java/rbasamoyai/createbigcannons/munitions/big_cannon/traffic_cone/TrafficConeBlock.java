package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.InertProjectileBlock;

public class TrafficConeBlock extends InertProjectileBlock {

	public TrafficConeBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(3, 0, 3, 13, 2, 13), box(5, 2, 5, 11, 16, 11));
		return new AllShapes.Builder(base).forDirectional();
	}

	@Override
	public EntityType<? extends AbstractBigCannonProjectile<?>> getAssociatedEntityType() {
		return CBCEntityTypes.TRAFFIC_CONE.get();
	}

}
