package com.github.shinjoy991.armorautoswap.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientKeyMapping {
    public static final String KEY_CATEGORY_RELOAD = "key.category.armor_auto_swap";
    public static final String KEY_DESCRIPTION = "key.category.armor_auto_swap.reload";
    public static final KeyBinding SWAP_KEY = new KeyBinding(KEY_DESCRIPTION,
            KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_0,
            KEY_CATEGORY_RELOAD);
}