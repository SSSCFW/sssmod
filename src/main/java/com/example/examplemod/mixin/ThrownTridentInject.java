package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.examplemod.enchants.Enchant;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;

@Mixin(ThrownTrident.class)
public class ThrownTridentInject {
    @SuppressWarnings("deprecation")
    @Inject(method = "onHitEntity", at = @At(value = "HEAD"))
    public void onHitEntityMixin(EntityHitResult p_36757_, CallbackInfo info) {
        Projectile t = (Projectile) (Object) this;
        if (t.getOwner() != null) {
            for (ItemStack item : t.getOwner().getHandSlots()) {
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchant.Rapidfire.get(), item) > 0) {
                    Entity entity = p_36757_.getEntity();
                    entity.invulnerableTime = 0;
                    break;
                }
            }
        }
        
    }
}
