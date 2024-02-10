package com.example.examplemod.enchants;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.enchants.Enchantments.ExperienceEnchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class experience {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExampleMod.MODID);

    public static final RegistryObject<Enchantment> Experience = ENCHANTMENTS.register("experience", () -> new ExperienceEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
}
