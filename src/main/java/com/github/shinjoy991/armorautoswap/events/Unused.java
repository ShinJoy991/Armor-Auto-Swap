//package com.github.shinjoy991.armorautoswap.events;
//
//import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
//import com.github.shinjoy991.armorautoswap.Config;
//import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.item.ArmorItem;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraftforge.event.entity.living.LivingHurtEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static net.minecraft.world.entity.EquipmentSlot.*;
//
//
//@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
//public class DamageEvents {
//
//    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();
//
//    @SubscribeEvent
//    public static void onPlayerDestroyArmor(LivingHurtEvent e) {
//        if (e.getEntity() instanceof ServerPlayer player) {
//            boolean checkCapsule = false;
//            if (Config.percentNumber == 0) {
//                return;
//            }
////            DataComponentMap capsuleDataMap = null;
//            List<ItemStack> capsuleContainer = new ArrayList<>();
//            List<ItemStack> capsuleList = new ArrayList<>();
//            ItemStack capsuleStack = null;
//            int i = 0;
//            for (EquipmentSlot slot : EquipmentSlot.values()) {
//                if (slot.getFilterFlag() < 1 || slot.getFilterFlag() > 4) {
//                    continue;
//                }
//                ItemStack armor = player.getItemBySlot(slot);
//                if (!armor.isEmpty()) {
//                    int currentDurability = armor.getMaxDamage() - armor.getDamageValue();
//                    int maxDurability = armor.getMaxDamage();
//                    float threshold = maxDurability * Config.percentNumber;
//
//                    if (armorSwapEnabled.getOrDefault(player.getUUID(), Config.defaultMode)) {
//                        if (currentDurability - e.getAmount() / 4.0 < threshold / 100.0) {
//                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BINDING_CURSE, armor) > 0) {
//                                continue;
//                            }
//                            if (!checkCapsule) {
//                                checkCapsule = true;
//                                capsuleList = haveCapsuleWardrobeItem(player);
//                                if (capsuleList.isEmpty()) {
//                                    return;
//                                }
//                                for (ItemStack capsule : capsuleList) {
//                                    capsuleStack = capsule;
//                                    CompoundTag capsuleTag = capsuleStack.getOrCreateTag();
//                                    if (capsuleTag.contains("CONTAINER_TAG", Tag.TAG_LIST)) {
//                                        ListTag containerTagList = capsuleTag.getList("CONTAINER_TAG", Tag.TAG_COMPOUND);
//                                        capsuleContainer = new ArrayList<>(4);
//                                        for (int k = 0; k < 4; k++) {
//                                            capsuleContainer.add(ItemStack.EMPTY);
//                                        }
//                                        for (int k = 0; k < containerTagList.size(); k++) {
//                                            CompoundTag itemTag = containerTagList.getCompound(k);
//                                            ItemStack stack = ItemStack.of(itemTag);
//                                            if (stack.getItem() instanceof ArmorItem armorStack) {
//                                                if (armorStack.getEquipmentSlot() == HEAD) {
//                                                    capsuleContainer.set(0, stack);
//                                                }
//                                                else if (armorStack.getEquipmentSlot() == CHEST) {
//                                                    capsuleContainer.set(1, stack);
//                                                }
//                                                else if (armorStack.getEquipmentSlot() == LEGS) {
//                                                        capsuleContainer.set(2, stack);
//                                                    }
//                                                    else if (armorStack.getEquipmentSlot() == FEET) {
//                                                            capsuleContainer.set(3, stack);
//                                                        }
//                                            }
//                                        }
//                                        if (capsuleContainer.get(0) == capsuleContainer.get(1) &&
//                                                capsuleContainer.get(2) == capsuleContainer.get(3) &&
//                                                capsuleContainer.get(0) == ItemStack.EMPTY) {
//                                            continue;
//                                        }
//                                        break;
//                                    }
//                                }
//                            }
//                            int replacementSlot = Math.abs(slot.getFilterFlag() - 4);
//                            try {
//                                ItemStack replacementItem = capsuleContainer.get(replacementSlot);
//                                if (!replacementItem.isEmpty() && capsuleStack.getItem() instanceof CapsuleWardrobeItem capsule) {
//
//                                    boolean swapped = switch (replacementSlot) {
//                                        case 3 -> capsule.swappedFeet;
//                                        case 2 -> capsule.swappedLegs;
//                                        case 1 -> capsule.swappedChest;
//                                        case 0 -> capsule.swappedHead;
//                                        default -> false;
//                                    };
//                                    if (swapped)
//                                        continue;
//                                    try {
//                                        player.setItemSlot(slot, replacementItem);
//                                        capsuleContainer.set(replacementSlot, armor.copy());
//                                    } catch (Exception exception) {
//                                        continue;
//                                    }
//                                    switch (replacementSlot) {
//                                        case 3 -> capsule.swappedFeet = true;
//                                        case 2 -> capsule.swappedLegs = true;
//                                        case 1 -> capsule.swappedChest = true;
//                                        case 0 -> capsule.swappedHead = true;
//                                    }
//                                }
//                            } catch (IndexOutOfBoundsException exception) {
//                                continue;
//                            }
//                        }
//                    }
//
//                }
//
//            }
//            if (checkCapsule) {
//                CompoundTag capsuleTag = capsuleStack.getOrCreateTag();
//                ListTag containerTagList = new ListTag();
//
//                for (ItemStack stack : capsuleContainer) {
//                    CompoundTag itemTag = new CompoundTag();
//                    stack.save(itemTag);
//                    containerTagList.add(itemTag);
//                }
//
//                capsuleTag.put("CONTAINER_TAG", containerTagList);
//                capsuleStack.setTag(capsuleTag);
//            }
//
//        }
//    }
//
//    private static List<ItemStack> haveCapsuleWardrobeItem(ServerPlayer player) {
//        List<ItemStack> capsuleList = new ArrayList<>();
//        for (int i = 0; i < 36; i++) {
//            ItemStack itemStack = player.getInventory().items.get(i);
//            if (!itemStack.isEmpty() && itemStack.getItem() instanceof CapsuleWardrobeItem) {
//                capsuleList.add(itemStack);
//            }
//        }
//        return capsuleList;
//    }
//
//    public static void setEnable(UUID uuid, boolean enable) {
//        armorSwapEnabled.put(uuid, enable);
//    }
//}