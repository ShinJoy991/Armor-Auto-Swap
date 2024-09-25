package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class NetworkHandler {
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(ArmorAutoSwap.MOD_ID, "network");

    public static SimpleChannel registerNetworkChannel() {
        final SimpleChannel channel = ChannelBuilder.named(CHANNEL_NAME)
                .acceptedVersions(Channel.VersionTest.exact(1))
                .networkProtocolVersion(1)
                .simpleChannel();
        ArmorAutoSwap.NETWORK = channel;

        channel.messageBuilder(ArmorSwapPacket.class, 0)
                .encoder(ArmorSwapPacket::encode)
                .decoder(ArmorSwapPacket::decode)
                .consumerMainThread(ArmorSwapPacket::handle)
                .add();
        return channel;
    }
}