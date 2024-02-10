package com.example.examplemod.blocks;

import com.example.examplemod.blockentities.sssBentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IronFurnace extends Block implements EntityBlock {
    public IronFurnace(AbstractFurnaceBlock.Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FurnaceBlockEntity(pos, state);
        //return sssBentities.IRON_FURNACE_ENTITY.get().create(pos, state);
    }
}