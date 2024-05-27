package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.events.DamageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ArmorSwapSyncPacket {
    private final UUID playerUUID;
    private final boolean isEnabled;

    public ArmorSwapSyncPacket(UUID playerUUID, boolean isEnabled) {
        this.playerUUID = playerUUID;
        this.isEnabled = isEnabled;
    }

    public static void encode(ArmorSwapSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.playerUUID);
        buffer.writeBoolean(packet.isEnabled);
    }

    public static ArmorSwapSyncPacket decode(FriendlyByteBuf buffer) {
        UUID playerUUID = buffer.readUUID();
        boolean isEnabled = buffer.readBoolean();
        return new ArmorSwapSyncPacket(playerUUID, isEnabled);
    }

    public static void handle(ArmorSwapSyncPacket packet,
            CustomPayloadEvent.Context contextSupplier) {
       // NetworkEvent.Context context = contextSupplier.get();
        contextSupplier.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null && minecraft.player.getUUID().equals(packet.playerUUID)) {
                DamageEvents.armorSwapEnabled.put(packet.playerUUID, packet.isEnabled);
            }
        });
        contextSupplier.setPacketHandled(true);
    }
}
