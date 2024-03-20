package com.example.examplemod.blocks;

import javax.annotation.Nullable;

import com.example.examplemod.blockentities.sssBentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SuperChest extends Block implements EntityBlock {
    public SuperChest(AbstractFurnaceBlock.Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        //return new FurnaceBlockEntity(pos, state);
        return sssBentities.SUPERCHEST_ENTITY.get().create(pos, state);
    }
}
