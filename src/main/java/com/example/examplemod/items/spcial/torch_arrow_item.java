package com.example.examplemod.items.spcial;

import com.example.examplemod.entities.entityInit;
import com.example.examplemod.entities.torch_arrow;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class torch_arrow_item extends ArrowItem {
    public torch_arrow_item(Properties props) {
        super(props);
    }

    public AbstractArrow createArrow(Level level, ItemStack ammoStack, LivingEntity shooter) {
        torch_arrow arrow = new torch_arrow(entityInit.TORCH_ARROW.get(), shooter, level, ammoStack);
        arrow.setOwner(shooter);
        return arrow;
    }

}