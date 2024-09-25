package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.events.DamageEvents;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ArmorSwapPacket {

    private final boolean isEnabled;

    public ArmorSwapPacket(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public static ArmorSwapPacket decode(final FriendlyByteBuf buffer) {
        final boolean isEnabled = buffer.readBoolean();
        return new ArmorSwapPacket(isEnabled);
    }

    public static void encode(final ArmorSwapPacket message, final FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.isEnabled);
    }

    public static void handle(final ArmorSwapPacket message, final CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player instanceof ServerPlayer) {
                DamageEvents.setEnable(player.getUUID(), message.isEnabled);
                if (message.isEnabled) {
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack item = player.getInventory().getItem(i);

                        if (item.getItem() instanceof CapsuleWardrobeItem capsule) {
                            capsule.swappedFeet = false;
                            capsule.swappedLegs = false;
                            capsule.swappedChest = false;
                            capsule.swappedHead = false;
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}