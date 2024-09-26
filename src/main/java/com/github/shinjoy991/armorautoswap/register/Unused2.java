//package com.github.shinjoy991.armorautoswap.register;
//
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.SimpleContainer;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.InventoryMenu;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//
//public class CapsuleWardrobeMenu extends AbstractContainerMenu {
//    private static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
//    private static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
//    private static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
//    private static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
//    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
//    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
//    private final SimpleContainer container;
//    private final Player player;
//
//    public CapsuleWardrobeMenu(int id, Inventory playerInv, SimpleContainer container) {
//        super(ModMenuRegistry.ARMOR_AUTO_SWAP.get(), id);
//        this.container = container;
//        this.player = playerInv.player;
//
//        for (int k = 0; k < 4; k++) {
//            final EquipmentSlot equipmentslot = SLOT_IDS[k];
//            this.addSlot(new Slot(container, k, 62 + 18, 17 + k * 18) {
//
//                @Override
//                public int getMaxStackSize() {
//                    return 1;
//                }
//
//                @Override
//                public void onTake(Player player, ItemStack itemStack) {
//                    ItemStack item = player.getMainHandItem();
//                    if (item.getItem() instanceof CapsuleWardrobeItem capsule) {
//                        switch (this.getContainerSlot()) {
//                            case 3 -> capsule.swappedFeet = false;
//                            case 2 -> capsule.swappedLegs = false;
//                            case 1 -> capsule.swappedChest = false;
//                            case 0 -> capsule.swappedHead = false;
//                            default -> {
//                            }
//                        }
//                    }
//                    this.setChanged();
//                }
//
//                @Override
//                public boolean mayPlace(ItemStack p_39746_) {
//                    return p_39746_.canEquip(equipmentslot, player);
//                }
//
//                @Override
//                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
//                    return Pair.of(InventoryMenu.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
//                }
//            });
//        }
//        for (int y = 0; y < 3; ++y) {
//            for (int x = 0; x < 9; ++x) {
//                this.addSlot(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + 18 + y * 18));
//            }
//        }
//
//        for (int x = 0; x < 9; ++x) {
//            this.addSlot(new Slot(playerInv, x, 8 + x * 18, 142 + 18));
//        }
//    }
//
//    @Override
//    public boolean stillValid(Player player) {
//        return this.container.stillValid(player);
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player player, int index) {
//        ItemStack returnStack = ItemStack.EMPTY;
//        Slot slot = this.slots.get(index);
//        if (slot.hasItem()) {
//            ItemStack slotStack = slot.getItem();
//            returnStack = slotStack.copy();
//            if (index < 4) {
//                if (!this.moveItemStackTo(slotStack, 4, this.slots.size(), true)) {
//                    return ItemStack.EMPTY;
//                }
//                ItemStack item = player.getMainHandItem();
//                if (item.getItem() instanceof CapsuleWardrobeItem capsule) {
//                    switch (index) {
//                        case 3 -> capsule.swappedFeet = false;
//                        case 2 -> capsule.swappedLegs = false;
//                        case 1 -> capsule.swappedChest = false;
//                        case 0 -> capsule.swappedHead = false;
//                        default -> {
//                        }
//                    }
//                }
//            } else
//                if (!this.moveItemStackTo(slotStack, 0, 4, false)) {
//                    return ItemStack.EMPTY;
//                }
//
//            if (slotStack.isEmpty())
//                slot.set(ItemStack.EMPTY);
//            else
//                slot.setChanged();
//        }
//
//        return returnStack;
//    }
//
//    @Override
//    public void removed(Player player) {
//        super.removed(player);
//
//        ItemStack capsuleStack = player.getMainHandItem();
//        if (!capsuleStack.isEmpty() && capsuleStack.getItem() instanceof CapsuleWardrobeItem) {
//            ListTag containerTag = new ListTag();
//            for (int i = 0; i < this.container.getContainerSize(); i++) {
//                ItemStack itemStack = this.container.getItem(i);
//                if (!itemStack.isEmpty()) {
//                    CompoundTag itemTag = new CompoundTag();
//                    itemStack.save(itemTag);
//                    containerTag.add(itemTag);
//                }
//            }
//            CompoundTag nbt = capsuleStack.getOrCreateTag();
//            nbt.put("CONTAINER_TAG", containerTag);
//        }
//
//        this.container.stopOpen(player);
//    }
//
//}