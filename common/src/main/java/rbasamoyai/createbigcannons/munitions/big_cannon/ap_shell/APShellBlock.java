package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class APShellBlock extends SimpleShellBlock<APShellProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(APShellBlock::new);

	public APShellBlock(Properties properties) {
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
	public EntityType<? extends APShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.AP_SHELL.get();
	}

}
