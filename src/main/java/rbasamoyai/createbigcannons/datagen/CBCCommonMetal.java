package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.foundation.data.recipe.Mods;

import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * Adapted from {@link com.simibubi.create.foundation.data.recipe.CommonMetal}.
 */
public enum CBCCommonMetal {

    CAST_IRON(false),
    BRONZE(false),
    NETHERSTEEL(false);

    public final String name;
    public final boolean isNatural;

    public final ItemLikeTag ores;
    public final TagKey<Item> rawOres;
    public final ItemLikeTag rawStorageBlocks;
    public final TagKey<Item> ingots;
    public final ItemLikeTag storageBlocks;
    public final TagKey<Item> nuggets;
    public final TagKey<Item> plates;

    CBCCommonMetal(boolean natural) {
        this.name = Lang.asId(name());

        this.isNatural = natural;

        this.ores = new ItemLikeTag("ores/" + this.name);
        this.rawOres = itemTag("raw_materials/" + this.name);
        this.rawStorageBlocks = new ItemLikeTag("storage_blocks/raw_" + this.name);
        this.ingots = itemTag("ingots/" + this.name);
        this.storageBlocks = new ItemLikeTag("storage_blocks/" + this.name);
        this.nuggets = itemTag("nuggets/" + this.name);
        this.plates = itemTag("plates/" + this.name);
    }

    public String getName(Mods mod) {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    private static TagKey<Item> itemTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Block> blockTag(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public record ItemLikeTag(TagKey<Item> items, TagKey<Block> blocks) {
        private ItemLikeTag(String path) {
            this(itemTag(path), blockTag(path));
        }
    }

}
