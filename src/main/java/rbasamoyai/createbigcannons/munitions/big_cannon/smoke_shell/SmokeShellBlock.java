package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class SmokeShellBlock extends SimpleShellBlock<SmokeShellProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(SmokeShellBlock::new);

	public SmokeShellBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.SMOKE_SHELL.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends SmokeShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SMOKE_SHELL.get();
	}

}
