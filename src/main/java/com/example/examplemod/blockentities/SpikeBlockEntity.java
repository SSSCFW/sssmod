package com.example.examplemod.blockentities;

import java.util.List;
import net.minecraft.world.entity.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SpikeBlockEntity extends BlockEntity {
    public SpikeBlockEntity(BlockPos pos, BlockState state) {
        super(sssBentities.SPIKEBLOCK_ENTITY.get(), pos, state);
    }
    int timer = 0;

    final static int RANGE = 0;
    private static void hurtMobs(Level level, BlockPos pos) {
        AABB box = new AABB(pos).inflate(RANGE, RANGE, RANGE);

        List<Entity> entities = level.getEntities(null, box);
        for (Entity target : entities){
            if (target instanceof LivingEntity && !(target instanceof Player) && target.getType() != EntityType.ITEM && target.getType() != EntityType.EXPERIENCE_ORB) {
                target.hurt(level.damageSources().magic(), 20);
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        SpikeBlockEntity tile = (SpikeBlockEntity) be;
        
        if (!level.isClientSide()) {
            tile.timer++;
            if (tile.timer > 20){
                tile.timer = 0;
        
                tile.hurtMobs(level, pos);
            }
        }
    }



}
