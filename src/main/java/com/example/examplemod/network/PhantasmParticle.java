package com.example.examplemod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PhantasmParticle {
    private final Vec3 pos;

    public PhantasmParticle(Vec3 pos) {
        this.pos = pos;

    }

    public PhantasmParticle(FriendlyByteBuf buffer) {
        this.pos = buffer.readVec3();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVec3(this.pos);
    }

    public void handle(CustomPayloadEvent.Context context) {
        for(int i = 0; i < 360; i++) {
            for(int i2 = -180; i2 < 180; i2++) {
                if (i % 40 == 0 && i2 % 2 == 0) {
                    Minecraft.getInstance().level.addAlwaysVisibleParticle(ParticleTypes.FIREWORK, true, 
                    this.pos.x, this.pos.y + 1.0D, this.pos.z,
                                Math.cos(i) * (1 - Math.abs(i2) / 15d), i2 / 15d, Math.sin(i) * (1 - Math.abs(i2) / 15d));
                    
                }
            }
            if (i % 2 == 0) {
                Minecraft.getInstance().level.addAlwaysVisibleParticle(ParticleTypes.FIREWORK, true,
                        this.pos.x, this.pos.y + 1.0D, this.pos.z,
                                    Math.cos(i) * 3d, 0d, Math.sin(i) * 3d);
            }

        }
    }
}
