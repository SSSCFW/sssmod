package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.example.examplemod.enchants.Enchant;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(Player.class)
public class PlayerInject {

    @SuppressWarnings("deprecation")
    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"))
    public float getAttackStrengthScaleMixin(float p_36404_, CallbackInfoReturnable<Float> info) {
        Player t = ((Player) (Object) this);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchant.Cooltime.get(), t.getMainHandItem()) > 0) {
            return (float)1.0F;
        }
        return info.getReturnValueF();
    }
}
