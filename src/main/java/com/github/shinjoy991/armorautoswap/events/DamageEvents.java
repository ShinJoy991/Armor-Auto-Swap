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

import static com.github.shinjoy991.armorautoswap.client.InputEvents.armorSwapEnabled;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
public class DamageEvents {

    @SubscribeEvent
    public static void onPlayerDestroyArmor(LivingHurtEvent e) {

        if (e.getEntity() instanceof ServerPlayer player) {
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
                        if (armorSwapEnabled.getOrDefault(player.getUUID(), Config.defaultMode)) {
                            if (currentDurability - e.getAmount() / 4.0 < threshold/100.0) {

                                ItemStack replacement = findReplacement(player, armor);
                                if (!replacement.isEmpty()) {

                                    if (!player.getInventory().add(armor.copy())) {

                                        player.drop(armor.copy(), false);
                                    }
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

    private static ItemStack findReplacement(Player player, ItemStack armor) {
        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = player.getInventory().items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getSlot() == ((ArmorItem) armor.getItem()).getSlot() && itemStack != armor) {
                    return itemStack;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.getInventory().items.get(i);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArmorItem armorItem) {
                if (armorItem.getSlot() == ((ArmorItem) armor.getItem()).getSlot() && itemStack != armor) {
                    return itemStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }
}