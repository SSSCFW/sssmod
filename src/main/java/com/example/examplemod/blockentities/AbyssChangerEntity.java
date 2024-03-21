package com.example.examplemod.blockentities;

import java.util.List;

import org.joml.Random;

import com.example.examplemod.items.sssitems;

import net.minecraft.world.entity.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AbyssChangerEntity extends BlockEntity {
    public AbyssChangerEntity(BlockPos pos, BlockState state) {
        super(sssBentities.ABYSSCHANGER_ENTITY.get(), pos, state);
    }
    int timer = 0;
    static Random random = new Random();

    final static int RANGE = 0;
    private static void change(Level level, BlockPos pos) {
        AABB box = new AABB(pos).inflate(RANGE, RANGE, RANGE);

        List<Entity> entities = level.getEntities(null, box);
        for (Entity target : entities){
            if (target instanceof ItemEntity item) {
                if (item.getItem().getOrCreateTag().getBoolean("abyss")) {
                    item.discard();
                    if (random.nextInt(2) == 0) {
                        ItemStack matter = sssitems.ABYSS_MATTER.get().getDefaultInstance();
                        //matter.setCount(item.getItem().getCount());
                        ItemEntity entity = new ItemEntity(level, pos.getX()+0.5, pos.getY()+2, pos.getZ()+0.5, matter);
                        level.addFreshEntity(entity);
                    }
                }
                else if (item.getItem().getItem() == sssitems.ABYSS_MATTER.get() && item.getItem().getCount() >= 64) {
                    item.discard();
                    ItemStack matter = sssitems.COST_PRIZE.get().getDefaultInstance();
                    ItemEntity entity = new ItemEntity(level, pos.getX()+0.5, pos.getY()+2, pos.getZ()+0.5, matter);
                    level.addFreshEntity(entity);
                    
                }
                else if (item.getItem().getItem() == sssitems.COST_PRIZE.get() && item.getItem().getCount() >= 64) {
                    item.discard();
                    ItemStack matter = sssitems.BLACKHOLE_PRIZE.get().getDefaultInstance();
                    ItemEntity entity = new ItemEntity(level, pos.getX()+0.5, pos.getY()+2, pos.getZ()+0.5, matter);
                    level.addFreshEntity(entity);
                    
                }
            }
        }
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        AbyssChangerEntity tile = (AbyssChangerEntity) be;
        
        if (!level.isClientSide()) {
            tile.timer++;
            if (tile.timer > 20){
                tile.timer = 0;
        
                tile.change(level, pos);
            }
        }
    }



}
