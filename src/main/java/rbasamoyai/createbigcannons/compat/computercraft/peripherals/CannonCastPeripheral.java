package rbasamoyai.createbigcannons.compat.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;

public class CannonCastPeripheral implements GenericPeripheral {
	@Override
	public String id() {
		return CreateBigCannons.MOD_ID+":cannon_cast";
	}

	@Override
	public PeripheralType getType() {
		return PeripheralType.ofAdditional("fluid_storage");
	}

	@LuaFunction(mainThread = true)
	public double getHeight(AbstractCannonCastBlockEntity ent) throws LuaException{
		AbstractCannonCastBlockEntity cont = ent.getControllerBE();
		if (cont == null) throw new LuaException("Cast is not assembled");
		return cont.getHeight();
	}

	@LuaFunction(mainThread = true)
	public double getFillLevel(AbstractCannonCastBlockEntity ent) throws LuaException{
		AbstractCannonCastBlockEntity cont = ent.getControllerBE();
		if (cont == null) throw new LuaException("Cast is not assembled");
		return cont.getFillState();
	}

	@LuaFunction(mainThread = true)
	public double getCastingLevel(AbstractCannonCastBlockEntity ent) throws LuaException{
		AbstractCannonCastBlockEntity cont = ent.getControllerBE();
		if (cont == null) throw new LuaException("Cast is not assembled");
		return cont.getCastingState();
	}

	@LuaFunction(mainThread = true)
	public boolean isFilled(AbstractCannonCastBlockEntity ent) throws LuaException{
		AbstractCannonCastBlockEntity cont = ent.getControllerBE();
		if (cont == null) throw new LuaException("Cast is not assembled");
		return cont.getFillState() == 1;
	}

	@LuaFunction(mainThread = true)
	public boolean hasFinishedCasting(AbstractCannonCastBlockEntity ent) throws LuaException{
		AbstractCannonCastBlockEntity cont = ent.getControllerBE();
		if (cont == null) throw new LuaException("Cast is not assembled");
		return cont.getCastingState() == 1;
	}
}
