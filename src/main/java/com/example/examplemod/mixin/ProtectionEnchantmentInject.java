package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.example.examplemod.enchants.Enchant;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentInject {

    @Inject(method = "getFireAfterDampener", at = @At("RETURN"))
    private static int getFireAfterDampenerMixin(LivingEntity p_45139_, int p_45140_, CallbackInfoReturnable<Integer> info) {
        int ignore = EnchantmentHelper.getEnchantmentLevel(Enchant.IgnoreFire.get(), p_45139_);
        if (ignore > 0) {
            return -1000000;
        } 
        return info.getReturnValueI();
    }
}
