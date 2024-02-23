package com.example.examplemod.entities;

import java.util.List;
import java.util.function.IntFunction;

import javax.annotation.Nullable;

import com.example.examplemod.items.sssitems;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class flight_boat extends Boat{
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    public static final int PADDLE_LEFT = 0;
    public static final int PADDLE_RIGHT = 1;
    private static final int TIME_TO_EJECT = 60;
    private static final float PADDLE_SPEED = ((float)Math.PI / 8F);
    public static final double PADDLE_SOUND_TIME = (double)((float)Math.PI / 4F);
    public static final int BUBBLE_TIME = 60;
    private final float[] paddlePositions = new float[2];
    private float invFriction;
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private double waterLevel;
    private float landFriction;
    private Boat.Status status;
    private Boat.Status oldStatus;
    private double lastYd;
    private boolean isAboveBubbleColumn;
    private boolean bubbleColumnDirectionIsDown;
    private float bubbleMultiplier;
    private float bubbleAngle;
    private float bubbleAngleO;
        
    public flight_boat(EntityType<? extends flight_boat> p_38290_, Level p_38291_) {
        super(p_38290_, p_38291_);
        this.blocksBuilding = true;
    }

    public flight_boat(Level p_38293_, double p_38294_, double p_38295_, double p_38296_) {
        this(entityInit.FLIGHT_BOAT.get(), p_38293_);
        this.setPos(p_38294_, p_38295_, p_38296_);
        this.xo = p_38294_;
        this.yo = p_38295_;
        this.zo = p_38296_;
    }

    @Override
    public void setInput(boolean p_38343_, boolean p_38344_, boolean p_38345_, boolean p_38346_) {
        this.inputLeft = p_38343_;
        this.inputRight = p_38344_;
        this.inputUp = p_38345_;
        this.inputDown = p_38346_;
     }



    @Override
    public Item getDropItem() {
        return sssitems.FLIGHT_BOAT.get();
    }

    @Nullable
    private Boat.Status isUnderwater() {
        AABB aabb = this.getBoundingBox();
        double d0 = aabb.maxY + 0.001D;
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.maxY);
        int l = Mth.ceil(d0);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                blockpos$mutableblockpos.set(k1, l1, i2);
                FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                if (this.canBoatInFluid(fluidstate) && d0 < (double)((float)blockpos$mutableblockpos.getY() + fluidstate.getHeight(this.level(), blockpos$mutableblockpos))) {
                    if (!fluidstate.isSource()) {
                        return Boat.Status.UNDER_FLOWING_WATER;
                    }

                    flag = true;
                }
                }
            }
        }

        return flag ? Boat.Status.UNDER_WATER : null;
    }

    
    private boolean checkInWater() {
        AABB aabb = this.getBoundingBox();
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.minY);
        int l = Mth.ceil(aabb.minY + 0.001D);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k1 = i; k1 < j; ++k1) {
        for(int l1 = k; l1 < l; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
                blockpos$mutableblockpos.set(k1, l1, i2);
                FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                if (this.canBoatInFluid(fluidstate)) {
                    float f = (float)l1 + fluidstate.getHeight(this.level(), blockpos$mutableblockpos);
                    this.waterLevel = Math.max((double)f, this.waterLevel);
                    flag |= aabb.minY < (double)f;
                }
            }
        }
        }

        return flag;
    }

    private Boat.Status getStatus() {
        Boat.Status boat$status = this.isUnderwater();
        if (boat$status != null) {
           this.waterLevel = this.getBoundingBox().maxY;
           return boat$status;
        } else if (this.checkInWater()) {
           return Boat.Status.IN_WATER;
        } else {
           float f = this.getGroundFriction();
           if (f > 0.0F) {
              this.landFriction = f;
              return Boat.Status.ON_LAND;
           } else {
              return Boat.Status.IN_AIR;
           }
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
            --this.lerpSteps;
        }
    }

    
    private void floatBoat() {
        double d0 = (double)-0.04F;
        double d1 = this.isNoGravity() ? 0.0D : (double)-0.001F;
        double d2 = 0.0D;
        this.invFriction = 0.05F;
        if (this.oldStatus == Boat.Status.IN_AIR && this.status != Boat.Status.IN_AIR && this.status != Boat.Status.ON_LAND) {
        this.waterLevel = this.getY(1.0D);
        this.setPos(this.getX(), (double)(this.getWaterLevelAbove() - this.getBbHeight()) + 0.101D, this.getZ());
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
        this.lastYd = 0.0D;
        this.status = Boat.Status.IN_WATER;
        } else {
        if (this.status == Boat.Status.IN_WATER) {
            d2 = (this.waterLevel - this.getY()) / (double)this.getBbHeight();
            this.invFriction = 0.9F;
        } else if (this.status == Boat.Status.UNDER_FLOWING_WATER) {
            d1 = 0.01D;
            this.invFriction = 0.9F;
        } else if (this.status == Boat.Status.UNDER_WATER) {
            d2 = (double)0.01F;
            this.invFriction = 0.45F;
        } else if (this.status == Boat.Status.IN_AIR) {
            this.invFriction = 0.9F;
        } else if (this.status == Boat.Status.ON_LAND) {
            this.invFriction = 0.9F;
        }

        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x * (double)this.invFriction, d1, vec3.z * (double)this.invFriction);
        this.deltaRotation *= this.invFriction;
        /*if (d2 > 0.0D) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, (vec31.y + d2 * 0.06153846016296973D) * 0.75D, vec31.z);
        }*/
        }

    }

    private int getBubbleTime() {
      return this.entityData.get(DATA_ID_BUBBLE_TIME);
     }
     
    private void setBubbleTime(int p_38367_) {
        this.entityData.set(DATA_ID_BUBBLE_TIME, p_38367_);
    }

    private void tickBubbleColumn() {
        if (this.level().isClientSide) {
           int i = this.getBubbleTime();
           if (i > 0) {
              this.bubbleMultiplier += 0.05F;
           } else {
              this.bubbleMultiplier -= 0.1F;
           }
  
           this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0F, 1.0F);
           this.bubbleAngleO = this.bubbleAngle;
           this.bubbleAngle = 10.0F * (float)Math.sin((double)(0.5F * (float)this.level().getGameTime())) * this.bubbleMultiplier;
        } else {
           if (!this.isAboveBubbleColumn) {
              this.setBubbleTime(0);
           }
  
           int k = this.getBubbleTime();
           if (k > 0) {
              --k;
              this.setBubbleTime(k);
              int j = 60 - k - 1;
              if (j > 0 && k == 0) {
                 this.setBubbleTime(0);
                 Vec3 vec3 = this.getDeltaMovement();
                 if (this.bubbleColumnDirectionIsDown) {
                    this.setDeltaMovement(vec3.add(0.0D, -0.7D, 0.0D));
                    this.ejectPassengers();
                 } else {
                    this.setDeltaMovement(vec3.x, this.hasPassenger((p_150274_) -> {
                       return p_150274_ instanceof Player;
                    }) ? 2.7D : 0.6D, vec3.z);
                 }
              }
  
              this.isAboveBubbleColumn = false;
           }
        }
  
    }

    @Override
    public void tick() {
        this.oldStatus = this.status;
        this.status = this.getStatus();
        if (this.status != Boat.Status.UNDER_WATER && this.status != Boat.Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        } else {
            ++this.outOfControlTicks;
        }

        if (!this.level().isClientSide && this.outOfControlTicks >= 60.0F) {
            this.ejectPassengers();
        }

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        super.tick();
        this.tickLerp();
        if (this.isControlledByLocalInstance()) {
            if (!(this.getFirstPassenger() instanceof Player)) {
                this.setPaddleState(false, false);
            }

            this.floatBoat();
            if (this.level().isClientSide) {
                this.controlBoat();
                this.level().sendPacketToServer(new ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.tickBubbleColumn();

        for(int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && (double)(this.paddlePositions[i] % ((float)Math.PI * 2F)) <= (double)((float)Math.PI / 4F) && (double)((this.paddlePositions[i] + ((float)Math.PI / 8F)) % ((float)Math.PI * 2F)) >= (double)((float)Math.PI / 4F)) {
                SoundEvent soundevent = this.getPaddleSound();
                if (soundevent != null) {
                    Vec3 vec3 = this.getViewVector(1.0F);
                    double d0 = i == 1 ? -vec3.z : vec3.z;
                    double d1 = i == 1 ? vec3.x : -vec3.x;
                    this.level().playSound((Player)null, this.getX() + d0, this.getY(), this.getZ() + d1, soundevent, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
                }
                }

                this.paddlePositions[i] += ((float)Math.PI / 8F);
            } else {
                this.paddlePositions[i] = 0.0F;
            }
        }

        this.checkInsideBlocks();
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate((double)0.2F, (double)-0.01F, (double)0.2F), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);

            for(Entity entity : list) {
                if (this.level().isClientSide) {
                    if (entity instanceof LocalPlayer player) {
                        this.inputJump = player.input.jumping;
                   }
                }
                if (!entity.hasPassenger(this)) {
                if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && this.hasEnoughSpaceFor(entity) && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                    entity.startRiding(this);
                    
                } else {
                    this.push(entity);
                }
                }
            }
        }

    }
    private boolean inputJump;
    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;
            double fly = 0.0D;
            if (this.inputLeft) {
                --this.deltaRotation;
            }

            if (this.inputRight) {
                ++this.deltaRotation;
            }

            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                f += 0.005F;
            }

            this.setYRot(this.getYRot() + this.deltaRotation);
            if (this.inputUp) {
                f += 0.15F;
            }

            if (this.inputDown) {
                f -= 0.15F;
            }
            if (inputJump) {
                fly += 0.2F;
                inputJump = false;
            }

            this.setDeltaMovement(this.getDeltaMovement().add((double)(Mth.sin(-this.getYRot() * ((float)Math.PI / 180F)) * f), fly, (double)(Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * f)));
            this.setPaddleState(this.inputRight && !this.inputLeft || this.inputUp, this.inputLeft && !this.inputRight || this.inputUp);
        }
    }

    public void setVariant(Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    public Type getModVariant() {
        return Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, Type.TORCH.ordinal());
        this.entityData.define(DATA_ID_PADDLE_LEFT, false);
        this.entityData.define(DATA_ID_PADDLE_RIGHT, false);
        this.entityData.define(DATA_ID_BUBBLE_TIME, 0);
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getModVariant().getSerializedName());
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(Type.byName(pCompound.getString("Type")));
        }
    }


    public static enum Type implements StringRepresentable {
        TORCH(Blocks.TORCH, "flight_boat");

        private final String name;
        private final Block planks;
        public static final StringRepresentable.EnumCodec<flight_boat.Type> CODEC = StringRepresentable.fromEnum(flight_boat.Type::values);
        private static final IntFunction<flight_boat.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        public String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        /**
         * Get a boat type by its enum ordinal
         */
        public static flight_boat.Type byId(int pId) {
            return BY_ID.apply(pId);
        }

        public static flight_boat.Type byName(String pName) {
            return CODEC.byName(pName, TORCH);
        }
    }
}
