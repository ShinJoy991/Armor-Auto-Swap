package com.github.shinjoy991.armorautoswap.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorSwapPacketClientManage {
    private final boolean swapEnabled;

    public ArmorSwapPacketClientManage(boolean swapEnabled) {
        this.swapEnabled = swapEnabled;
    }

    public static void encode(ArmorSwapPacketClientManage packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.swapEnabled);
    }

    public static ArmorSwapPacketClientManage decode(FriendlyByteBuf buffer) {
        return new ArmorSwapPacketClientManage(buffer.readBoolean());
    }

    public static void handle(ArmorSwapPacketClientManage packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            InputEvents.armorSwapEnabled.put(player.getUUID(), packet.swapEnabled);
        });
        context.setPacketHandled(true);
    }
}
