package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.math.Matrix4f;

@Mixin(Matrix4f.class)
public interface Matrix4fAccessor {

	@Accessor("m00") void setM00(float f);
	@Accessor("m01") void setM01(float f);
	@Accessor("m02") void setM02(float f);
	@Accessor("m03") void setM03(float f);
	@Accessor("m10") void setM10(float f);
	@Accessor("m11") void setM11(float f);
	@Accessor("m12") void setM12(float f);
	@Accessor("m13") void setM13(float f);
	@Accessor("m20") void setM20(float f);
	@Accessor("m21") void setM21(float f);
	@Accessor("m22") void setM22(float f);
	@Accessor("m23") void setM23(float f);
	@Accessor("m30") void setM30(float f);
	@Accessor("m31") void setM31(float f);
	@Accessor("m32") void setM32(float f);
	@Accessor("m33") void setM33(float f);

}
