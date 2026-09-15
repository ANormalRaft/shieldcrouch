package com.anormalraft.shieldcrouch.mixin;

import com.anormalraft.shieldcrouch.config.CommonConfig;
import com.mojang.blaze3d.platform.WindowEventHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.neoforge.client.extensions.IMinecraftExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler, IMinecraftExtension {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow @Nullable public MultiPlayerGameMode gameMode;

    @Shadow @Final
    public Options options;

    public MinecraftMixin(String name) {
        super(name);
    }

    @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 2))
    public boolean stepIntoKeyUseCheck(KeyMapping instance){
        if(CommonConfig.SHIELD_CROUCH.get()) {
            return false;
        } else {
            return instance.isDown();
        }
    }

    //Since we forcibly step into the release step in order for the crouching behavior to work without using the Use key, we need to re-do the original release logic for non-shield items. Note that this section of the code only triggers if we have a useItem already confirmed (there will always be a useItem in need of checking whether it needs to be released or not here)
    @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;releaseUsingItem(Lnet/minecraft/world/entity/player/Player;)V"))
    public void denyUseItemCancel(MultiPlayerGameMode instance, Player player){
        if(CommonConfig.SHIELD_CROUCH.get()) {
            if(CommonConfig.DISABLE_RIGHT_CLICK.get()) {
                //This is the usual (non-shield) item check since we forced our way through with stepIntoKeyUseCheck we need to include it
                if (!(player.getUseItem().getItem() instanceof ShieldItem)) {
                    if (!this.options.keyUse.isDown()) {
                        instance.releaseUsingItem(player);
                    }
                //If a shield is the useItem
                } else {
                    //If we are crouching, do nothing (continue the behavior of using the item). Else, release the use item (deactivate shield)
                    if (!player.isShiftKeyDown()) {
                        instance.releaseUsingItem(player);
                    }
                }
            //Disable right click is off variant
            } else {
                if (!player.isShiftKeyDown()) {
                    if (!this.options.keyUse.isDown()) {
                        instance.releaseUsingItem(player);
                    }
                } else {
                    if (!(player.getUseItem().getItem() instanceof ShieldItem)) {
                        if (!this.options.keyUse.isDown()) {
                            instance.releaseUsingItem(player);
                        }
                    }
                }
            }
        //Default behavior
        } else {
            instance.releaseUsingItem(player);
        }
    }
}
