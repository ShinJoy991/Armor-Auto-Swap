package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.events.DamageEvents;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class ArmorSwapPacket {
    private final UUID playerUUID;
    private final boolean isEnabled;

    public ArmorSwapPacket(UUID playerUUID, boolean isEnabled) {
        this.playerUUID = playerUUID;
        this.isEnabled = isEnabled;
    }

    public static void encode(ArmorSwapPacket packet, PacketBuffer buffer) {
        buffer.writeUUID(packet.playerUUID);
        buffer.writeBoolean(packet.isEnabled);
    }

    public static ArmorSwapPacket decode(PacketBuffer buffer) {
        UUID playerUUID = buffer.readUUID();
        boolean isEnabled = buffer.readBoolean();
        return new ArmorSwapPacket(playerUUID, isEnabled);
    }

    public static void handle(ArmorSwapPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null && player.getUUID().equals(packet.playerUUID)) {
                DamageEvents.armorSwapEnabled.put(packet.playerUUID, packet.isEnabled);
                NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new ArmorSwapSyncPacket(packet.playerUUID, packet.isEnabled));
            }
        });
        context.setPacketHandled(true);
    }
}