package com.anormalraft.shieldcrouch.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHIELD_CROUCH = BUILDER
            .comment("Should the shield be only activated on crouch instead of right click. Will disable right click for the shield if true \nDefault: true")
            .define("shieldCrouch", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
