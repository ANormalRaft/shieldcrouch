package com.anormalraft.shieldcrouch.mixin;

import com.anormalraft.shieldcrouch.config.CommonConfig;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    //Keeps the crouch speed the same. In vanilla, shielding whilst crouching slows you down even more
    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
    public boolean removeSpeedDropFromShieldUse(LocalPlayer instance){
        if(CommonConfig.SHIELD_CROUCH.get()) {
            return (this.isUsingItem() && !(this.getUseItem().getItem() instanceof ShieldItem));
        }
        return instance.isUsingItem();
    }
}
