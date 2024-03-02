package com.example.examplemod.entities;

import org.joml.Random;

import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.enchants.Enchant;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

public class rapid_skeleton extends Skeleton {

    public rapid_skeleton(EntityType<? extends Skeleton> p_33570_, Level p_33571_) {
        super(p_33570_, p_33571_);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void doFreezeConversion() {
        return;
     }

    @Override
    public void tick() {
        if (this.isUsingItem()) {
            LivingEntity livingentity = this.getTarget();
            if(livingentity != null) {
                super.performRangedAttack(livingentity, 1.0F);
            }
        }
        super.tick();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 200.0D)
        .add(Attributes.FOLLOW_RANGE, 100.0D)
        .add(Attributes.MOVEMENT_SPEED, (double)0.36F)
        .add(Attributes.ATTACK_DAMAGE, 10.0D)
        .add(Attributes.ARMOR, 2.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 5D);
    }

    private ItemStack equip_enchant(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("color", 0xa9a9a9);
        
        CompoundTag tag2 = new CompoundTag();
        tag2.put("display", tag);
        tag2.putBoolean("abyss", true);
        item.setTag(tag2);
        item.setDamageValue(60);
        item.setRepairCost(9999999);
        item.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 5);
        item.enchant(Enchantments.FIRE_PROTECTION, 5);
        item.enchant(Enchantments.FALL_PROTECTION, 10);
        item.enchant(Enchantments.PROJECTILE_PROTECTION, 15);
        item.enchant(Enchantments.BLAST_PROTECTION, 15);
        item.setHoverName(Component.literal("§e超合金アーマー"));
        
        
        return item;
    }

    @Override
    public boolean startRiding(Entity p_21396_, boolean p_21397_) {
        return false;
    }

    @Override
    public boolean canHoldItem(ItemStack p_34332_) {
        return false;
    }
  
    @Override
    public boolean wantsToPickUp(ItemStack p_182400_) {
        return false;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, MobSpawnType p_34299_, SpawnGroupData p_34300_, CompoundTag p_34301_) {
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_34299_, p_34300_, p_34301_);
        float f = p_34298_.getSpecialMultiplier();

        ItemStack head = Items.NETHERITE_BLOCK.getDefaultInstance();
        ItemStack chest = Items.LEATHER_CHESTPLATE.getDefaultInstance();
        ItemStack legs = Items.LEATHER_LEGGINGS.getDefaultInstance();
        ItemStack feet = Items.LEATHER_BOOTS.getDefaultInstance();

        ItemStack weapon = Items.BOW.getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("abyss", true);
        weapon.setTag(tag);
        weapon.setRepairCost(9999999);
        weapon.enchant(Enchant.Rapidfire.get(), 1);
        weapon.setHoverName(Component.literal("§e§lミニボウ"));
        //Random rand = new Random();

        this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        this.setItemSlot(EquipmentSlot.HEAD, this.equip_enchant(head));
        this.setItemSlot(EquipmentSlot.CHEST, this.equip_enchant(chest));
        this.setItemSlot(EquipmentSlot.LEGS, this.equip_enchant(legs));
        this.setItemSlot(EquipmentSlot.FEET, this.equip_enchant(feet));
        this.setDropChance(EquipmentSlot.HEAD, 0);
        this.setDropChance(EquipmentSlot.CHEST, 0.05F);
        this.setDropChance(EquipmentSlot.LEGS, 0.05F);
        this.setDropChance(EquipmentSlot.FEET, 0.05F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        this.addEffect(new MobEffectInstance(MobEffects.JUMP, 2147483647, 5));
        this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 2147483647, 5));
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2147483647, 1));
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2147483647, 1));
        
        return p_34300_;
   }

   public static boolean canSpawn(EntityType<rapid_skeleton> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(entityType, (ServerLevelAccessor) level, spawnType, pos, random) && level.getBlockState(pos.below()).is(sssblocks.ABYSS_BLOCK.get());
    }

}
