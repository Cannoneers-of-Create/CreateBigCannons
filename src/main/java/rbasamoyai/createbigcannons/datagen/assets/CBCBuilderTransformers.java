package rbasamoyai.createbigcannons.datagen.assets;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;

public class CBCBuilderTransformers {

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBarrel(String material, boolean bored) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> builtUpCannonBarrel(String material, boolean bored) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonChamber(String material, boolean bored) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> builtUpCannonChamber(String material, boolean bored) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> thickCannonChamber(String material, boolean bored) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonPart(ResourceLocation model, String side, String end) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> sizedCannon(String model, String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> sizedHollowCannon(String sizePath, String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonEnd(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block & BigCannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreech(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreechUnbored(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreechIncomplete(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block & BigCannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreech(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block & BigCannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreechUnbored(String pathAndMaterial, String typePathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreechIncomplete(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> screwLock(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonBarrel(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonBreech(String pathAndMaterial, boolean complete) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonRecoilSpring(String pathAndMaterial, boolean complete) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonBarrel(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonRecoilSpring(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonBreech(String pathAndMaterial) { throw new AssertionError(); }

	private static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonBlock(ResourceLocation tex, ResourceLocation tex1, ResourceLocation model) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> autocannonBreechExtractor(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> ramHead() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> wormHead() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonLoader() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonDrill() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonDrillBit() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBuilder() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBuilderHead() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectileLegacy(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial, boolean useStandardModel) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> fuzedProjectileItem(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> powderCharge() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block & BigCannonPropellantBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> bigCartridge() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonMount() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> yawController() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonMountExtension() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonCarriage() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> invisibleWithParticle(String path) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> slidingBreechblock(String pathAndMaterial) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> castMould(String size) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> castIronScrapLoot(int count) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> bronzeScrapLoot(int count) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> steelScrapLoot(int count) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> nethersteelScrapLoot(int count) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> shellLoot(NonNullFunction<CopyNbtFunction.Builder, CopyNbtFunction.Builder> additionalCopyData) { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> shellLoot() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> builtUpCannon() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simpleBlock(String path) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> trafficCone() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> dropMortarShell() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> dropMortarShellItem() { throw new AssertionError(); }
	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> dropMortarEnd(String pathAndMaterial) { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> safeNbt() { throw new AssertionError(); }

	@ExpectPlatform public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonAmmoContainer(boolean isCreative) { throw new AssertionError(); }

	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> tracerProjectileLoot() { return tracerProjectileLoot(t -> t); }

	@ExpectPlatform public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> tracerProjectileLoot(NonNullFunction<CopyNbtFunction.Builder, CopyNbtFunction.Builder> additionalCopyData) { throw new AssertionError(); }

}
