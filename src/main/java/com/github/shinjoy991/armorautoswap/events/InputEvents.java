package com.github.shinjoy991.armorautoswap.events;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import com.github.shinjoy991.armorautoswap.client.ArmorSwapPacket;
import com.github.shinjoy991.armorautoswap.client.ClientKeyMapping;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientKeyMapping.DRINKING_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                UUID playerUUID = minecraft.player.getUUID();
                boolean currentEnabledState =
                        DamageEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
                boolean isEnabled = !currentEnabledState;

                NetworkHandler.CHANNEL.sendToServer(new ArmorSwapPacket(playerUUID, isEnabled));

                IFormattableTextComponent message;
                if (isEnabled) {
                    message = new StringTextComponent("Armor swap mode ")
                            .append(new StringTextComponent("enabled").setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));
                } else {
                    message = new StringTextComponent("Armor swap mode ")
                            .append(new StringTextComponent("disabled").setStyle(Style.EMPTY.withColor(TextFormatting.RED)));
                }

                minecraft.gui.setOverlayMessage(message, false);
            }
        }
    }
}