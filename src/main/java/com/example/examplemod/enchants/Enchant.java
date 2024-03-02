package com.example.examplemod.enchants;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.enchants.Enchantments.CooltimeEnchantment;
import com.example.examplemod.enchants.Enchantments.ExperienceEnchantment;
import com.example.examplemod.enchants.Enchantments.RapidfireEnchantment;
import com.example.examplemod.enchants.Enchantments.SuperAttackEnchantment;
import com.example.examplemod.enchants.Enchantments.HomingEnchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Enchant {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExampleMod.MODID);

    public static final RegistryObject<Enchantment> Experience = ENCHANTMENTS.register("experience", () -> new ExperienceEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> Rapidfire = ENCHANTMENTS.register("rapidfire", () -> new RapidfireEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> Homing = ENCHANTMENTS.register("homing", () -> new HomingEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SuperAttack = ENCHANTMENTS.register("superattack", () -> new SuperAttackEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> Cooltime = ENCHANTMENTS.register("cooltime", () -> new CooltimeEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
}
