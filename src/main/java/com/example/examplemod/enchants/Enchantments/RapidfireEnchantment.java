package com.example.examplemod.enchants.Enchantments;

import com.example.examplemod.network.RapidfireToggleReq;
import com.example.examplemod.network.packet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class RapidfireEnchantment extends Enchantment {
    public static final String RAPID_FIRE_TAG_NAME = "rapidfire_off";

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
        public static void rapidfire(PlayerInteractEvent.LeftClickEmpty event) {
            var item = event.getEntity().getMainHandItem();
            if (item.isEmpty()) {
                return;
            }

            var tag = item.getOrCreateTag();
            boolean isRapidFire = tag.getBoolean(RAPID_FIRE_TAG_NAME);

            packet.sendToServer(new RapidfireToggleReq(!isRapidFire));
        }
    }
}
