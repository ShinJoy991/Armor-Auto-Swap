package com.github.shinjoy991.armorautoswap.events;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.shinjoy991.armorautoswap.client.InputEvents.armorSwapEnabled;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
public class DamageEvents {

    @SubscribeEvent
    public static void onPlayerDestroyArmor(LivingHurtEvent e) {

        if (e.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) e.getEntity();
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                if (slot.getType() == EquipmentSlotType.Group.ARMOR) {
                    ItemStack armor = player.getItemBySlot(slot);
                    if (!armor.isEmpty() && armor.getItem() instanceof ArmorItem) {
                        int currentDurability = armor.getMaxDamage() - armor.getDamageValue();
                        int maxDurability = armor.getMaxDamage();
                        float threshold = maxDurability * Config.percentNumber;
                        if (threshold == 0) {
                            return;
                        }
                        if (armorSwapEnabled.getOrDefault(player.getUUID(), Config.defaultMode)) {
                            if (currentDurability - e.getAmount() / 4.0 < threshold/100.0) {

                                int replacementSlot = findReplacement(player, armor);
                                if (replacementSlot != -1) {
                                    ItemStack replacement = player.inventory.items.get(replacementSlot);
                                    player.setItemSlot(slot, replacement.copy());
                                    replacement.shrink(1);
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private static int findReplacement(ServerPlayerEntity player, ItemStack armor) {
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = player.inventory.items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                if (armorItem.getSlot() == ((ArmorItem) armor.getItem()).getSlot() && itemStack != armor) {
                    return i;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.inventory.items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem) itemStack.getItem();
                if (armorItem.getSlot() == ((ArmorItem) armor.getItem()).getSlot() && itemStack != armor) {
                    return i;
                }
            }
        }
        return -1;
    }
}