package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ArmorAutoSwap.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, ArmorSwapPacket.class, ArmorSwapPacket::encode,
                ArmorSwapPacket::decode, ArmorSwapPacket::handle);
        CHANNEL.registerMessage(id++, ArmorSwapSyncPacket.class, ArmorSwapSyncPacket::encode,
                ArmorSwapSyncPacket::decode, ArmorSwapSyncPacket::handle);
    }
}