package com.example.examplemod.entities;

import java.util.Arrays;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.example.examplemod.Config;
import com.example.examplemod.ExampleMod;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;

public class noname_arrow extends AbstractArrow {

   public noname_arrow(EntityType<noname_arrow> p_36717_, Level p_36719_) {
      super(p_36717_, p_36719_, ItemStack.EMPTY);

   }

   public noname_arrow(EntityType<noname_arrow> p_36711_, double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
      super(p_36711_, p_36715_, ItemStack.EMPTY);
   }

   public noname_arrow(EntityType<noname_arrow> p_36721_, LivingEntity p_312718_, Level p_36722_, ItemStack p_36723_) {
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
   public void tick() {
      super.tick();
      boolean flag = this.isNoPhysics();
      Vec3 vec3 = this.getDeltaMovement();
      if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
         double d0 = vec3.horizontalDistance();
         this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
         this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
         this.yRotO = this.getYRot();
         this.xRotO = this.getXRot();
      }

      BlockPos blockpos = this.blockPosition();
      BlockState blockstate = this.level().getBlockState(blockpos);
      if (!blockstate.isAir() && !flag) {
         VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
         if (!voxelshape.isEmpty()) {
            Vec3 vec31 = this.position();

            for(AABB aabb : voxelshape.toAabbs()) {
               if (aabb.move(blockpos).contains(vec31)) {
                  this.inGround = true;
                  break;
               }
            }
         }
      }

      if (this.shakeTime > 0) {
         --this.shakeTime;
      }

      if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW) || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType))) {
         this.clearFire();
      }

      if (this.inGround && !flag) {
         if (this.lastState != blockstate && this.shouldFall()) {
            this.startFalling();
         } else if (!this.level().isClientSide) {
            this.tickDespawn();
         }

         ++this.inGroundTime;
      } else {
         this.inGroundTime = 0;
         Vec3 vec32 = this.position();
         Vec3 vec33 = vec32.add(vec3);
         HitResult hitresult = this.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
         if (hitresult.getType() != HitResult.Type.MISS) {
            vec33 = hitresult.getLocation();
         }

         while(!this.isRemoved()) {
            EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
            if (entityhitresult != null) {
               hitresult = entityhitresult;
            }

            if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
               Entity entity = ((EntityHitResult)hitresult).getEntity();
               Entity entity1 = this.getOwner();
               if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                  hitresult = null;
                  entityhitresult = null;
               }
            }

            if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !flag) {
               switch (net.minecraftforge.event.ForgeEventFactory.onProjectileImpactResult(this, hitresult)) {
                  case SKIP_ENTITY:
                     if (hitresult.getType() != HitResult.Type.ENTITY) { // If there is no entity, we just return default behaviour
                        this.onHit(hitresult);
                        this.hasImpulse = true;
                        break;
                     }
                     ignoredEntities.add(entityhitresult.getEntity().getId());
                     entityhitresult = null; // Don't process any further
                     break;
                  case STOP_AT_CURRENT_NO_DAMAGE:
                     this.discard();
                     entityhitresult = null; // Don't process any further
                     break;
                  case STOP_AT_CURRENT:
                     this.setPierceLevel((byte) 0);
                  case DEFAULT:
                     this.onHit(hitresult);
                     this.hasImpulse = true;
                     break;
               }
            }

            if (entityhitresult == null || this.getPierceLevel() <= 0) {
               break;
            }

            hitresult = null;
         }

         if (this.isRemoved())
            return;

         vec3 = this.getDeltaMovement();
         double d5 = vec3.x;
         double d6 = vec3.y;
         double d1 = vec3.z;
         for(int i = 0; i < 4; ++i) {
            this.level().addParticle(ParticleTypes.ENCHANTED_HIT, this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
            this.level().addParticle(ParticleTypes.ENCHANT, this.getX() + d5 * (double)i / 4.0D, this.getY() + d6 * (double)i / 4.0D, this.getZ() + d1 * (double)i / 4.0D, -d5, -d6 + 0.2D, -d1);
         }

         double d7 = this.getX() + d5;
         double d2 = this.getY() + d6;
         double d3 = this.getZ() + d1;
         double d4 = vec3.horizontalDistance();
         if (flag) {
            this.setYRot((float)(Mth.atan2(-d5, -d1) * (double)(180F / (float)Math.PI)));
         } else {
            this.setYRot((float)(Mth.atan2(d5, d1) * (double)(180F / (float)Math.PI)));
         }

         this.setXRot((float)(Mth.atan2(d6, d4) * (double)(180F / (float)Math.PI)));
         this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
         this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
         float f = 0.99F;
         float f1 = 0.05F;
         if (this.isInWater()) {
            for(int j = 0; j < 4; ++j) {
               float f2 = 0.25F;
               this.level().addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25D, d2 - d6 * 0.25D, d3 - d1 * 0.25D, d5, d6, d1);
            }

            f = this.getWaterInertia();
         }

         this.setDeltaMovement(vec3.scale((double)f));
         if (!this.isNoGravity() && !flag) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - (double)0.05F, vec34.z);
         }

         this.setPos(d7, d2, d3);
         this.checkInsideBlocks();
      }
   }

   private boolean shouldFall() {
      return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
   }

   private void startFalling() {
      this.inGround = false;
      Vec3 vec3 = this.getDeltaMovement();
      this.setDeltaMovement(vec3.multiply((double)(this.random.nextFloat() * 0.2F), (double)(this.random.nextFloat() * 0.2F), (double)(this.random.nextFloat() * 0.2F)));
      this.life = 0;
   }

   
   @Override
   protected void onHitEntity(EntityHitResult p_36757_) {
      super.onHitEntity(p_36757_);
      Entity entity = p_36757_.getEntity();
      float f = (float)this.getDeltaMovement().length();
      int i = Mth.ceil(Mth.clamp((double)f * this.baseDamage, 0.0D, (double)Integer.MAX_VALUE));
      setPierceLevel((byte) 0);
      if (this.getPierceLevel() > 0) {
         if (this.piercingIgnoreEntityIds == null) {
            this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
         }

         if (this.piercedAndKilledEntities == null) {
            this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
         }

         if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
            this.discard();
            return;
         }

         this.piercingIgnoreEntityIds.add(entity.getId());
      }

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
         BrokenPhantasm(20.0f);
         this.discard();
         //this.level().explode(this, this.getX(), this.getY(), this.getZ(), 10.0f, true, Level.ExplosionInteraction.MOB);
      }
      
   }

   @Override
   protected void onHitBlock(BlockHitResult p_36755_) {
      BrokenPhantasm(20.0f);
      this.discard();
      return;
   }

   private void BrokenPhantasm(float radius) {
      float f2 = radius;
      double x = this.getX();
      double y = this.getY();
      double z = this.getZ();
      int k1 = Mth.floor(x - (double)f2 - 1.0D);
      int l1 = Mth.floor(x + (double)f2 + 1.0D);
      int i2 = Mth.floor(y - (double)f2 - 1.0D);
      int i1 = Mth.floor(y + (double)f2 + 1.0D);
      int j2 = Mth.floor(z - (double)f2 - 1.0D);
      int j1 = Mth.floor(z + (double)f2 + 1.0D);
      List<Entity> list = this.level().getEntities(this, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
      Entity owner = this.getOwner();
      DamageSource damagesource = this.damageSources().magic();
      boolean pvp = Config.playerdamage;

      this.playSound(SoundEvents.GENERIC_EXPLODE, 8.0F, 0.9F);
      for(Entity entity : list) {
         if (owner != entity && entity.getType() != EntityType.ITEM && entity.getType() != EntityType.EXPERIENCE_ORB && entity.getType() != EntityType.ARROW) {
            if (!pvp || pvp && !(entity instanceof Player)) {
               entity.invulnerableTime = 0;
               entity.hurt(damagesource, (float) (getBaseDamage()*2));
            }
         }
      }
      spawnPhantasmParticles((double) radius, x, y, z);
      
   }

   private void spawnPhantasmParticles(double radius, double x, double y, double z) {
      for(int i = 0; i < 360; i++) {
          for(int i2 = -180; i2 < 180; i2++) {
             if (i % 40 == 0) {
                this.level().addAlwaysVisibleParticle(ParticleTypes.FIREWORK,
                x, y + 1.0D, z,
                      Math.cos(i) * (1-Math.abs(i2)/180d), i2/180d, Math.sin(i) * (1-Math.abs(i2)/180d));
                  
             }
          }
      }
      System.out.println("particle end");
  }
   


}
