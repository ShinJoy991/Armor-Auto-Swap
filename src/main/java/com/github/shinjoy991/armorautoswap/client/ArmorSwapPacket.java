package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.events.DamageEvents;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorSwapPacket {

    private final boolean isEnabled;

    public ArmorSwapPacket(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public static void encode(ArmorSwapPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.isEnabled);
    }

    public static ArmorSwapPacket decode(FriendlyByteBuf buffer) {
        boolean isEnabled = buffer.readBoolean();
        return new ArmorSwapPacket(isEnabled);
    }

    public static void handle(ArmorSwapPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player instanceof ServerPlayer) {
                DamageEvents.setEnable(player.getUUID(), packet.isEnabled);
                if (packet.isEnabled) {
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack item = player.getInventory().getItem(i);
                        if (item.getItem() instanceof CapsuleWardrobeItem capsule) {
                            capsule.setSwappedState(item, EquipmentSlot.FEET, false);
                            capsule.setSwappedState(item, EquipmentSlot.LEGS, false);
                            capsule.setSwappedState(item, EquipmentSlot.CHEST, false);
                            capsule.setSwappedState(item, EquipmentSlot.HEAD, false);
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}