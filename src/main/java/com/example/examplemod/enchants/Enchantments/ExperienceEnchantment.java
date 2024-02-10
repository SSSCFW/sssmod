package com.example.examplemod.enchants.Enchantments;

import com.example.examplemod.enchants.experience;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.Level;

public class ExperienceEnchantment extends Enchantment {
    public ExperienceEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
		super(rarity, category, slots);
	}

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
	public boolean isTreasureOnly() {
		return false;
	}

    @Override
	public boolean isAllowedOnBooks() {
		return true;
	}

	@Override
	public boolean isTradeable() {
		return true;
	}

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ExperienceingHandler {
        @SubscribeEvent
        public static void experience(LivingDropsEvent event){
            if (event.getSource().getEntity() instanceof Player player) {
                int level = EnchantmentHelper.getEnchantmentLevel(experience.Experience.get(), player);
                Entity entity = event.getEntity();
                ExperienceOrb exp = new ExperienceOrb(entity.level(), entity.getX(), entity.getY(), entity.getZ(), level * 5);
                entity.level().addFreshEntity(exp);
            }

        }
    }
}
