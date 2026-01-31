package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.InertProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class TrafficConeBlock extends InertProjectileBlock {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(TrafficConeBlock::new);

	public TrafficConeBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

	@Override
	protected VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(3, 0, 3, 13, 2, 13), box(5, 2, 5, 11, 16, 11));
		return new AllShapes.Builder(base).forDirectional();
	}

	@Override
	public EntityType<? extends AbstractBigCannonProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.TRAFFIC_CONE.get();
	}

}
