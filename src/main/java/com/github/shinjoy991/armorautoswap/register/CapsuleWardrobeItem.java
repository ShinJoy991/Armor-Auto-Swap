package com.github.shinjoy991.armorautoswap.register;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class CapsuleWardrobeItem extends Item {

    public CapsuleWardrobeItem(Item.Properties properties) {
        super(properties);
    }

    public void setSwappedState(ItemStack stack, EquipmentSlotType slot, boolean swapped) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putBoolean("swapped" + slot.getName(), swapped);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if (level.isClientSide())
            return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        if (!level.isClientSide() && player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            if (hand == Hand.MAIN_HAND) {
                serverPlayer.openMenu(new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new StringTextComponent("Capsule Wardrobe");
                    }

                    @Override
                    public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
                        IInventory container = new Inventory(4);
                        ItemStack heldItem = player.getMainHandItem();
                        CompoundNBT nbt = heldItem.getOrCreateTag();
                        for (int k = 0; k < 4; k++) {
                            if (nbt.contains("CONTAINER_" + k)) {
                                CompoundNBT containerTag = nbt.getCompound("CONTAINER_" + k);
                                ItemStack stack = ItemStack.of(containerTag);
                                container.setItem(k, stack);
                            }
                        }

                        return new CapsuleWardrobeMenu(id, player, inv, container, player.inventory.selected);
                    }
                });
            }
        }
        return ActionResult.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}