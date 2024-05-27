package com.github.shinjoy991.armorautoswap.events;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
public class DamageEvents {
    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDestroyArmor(LivingHurtEvent e) {

        if (e.getEntity() instanceof ServerPlayer player) {
            UUID playerUUID = player.getUUID();
            boolean isEnabled = armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack armor = player.getItemBySlot(slot);
                    if (!armor.isEmpty() && armor.getItem() instanceof ArmorItem) {
                        int currentDurability = armor.getMaxDamage() - armor.getDamageValue();
                        int maxDurability = armor.getMaxDamage();
                        float threshold = maxDurability * Config.percentNumber;
                        if (threshold == 0) {
                            return;
                        }
                        if (isEnabled) {
                            if (currentDurability - e.getAmount() / 4.0 < threshold/100.0) {

                                int replacementSlot = findReplacement(player, armor);
                                if (replacementSlot != -1) {
                                    ItemStack replacement = player.getInventory().items.get(replacementSlot);
                                    player.setItemSlot(slot, replacement.copy());
                                    player.getInventory().items.set(replacementSlot, armor.copy());
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private static int findReplacement(Player player, ItemStack armor) {
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = player.getInventory().items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getEquipmentSlot() == ((ArmorItem) armor.getItem()).getEquipmentSlot() && itemStack != armor) {
                    return i;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getEquipmentSlot() == ((ArmorItem) armor.getItem()).getEquipmentSlot() && itemStack != armor) {
                    return i;
                }
            }
        }
        return -1;
    }
}