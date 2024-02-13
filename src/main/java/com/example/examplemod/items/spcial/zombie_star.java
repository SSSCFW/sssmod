package com.example.examplemod.items.spcial;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class zombie_star extends Item {
    public zombie_star(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Villager villager) {
            ZombieVillager zombievillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
            if (player.level() instanceof ServerLevel) {
                ServerLevel p_219160_ = (ServerLevel) player.level();
                if (zombievillager != null) {
                    zombievillager.finalizeSpawn(p_219160_, p_219160_.getCurrentDifficultyAt(zombievillager.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), (CompoundTag)null);
                    zombievillager.setVillagerData(villager.getVillagerData());
                    zombievillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE));
                    zombievillager.setTradeOffers(villager.getOffers().createTag());
                    zombievillager.setVillagerXp(villager.getVillagerXp());
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert(villager, zombievillager);
                    itemStack.shrink(1);
                }
            }
        }
        return super.interactLivingEntity(itemStack, player, entity, hand);
    }

}