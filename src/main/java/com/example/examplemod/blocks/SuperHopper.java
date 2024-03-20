package com.example.examplemod.blocks;

import javax.annotation.Nullable;

import com.example.examplemod.blockentities.IronSpikeBlockEntity;
import com.example.examplemod.blockentities.SuperHopperEntity;
import com.example.examplemod.blockentities.sssBentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SuperHopper extends HopperBlock implements EntityBlock {
    public SuperHopper(AbstractFurnaceBlock.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(ENABLED, Boolean.valueOf(true)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_54041_) {
        Direction direction = p_54041_.getClickedFace().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction).setValue(ENABLED, Boolean.valueOf(true));
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153378_, BlockState p_153379_, BlockEntityType<T> p_153380_) {
        //return p_153380_ == sssBentities.SUPERHOPPER_ENTITY.get() ? SuperHopperEntity::pushItemsTick : null;
        return p_153378_.isClientSide ? null : createTickerHelper(p_153380_, sssBentities.SUPERHOPPER_ENTITY.get(), SuperHopperEntity::pushItemsTick);
    }

    @Override
    public InteractionResult use(BlockState p_54071_, Level p_54072_, BlockPos p_54073_, Player p_54074_, InteractionHand p_54075_, BlockHitResult p_54076_) {
      if (p_54072_.isClientSide) {
         return InteractionResult.SUCCESS;
      } else {
         BlockEntity blockentity = p_54072_.getBlockEntity(p_54073_);
         if (blockentity instanceof SuperHopperEntity) {
            p_54074_.openMenu((SuperHopperEntity)blockentity);
            p_54074_.awardStat(Stats.INSPECT_HOPPER);
         }

         return InteractionResult.CONSUME;
      }
   }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        //return new HopperBlockEntity(pos, state);
        //Blocks.HOPPER
        return sssBentities.SUPERHOPPER_ENTITY.get().create(pos, state);
    }
}