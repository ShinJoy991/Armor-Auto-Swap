package com.github.shinjoy991.armorautoswap.command;

import com.github.shinjoy991.armorautoswap.Config;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class ReloadCommand {
    public ReloadCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ArmorAutoSwapReload").executes((command) -> CustomCommand1a(command.getSource())));
    }

    private int CustomCommand1a(CommandSourceStack source) {

        Entity player = source.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return 0;
        }
        if (player.hasPermissions(4)) {
            if (!player.getCommandSenderWorld().isClientSide && player.getServer() != null) {
                Config.reloadCommand();

                MutableComponent message = Component.literal("[ArmorAutoSwap] ")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                        .append(Component.literal("Reloaded").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                player.sendSystemMessage(message);
            }
        }
        return 0;
    }
}