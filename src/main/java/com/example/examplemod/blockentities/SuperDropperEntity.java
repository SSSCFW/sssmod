package com.example.examplemod.blockentities;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SuperDropperEntity extends DropperBlockEntity {
    public SuperDropperEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }



}
