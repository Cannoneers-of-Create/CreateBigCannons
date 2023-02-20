package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class TrafficConeBlock extends ProjectileBlock {

	public TrafficConeBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity blockEntity) {
		return CBCEntityTypes.TRAFFIC_CONE.create(level);
	}

	@Override
	protected VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(3, 0, 3, 13, 2, 13), box(5, 2, 5, 11, 16, 11));
		return new AllShapes.Builder(base).forDirectional();
	}

}
