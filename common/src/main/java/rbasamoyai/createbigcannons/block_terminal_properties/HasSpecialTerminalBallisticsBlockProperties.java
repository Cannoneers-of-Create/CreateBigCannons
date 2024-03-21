package rbasamoyai.createbigcannons.block_terminal_properties;

import com.google.gson.JsonObject;

public interface HasSpecialTerminalBallisticsBlockProperties extends TerminalBallisticsBlockPropertiesProvider {

	default void loadTerminalBallisticsBlockPropertiesFromJson(String id, JsonObject obj) {}

}
