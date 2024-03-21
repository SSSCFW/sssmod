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
import org.slf4j.Logger;

public class SuperDropper extends DropperBlock {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<DropperBlock> CODEC = simpleCodec(DropperBlock::new);
    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();


    public SuperDropper(BlockBehaviour.Properties props) {
        super(props);
    }

    public BlockEntity newBlockEntity(BlockPos p_153179_, BlockState p_153180_) {
      return sssBentities.SUPERDROPPER_ENTITY.get().create(p_153179_, p_153180_);
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

    @Override
    protected void dispenseFrom(ServerLevel p_52944_, BlockState p_301813_, BlockPos p_52945_) {
        DispenserBlockEntity dispenserblockentity = p_52944_.getBlockEntity(p_52945_, BlockEntityType.DROPPER).orElse((DropperBlockEntity)null);
        if (dispenserblockentity == null) {
            LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", (Object)p_52945_);
        } else {
            BlockSource blocksource = new BlockSource(p_52944_, p_52945_, p_301813_, dispenserblockentity);
            //int i = dispenserblockentity.getRandomSlot(p_52944_.random);
            for (int i = 0; i < dispenserblockentity.getContainerSize(); ++i) {
                if (i < 0) {
                    p_52944_.levelEvent(1001, p_52945_, 0);
                } else {
                    ItemStack itemstack = dispenserblockentity.getItem(i);
                if (!itemstack.isEmpty() && com.example.examplemod.hook.InventoryCodeHooks.dropperInsertHook2(p_52944_, p_52945_, dispenserblockentity, i, itemstack)) {
                    Direction direction = p_52944_.getBlockState(p_52945_).getValue(FACING);
                    Container container = HopperBlockEntity.getContainerAt(p_52944_, p_52945_.relative(direction));
                    ItemStack itemstack1;
                    if (container == null) {
                        //itemstack1 = DISPENSE_BEHAVIOUR.dispense(blocksource, itemstack);
                        itemstack1 = this.dispense(blocksource, itemstack);
                    } else {
                        itemstack1 = HopperBlockEntity.addItem(dispenserblockentity, container, itemstack.copy().split(64), direction.getOpposite());
                        if (itemstack1.isEmpty()) {
                            itemstack1 = itemstack.copy();
                            itemstack1.shrink(64);
                        } else {
                            itemstack1 = itemstack.copy();
                        }
                    }

                    dispenserblockentity.setItem(i, itemstack1);
                    }
                }
            }
        }
    }
}
