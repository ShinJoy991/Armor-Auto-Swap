package com.github.shinjoy991.armorautoswap.events;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeItem;
import com.github.shinjoy991.armorautoswap.register.CapsuleWardrobeMenu;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.shinjoy991.armorautoswap.helpers.SwappedTagUtil.getSwappedTag;
import static com.github.shinjoy991.armorautoswap.helpers.SwappedTagUtil.putSwappedTag;
import static net.minecraft.core.component.DataComponents.CONTAINER;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
public class DamageEvents {

    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDestroyArmor(LivingHurtEvent e) {
        if (e.getEntity() instanceof ServerPlayer player) {
            boolean checkCapsule = false;
            if (Config.percentNumber == 0) {
                return;
            }
            if (player.containerMenu instanceof CapsuleWardrobeMenu) {
                return;
            }
            DataComponentMap capsuleDataMap = null;
            List<ItemStack> capsuleContainer = new ArrayList<>();
            List<ItemStack> capsuleList = new ArrayList<>();
            ItemStack capsuleStack = null;
            int i = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
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
                                    capsuleDataMap = capsule.getComponents();
                                    if (capsuleDataMap.get(CONTAINER) != null) {
                                        capsuleContainer = capsuleDataMap.get(CONTAINER).stream().collect(Collectors.toCollection(ArrayList::new));
                                        if (capsuleContainer.isEmpty()) {
                                            continue;
                                        }
                                        break;
                                    }
                                }
                            }
                            int replacementSlot = Math.abs(slot.getFilterFlag() - 4);
                            try {
                                ItemStack replacementItem = capsuleContainer.get(replacementSlot);
                                if (!replacementItem.isEmpty() && capsuleStack.getItem() instanceof CapsuleWardrobeItem) {

                                    boolean swapped = switch (replacementSlot) {
                                        case 3 -> getSwappedTag(capsuleStack, "swappedfeet");
                                        case 2 -> getSwappedTag(capsuleStack, "swappedlegs");
                                        case 1 -> getSwappedTag(capsuleStack, "swappedchest");
                                        case 0 -> getSwappedTag(capsuleStack, "swappedhead");
                                        default -> false;
                                    };
                                    if (swapped)
                                        continue;
                                    try {
                                        player.setItemSlot(slot, replacementItem);
                                        capsuleContainer.set(replacementSlot, armor.copy());
                                    } catch (Exception exception) {
                                        continue;
                                    }
                                    switch (replacementSlot) {
                                        case 3 -> putSwappedTag("swappedfeet", capsuleStack, true);
                                        case 2 -> putSwappedTag("swappedlegs", capsuleStack, true);
                                        case 1 -> putSwappedTag("swappedchest", capsuleStack, true);
                                        case 0 -> putSwappedTag("swappedhead", capsuleStack, true);
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
                capsuleStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(capsuleContainer));
            }

        }
    }

    private static List<ItemStack> haveCapsuleWardrobeItem(ServerPlayer player) {
        List<ItemStack> capsuleList = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = player.getInventory().items.get(i);
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