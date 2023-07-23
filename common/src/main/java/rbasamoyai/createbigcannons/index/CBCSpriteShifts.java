package rbasamoyai.createbigcannons.index;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.connected_textures.CBCCTSpriteShifter;

public class CBCSpriteShifts {

	public static final CTSpriteShiftEntry
		STEEL_CANNON_BARREL = cannon("cannon_barrel/steel_cannon_barrel"),
		BUILT_UP_STEEL_CANNON_BARREL = cannon("cannon_barrel/built_up_steel_cannon_barrel"),
		STEEL_CANNON_CHAMBER = cannon("cannon_chamber/steel_cannon_chamber"),
		BUILT_UP_STEEL_CANNON_CHAMBER = cannon("cannon_chamber/built_up_steel_cannon_chamber", 2),
		THICK_STEEL_CANNON_CHAMBER = cannon("cannon_chamber/thick_steel_cannon_chamber", 2),
		NETHERSTEEL_CANNON_BARREL = cannon("cannon_barrel/nethersteel_cannon_barrel"),
		BUILT_UP_NETHERSTEEL_CANNON_BARREL = cannon("cannon_barrel/built_up_nethersteel_cannon_barrel"),
		NETHERSTEEL_CANNON_CHAMBER = cannon("cannon_chamber/nethersteel_cannon_chamber"),
		BUILT_UP_NETHERSTEEL_CANNON_CHAMBER = cannon("cannon_chamber/built_up_nethersteel_cannon_chamber", 2),
		THICK_NETHERSTEEL_CANNON_CHAMBER = cannon("cannon_chamber/thick_nethersteel_cannon_chamber", 2);

	private static CTSpriteShiftEntry cannon(String name) { return cannon(name, 1); }

	private static CTSpriteShiftEntry cannon(String name, int spriteScale) {
		return CBCCTSpriteShifter.getCT(CBCCTTypes.CANNON, spriteScale, CreateBigCannons.resource("block/" + name + "_side"),
			CreateBigCannons.resource("block/" + name + "_side_connected"));
	}

}
