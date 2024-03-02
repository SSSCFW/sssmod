package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.examplemod.enchants.Enchant;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

@Mixin(LivingEntity.class)
public class LivingEntityInject {
    @Shadow public ItemStack useItem;

    @SuppressWarnings("deprecation")
    @Inject(method = "updateUsingItem", at = @At(value = "HEAD"))
    public void updateUsingItemMixin(ItemStack item, CallbackInfo info) {
        LivingEntity t = (LivingEntity) (Object) this;
        if (!t.level().isClientSide()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchant.Rapidfire.get(), item) > 0 && !item.getTag().getBoolean("rapidfire_off")) {
                this.useItem.releaseUsing(t.level(), t, t.getUseItemRemainingTicks());
            }
        }
    }
}
