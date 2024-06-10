package rbasamoyai.createbigcannons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.base.CBCUtils;
import rbasamoyai.createbigcannons.base.PartialBlockDamageManager;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CBCCannonContraptionTypes;
import rbasamoyai.createbigcannons.index.CBCArmInteractionPointTypes;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCChecks;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;
import rbasamoyai.createbigcannons.remix.CustomExplosion;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class CreateBigCannons {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
	public static final PartialBlockDamageManager BLOCK_DAMAGE = new PartialBlockDamageManager();

	public static void init() {
		CBCSoundEvents.prepare();

		CBCMunitionPropertiesHandlers.init();
		ModGroup.register();
		CBCBlocks.register();
		CBCItems.register();
		CBCBlockEntities.register();
		CBCEntityTypes.register();
		CBCMenuTypes.register();
		CBCFluids.register();
		CBCRecipeTypes.register();
		CBCCannonContraptionTypes.register();

		CBCContraptionTypes.prepare();
		CBCArmInteractionPointTypes.register();
		CBCChecks.register();
		CBCTags.register();

		CBCRootNetwork.init();
	}

	static {
		REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
			.andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}

	public static ResourceLocation resource(String path) {
		return CBCUtils.location(MOD_ID, path);
	}

	public static final ResourceLocation SCREEN_SHAKE_HANDLER_ID = resource("shake_handler");

	public static void shakePlayerScreen(ServerPlayer player, ScreenShakeEffect effect) {
		RitchiesProjectileLib.shakePlayerScreen(player, SCREEN_SHAKE_HANDLER_ID, effect);
	}

	public static <T extends Explosion & CustomExplosion> void handleCustomExplosion(Level level, T explosion) {
		if (IndexPlatform.onExplosionStart(level, explosion))
			return;
		explosion.explode();
		explosion.finalizeExplosion(level.isClientSide);
		if (!(level instanceof ServerLevel slevel))
			return;
		if (explosion.getBlockInteraction() == Explosion.BlockInteraction.NONE)
			explosion.clearToBlow();
		for (ServerPlayer player : slevel.players())
			explosion.sendExplosionToClient(player);
	}

}
