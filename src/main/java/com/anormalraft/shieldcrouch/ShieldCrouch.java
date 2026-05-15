package com.anormalraft.shieldcrouch;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ShieldCrouch.MODID)
public class ShieldCrouch {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "shieldcrouch";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    public ShieldCrouch(IEventBus modEventBus, ModContainer modContainer) {

        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    //Cancels the use of the shield on right click
    @SubscribeEvent
    public void onPlayerInteractRightClick(PlayerInteractEvent.RightClickItem event){
        if(Config.SHIELD_CROUCH.get()) {
            if (event.getItemStack().getItem() instanceof ShieldItem) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickEvent(PlayerTickEvent.Pre event){
        Player player = event.getEntity();
        //The rest is in MinecraftMixin
        if(Config.SHIELD_CROUCH.get()) {
            activateShieldOnCrouch(player);
        }
    }

    //Helper method that handles some parts of the logic of allowing shielding with crouching
    public void activateShieldOnCrouch(Player player){
        ItemStack offhandItem = player.getOffhandItem();
        ItemStack mainhandItem = player.getMainHandItem();
        if (player.isShiftKeyDown() && (offhandItem.getItem() instanceof ShieldItem || mainhandItem.getItem() instanceof ShieldItem)) {
            //Offhand has priority
            if (offhandItem.getItem() instanceof ShieldItem && !player.getCooldowns().isOnCooldown(offhandItem.getItem())) {
                if (!(player.getUseItem() == offhandItem)) {
                    offhandItem.use(player.level(), player, InteractionHand.OFF_HAND);
                }
            } else {
                if(mainhandItem.getItem() instanceof ShieldItem && !player.getCooldowns().isOnCooldown(mainhandItem.getItem())) {
                    if (!(player.getUseItem() == mainhandItem)) {
                        mainhandItem.use(player.level(), player, InteractionHand.MAIN_HAND);
                    }
                }
            }
        }
    }


}
