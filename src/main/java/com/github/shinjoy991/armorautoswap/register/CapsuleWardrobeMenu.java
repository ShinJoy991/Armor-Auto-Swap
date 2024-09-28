package com.github.shinjoy991.armorautoswap.register;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.inventory.EquipmentSlotType.*;

public class CapsuleWardrobeMenu extends Container {
    private static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlotType[] SLOT_IDS = new EquipmentSlotType[]{HEAD, CHEST, LEGS, FEET};
    private final IInventory container;
    private final ItemStack openWardrobe;
    private final int originSlot;

    public CapsuleWardrobeMenu(int id, PlayerEntity player, PlayerInventory playerInv, IInventory container, Integer originSlot) {
        super(ModMenuRegistry.ARMOR_AUTO_SWAP.get(), id);
        this.container = container;
        this.openWardrobe = player.getMainHandItem();
        this.originSlot = originSlot + 4;
        for (int k = 0; k < 4; k++) {
            final EquipmentSlotType equipmentslot = SLOT_IDS[k];
            this.addSlot(new Slot(container, k, 62 + 18, 17 + k * 18) {

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public ItemStack onTake(PlayerEntity player, ItemStack itemStack) {
                    if (openWardrobe.getItem() instanceof CapsuleWardrobeItem) {
                        CapsuleWardrobeItem capsule = (CapsuleWardrobeItem) openWardrobe.getItem();
                        switch (this.getSlotIndex()) {
                            case 3: capsule.setSwappedState(openWardrobe, FEET, false);
                            case 2: capsule.setSwappedState(openWardrobe, LEGS, false);
                            case 1: capsule.setSwappedState(openWardrobe, CHEST, false);
                            case 0: capsule.setSwappedState(openWardrobe, HEAD, false);
                            default: {
                            }
                        }
                    }
                    slotSave(this.getSlotIndex(), ItemStack.EMPTY);
                    this.setChanged();
                    return itemStack;
                }

                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return p_39746_.canEquip(equipmentslot, player);
                }


                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(PlayerContainer.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142 + 18));
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + 18 + y * 18));
            }
        }
    }


    public boolean stillValid(PlayerEntity p_75145_1_) {
        return this.container.stillValid(p_75145_1_);
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickType, PlayerEntity player) {
        if (slotId != this.originSlot) {
            return super.clicked(slotId, dragType, clickType, player);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack returnStack;
        Slot fromSlot = getSlot(index);
        if (!fromSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack fromStack = fromSlot.getItem();
        returnStack = fromStack.copy();
        if (index < 4) {
            if (!this.moveItemStackTo(fromStack, 4, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }

        } else
            if (!this.moveItemStackTo(fromStack, 0, 4, false)) {
                return ItemStack.EMPTY;
            } else
                return ItemStack.EMPTY;
        fromSlot.setChanged();
        fromSlot.onTake(player, fromStack);

        return returnStack;
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        slotSave(0, this.container.getItem(0));
        slotSave(1, this.container.getItem(1));
        slotSave(2, this.container.getItem(2));
        slotSave(3, this.container.getItem(3));
        this.container.stopOpen(player);
    }


    private void slotSave(int containerSlot, ItemStack stack) {
        CompoundNBT tag = openWardrobe.getOrCreateTag();
        if (stack == ItemStack.EMPTY || stack.getItem() == Items.AIR) {
            tag.remove("CONTAINER_" + containerSlot);
        } else
            if (containerSlot >= 0) {
                tag.put("CONTAINER_" + containerSlot, stack.serializeNBT());
            }
        openWardrobe.setTag(tag);
    }
}