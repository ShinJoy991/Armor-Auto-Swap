package com.github.shinjoy991.armorautoswap.events;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import com.github.shinjoy991.armorautoswap.Config;
import com.github.shinjoy991.armorautoswap.client.ArmorSwapPacket;
import com.github.shinjoy991.armorautoswap.client.ClientKeyMapping;
import com.github.shinjoy991.armorautoswap.client.NetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ClientKeyMapping.DRINKING_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                UUID playerUUID = minecraft.player.getUUID();
                boolean currentEnabledState =
                        DamageEvents.armorSwapEnabled.getOrDefault(playerUUID, Config.defaultMode);
                boolean isEnabled = !currentEnabledState;

                NetworkHandler.CHANNEL.send(new ArmorSwapPacket(playerUUID, isEnabled),
                        PacketDistributor.SERVER.noArg());

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
}