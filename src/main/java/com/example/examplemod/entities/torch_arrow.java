package com.example.examplemod.entities;

import java.util.Arrays;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import javax.annotation.Nullable;

import org.joml.Random;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;


public class torch_arrow extends AbstractArrow {

   public torch_arrow(EntityType<torch_arrow> p_36717_, Level p_36719_) {
      super(p_36717_, p_36719_, ItemStack.EMPTY);

   }

   public torch_arrow(EntityType<torch_arrow> p_36711_, double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
      super(p_36711_, p_36715_, ItemStack.EMPTY);
   }

   public torch_arrow(EntityType<torch_arrow> p_36721_, LivingEntity p_312718_, Level p_36722_, ItemStack p_36723_) {
      super(p_36721_, p_312718_.getX(), p_312718_.getEyeY() - (double)0.1F, p_312718_.getZ(), p_36722_, p_36723_);
   }


   private static final double ARROW_BASE_DAMAGE = 2.0D;
   private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
   private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
   private static final int FLAG_CRIT = 1;
   private static final int FLAG_NOPHYSICS = 2;
   private static final int FLAG_CROSSBOW = 4;

   private int life;
   private double baseDamage = 2.0D;
   private int knockback;
   private SoundEvent soundEvent = this.getDefaultHitGroundSoundEvent();
   private BlockState lastState;
   @Nullable
   private IntOpenHashSet piercingIgnoreEntityIds;
   @Nullable
   private List<Entity> piercedAndKilledEntities;
   private ItemStack pickupItemStack;

   private final IntOpenHashSet ignoredEntities = new IntOpenHashSet();

   @Override
   protected void onHitEntity(EntityHitResult p_36757_) {
      super.onHitEntity(p_36757_);
      Entity entity = p_36757_.getEntity();
      float f = (float)this.getDeltaMovement().length();
      int i = Mth.ceil(Mth.clamp((double)f * this.baseDamage, 0.0D, (double)Integer.MAX_VALUE));
      setPierceLevel((byte) 0);

      if (this.isCritArrow()) {
         long j = (long)this.random.nextInt(i / 2 + 2);
         i = (int)Math.min(j + (long)i, 2147483647L);
      }

      Entity entity1 = this.getOwner();
      DamageSource damagesource;
      if (entity1 == null) {
         damagesource = this.damageSources().arrow(this, this);
      } else {
         damagesource = this.damageSources().arrow(this, entity1);
         if (entity1 instanceof LivingEntity) {
            ((LivingEntity)entity1).setLastHurtMob(entity);
         }
      }

      boolean flag = entity.getType() == EntityType.ENDERMAN;
      int k = entity.getRemainingFireTicks();
      boolean flag1 = entity.getType().is(EntityTypeTags.DEFLECTS_ARROWS);
      if (this.isOnFire() && !flag && !flag1) {
         entity.setSecondsOnFire(5);
      }
      entity.invulnerableTime = 0;

      if (entity.hurt(damagesource, (float)i)) {
         if (flag) {
            return;
         }
         
         if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            if (!this.level().isClientSide && this.getPierceLevel() <= 0) {
               livingentity.setArrowCount(livingentity.getArrowCount() + 1);
            }

            if (this.knockback > 0) {
               double d0 = Math.max(0.0D, 1.0D - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
               Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D * d0);
               if (vec3.lengthSqr() > 0.0D) {
                  livingentity.push(vec3.x, 0.1D, vec3.z);
               }
            }

            if (!this.level().isClientSide && entity1 instanceof LivingEntity) {
               EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
               EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity);
            }

            this.doPostHurtEffects(livingentity);
            if (entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer && !this.isSilent()) {
               ((ServerPlayer)entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
            }

            if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
               this.piercedAndKilledEntities.add(livingentity);
            }

            if (!this.level().isClientSide && entity1 instanceof ServerPlayer) {
               ServerPlayer serverplayer = (ServerPlayer)entity1;
               if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                  CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, this.piercedAndKilledEntities);
               } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                  CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverplayer, Arrays.asList(entity));
               }
            }
         }

         this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
         
         this.discard();
         
         
      }
       else {
         entity.setRemainingFireTicks(k);
         this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
         this.setYRot(this.getYRot() + 180.0F);
         this.yRotO += 180.0F;
         if (!this.level().isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
            if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
               this.spawnAtLocation(this.getPickupItem(), 0.1F);
            }
            
            this.discard();
         }
      }
      if (entity1 != entity) {
         TorchExplosion(20.0f);
         this.discard();
         
         //this.level().explode(this, this.getX(), this.getY(), this.getZ(), 10.0f, true, Level.ExplosionInteraction.MOB);
      }
      
   }

   @Override
   protected void onHitBlock(BlockHitResult p_36755_) {
      TorchExplosion(20.0f);
      this.discard();
      
      return;
   }

   private void TorchExplosion(float radius) {
      float f2 = radius;
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      BlockState blocktorch = Blocks.TORCH.defaultBlockState();
      this.playSound(SoundEvents.GENERIC_EXPLODE, 5.0F, 1.2F);
      FallingBlockEntity.fall(this.level(), new BlockPos((int) x,(int) y,(int) z), blocktorch);
      Random rand = new Random();
      for (int i = 0; i < 10; ++i) {
         FallingBlockEntity torch = FallingBlockEntity.fall(this.level(), new BlockPos((int) x,(int) y,(int) z), blocktorch);
         torch.moveRelative(1, new Vec3(-10+rand.nextInt(20), rand.nextInt(10), -10+rand.nextInt(20)));
         this.level().addFreshEntity(torch);
      }
      
   }

   @Override
   protected ItemStack getPickupItem() {
      return ItemStack.EMPTY;
   }

   


}
