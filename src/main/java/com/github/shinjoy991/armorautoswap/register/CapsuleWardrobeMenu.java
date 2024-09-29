package com.github.shinjoy991.armorautoswap.register;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static net.minecraft.world.entity.EquipmentSlot.*;

public class CapsuleWardrobeMenu extends AbstractContainerMenu {
    private static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{HEAD, EquipmentSlot.CHEST, LEGS, FEET};
    private final Container container;
    private final Player player;
    private final ItemStack openWardrobe;
    private final int originSlot;
    public CapsuleWardrobeMenu(int id, Inventory playerInv, Container container) {
        super(ModMenuRegistry.ARMOR_AUTO_SWAP.get(), id);
        this.container = container;
        this.player = playerInv.player;
        this.openWardrobe = player.getMainHandItem();
        this.originSlot = playerInv.selected + 4;
        for (int k = 0; k < 4; k++) {
            final EquipmentSlot equipmentslot = SLOT_IDS[k];
            this.addSlot(new Slot(container, k, 62 + 18, 17 + k * 18) {

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public void onTake(Player player, ItemStack itemStack) {
                    if (openWardrobe.getItem() instanceof CapsuleWardrobeItem capsule) {
                        switch (this.getContainerSlot()) {
                            case 3 -> capsule.setSwappedState(openWardrobe, FEET, false);
                            case 2 -> capsule.setSwappedState(openWardrobe, LEGS, false);
                            case 1 -> capsule.setSwappedState(openWardrobe, CHEST, false);
                            case 0 -> capsule.setSwappedState(openWardrobe, HEAD, false);
                            default -> {
                            }
                        }
                    }
                    slotSave(this.getContainerSlot(), ItemStack.EMPTY);
                    this.setChanged();
                }

                @Override
                public boolean mayPlace(ItemStack p_39746_) {
                    return p_39746_.canEquip(equipmentslot, player);
                }


                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
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


    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void clicked(int p_150400_, int p_150401_, ClickType p_150402_, Player p_150403_) {
        if (p_150400_ != this.originSlot) {
            super.clicked(p_150400_, p_150401_, p_150402_, p_150403_);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
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
    public void removed(Player player) {
        super.removed(player);
        slotSave(0, this.container.getItem(0));
        slotSave(1, this.container.getItem(1));
        slotSave(2, this.container.getItem(2));
        slotSave(3, this.container.getItem(3));
        this.container.stopOpen(player);
    }


    private void slotSave(int containerSlot, ItemStack stack) {
        CompoundTag tag = openWardrobe.getOrCreateTag();
        if (stack == ItemStack.EMPTY || stack.getItem() == Items.AIR) {
            tag.remove("CONTAINER_" + containerSlot);
        } else
            if (containerSlot >= 0) {
                tag.put("CONTAINER_" + containerSlot, stack.serializeNBT());
            }
        openWardrobe.setTag(tag);
    }
}