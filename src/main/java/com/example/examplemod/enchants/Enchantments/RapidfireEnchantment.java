package com.example.examplemod.enchants.Enchantments;

import com.example.examplemod.enchants.Enchant;
import com.example.examplemod.network.SetRapidfireNBT;
import com.example.examplemod.network.packet;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

public class RapidfireEnchantment extends Enchantment {
    public RapidfireEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
		super(rarity, category, slots);
	}

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
	public boolean isTreasureOnly() {
		return true;
	}

    @Override
	public boolean isAllowedOnBooks() {
		return true;
	}

	@Override
	public boolean isTradeable() {
		return true;
	}

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ExperienceingHandler {
        @SubscribeEvent
        public static void rapidfire(PlayerInteractEvent.LeftClickEmpty event){
            Player player = event.getEntity(); 
            if (player.isShiftKeyDown()) {
                ItemStack item = player.getMainHandItem();
                @SuppressWarnings("deprecation")
                int level = EnchantmentHelper.getItemEnchantmentLevel(Enchant.Rapidfire.get(), item);
                if (level > 0) {
                    CompoundTag tag = item.getOrCreateTag();
                    Boolean rapidfire = true;
                    if (!tag.getBoolean("rapidfire_off")) {
                        tag.putBoolean("rapidfire_off", rapidfire);
                    } else {
                        rapidfire = false;
                        tag.putBoolean("rapidfire_off", rapidfire);
                    }
                    
                    item.setTag(tag);
                    packet.sendToServer(new SetRapidfireNBT(item));
                    player.sendSystemMessage(Component.literal(rapidfire ? "連射 OFF" : "連射 ON"));
                }
            }
        }
    }
}
