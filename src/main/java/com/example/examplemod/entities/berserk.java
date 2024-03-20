package com.example.examplemod.entities;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.joml.Matrix3d;
import org.joml.Random;
import org.joml.Vector3d;

import com.example.examplemod.blocks.sssblocks;
import com.example.examplemod.enchants.Enchant;
import com.example.examplemod.network.RangeAttackParticle;
import com.example.examplemod.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class berserk extends Zombie {
    private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
    private int control_arrow_cooltime = 0;
    private int summon_sword_cooltime = 0;
    private int hate_time = 0;
    private int arrow_time = 0;
    private int summon_sword_time = 0;
    private List<AbstractArrow> candidates = new ArrayList<>();
    private List<ThrownTrident> tridents = new ArrayList<>();

    public berserk(EntityType<? extends Zombie> p_34271_, Level p_34272_) {
        super(p_34271_, p_34272_);
     }
  
    public berserk(Level p_34274_) {
        super(EntityType.ZOMBIE, p_34274_);
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (p_28879_) -> {
            return p_28879_ instanceof Enemy;
        }));
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 1000.0D)
        .add(Attributes.FOLLOW_RANGE, 110.0D)
        .add(Attributes.MOVEMENT_SPEED, (double)6.5F)
        .add(Attributes.ATTACK_DAMAGE, 5.0D)
        .add(Attributes.ARMOR, 5.0D)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1D)
        .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0F)
        .add(Attributes.ATTACK_SPEED, 10.0D);
     }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public void tick() {
        if (this.getTarget() != null) {
            if (control_arrow_cooltime <= 0) {
                control_arrow_cooltime = 120;
                arrow_time = 1;
            }
            if (summon_sword_cooltime <= 0) {
                summon_sword_cooltime = 100;
                summon_sword_time = 1;
            }
        }
        if (arrow_time > 0) {
            control_arrow(this.getTarget());
            arrow_time++;
        }
        if (summon_sword_time > 0) {
            summon_sword(this.getTarget());
            summon_sword_time++;
        }
        if (control_arrow_cooltime > 0) control_arrow_cooltime--;
        if (summon_sword_cooltime > 0) summon_sword_cooltime--;
        if (hate_time > 100) {
            hate_time = 0;
            List<Mob> hate = level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(75));
            hate = hate.stream().filter(mob -> (mob instanceof Enemy && mob != this && mob.getTarget() == null)).collect(Collectors.toList());
            for (Mob mob : hate) {
                if (mob.getTarget() != null) continue;
                mob.setTarget(this);
            }
        }
        hate_time++;
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
        item.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 20);
        item.enchant(Enchantments.FIRE_PROTECTION, 20);
        item.enchant(Enchantments.FALL_PROTECTION, 20);
        item.enchant(Enchantments.PROJECTILE_PROTECTION, 20);
        item.enchant(Enchantments.BLAST_PROTECTION, 20);
        item.enchant(Enchantments.DEPTH_STRIDER, 20);
        item.enchant(Enchantments.UNBREAKING, 20);
        item.setHoverName(Component.literal("§4呪いの鎧"));
        
        
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
    public boolean doHurtTarget(Entity entity) {
        range_attack(entity);
        entity.invulnerableTime = 4;
        if (this.random.nextFloat() < 0.3F) {
            this.teleport((LivingEntity)entity);
        }
        return super.doHurtTarget(entity);
    }

    private ThrownTrident ajust_trident(Entity target, ThrownTrident trident, int i) {
        if (target != null) {
            Vec3 vec = new Vec3(target.getX()+this.random.nextInt(12)-6, target.getY()+this.random.nextInt(6)+5, target.getZ()+this.random.nextInt(12)-6);
            trident.setPos(vec);
            trident.setDeltaMovement(0, 0.0001F, 0);
            trident.shoot(0, -1, 0, 0.000001F, 0);
        }
        //Vec2 rotation = this.getRotationVector();
        //Vec2 Yrotation = new Vec2(0, rotation.y);
        //.directionFromRotation(Yrotation);
        
        return trident;
    }

    @SuppressWarnings("null")
    private void summon_sword(Entity target) {
        if (summon_sword_time == 1) {
            for (int i = 0; i < 16; i++) {
                if (this.getTarget() != null) {
                    ThrownTrident trident = EntityType.TRIDENT.create(this.level());
                    trident.setNoGravity(true);
                    //trident.setNoPhysics(true);
                    trident.setOwner(this);
                    trident = ajust_trident(target, trident, i);
                    tridents.add(trident);
                    this.level().addFreshEntity(trident);
                }
            }
        }
        else if (summon_sword_time == 20 || this.random.nextFloat() < 0.3F && summon_sword_time == 2) {
            for (ThrownTrident trident : tridents) {
                trident.setNoGravity(false);
                trident.setCritArrow(true);
                /*if (target != null) {
                    Vec3 arrowMotion = getDeltaMovement();

                    Vec3 targetLoc = target.position().add(0, target.getBbHeight() / 2, 0);

                    Vec3 lookVec = targetLoc.subtract(trident.position());
                    Vector3d adjustedLookVec = transform(arrowMotion, lookVec);
                    trident.shoot(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z, 13.1F+(float)(this.random.nextInt(10)-5), 1);
                }*/
                //trident.setNoPhysics(false);
                trident.shoot(0, -1, 0, 3.1F, 1);
            }
            tridents.clear();
            summon_sword_time = -20;
        }
    }

    private void control_arrow(Entity entity) {
        if (candidates.isEmpty()) {
            candidates = level().getEntitiesOfClass(AbstractArrow.class, this.getBoundingBox().inflate(24));
            candidates = candidates.stream().filter(mob -> !(tridents.contains(mob))).collect(Collectors.toList());

            int lenth = candidates.size();
            if (lenth > 500) lenth = 500;
            candidates = candidates.subList(0, lenth);
        }
        Random rand = new Random();
		if (!candidates.isEmpty()) {
			candidates.sort(Comparator.comparingDouble(berserk.this::distanceToSqr));
            List<Integer> list = Arrays.asList(1, 30, 60);
            if (arrow_time > 60) {
                arrow_time = -20;
                candidates.clear();
            }
            List<Mob> hate = new ArrayList<>();
            if (arrow_time == 60) {
                hate = level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(75));
                hate = hate.stream().filter(mob -> (mob instanceof Enemy && mob != this && mob.getTarget() == this && mob == this.getTarget())).collect(Collectors.toList());
            }
            if ( list.contains(arrow_time))  {
                for (AbstractArrow arrow : candidates) {
                    if (arrow == null) continue;
                    switch (arrow_time) {
                        case 1:
                            if (arrow.isNoGravity()) break;
                            arrow.setNoGravity(true);
                            arrow.setOwner(this);
                            arrow.setDeltaMovement(0, 3, 0);
                            arrow.setPos(arrow.getX(), arrow.getY()+0.5, arrow.getZ());
                            break;
                        case 30:
                            if (arrow.getOwner() != this) break;
                            arrow.setDeltaMovement(0, 0, 0);
                            if (entity != null) {
                                Vec3 arrowMotion = getDeltaMovement();

                                Vec3 targetLoc = entity.position().add(0, entity.getBbHeight() / 2, 0);

                                Vec3 lookVec = targetLoc.subtract(arrow.position());
                                Vector3d adjustedLookVec = transform(arrowMotion, lookVec);
                                arrow.shoot(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z, 0.001F, 0);
                            }
                            break;
                        case 60:
                            if (arrow.getOwner() != this) break;
                            arrow.setCritArrow(true);

                            if (entity != null && hate.isEmpty()) {
                                Vec3 arrowMotion = getDeltaMovement();

                                Vec3 targetLoc = entity.position().add(0, entity.getBbHeight() / 2, 0);

                                Vec3 lookVec = targetLoc.subtract(arrow.position());
                                Vector3d adjustedLookVec = transform(arrowMotion, lookVec);
                                arrow.shoot(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z, 13.1F+(float)(this.random.nextInt(10)-5), 1);
                            }
                            else if (!hate.isEmpty()){
                                Entity target = hate.get(rand.nextInt(hate.size()));
                                Vec3 arrowMotion = getDeltaMovement();

                                Vec3 targetLoc = target.position().add(0, target.getBbHeight() / 2, 0);

                                Vec3 lookVec = targetLoc.subtract(arrow.position());
                                Vector3d adjustedLookVec = transform(arrowMotion, lookVec);
                                arrow.shoot(adjustedLookVec.x, adjustedLookVec.y, adjustedLookVec.z, 13.1F+(float)(this.random.nextInt(10)-5), 1);
                            }
                            arrow.setNoGravity(false);
                            break;
                        default:
                            break;
                    }
                    
                }
            }
            
            
		}
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 20) {
            arrow_time = 60;
            control_arrow(this.getTarget());
        }
        super.tickDeath();
    }

    private static final double MAX_MAGNITUDE = Math.PI / 2;

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

    private void range_attack(Entity target) {
        float f2 = 3.5F;
        double x = target.getX();
        double y = target.getY();
        double z = target.getZ();
        int k1 = Mth.floor(x - (double)f2 - 1.0D);
        int l1 = Mth.floor(x + (double)f2 + 1.0D);
        int i2 = Mth.floor(y - (double)f2 - 1.0D);
        int i1 = Mth.floor(y + (double)f2 + 1.0D);
        int j2 = Mth.floor(z - (double)f2 - 1.0D);
        int j1 = Mth.floor(z + (double)f2 + 1.0D);
        List<Entity> list = this.level().getEntities(this, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        Entity owner = this;
        DamageSource damagesource = this.damageSources().mobAttack(owner instanceof LivingEntity ? (LivingEntity)owner : null);
        float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float kb = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)target).getMobType());
            kb += (float)EnchantmentHelper.getKnockbackBonus(this);
        }
        this.playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, 1.5F, 1.5F);
        for(Entity entity : list) {
            if (target != entity &&owner != entity && entity.getType() != EntityType.ITEM && entity.getType() != EntityType.EXPERIENCE_ORB && entity.getType() != EntityType.ARROW && entity.getType() != entityInit.ABYSS.get()) {
                entity.invulnerableTime = 0;
                entity.hurt(damagesource, damage);
                if (kb > 0.0F && entity instanceof LivingEntity) {
                    ((LivingEntity)entity).knockback((double)(kb * 0.5F), (double)Mth.sin(this.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(this.getYRot() * ((float)Math.PI / 180F))));
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
            }
        }
        Vec3 vec3 = new Vec3(x, y, z);
        packet.sendToAllClientsInDimention(new RangeAttackParticle(vec3), this.level().dimension());
    }

    @Override
    public boolean hurt(DamageSource dmgsource, float p_34289_) {
        Random rand = new Random();
        if (dmgsource.is(DamageTypeTags.IS_PROJECTILE)) {
            //if (rand.nextFloat() < 0.98F) {
            if (dmgsource.getEntity() instanceof AbstractArrow arrow) {
                arrow.deflect();
            }
            return false;
            //}
        }
        
        LivingEntity livingentity = this.getTarget();
        if (livingentity == null && dmgsource.getEntity() instanceof LivingEntity) {
            livingentity = (LivingEntity)dmgsource.getEntity();
        }
        if (rand.nextInt(5) == 0) {
            if (livingentity != null) {
                range_attack(livingentity);
            }
        }
        if (rand.nextInt(4) == 0) {
            this.teleport(livingentity);
        }
        return super.hurt(dmgsource, p_34289_);
        
    }

    private boolean teleport(LivingEntity target) {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0;
            double d1;
            double d2;
            if (target != null) {
                d0 = target.getX() + (this.random.nextDouble() - 0.5D) * 12.0D;
                d1 = target.getY() + (double)(this.random.nextInt(7));
                d2 = target.getZ() + (this.random.nextDouble() - 0.5D) * 12.0D;
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
        ItemStack head = Items.REDSTONE_BLOCK.getDefaultInstance();
        ItemStack chest = Items.NETHERITE_CHESTPLATE.getDefaultInstance();
        ItemStack legs = Items.NETHERITE_LEGGINGS.getDefaultInstance();
        ItemStack feet = Items.NETHERITE_BOOTS.getDefaultInstance();

        ItemStack weapon = Items.NETHERITE_SWORD.getDefaultInstance();
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("abyss", true);
        weapon.setTag(tag);
        weapon.setRepairCost(9999999);
        weapon.enchant(Enchantments.SHARPNESS, 15);
        weapon.enchant(Enchantments.KNOCKBACK, 1);
        weapon.enchant(Enchantments.BANE_OF_ARTHROPODS, 15);
        weapon.enchant(Enchant.Rapidfire.get(), 1);
        weapon.setHoverName(Component.literal("§4§l呪いの剣"));
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

    public static boolean canSpawn(EntityType<berserk> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(entityType, (ServerLevelAccessor) level, spawnType, pos, random) && level.getBlockState(pos.below()).is(sssblocks.ABYSS_BLOCK.get());
    }
}