package rbasamoyai.createbigcannons.index;

import static net.minecraft.world.level.block.Block.box;

import com.simibubi.create.AllShapes.Builder;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.world.phys.shapes.Shapes;

public class CBCShapes {

	public static final VoxelShaper
	CANNON_END = new Builder(Shapes.or(box(0, 0, 0, 16, 8, 16), box(6, 8, 6, 10, 10, 10), box(5, 10, 5, 11, 16, 11))).forDirectional(),
	SCREW_BREECH = new Builder(Shapes.or(box(0, 0, 0, 16, 8, 16), box(6, 8, 6, 10, 16, 10))).forDirectional(),
	INCOMPLETE_SCREW_BREECH = new Builder(box(0, 0, 0, 16, 8, 16)).forDirectional(),
	DROP_MORTAR_END = new Builder(Shapes.or(box(0, 0, 0, 16, 8, 16), box(6, 8, 6, 10, 10, 10), box(5, 10, 5, 11, 16, 11), box(7, -8, 7, 9, 0, 9))).forDirectional(),
	AUTOCANNON_AMMO_CONTAINER = new Builder(box(1, 0, 4, 15, 11, 12)).forHorizontalAxis();

}
