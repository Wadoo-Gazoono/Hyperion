package com.Wadoo.hyperion.server.util.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HyperionConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> BASALT_CAPSLING_WEIGHT;
    public static final ForgeConfigSpec.ConfigValue<Integer> BASALT_CAPSLING_MIN;
    public static final ForgeConfigSpec.ConfigValue<Integer> BASALT_CAPSLING_MAX;



    static {
        BUILDER.push("Hyperion's Config");
        BUILDER.comment("\nBasalt Capsling");
        BASALT_CAPSLING_WEIGHT = BUILDER.comment("Basalt Capsling Spawn Weight. The Default Value is 80").define("Basalt Caplsing Spawn Weight", 20);
        BASALT_CAPSLING_MIN = BUILDER.comment("The smallest group size Basalt Capslings can spawn in. The Default Value is 2").define("Basalt Caplsing Spawn Min", 2);
        BASALT_CAPSLING_MAX = BUILDER.comment("The largest group size Basalt Capslings can spawn in. The Default Value is 5").define("Basalt Capsling Spawn Max", 5);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
