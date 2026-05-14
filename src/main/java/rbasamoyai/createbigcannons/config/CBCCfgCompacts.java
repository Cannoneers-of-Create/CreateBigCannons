package rbasamoyai.createbigcannons.config;

import net.createmod.catnip.config.ConfigBase;

public class CBCCfgCompacts extends ConfigBase {

    public final ConfigGroup sableCompact = group(0, "sable", "Sable");
    public final ConfigFloat recoilingFactor = f(5, 0, "recoilingFactor", Comments.recoilingFactor);

    public CBCCfgCompacts() { super(); }

    @Override public String getName() { return  "compacts"; }

    private static class Comments {
        static String recoilingFactor = "How strong the cannon recoil";
    }
}
