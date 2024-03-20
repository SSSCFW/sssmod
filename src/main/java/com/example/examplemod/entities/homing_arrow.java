package com.example.examplemod.entities;

import java.util.Comparator;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.joml.Matrix3d;
import org.joml.Vector3d;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;


public class homing_arrow extends AbstractArrow {

   private static final EntityDataAccessor<Integer> DW_TARGET_ID = SynchedEntityData.defineId(homing_arrow.class, EntityDataSerializers.INT);
	//Dividing by higher numbers kills accuracy
	private static final double MAX_MAGNITUDE = Math.PI / 2;
	private static final int NO_TARGET = -1;

	private int newTargetCooldown = 0;

   public homing_arrow(EntityType<homing_arrow> p_36717_, Level p_36719_) {
      super(p_36717_, p_36719_, ItemStack.EMPTY);

   }

   public homing_arrow(EntityType<homing_arrow> p_36711_, double p_36712_, double p_36713_, double p_36714_, Level p_36715_) {
      super(p_36711_, p_36715_, ItemStack.EMPTY);
   }

   public homing_arrow(EntityType<homing_arrow> p_36721_, LivingEntity p_312718_, Level p_36722_, ItemStack p_36723_) {
      super(p_36721_, p_312718_.getX(), p_312718_.getEyeY() - (double)0.1F, p_312718_.getZ(), p_36722_, p_36723_);
   }

   @Override
   protected void onHitEntity(EntityHitResult p_36757_) {
      Entity entity = p_36757_.getEntity();
      entity.invulnerableTime = 0;
      this.setPierceLevel((byte) 0);
      
      super.onHitEntity(p_36757_);
   }

   @Override
   protected ItemStack getPickupItem() {
      return ItemStack.EMPTY;
   }

   @Override
	public void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DW_TARGET_ID, NO_TARGET); // Target entity id
	}

	@Override
	public void tick() {
		if (tickCount > 3) {
			if (!level().isClientSide) {
				Entity target = getTarget();
				if (target != null && (!target.isAlive() || this.inGround)) {
					entityData.set(DW_TARGET_ID, NO_TARGET);
					target = null;
				}

				if (target == null && !this.inGround && newTargetCooldown <= 0) {
					findNewTarget();
				} else {
					newTargetCooldown--;
				}
			}

			Entity target = getTarget();
			if (target != null && !this.inGround) {
				Vec3 arrowMotion = getDeltaMovement();
				//Vec3 particlePos = position().add(arrowMotion.scale(0.25));
				//Vec3 particleSpeed = arrowMotion.scale(-0.5).add(0, 0.2, 0);
				//this.level().addParticle(ParticleTypes.FLAME, particlePos.x(), particlePos.y(), particlePos.z(), particleSpeed.x(), particleSpeed.y(), particleSpeed.z());
				//this.level().addParticle(ParticleTypes.FLAME, particlePos.x(), particlePos.y(), particlePos.z(), particleSpeed.x(), particleSpeed.y(), particleSpeed.z());

				Vec3 targetLoc = target.position().add(0, target.getBbHeight() / 2, 0);

				// Get the vector that points straight from the arrow to the target
				Vec3 lookVec = targetLoc.subtract(position());

				// Create the rotation using the axis and our angle and adjust the vector to it
				Vector3d adjustedLookVec = transform(arrowMotion, lookVec);

				// Tell mc to adjust our rotation accordingly
				shoot(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z, 1.1F, 0);
            this.setBaseDamage(2+this.getBaseDamage());
			}
		}
		super.tick();
	}

	private Vector3d transform(Vec3 arrowMotion, Vec3 lookVec) {
		Vector3d normal = new Vector3d(arrowMotion.x, arrowMotion.y, arrowMotion.z);
		// Find the cross product to determine the axis of rotation
		Vec3 axis = arrowMotion.cross(lookVec).normalize();
		if (axis == Vec3.ZERO) {
			//If the axis is so small that it zero outs, keep the motion as is
			return normal;
		}
		Vector3d look = new Vector3d(lookVec.x, lookVec.y, lookVec.z);
		// Find the angle between the direct vec and arrow vec, and then clamp it, so it arcs a bit
		double angle = Mth.clamp(normal.angle(look), -MAX_MAGNITUDE, MAX_MAGNITUDE);
		return new Matrix3d().rotation(angle, axis.x, axis.y, axis.z).transform(normal);
	}

	private void findNewTarget() {
		List<Mob> candidates = level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(12));

		if (!candidates.isEmpty()) {
			candidates.sort(Comparator.comparingDouble(homing_arrow.this::distanceToSqr));
			List<Mob> entities = candidates.stream().filter(mob -> !(mob instanceof Animal || mob instanceof Villager || mob == getOwner())).collect(Collectors.toList());
			if (!entities.isEmpty()) { 
				entityData.set(DW_TARGET_ID, entities.get(0).getId());
			}
		}

		newTargetCooldown = 5;
	}

	@Nullable
	private Entity getTarget() {
		return level().getEntity(entityData.get(DW_TARGET_ID));
	}

   


}
