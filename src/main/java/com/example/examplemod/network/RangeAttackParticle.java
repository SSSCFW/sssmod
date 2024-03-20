package com.example.examplemod.network;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class RangeAttackParticle {
    private final Vec3 pos;

    public RangeAttackParticle(Vec3 pos) {
        this.pos = pos;

    }

    public RangeAttackParticle(FriendlyByteBuf buffer) {
        this.pos = buffer.readVec3();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVec3(this.pos);
    }

    public void handle(CustomPayloadEvent.Context context) {
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            Minecraft.getInstance().level.addAlwaysVisibleParticle(ParticleTypes.SWEEP_ATTACK, true,
                this.pos.x+rand.nextInt(4)-2, this.pos.y+rand.nextInt(2), this.pos.z+rand.nextInt(4)-2,
                           0d, 0d, 0d);
        }
        }
        
}
