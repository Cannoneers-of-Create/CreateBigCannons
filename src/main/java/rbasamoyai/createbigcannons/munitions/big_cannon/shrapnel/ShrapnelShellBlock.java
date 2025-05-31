package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class ShrapnelShellBlock extends SimpleShellBlock<ShrapnelShellProjectile> {

    public static final MapCodec<? extends DirectionalBlock> CODEC = simpleCodec(ShrapnelShellBlock::new);

	public ShrapnelShellBlock(Properties properties) {
		super(properties);
	}

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return CODEC; }

    @Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.SHRAPNEL_SHELL.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends ShrapnelShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SHRAPNEL_SHELL.get();
	}

}
