package com.github.shinjoy991.armorautoswap.register;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CapsuleWardrobeItem extends Item {

    public CapsuleWardrobeItem(Properties properties) {
        super(properties);
    }
    public void setSwappedState(ItemStack stack, EquipmentSlot slot, boolean swapped) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putBoolean("swapped" + slot.getName(), swapped);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide())
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            if (hand == InteractionHand.MAIN_HAND) {
                serverPlayer.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.literal("Capsule Wardrobe");
                    }


                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                        SimpleContainer container = new SimpleContainer(4);
                        ItemStack heldItem = player.getMainHandItem();
                        CompoundTag nbt = heldItem.getOrCreateTag();
                        for (int k = 0; k < 4; k++) {
                            if (nbt.contains("CONTAINER_" + k)) {
                                CompoundTag containerTag = nbt.getCompound("CONTAINER_" + k);
                                ItemStack stack = ItemStack.of(containerTag);
                                container.setItem(k, stack);
                            }
                        }

                        return new CapsuleWardrobeMenu(id, inv, container);
                    }
                });
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}