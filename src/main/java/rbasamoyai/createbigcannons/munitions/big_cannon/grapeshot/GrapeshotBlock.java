package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class GrapeshotBlock extends ProjectileBlock<GrapeshotBagProjectile> {

	public GrapeshotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		return CBCEntityTypes.BAG_OF_GRAPESHOT.create(level);
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, ItemStack itemStack) {
		return CBCEntityTypes.BAG_OF_GRAPESHOT.create(level);
	}

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
        return CBCEntityTypes.BAG_OF_GRAPESHOT.create(level);
    }

    @Override
	public EntityType<? extends GrapeshotBagProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.BAG_OF_GRAPESHOT.get();
	}

}
