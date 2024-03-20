package com.example.examplemod.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(EnderDragon.class)
public class EnderDragonInject2 extends Mob implements Enemy  {

    @Shadow @Final private final EnderDragonPhaseManager phaseManager;
    @Final  public final EnderDragonPart head;
    @Shadow private float sittingDamageReceived;

    protected EnderDragonInject2(EntityType<? extends EnderDragon> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
        EnderDragon t = ((EnderDragon) (Object) this);
        this.phaseManager = new EnderDragonPhaseManager(t);
        this.head = new EnderDragonPart(t, "head", 1.0F, 1.0F);
    }

    protected boolean reallyHurt(DamageSource p_31162_, float p_31163_) {
        return super.hurt(p_31162_, p_31163_);
     }

    public boolean hurt(EnderDragonPart p_31121_, DamageSource p_31122_, float p_31123_) {
        EnderDragon t = ((EnderDragon) (Object) this);
        if (this.phaseManager.getCurrentPhase().getPhase() == EnderDragonPhase.DYING) {
            return false;
        } else {
            p_31123_ = this.phaseManager.getCurrentPhase().onHurt(p_31122_, p_31123_);
            if (p_31121_ != this.head) {
                p_31123_ = p_31123_ / 4.0F + Math.min(p_31123_, 1.0F);
            }

            if (p_31123_ < 0.01F) {
                return false;
            } else {
                //if (p_31122_.getEntity() instanceof Player || p_31122_.is(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)) {
                    float f = this.getHealth();
                    this.reallyHurt(p_31122_, p_31123_);
                    if (this.isDeadOrDying() && !this.phaseManager.getCurrentPhase().isSitting()) {
                        this.setHealth(1.0F);
                        this.phaseManager.setPhase(EnderDragonPhase.DYING);
                    }

                    if (this.phaseManager.getCurrentPhase().isSitting()) {
                        this.sittingDamageReceived = this.sittingDamageReceived + f - this.getHealth();
                        if (this.sittingDamageReceived > 0.25F * this.getMaxHealth()) {
                            this.sittingDamageReceived = 0.0F;
                            this.phaseManager.setPhase(EnderDragonPhase.TAKEOFF);
                        }
                    }
                //}

                return true;
            }
        }
   }
    
}
