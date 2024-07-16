package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.math.Matrix3f;

@Mixin(Matrix3f.class)
public interface Matrix3fAccessor {

	@Accessor("m00") void setM00(float f);
	@Accessor("m01") void setM01(float f);
	@Accessor("m02") void setM02(float f);
	@Accessor("m10") void setM10(float f);
	@Accessor("m11") void setM11(float f);
	@Accessor("m12") void setM12(float f);
	@Accessor("m20") void setM20(float f);
	@Accessor("m21") void setM21(float f);
	@Accessor("m22") void setM22(float f);
	@Accessor("m00") float getM00();
	@Accessor("m01") float getM01();
	@Accessor("m02") float getM02();
	@Accessor("m10") float getM10();
	@Accessor("m11") float getM11();
	@Accessor("m12") float getM12();
	@Accessor("m20") float getM20();
	@Accessor("m21") float getM21();
	@Accessor("m22") float getM22();


}
