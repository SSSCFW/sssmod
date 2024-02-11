package com.example.examplemod.blockentities;

import java.util.Random;

import javax.swing.text.html.parser.Entity;

import com.example.examplemod.blocks.sssblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public class MonsterBlockEntity extends BlockEntity {
    public MonsterBlockEntity(BlockPos pos, BlockState state) {
        super(sssBentities.MONSTERBLOCK_ENTITY.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        MonsterBlockEntity tile = (MonsterBlockEntity) be;
        int light = level.getLightEmission(pos.above());
        if (light >= 9) return;
        Random rand = new Random();
        if (rand.nextInt(8) != 0) return;
        AABB aabb = new AABB(pos).inflate(-7, 4, 7);
        int x = pos.getX();
        int y = pos.getY() + 1;
        int z = pos.getZ();
        // your code here
    }



}
