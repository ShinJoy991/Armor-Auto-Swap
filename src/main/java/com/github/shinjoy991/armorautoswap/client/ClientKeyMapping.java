package com.github.shinjoy991.armorautoswap.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientKeyMapping {
    public static final String KEY_CATEGORY_RELOAD = "key.category.armor_auto_swap";
    public static final String KEY_DESCRIPTION = "key.category.armor_auto_swap.reload";
    public static final KeyMapping SWAP_KEY = new KeyMapping(KEY_DESCRIPTION,
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_0,
            KEY_CATEGORY_RELOAD);
}