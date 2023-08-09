package rbasamoyai.createbigcannons.index;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;

public class CBCDataSerializers {
	public static final EntityDataSerializer<EndFluidStack> FLUID_STACK_SERIALIZER = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buf, EndFluidStack fluid) {
			fluid.writeBuf(buf);
		}

		@Override
		public EndFluidStack read(FriendlyByteBuf buf) {
			return EndFluidStack.readBuf(buf);
		}

		@Override
		public EndFluidStack copy(EndFluidStack fluid) {
			return fluid.copy();
		}
	};

	public static void registerSerializers() {
		EntityDataSerializers.registerSerializer(FLUID_STACK_SERIALIZER);
	}
}
