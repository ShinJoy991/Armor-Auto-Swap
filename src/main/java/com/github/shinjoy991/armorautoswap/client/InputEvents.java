package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientKeyMapping.DRINKING_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                UUID playerUUID = minecraft.player.getUUID();
                boolean isEnabled = armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
                isEnabled = !isEnabled;
                armorSwapEnabled.put(playerUUID, isEnabled);

                MutableComponent message;
                if (isEnabled) {
                    message = new TextComponent("Armor swap mode ")
                            .append(new TextComponent("enabled").withStyle(ChatFormatting.GREEN));
                } else {
                    message = new TextComponent("Armor swap mode ")
                            .append(new TextComponent("disabled").withStyle(ChatFormatting.RED));
                }

                minecraft.gui.setOverlayMessage(message, false);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT, bus =
            Mod.EventBusSubscriber.Bus.MOD)

    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ClientRegistry.registerKeyBinding(ClientKeyMapping.DRINKING_KEY);
        }
    }
}