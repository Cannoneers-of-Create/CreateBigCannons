package rbasamoyai.createbigcannons.utils;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

/**
 * Ease porting to versions past 1.19.3
 */
public class CBCRegistryUtils {

	//////// Block registry access aliases ////////
	public static Registry<Block> getBlockRegistry() { return Registry.BLOCK; }
	public static ResourceKey<Registry<Block>> getBlockRegistryKey() { return Registry.BLOCK_REGISTRY; }
	public static Stream<Block> streamAllBlocks() { return getBlockRegistry().stream(); }
	public static ResourceLocation getBlockLocation(Block block) { return getBlockRegistry().getKey(block); }
	public static int getBlockNumericId(Block block) { return getBlockRegistry().getId(block); }
	public static Block getBlock(ResourceLocation id) { return getBlockRegistry().get(id); }
	public static Block getBlock(int id) { return getBlockRegistry().byId(id); }
	public static Optional<Block> getOptionalBlock(ResourceLocation id) { return getBlockRegistry().getOptional(id); }
	public static TagKey<Block> createBlockTag(ResourceLocation id) { return TagKey.create(getBlockRegistryKey(), id); }
	public static Iterable<Holder<Block>> getBlockTagEntries(TagKey<Block> tag) { return getBlockRegistry().getTagOrEmpty(tag); }

	//////// Item registry access aliases ////////
	public static Registry<Item> getItemRegistry() { return Registry.ITEM; }
	public static ResourceKey<Registry<Item>> getItemRegistryKey() { return Registry.ITEM_REGISTRY; }
	public static Stream<Item> streamAllItems() { return getItemRegistry().stream(); }
	public static ResourceLocation getItemLocation(Item item) { return getItemRegistry().getKey(item); }
	public static int getItemNumericId(Item item) { return getItemRegistry().getId(item); }
	public static Item getItem(ResourceLocation id) { return getItemRegistry().get(id); }
	public static Item getItem(int id) { return getItemRegistry().byId(id); }
	public static Optional<Item> getOptionalItem(ResourceLocation id) { return getItemRegistry().getOptional(id); }
	public static TagKey<Item> createItemTag(ResourceLocation id) { return TagKey.create(getItemRegistryKey(), id); }
	public static Iterable<Holder<Item>> getItemTagEntries(TagKey<Item> tag) { return getItemRegistry().getTagOrEmpty(tag); }

	//////// Fluid registry access aliases ////////
	public static Registry<Fluid> getFluidRegistry() { return Registry.FLUID; }
	public static ResourceKey<Registry<Fluid>> getFluidRegistryKey() { return Registry.FLUID_REGISTRY; }
	public static Stream<Fluid> streamAllFluids() { return getFluidRegistry().stream(); }
	public static ResourceLocation getFluidLocation(Fluid fluid) { return getFluidRegistry().getKey(fluid); }
	public static int getFluidNumericId(Fluid fluid) { return getFluidRegistry().getId(fluid); }
	public static Fluid getFluid(ResourceLocation id) { return getFluidRegistry().get(id); }
	public static Fluid getFluid(int id) { return getFluidRegistry().byId(id); }
	public static Optional<Fluid> getOptionalFluid(ResourceLocation id) { return getFluidRegistry().getOptional(id); }
	public static TagKey<Fluid> createFluidTag(ResourceLocation id) { return TagKey.create(getFluidRegistryKey(), id); }
	public static Iterable<Holder<Fluid>> getFluidTagEntries(TagKey<Fluid> tag) { return getFluidRegistry().getTagOrEmpty(tag); }

	//////// Block entity type registry access aliases ////////
	public static Registry<BlockEntityType<?>> getBlockEntityTypeRegistry() { return Registry.BLOCK_ENTITY_TYPE; }
	public static ResourceKey<Registry<BlockEntityType<?>>> getBlockEntityTypeRegistryKey() { return Registry.BLOCK_ENTITY_TYPE_REGISTRY; }
	public static Stream<BlockEntityType<?>> streamAllBlockEntityTypes() { return getBlockEntityTypeRegistry().stream(); }
	public static ResourceLocation getBlockEntityTypeLocation(BlockEntityType<?> beType) { return getBlockEntityTypeRegistry().getKey(beType); }
	public static int getBlockEntityTypeNumericId(BlockEntityType<?> beType) { return getBlockEntityTypeRegistry().getId(beType); }
	public static BlockEntityType<?> getBlockEntityType(ResourceLocation id) { return getBlockEntityTypeRegistry().get(id); }
	public static BlockEntityType<?> getBlockEntityType(int id) { return getBlockEntityTypeRegistry().byId(id); }
	public static Optional<BlockEntityType<?>> getOptionalBlockEntityType(ResourceLocation id) { return getBlockEntityTypeRegistry().getOptional(id); }

	//////// Entity type registry access aliases ////////
	public static Registry<EntityType<?>> getEntityTypeRegistry() { return Registry.ENTITY_TYPE; }
	public static ResourceKey<Registry<EntityType<?>>> getEntityTypeRegistryKey() { return Registry.ENTITY_TYPE_REGISTRY; }
	public static Stream<EntityType<?>> streamAllEntityTypes() { return getEntityTypeRegistry().stream(); }
	public static ResourceLocation getEntityTypeLocation(EntityType<?> entityType) { return getEntityTypeRegistry().getKey(entityType); }
	public static int getEntityTypeNumericId(EntityType<?> entityType) { return getEntityTypeRegistry().getId(entityType); }
	public static EntityType<?> getEntityType(ResourceLocation id) { return getEntityTypeRegistry().get(id); }
	public static EntityType<?> getEntityType(int id) { return getEntityTypeRegistry().byId(id); }
	public static Optional<EntityType<?>> getOptionalEntityType(ResourceLocation id) { return getEntityTypeRegistry().getOptional(id); }
	public static TagKey<EntityType<?>> createEntityTypeTag(ResourceLocation id) { return TagKey.create(getEntityTypeRegistryKey(), id); }
	public static Iterable<Holder<EntityType<?>>> getEntityTypeTagEntries(TagKey<EntityType<?>> tag) { return getEntityTypeRegistry().getTagOrEmpty(tag); }

	//////// Less-used registry access aliases ////////
	public static ResourceKey<Registry<Level>> getDimensionRegistryKey() { return Registry.DIMENSION_REGISTRY; }

	private CBCRegistryUtils() {}

}
