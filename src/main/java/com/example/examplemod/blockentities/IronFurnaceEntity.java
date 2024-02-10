package com.example.examplemod.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class IronFurnaceEntity extends BlockEntity {
    public IronFurnaceEntity(BlockPos pos, BlockState state) {
        super(sssBentities.IRON_FURNACE_ENTITY.get(), pos, state);
    }
}
