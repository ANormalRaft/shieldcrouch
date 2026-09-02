package com.anormalraft.shieldcrouch.mixin;

import com.anormalraft.shieldcrouch.config.ServerConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//Thank you Fabric mixin docs https://wiki.fabricmc.net/tutorial:mixin_tips
@Mixin(targets = "net.minecraft.world.effect.PoisonMobEffect")
public class PoisonMobEffectMixin extends MobEffect {

    protected PoisonMobEffectMixin(MobEffectCategory category, int color) {
        super(category, color);
    }

    //Makes the 1 hp health check always true by giving it a 2 regardless of player health
    @Redirect(method = "applyEffectTick", at = @At(value="INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getHealth()F"))
    public float removeOneHpStop(LivingEntity instance){
        if(ServerConfig.LETHAL_POISON.getAsBoolean()) {
            return 2.0F;
        } else {
            return instance.getHealth();
        }
    }

}
