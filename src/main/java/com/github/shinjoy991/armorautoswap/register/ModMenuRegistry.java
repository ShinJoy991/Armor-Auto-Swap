package com.github.shinjoy991.armorautoswap.register;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuRegistry {
    public static final DeferredRegister<ContainerType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ArmorAutoSwap.MOD_ID);

    public static final RegistryObject<ContainerType<CapsuleWardrobeMenu>> ARMOR_AUTO_SWAP = MENU_TYPES.register("armor_auto_swap",
            () -> IForgeContainerType.create((id, inv, data) -> {
                PlayerEntity player = inv.player;
                IInventory container = new Inventory(4);
                Integer originSlot = player.inventory.selected;
                return new CapsuleWardrobeMenu(id, player, inv, container, originSlot);
            }));
}