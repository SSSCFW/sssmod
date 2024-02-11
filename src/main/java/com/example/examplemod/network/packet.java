package com.example.examplemod.network;

import com.example.examplemod.ExampleMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraft.world.level.chunk.LevelChunk;


public class packet {
    private static final SimpleChannel INSTANCE = ChannelBuilder.named(
        new ResourceLocation(ExampleMod.MODID, "main"))
        .serverAcceptedVersions((status, version) -> true)
        .clientAcceptedVersions((status, version) -> true)
        .networkProtocolVersion(1)
        .simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(PhantasmParticle.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PhantasmParticle::encode)
                .decoder(PhantasmParticle::new)
                .consumerMainThread(PhantasmParticle::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToLevelClients(Object msg, LevelChunk levelChunk) {
        INSTANCE.send(msg, PacketDistributor.TRACKING_CHUNK.with(levelChunk));
    }

    public static void sendToAllClients(Object msg) {
        INSTANCE.send(msg, PacketDistributor.ALL.noArg());
    }
}
