package com.example.examplemod.blocks;

import com.example.examplemod.blockentities.AbyssChangerEntity;
import com.example.examplemod.blockentities.IronSpikeBlockEntity;
import com.example.examplemod.blockentities.sssBentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbyssChanger extends Block implements EntityBlock {
    public AbyssChanger(AbstractFurnaceBlock.Properties props) {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return sssBentities.ABYSSCHANGER_ENTITY.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == sssBentities.ABYSSCHANGER_ENTITY.get() ? AbyssChangerEntity::tick : null;
    }
}