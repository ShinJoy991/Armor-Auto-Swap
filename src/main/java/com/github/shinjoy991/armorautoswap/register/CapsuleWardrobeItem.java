package com.github.shinjoy991.armorautoswap.register;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

import static net.minecraft.core.component.DataComponents.CONTAINER;

public class CapsuleWardrobeItem extends Item {

    public CapsuleWardrobeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            if (hand == InteractionHand.MAIN_HAND) {
                serverPlayer.openMenu(new MenuProvider() {

                    @Override
                    public Component getDisplayName() {
                        return Component.literal("Capsule Wardrobe");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                        SimpleContainer container = new SimpleContainer(4);

                        ItemStack heldItem = player.getItemInHand(hand);
                        DataComponentMap dataMap = heldItem.getComponents();

                        if (dataMap.get(CONTAINER) != null) {
                            List<ItemStack> stackContainer = dataMap.get(CONTAINER).stream().toList();

                            for (int i = 0; i < stackContainer.size(); i++) {
                                container.setItem(i, stackContainer.get(i));
                            }
                        }

                        return new CapsuleWardrobeMenu(id, inv, container);
                    }
                }, player.blockPosition());
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}