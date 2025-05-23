package rbasamoyai.createbigcannons.munitions.big_cannon.he_shell;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class HEShellBlock extends SimpleShellBlock<HEShellProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(HEShellBlock::new);

	public HEShellBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends HEShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.HE_SHELL.get();
	}

}
