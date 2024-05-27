package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

public class NetworkHandler {

    public static final SimpleChannel CHANNEL = ChannelBuilder.named(
                    new ResourceLocation(ArmorAutoSwap.MOD_ID, "main"))
            .serverAcceptedVersions((status, version) -> true)
            .clientAcceptedVersions((status, version) -> true)
            .networkProtocolVersion(1)
            .simpleChannel();

    public static void register() {
        int id = 0;
        CHANNEL.messageBuilder(ArmorSwapPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ArmorSwapPacket::encode)
                .decoder(ArmorSwapPacket::decode)
                .consumerNetworkThread(ArmorSwapPacket::handle)
                .add();
        CHANNEL.messageBuilder(ArmorSwapSyncPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ArmorSwapSyncPacket::encode)
                .decoder(ArmorSwapSyncPacket::decode)
                .consumerNetworkThread(ArmorSwapSyncPacket::handle)
                .add();
    }
}