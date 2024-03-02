package com.example.examplemod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SetRapidfireNBT {
    private final ItemStack item;

    public SetRapidfireNBT(ItemStack item) {
        this.item = item;

    }

    public SetRapidfireNBT(FriendlyByteBuf buffer) {
        this.item = buffer.readItem();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.item);
    }

    public void handle(CustomPayloadEvent.Context context) {
        CompoundTag tag = this.item.getOrCreateTag();
        Boolean rapidfire = true;
        if (!tag.getBoolean("rapidfire_off")) {
            tag.putBoolean("rapidfire_off", rapidfire);
        } else {
            rapidfire = false;
            tag.putBoolean("rapidfire_off", rapidfire);
        }
        this.item.setTag(tag);
    }
}
