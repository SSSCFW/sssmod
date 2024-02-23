package com.example.examplemod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ExplosionParticle {
    private final Vec3 pos;

    public ExplosionParticle(Vec3 pos) {
        this.pos = pos;

    }

    public ExplosionParticle(FriendlyByteBuf buffer) {
        this.pos = buffer.readVec3();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVec3(this.pos);
    }

    public void handle(CustomPayloadEvent.Context context) {
        Minecraft.getInstance().level.addAlwaysVisibleParticle(ParticleTypes.EXPLOSION_EMITTER, true,
                this.pos.x, this.pos.y, this.pos.z,
                           0d, 0d, 0d);
        }
}
