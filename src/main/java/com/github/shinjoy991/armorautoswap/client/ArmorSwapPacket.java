package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.events.DamageEvents;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorSwapPacket {

    private final boolean isEnabled;

    public ArmorSwapPacket(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public static void encode(ArmorSwapPacket packet, PacketBuffer buffer) {
        buffer.writeBoolean(packet.isEnabled);
    }

    public static ArmorSwapPacket decode(PacketBuffer buffer) {
        boolean isEnabled = buffer.readBoolean();
        return new ArmorSwapPacket(isEnabled);
    }

    public static void handle(ArmorSwapPacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player instanceof ServerPlayerEntity) {
                DamageEvents.setEnable(player.getUUID(), packet.isEnabled);
                if (packet.isEnabled) {
                    for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                        ItemStack item = player.inventory.getItem(i);
                        if (item.getItem() instanceof CapsuleWardrobeItem) {
                            CapsuleWardrobeItem capsule = (CapsuleWardrobeItem) item.getItem();
                            capsule.setSwappedState(item, EquipmentSlotType.FEET, false);
                            capsule.setSwappedState(item, EquipmentSlotType.LEGS, false);
                            capsule.setSwappedState(item, EquipmentSlotType.CHEST, false);
                            capsule.setSwappedState(item, EquipmentSlotType.HEAD, false);
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}