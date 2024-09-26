//package com.github.shinjoy991.armorautoswap.register;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.MenuProvider;
//import net.minecraft.world.SimpleContainer;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.item.ArmorItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static net.minecraft.world.entity.EquipmentSlot.*;
//import static net.minecraft.world.entity.EquipmentSlot.FEET;
//
//public class CapsuleWardrobeItem extends Item {
//
//    public boolean swappedFeet = false;
//    public boolean swappedLegs = false;
//    public boolean swappedChest = false;
//    public boolean swappedHead = false;
//    public CapsuleWardrobeItem(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
//            serverPlayer.openMenu(new MenuProvider() {
//                @Override
//                public Component getDisplayName() {
//                    return Component.literal("Capsule Wardrobe");
//                }
//
//
//                @Override
//                public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
//                    SimpleContainer container = new SimpleContainer(4);
//                    ItemStack heldItem = player.getItemInHand(hand);
//
//                    CompoundTag nbt = heldItem.getOrCreateTag();
//
//                    if (nbt.contains("CONTAINER_TAG", Tag.TAG_LIST)) {
//                        ListTag stackList = nbt.getList("CONTAINER_TAG", Tag.TAG_COMPOUND);
//                        List<ItemStack> capsuleContainer = new ArrayList<>(4);
//                        for (int k = 0; k < 4; k++) {
//                            capsuleContainer.add(ItemStack.EMPTY);
//                        }
//                        for (int k = 0; k < capsuleContainer.size(); k++) {
//                            CompoundTag itemTag = stackList.getCompound(k);
//                            ItemStack stack = ItemStack.of(itemTag);
//                            if (stack.getItem() instanceof ArmorItem armorStack) {
//                                if (armorStack.getEquipmentSlot() == HEAD) {
//                                    container.setItem(0, stack);
//                                }
//                                else if (armorStack.getEquipmentSlot() == CHEST) {
//                                    container.setItem(1, stack);
//                                }
//                                else if (armorStack.getEquipmentSlot() == LEGS) {
//                                        container.setItem(2, stack);
//                                    }
//                                    else if (armorStack.getEquipmentSlot() == FEET) {
//                                            container.setItem(3, stack);
//                                        }
//                            }
//                        }
////                        for (int i = 0; i < stackList.size(); i++) {
////                            CompoundTag itemTag = stackList.getCompound(i);
////                            ItemStack stack = ItemStack.of(itemTag);
////                            container.setItem(i, stack);
////                        }
//                    }
//
//                    return new CapsuleWardrobeMenu(id, inv, container);
//                }
//            });
//        }
//        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
//    }
//}