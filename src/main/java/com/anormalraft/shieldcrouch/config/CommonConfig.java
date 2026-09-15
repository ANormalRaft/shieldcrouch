package com.anormalraft.shieldcrouch.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHIELD_CROUCH = BUILDER
            .comment("Should the shield be activated on crouch \nDefault: true")
            .define("shieldCrouch", true);

    public static final ModConfigSpec.BooleanValue DISABLE_RIGHT_CLICK = BUILDER
            .comment("Should the right click interaction with a shield (to activate it) be disabled \nShould not be true if shieldCrouch is false \nDefault: true")
            .define("disableRightClick", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
