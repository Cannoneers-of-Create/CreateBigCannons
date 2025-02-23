package rbasamoyai.createbigcannons.compat.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.HashMap;
import java.util.Map;

public class FuzedProjectilePeripheral implements GenericPeripheral {
	@Override
	public String id() {
		return CreateBigCannons.MOD_ID+":fuzed_projectile";
	}

	@LuaFunction(mainThread = true)
	public String getName(FuzedBlockEntity ent){
		return ent.getBlockState().getBlock().getName().getString();
	}

	@LuaFunction(mainThread = true)
	public Map<? super String, Object> getFuze(FuzedBlockEntity ent) {
		if (!ent.hasFuze()) {
			return null;
		}
		Map<? super String, Object> fuzeData = new HashMap<>();
		ItemStack fuze = ent.getFuze();
		if (fuze.getItem() instanceof FuzeItem fuzeItem) {
			int time = fuze.getOrCreateTag().getInt("FuzeTimer");
			int detonationDistance = fuze.getOrCreateTag().getInt("DetonationDistance");
			fuzeData.put("name", fuze.getDisplayName().getString());
			fuzeData.put("fuzeTimer", time);
			fuzeData.put("detonationDistance", detonationDistance);
		}
		return fuzeData;
	}

	@LuaFunction(mainThread = true)
	public boolean hasTracer(FuzedBlockEntity ent){
		return !ent.getTracer().isEmpty();
	}

	@LuaFunction(mainThread = true)
	public String getTracer(FuzedBlockEntity ent){
		if (ent.getTracer().isEmpty()){
			return "";
		}
		return ent.getTracer().getDisplayName().getString();
	}
}
