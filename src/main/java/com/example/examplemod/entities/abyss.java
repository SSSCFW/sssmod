package com.example.examplemod.entities;

import org.joml.Random;
import com.example.examplemod.items.sssitems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class abyss extends Zombie {

    public abyss(EntityType<? extends Zombie> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
     }
  
    public abyss(Level p_34274_) {
        super(EntityType.ZOMBIE, p_34274_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 600.0D)
        .add(Attributes.FOLLOW_RANGE, 76.0D)
        .add(Attributes.MOVEMENT_SPEED, (double)0.5F)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .add(Attributes.ARMOR, 2.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.1D)
        .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 1.0F);
     }

    private ItemStack equip_enchant(ItemStack item) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("color", 0x8759C6);
        CompoundTag tag2 = new CompoundTag();
        tag2.put("display", tag);
        item.setTag(tag2);
        item.setDamageValue(60);
        item.setRepairCost(9999999);
        item.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 10);
        item.enchant(Enchantments.FIRE_PROTECTION, 10);
        item.enchant(Enchantments.FALL_PROTECTION, 10);
        item.enchant(Enchantments.PROJECTILE_PROTECTION, 10);
        item.enchant(Enchantments.BLAST_PROTECTION, 10);
        item.setHoverName(Component.literal("§5終焉ヲ告ゲル者"));
        
        
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
        super.hurt(dmgsource, p_34289_);
        Random rand = new Random();
        if (rand.nextInt(5) == 0) {
            LivingEntity livingentity = this.getTarget();
            if (livingentity == null && dmgsource.getEntity() instanceof LivingEntity) {
                livingentity = (LivingEntity)dmgsource.getEntity();
            }
            this.teleport(livingentity);
        }
        return true;
    }

    private boolean teleport(LivingEntity target) {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0;
            double d1;
            double d2;
            if (target != null) {
                d0 = target.getX() + (this.random.nextDouble() - 0.5D) * 10.0D;
                d1 = target.getY() + (double)(this.random.nextInt(10));
                d2 = target.getZ() + (this.random.nextDouble() - 0.5D) * 10.0D;
            }
            else {
                d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 10.0D;
                d1 = this.getY() + (double)(this.random.nextInt(10));
                d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 10.0D;
            }
            return this.teleport(d0, d1, d2);
        } else {
           return false;
        }
    }

    @SuppressWarnings("deprecation")
    private boolean teleport(double p_32544_, double p_32545_, double p_32546_) {
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(p_32544_, p_32545_, p_32546_);

      while(blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
      }

      BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
      boolean flag = blockstate.blocksMotion();
      boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
      if (flag && !flag1) {
         net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, p_32544_, p_32545_, p_32546_);
         if (event.isCanceled()) return false;
         Vec3 vec3 = this.position();
         boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
         if (flag2) {
            this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
            if (!this.isSilent()) {
               this.level().playSound((Player)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
               this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
         }

         return flag2;
      } else {
         return false;
      }
   }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, MobSpawnType p_34299_, SpawnGroupData p_34300_, CompoundTag p_34301_) {
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_34299_, p_34300_, p_34301_);
        float f = p_34298_.getSpecialMultiplier();

        if (p_34300_ instanceof Zombie.ZombieGroupData) {
            this.setCanBreakDoors(true);
        }

        this.handleAttributes(f);
        ItemStack head = sssitems.NETHER_PORTAL.get().getDefaultInstance();
        ItemStack chest = Items.LEATHER_CHESTPLATE.getDefaultInstance();
        ItemStack legs = Items.LEATHER_LEGGINGS.getDefaultInstance();
        ItemStack feet = Items.LEATHER_BOOTS.getDefaultInstance();

        ItemStack weapon = Items.DIAMOND_PICKAXE.getDefaultInstance();
        weapon.setRepairCost(9999999);
        weapon.enchant(Enchantments.SHARPNESS, 10);
        weapon.enchant(Enchantments.FIRE_ASPECT, 10);
        weapon.enchant(Enchantments.BLOCK_EFFICIENCY, 10);
        weapon.enchant(Enchantments.BLOCK_FORTUNE, 5);
        weapon.enchant(Enchantments.MOB_LOOTING, 5);
        weapon.setHoverName(Component.literal("§e§l全能ノ一"));
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
        this.addEffect(new MobEffectInstance(MobEffects.JUMP, 2147483647, 4));
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2147483647, 1));
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2147483647, 1));
        
        return p_34300_;
   }

    private void init() {
        
    }
}