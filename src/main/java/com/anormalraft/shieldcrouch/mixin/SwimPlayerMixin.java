package com.anormalraft.shieldcrouch.mixin;

import com.anormalraft.shieldcrouch.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.StreamSupport;

@Mixin(Player.class)
public abstract class SwimPlayerMixin extends Entity implements IEntityExtension {

    public SwimPlayerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract Iterable<ItemStack> getHandSlots();

    //Allows to cancel sprint-swimming by crouching whilst holding a shield
    @Inject(method = "updateSwimming", at = @At("HEAD"), cancellable = true)
    public void enableShieldSwimInterrupt(CallbackInfo ci){
        if(Config.SHIELD_CROUCH.get()) {
            //Note: Probably the only way to downcast when mixins are involved
            if ((Entity) (Object) this instanceof Player) {
                if (this.isSwimming()) {
                    this.setSwimming(this.isSprinting() && (this.isInWater() || this.isInFluidType((fluidType, height) -> this.canSwimInFluidType(fluidType))) && !this.isPassenger() && !(StreamSupport.stream(this.getHandSlots().spliterator(), false).anyMatch(e -> e.getItem() instanceof ShieldItem) && this.isDescending()));
                } else {
                    this.setSwimming(this.isSprinting() && (this.isUnderWater() || this.canStartSwimming()) && !this.isPassenger() && !this.isDescending());
                }
                ci.cancel();
            }
        }
    }
}
