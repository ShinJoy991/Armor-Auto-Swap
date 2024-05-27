package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.events.DamageEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public class ArmorSwapPacket {
    private final UUID playerUUID;
    private final boolean isEnabled;

    public ArmorSwapPacket(UUID playerUUID, boolean isEnabled) {
        this.playerUUID = playerUUID;
        this.isEnabled = isEnabled;
    }

    public static void encode(ArmorSwapPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.playerUUID);
        buffer.writeBoolean(packet.isEnabled);
    }

    public static ArmorSwapPacket decode(FriendlyByteBuf buffer) {
        UUID playerUUID = buffer.readUUID();
        boolean isEnabled = buffer.readBoolean();
        return new ArmorSwapPacket(playerUUID, isEnabled);
    }

    public static void handle(ArmorSwapPacket packet,
            CustomPayloadEvent.Context contextSupplier) {
        contextSupplier.enqueueWork(() -> {
            ServerPlayer player = contextSupplier.getSender();
            if (player != null && player.getUUID().equals(packet.playerUUID)) {
                DamageEvents.armorSwapEnabled.put(packet.playerUUID, packet.isEnabled);
                NetworkHandler.CHANNEL.send(
                        new ArmorSwapSyncPacket(packet.playerUUID, packet.isEnabled),
                        PacketDistributor.PLAYER.with(player));
            }
        });
        contextSupplier.setPacketHandled(true);
    }
}