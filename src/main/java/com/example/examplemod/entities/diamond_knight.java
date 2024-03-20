package com.example.examplemod.entities;

import org.joml.Random;

import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.items.sssitems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class diamond_knight extends Zombie {
    private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
    
    public diamond_knight(EntityType<? extends Zombie> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
     }
  
    public diamond_knight(Level p_34274_) {
        super(EntityType.ZOMBIE, p_34274_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1000.0D)
        .add(Attributes.FOLLOW_RANGE, 64.0D)
        .add(Attributes.MOVEMENT_SPEED, (double)1.21F)
        .add(Attributes.ATTACK_DAMAGE, 0.0D)
        .add(Attributes.ARMOR, 0.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 10D)
        .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 1.0F);
     }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public void tick() {
        
        super.tick();
    }

    @Override
    protected void doUnderWaterConversion() {
        this.conversionTime = 1000;
        this.getEntityData().set(DATA_DROWNED_CONVERSION_ID, true);
    }

    private ItemStack equip_enchant(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        tag.putString("material", "minecraft:redstone");
        tag.putString("pattern", "minecraft:silence");
        CompoundTag tag2 = new CompoundTag();
        tag2.put("Trim", tag);
        tag2.putBoolean("abyss", true);
        item.setTag(tag2);
        item.setRepairCost(9999999);
        item.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 10);
        item.enchant(Enchantments.FIRE_PROTECTION, 100);
        item.enchant(Enchantments.FALL_PROTECTION, 100);
        item.enchant(Enchantments.PROJECTILE_PROTECTION, 100);
        item.enchant(Enchantments.BLAST_PROTECTION, 100);
        item.enchant(Enchantments.DEPTH_STRIDER, 100);
        item.enchant(Enchantments.UNBREAKING, 10);
        item.setHoverName(Component.literal("§9剣士の鎧"));
        
        
        return item;
    }

    @Override
    public boolean startRiding(Entity p_21396_, boolean p_21397_) {
        return false;
     }

    @Override
    public void setBaby(boolean p_34309_) {
        return;
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
    public boolean hurt(DamageSource dmgsource, float p_34289_) {
        if (dmgsource.is(DamageTypeTags.IS_PROJECTILE)) {
            if (this.getHealth() <= 300.0F) {
                if (dmgsource.getEntity() instanceof AbstractArrow arrow) {
                    arrow.deflect();
                }
                return false;
            }
        }
        Random rand = new Random();
        LivingEntity livingentity = this.getTarget();
        if (livingentity == null && dmgsource.getEntity() instanceof LivingEntity) {
            livingentity = (LivingEntity)dmgsource.getEntity();
        }
        if (rand.nextInt(5) == 0) {
            if (livingentity != null) {
                for (int i = -2; i < 3; ++i) {
                    for (int i2 = -2; i2 < 3; ++i2) {
                        EvokerFangs fang = EntityType.EVOKER_FANGS.create(this.level());
                        fang.setOwner(this);
                        fang.setPos(livingentity.getX()+i, livingentity.getY(), livingentity.getZ()+i2);
                        this.level().addFreshEntity(fang);
                    }
                }
                
            }
        }
        return super.hurt(dmgsource, p_34289_);
        
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, MobSpawnType p_34299_, SpawnGroupData p_34300_, CompoundTag p_34301_) {
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_34299_, p_34300_, p_34301_);
        float f = p_34298_.getSpecialMultiplier();

        if (p_34300_ instanceof Zombie.ZombieGroupData) {
            this.setCanBreakDoors(true);
        }

        this.handleAttributes(f);
        ItemStack head = Items.DIAMOND_BLOCK.getDefaultInstance();
        ItemStack chest = Items.DIAMOND_CHESTPLATE.getDefaultInstance();
        ItemStack legs = Items.DIAMOND_LEGGINGS.getDefaultInstance();
        ItemStack feet = Items.DIAMOND_BOOTS.getDefaultInstance();

        ItemStack weapon = Items.DIAMOND_SWORD.getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("abyss", true);
        weapon.setTag(tag);
        weapon.setRepairCost(9999999);
        weapon.enchant(Enchantments.SHARPNESS, 10);
        weapon.enchant(Enchantments.FIRE_ASPECT, 10);
        weapon.enchant(Enchantments.KNOCKBACK, 10);
        weapon.enchant(Enchantments.BLOCK_FORTUNE, 10);
        weapon.enchant(Enchantments.MOB_LOOTING, 10);
        weapon.setHoverName(Component.literal("§4§l剣士の剣"));
        Random rand = new Random();
        weapon.setDamageValue(1546+rand.nextInt(14));

        this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        this.setItemSlot(EquipmentSlot.HEAD, this.equip_enchant(head));
        this.setItemSlot(EquipmentSlot.CHEST, this.equip_enchant(chest));
        this.setItemSlot(EquipmentSlot.LEGS, this.equip_enchant(legs));
        this.setItemSlot(EquipmentSlot.FEET, this.equip_enchant(feet));
        this.setDropChance(EquipmentSlot.HEAD, 0);
        this.setDropChance(EquipmentSlot.CHEST, 0.05F);
        this.setDropChance(EquipmentSlot.LEGS, 0.05F);
        this.setDropChance(EquipmentSlot.FEET, 0.05F);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.05F);
        this.addEffect(new MobEffectInstance(MobEffects.JUMP, 2147483647, 5));
        this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 2147483647, 5));
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2147483647, 1));
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2147483647, 1));
        
        return p_34300_;
   }

    public static boolean canSpawn(EntityType<diamond_knight> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(entityType, (ServerLevelAccessor) level, spawnType, pos, random) && level.getBlockState(pos.below()).is(sssblocks.ABYSS_BLOCK.get());
    }
}