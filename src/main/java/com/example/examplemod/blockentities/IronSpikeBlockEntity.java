package com.example.examplemod.blockentities;

import java.util.List;
import net.minecraft.world.entity.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class IronSpikeBlockEntity extends BlockEntity {
    int timer = 0;

    public IronSpikeBlockEntity(BlockPos pos, BlockState state) {
        super(sssBentities.IRONSPIKEBLOCK_ENTITY.get(), pos, state);
    }
    

    final static int RANGE = 15;
    private static void hurtMobs(Level level, BlockPos pos) {
        AABB box = new AABB(pos).inflate(RANGE, RANGE, RANGE);

        List<Entity> entities = level.getEntities(null, box);
        for (Entity target : entities){
            if (target.getType() == EntityType.IRON_GOLEM) {
                target.hurt(level.damageSources().magic(), 100);
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        IronSpikeBlockEntity tile = (IronSpikeBlockEntity) be;
        if (!level.isClientSide()) {
            tile.timer++;
            if (tile.timer > 60){
                tile.timer = 0;
        
                tile.hurtMobs(level, pos);
            }
        }
    }




}
