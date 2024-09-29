package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    public static final HashMap<UUID, Boolean> armorSwapEnabled = new HashMap<>();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientKeyMapping.SWAP_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                UUID playerUUID = minecraft.player.getUUID();
                boolean isEnabled = armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
                isEnabled = !isEnabled;
                armorSwapEnabled.put(playerUUID, isEnabled);
                NetworkHandler.CHANNEL.sendToServer(new ArmorSwapPacket(isEnabled));

                Component message;
                if (isEnabled) {
                    message = new TextComponent("Armor swap mode ")
                            .append(new TextComponent("enabled").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                } else {
                    message = new TextComponent("Armor swap mode ")
                            .append(new TextComponent("disabled").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }
                minecraft.gui.setOverlayMessage(message, false);
            }
        }
    }
}