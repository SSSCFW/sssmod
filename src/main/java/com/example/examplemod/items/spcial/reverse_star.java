package com.example.examplemod.items.spcial;
import net.minecraft.nbt.CompoundTag;
/* 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;*/
import java.util.UUID;

import com.example.examplemod.mixin.ZombieVillagerAccessor;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class reverse_star extends Item {
    public reverse_star(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof ZombieVillager) {
            /*Method method;
            try {
                method = ZombieVillager.class.getDeclaredMethod("startConverting", UUID.class, int.class);
                method.setAccessible(true);
                method.invoke(entity, player.getUUID(), 0);
                itemStack.shrink(1);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }*/
            ((ZombieVillagerAccessor) entity).startConvertingAccessor(player.getUUID(), 0);
            itemStack.shrink(1);
        }
        return super.interactLivingEntity(itemStack, player, entity, hand);
    }

}