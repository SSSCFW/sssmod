package com.example.examplemod.blocks;

import com.example.examplemod.blockentities.MonsterBlockEntity;
import com.example.examplemod.blockentities.sssBentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MonsterBlock extends Block implements EntityBlock {
    public MonsterBlock(AbstractFurnaceBlock.Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return sssBentities.MONSTERBLOCK_ENTITY.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == sssBentities.MONSTERBLOCK_ENTITY.get() ? MonsterBlockEntity::tick : null;
    }
}