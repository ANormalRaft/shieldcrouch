package com.anormalraft.shieldcrouch.mixin;

import com.anormalraft.shieldcrouch.config.CommonConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//Thanks to the Combatify mod
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ILivingEntityExtension {

    @Shadow
    protected ItemStack useItem;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(method="isBlocking", at=@At(value = "RETURN", ordinal = 0))
    public boolean remove5TickDelay(boolean original){
        if(CommonConfig.SHIELD_CROUCH.get()) {
            return this.useItem.canPerformAction(ItemAbilities.SHIELD_BLOCK);
        }
        return original;
    }
}