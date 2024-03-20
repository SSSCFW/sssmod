package com.example.examplemod.enchants;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.enchants.Enchantments.CooltimeEnchantment;
import com.example.examplemod.enchants.Enchantments.ExperienceEnchantment;
import com.example.examplemod.enchants.Enchantments.RapidfireEnchantment;
import com.example.examplemod.enchants.Enchantments.Riptide2Enchantment;
import com.example.examplemod.enchants.Enchantments.SuperAttackEnchantment;
import com.example.examplemod.enchants.Enchantments.SuperAttackEnchantment2;
import com.example.examplemod.enchants.Enchantments.HomingEnchantment;
import com.example.examplemod.enchants.Enchantments.IgnoreFireEnchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Enchant {
    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final EquipmentSlot[] ALL_HAND = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ExampleMod.MODID);

    public static final RegistryObject<Enchantment> Experience = ENCHANTMENTS.register("experience", () -> new ExperienceEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> Rapidfire = ENCHANTMENTS.register("rapidfire", () -> new RapidfireEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> Homing = ENCHANTMENTS.register("homing", () -> new HomingEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SuperAttack = ENCHANTMENTS.register("superattack", () -> new SuperAttackEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SuperAttack2 = ENCHANTMENTS.register("superattack2", () -> new SuperAttackEnchantment2(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> Cooltime = ENCHANTMENTS.register("cooltime", () -> new CooltimeEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> IgnoreFire = ENCHANTMENTS.register("ignorefire", () -> new IgnoreFireEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, ARMOR_SLOTS));
    public static final RegistryObject<Enchantment> Riptide2 = ENCHANTMENTS.register("riptide2", () -> new Riptide2Enchantment(Enchantment.Rarity.RARE, EnchantmentCategory.TRIDENT, ALL_HAND));
}
