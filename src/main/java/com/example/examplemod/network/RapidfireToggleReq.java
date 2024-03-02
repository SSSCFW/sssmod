package com.example.examplemod.network;

import com.example.examplemod.enchants.Enchant;
import com.example.examplemod.enchants.Enchantments.RapidfireEnchantment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RapidfireToggleReq {
    private final boolean rapid;

    public RapidfireToggleReq(boolean rapid) {
        this.rapid = rapid;
    }

    public RapidfireToggleReq(FriendlyByteBuf buffer) {
        this.rapid = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.rapid);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            var sender = context.getSender();
            var item = sender.getMainHandItem();
            if (!sender.isShiftKeyDown() || item.isEmpty()) {
                return;
            }

            int level = EnchantmentHelper.getTagEnchantmentLevel(Enchant.Rapidfire.get(), item);
            if (level <= 0) {
                return;
            }

            var tag = item.getOrCreateTag();
            tag.putBoolean(RapidfireEnchantment.RAPID_FIRE_TAG_NAME, this.rapid);
            sender.sendSystemMessage(Component.literal(this.rapid ? "連射 OFF" : "連射 ON"));
        });
    }
}
