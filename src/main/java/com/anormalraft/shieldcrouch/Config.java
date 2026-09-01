package com.anormalraft.shieldcrouch;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHIELD_CROUCH = BUILDER
            .comment("Should the shield be only activated on crouch instead of right click. Will disable right click for the shield if true \nDefault: true")
            .define("shieldCrouch", true);

    public static final ModConfigSpec.BooleanValue LETHAL_POISON = BUILDER
            .comment("Should vanilla poison be lethal \nDefault: false")
            .define("lethalPoison", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
