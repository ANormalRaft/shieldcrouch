package com.anormalraft.shieldcrouch.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LETHAL_POISON = BUILDER
            .comment("Should vanilla poison be lethal \nDefault: false")
            .define("lethalPoison", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
