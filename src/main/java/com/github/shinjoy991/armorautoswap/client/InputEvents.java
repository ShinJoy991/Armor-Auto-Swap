package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

                IFormattableTextComponent message;
                if (isEnabled) {
                    message = new StringTextComponent("Armor swap mode ")
                            .append(new StringTextComponent("enabled").withStyle(TextFormatting.GREEN));
                } else {
                    message = new StringTextComponent("Armor swap mode ")
                            .append(new StringTextComponent("disabled").withStyle(TextFormatting.RED));
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