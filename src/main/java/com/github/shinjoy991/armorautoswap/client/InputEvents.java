package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ClientKeyMapping.DRINKING_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                UUID playerUUID = minecraft.player.getUUID();
                boolean isEnabled = armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
                isEnabled = !isEnabled;
                armorSwapEnabled.put(playerUUID, isEnabled);

                Component message;
                if (isEnabled) {
                    message = Component.literal("Armor swap mode ")
                            .append(Component.literal("enabled").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                } else {
                    message = Component.literal("Armor swap mode ")
                            .append(Component.literal("disabled").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }

                minecraft.gui.setOverlayMessage(message, false);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT, bus =
            Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(RegisterKeyMappingsEvent event) {
            event.register(ClientKeyMapping.DRINKING_KEY);
        }
    }
}