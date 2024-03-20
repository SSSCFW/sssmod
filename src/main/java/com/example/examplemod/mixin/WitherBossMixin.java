package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;

@Mixin(WitherBoss.class)
public class WitherBossMixin extends Monster implements PowerableMob, RangedAttackMob {
    @Shadow private int destroyBlocksTick;
    @Shadow @Final private final int[] idleHeadUpdates = new int[2];
    
    protected WitherBossMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        //TODO Auto-generated constructor stub
    }

    public boolean hurt(DamageSource p_31461_, float p_31462_) {
        WitherBoss t = (WitherBoss) (Object) this;
        if (this.isInvulnerableTo(p_31461_)) {
            return false;
        } else if (!p_31461_.is(DamageTypeTags.WITHER_IMMUNE_TO) && !(p_31461_.getEntity() instanceof WitherBoss)) {
            if (t.getInvulnerableTicks() > 0 && !p_31461_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return false;
            } else {
                if (this.isPowered()) {
                    Entity entity = p_31461_.getDirectEntity();
                    if (entity instanceof AbstractArrow) {
                        return false;
                    }
                }

                    Entity entity1 = p_31461_.getEntity();
                    /*if (entity1 != null && !(entity1 instanceof Player) && entity1 instanceof LivingEntity && ((LivingEntity)entity1).getMobType() == this.getMobType()) {
                        return false;
                    } else {*/
                        if (this.destroyBlocksTick <= 0) {
                            this.destroyBlocksTick = 20;
                        }

                        for(int i = 0; i < this.idleHeadUpdates.length; ++i) {
                            this.idleHeadUpdates[i] += 3;
                        }

                        return super.hurt(p_31461_, p_31462_);
                    //}
            }
        } else {
            return false;
        }
   }

    public void performRangedAttack(int p_31458_, LivingEntity p_31459_) {
        this.performRangedAttack(p_31458_, p_31459_.getX(), p_31459_.getY() + (double)p_31459_.getEyeHeight() * 0.5D, p_31459_.getZ(), p_31458_ == 0 && this.random.nextFloat() < 0.001F);
    }

    public boolean isPowered() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    private double getHeadX(int p_31515_) {
      if (p_31515_ <= 0) {
         return this.getX();
      } else {
         float f = (this.yBodyRot + (float)(180 * (p_31515_ - 1))) * ((float)Math.PI / 180F);
         float f1 = Mth.cos(f);
         return this.getX() + (double)f1 * 1.3D;
      }
   }

   private double getHeadY(int p_31517_) {
      return p_31517_ <= 0 ? this.getY() + 3.0D : this.getY() + 2.2D;
   }

   private double getHeadZ(int p_31519_) {
      if (p_31519_ <= 0) {
         return this.getZ();
      } else {
         float f = (this.yBodyRot + (float)(180 * (p_31519_ - 1))) * ((float)Math.PI / 180F);
         float f1 = Mth.sin(f);
         return this.getZ() + (double)f1 * 1.3D;
      }
   }

    private void performRangedAttack(int p_31449_, double p_31450_, double p_31451_, double p_31452_, boolean p_31453_) {
      if (!this.isSilent()) {
         this.level().levelEvent((Player)null, 1024, this.blockPosition(), 0);
      }

      double d0 = this.getHeadX(p_31449_);
      double d1 = this.getHeadY(p_31449_);
      double d2 = this.getHeadZ(p_31449_);
      double d3 = p_31450_ - d0;
      double d4 = p_31451_ - d1;
      double d5 = p_31452_ - d2;
      WitherSkull witherskull = new WitherSkull(this.level(), this, d3, d4, d5);
      witherskull.setOwner(this);
      if (p_31453_) {
         witherskull.setDangerous(true);
      }

      witherskull.setPosRaw(d0, d1, d2);
      this.level().addFreshEntity(witherskull);
   }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        this.performRangedAttack(0, p_33317_);
    }
    
}
