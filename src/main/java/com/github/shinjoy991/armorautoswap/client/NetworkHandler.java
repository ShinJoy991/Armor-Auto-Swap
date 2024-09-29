package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ArmorAutoSwap.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        ArmorAutoSwap.NETWORK = CHANNEL;
        CHANNEL.registerMessage(0, ArmorSwapPacket.class, ArmorSwapPacket::encode,
                ArmorSwapPacket::decode, ArmorSwapPacket::handle);

        CHANNEL.registerMessage(1, ArmorSwapPacketClientManage.class,
                ArmorSwapPacketClientManage::encode,
                ArmorSwapPacketClientManage::decode,
                ArmorSwapPacketClientManage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}