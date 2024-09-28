package com.github.shinjoy991.armorautoswap.events;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeMenu;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;


@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
public class DamageEvents {

    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDestroyArmor(LivingHurtEvent e) {
        if (e.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) e.getEntity();
            boolean checkCapsule = false;
            if (Config.percentNumber == 0) {
                return;
            }
            List<ItemStack> capsuleContainer = new ArrayList<>(Arrays.asList(
                    ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY
            ));
            if (player.containerMenu instanceof CapsuleWardrobeMenu) {
                return;
            }
            List<ItemStack> capsuleList;
            ItemStack capsuleStack = null;
            int i = 0;
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                if (slot.getFilterFlag() < 1 || slot.getFilterFlag() > 4) {
                    continue;
                }
                ItemStack armor = player.getItemBySlot(slot);
                if (!armor.isEmpty()) {
                    int currentDurability = armor.getMaxDamage() - armor.getDamageValue();
                    int maxDurability = armor.getMaxDamage();
                    float threshold = maxDurability * Config.percentNumber;
                    if (armorSwapEnabled.getOrDefault(player.getUUID(), Config.defaultMode)) {
                        if (currentDurability - e.getAmount() / 4.0 < threshold / 100.0) {
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BINDING_CURSE, armor) > 0) {
                                continue;
                            }
                            if (!checkCapsule) {
                                checkCapsule = true;
                                capsuleList = haveCapsuleWardrobeItem(player);
                                if (capsuleList.isEmpty()) {
                                    return;
                                }
                                for (ItemStack capsule : capsuleList) {
                                    capsuleStack = capsule;
                                    CompoundNBT capsuleTag = capsuleStack.getOrCreateTag();
                                    boolean has = false;
                                    for (int k = 0; k < 4; k++) {
                                        if (capsuleTag.contains("CONTAINER_" + k)) {
                                            has = true;
                                            CompoundNBT containerTag = capsuleTag.getCompound("CONTAINER_" + k);
                                            ItemStack stack = ItemStack.of(containerTag);
                                            capsuleContainer.set(k, stack);
                                        }
                                    }
                                    if (!has) {
                                        continue;
                                    }
                                    break;
                                }
                            }

                            int replacementSlot = Math.abs(slot.getFilterFlag() - 4);
                            try {
                                ItemStack replacementItem = capsuleContainer.get(replacementSlot);
                                if (!replacementItem.isEmpty() && capsuleStack.getItem() instanceof CapsuleWardrobeItem) {
                                    CompoundNBT nbt = capsuleStack.getOrCreateTag();
                                    boolean swapped = false;
                                    if (replacementSlot == 3) {
                                        swapped = nbt.getBoolean("swappedfeet");
                                    } else
                                        if (replacementSlot == 2) {
                                            swapped = nbt.getBoolean("swappedlegs");
                                        } else
                                            if (replacementSlot == 1) {
                                                swapped = nbt.getBoolean("swappedchest");
                                            } else
                                                if (replacementSlot == 0) {
                                                    swapped = nbt.getBoolean("swappedhead");
                                                } else {
                                                    continue;
                                                }
                                    if (swapped)
                                        continue;
                                    try {
                                        player.setItemSlot(slot, replacementItem);
                                        capsuleContainer.set(replacementSlot, armor.copy());
                                    } catch (Exception exception) {
                                        continue;
                                    }
                                    if (replacementSlot == 3) {
                                        nbt.putBoolean("swappedfeet", true);
                                    } else
                                        if (replacementSlot == 2) {
                                            nbt.putBoolean("swappedlegs", true);
                                        } else
                                            if (replacementSlot == 1) {
                                                nbt.putBoolean("swappedchest", true);
                                            } else
                                                if (replacementSlot == 0) {
                                                    nbt.putBoolean("swappedhead", true);
                                                }
                                }
                            } catch (IndexOutOfBoundsException exception) {
                                continue;
                            }
                        }
                    }

                }

            }

            if (checkCapsule) {

                CompoundNBT capsuleTag = capsuleStack.getOrCreateTag();
                for (int j = 0; j < 4; j++) {
                    ItemStack itemStack = capsuleContainer.get(j);
                    if (!itemStack.isEmpty()) {
                        capsuleTag.put("CONTAINER_" + j, itemStack.serializeNBT());
                    }
                }
            }

        }
    }

    private static List<ItemStack> haveCapsuleWardrobeItem(ServerPlayerEntity player) {
        List<ItemStack> capsuleList = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = player.inventory.items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof CapsuleWardrobeItem) {
                capsuleList.add(itemStack);
            }
        }
        return capsuleList;
    }

    public static void setEnable(UUID uuid, boolean enable) {
        armorSwapEnabled.put(uuid, enable);
    }
}