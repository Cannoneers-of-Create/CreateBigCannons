package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;

@Mixin(TagsProvider.TagAppender.class)
public interface TagAppenderAccessor {

	@Accessor("builder")
	TagBuilder getBuilder();

}
