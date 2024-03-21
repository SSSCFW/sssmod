package com.example.examplemod.blocks;

import javax.annotation.Nullable;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;

import com.example.examplemod.blockentities.SuperDropperEntity;
import com.example.examplemod.blockentities.sssBentities;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import org.slf4j.Logger;

public class Destroyer extends DropperBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<DropperBlock> CODEC = simpleCodec(DropperBlock::new);
    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();


    public Destroyer(BlockBehaviour.Properties props) {
        super(props);
    }

    public BlockEntity newBlockEntity(BlockPos p_153179_, BlockState p_153180_) {
      return sssBentities.DESTROYER_ENTITY.get().create(p_153179_, p_153180_);
    }

    private final ItemStack dispense(BlockSource p_123391_, ItemStack p_123392_) {
        ItemStack itemstack = this.execute(p_123391_, p_123392_);
        //this.playSound(p_123391_);
        //this.playAnimation(p_123391_, p_123391_.state().getValue(DispenserBlock.FACING));
        return itemstack;
    }

    private ItemStack execute(BlockSource p_301824_, ItemStack p_123386_) {
        Direction direction = p_301824_.state().getValue(DispenserBlock.FACING);
        Position position = DispenserBlock.getDispensePosition(p_301824_);
        ItemStack itemstack = p_123386_.split(64);
        DefaultDispenseItemBehavior.spawnItem(p_301824_.level(), itemstack, 6, direction, position);
        return p_123386_;
    }

    private boolean isFull(ItemStack item, DispenserBlockEntity destroyer) {
        for (int i = 0; i < destroyer.getContainerSize(); ++i) {
            if (destroyer.getItem(i).isEmpty()) {
                return false;
            }
            ItemStack item2 = destroyer.getItem(i);
            if (ItemHandlerHelper.canItemStacksStack(item2, item) && item2.getCount() < item2.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void dispenseFrom(ServerLevel serverLevel, BlockState blockState, BlockPos blockPos) {
        DispenserBlockEntity dispenserblockentity = serverLevel.getBlockEntity(blockPos, BlockEntityType.DROPPER).orElse((DropperBlockEntity)null);
        if (dispenserblockentity == null) {
            LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", (Object)blockPos);
        } else {
            Direction direction = serverLevel.getBlockState(blockPos).getValue(FACING);
            BlockPos block = blockPos.relative(direction);
            BlockState front_blockState = serverLevel.getBlockState(block);
            Block block1 = front_blockState.getBlock();
            ItemStack blockItemStack = block1.asItem().getDefaultInstance();
            Boolean full = isFull(blockItemStack, dispenserblockentity);
            Boolean ok = serverLevel.destroyBlock(block, full);
            if (ok && !full) {
                HopperBlockEntity.addItem(null, dispenserblockentity, blockItemStack, direction.getOpposite());
                //if (itemstack1.isEmpty()) {
                //    return;
                //}
            }
        }
    }
}
