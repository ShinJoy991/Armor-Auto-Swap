package com.github.shinjoy991.armorautoswap.client;

import com.github.shinjoy991.armorautoswap.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

import static com.github.shinjoy991.armorautoswap.ArmorAutoSwap.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
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

                IFormattableTextComponent message;
                if (isEnabled) {
                    message = new StringTextComponent("Armor swap mode ")
                            .append(new StringTextComponent("enabled").withStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));
                } else {
                    message = new StringTextComponent("Armor swap mode ")
                            .append(new StringTextComponent("disabled").withStyle(Style.EMPTY.withColor(TextFormatting.RED)));
                }
                minecraft.gui.setOverlayMessage(message, false);
            }
        }
    }

}