package com.github.shinjoy991.armorautoswap.register;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ArmorAutoSwap.MOD_ID);

    public static final RegistryObject<MenuType<CapsuleWardrobeMenu>> ARMOR_AUTO_SWAP = MENU_TYPES.register("armor_auto_swap",
        () -> IForgeMenuType.create((id, inv, data) -> new CapsuleWardrobeMenu(id, inv, new SimpleContainer(4))));
}