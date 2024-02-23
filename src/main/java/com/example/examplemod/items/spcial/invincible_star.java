package com.example.examplemod.items.spcial;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

/* 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;*/
import java.util.UUID;

import com.example.examplemod.mixin.ZombieVillagerAccessor;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class invincible_star extends Item {
    public invincible_star(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Villager || entity instanceof Wolf || entity instanceof Cat || entity instanceof Ocelot || entity instanceof Horse || entity instanceof Llama || entity instanceof Donkey) {
            /*Method method;
            try {
                method = ZombieVillager.class.getDeclaredMethod("startConverting", UUID.class, int.class);
                method.setAccessible(true);
                method.invoke(entity, player.getUUID(), 0);
                itemStack.shrink(1);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }*/
            if (entity.isInvulnerable()) {
                player.level().playSound(entity, new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()), 
                        SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.NEUTRAL, 2.0F, 1F);
                entity.setInvulnerable(false);
            }
            else {
                player.level().playSound(entity, new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()), 
                        SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2.0F, 1F);
                entity.setInvulnerable(true);
            }
        }
        return super.interactLivingEntity(itemStack, player, entity, hand);
    }

}