package rbasamoyai.createbigcannons.crafting.boring;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractIncompleteAutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class UnboredAutocannonBlock extends AbstractIncompleteAutocannonBlock {

	private final VoxelShaper shapes;
	private final Supplier<CannonCastShape> cannonShape;
    private final MapCodec<? extends DirectionalBlock> codec;

	public UnboredAutocannonBlock(Properties properties, AutocannonMaterial material, VoxelShape shape, Supplier<CannonCastShape> castShape) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(shape).forDirectional();
		this.cannonShape = castShape;
        this.codec = simpleCodec(this::fromSelf);
	}

	public static UnboredAutocannonBlock barrel(Properties properties, AutocannonMaterial material) {
		return new UnboredAutocannonBlock(properties, material, box(6, 0, 6, 10, 16, 10), () -> CannonCastShape.AUTOCANNON_BARREL);
	}

	public static UnboredAutocannonBlock recoilSpring(Properties properties, AutocannonMaterial material) {
		return new UnboredAutocannonBlock(properties, material, box(5, 0, 5, 11, 16, 11), () -> CannonCastShape.AUTOCANNON_RECOIL_SPRING);
	}

	public static UnboredAutocannonBlock breech(Properties properties, AutocannonMaterial material) {
		return new UnboredAutocannonBlock(properties, material, box(4, 0, 4, 12, 16, 12), () -> CannonCastShape.AUTOCANNON_BREECH);
	}

    private UnboredAutocannonBlock fromSelf(Properties properties) {
        return new UnboredAutocannonBlock(properties, this.getAutocannonMaterial(), this.shapes.get(Direction.UP), this.cannonShape);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.get(this.getFacing(state));
	}

	@Override
	public CannonCastShape getCannonShape() {
		return this.cannonShape.get();
	}

}
