package com.github.shinjoy991.armorautoswap.command;

import com.github.shinjoy991.armorautoswap.Config;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class ReloadCommand {
    public ReloadCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("ArmorAutoSwapReload").requires(command -> command
                .hasPermission(4)).executes((command) -> CustomCommand1a(command.getSource())));
    }

    private int CustomCommand1a(CommandSource source) {

        Entity player = source.getEntity();
        if (!(player instanceof ServerPlayerEntity)) {
            return 0;
        }
        if (!player.getCommandSenderWorld().isClientSide && player.getServer() != null) {
            Config.reloadCommand();
            IFormattableTextComponent message = new StringTextComponent("[ArmorAutoSwap] ")
                    .setStyle(Style.EMPTY.withColor(TextFormatting.GOLD))
                    .append(new StringTextComponent("Reloaded").setStyle(Style.EMPTY.withColor(TextFormatting.GREEN)));
            player.sendMessage(message, player.getUUID());
        }
        return Command.SINGLE_SUCCESS;
    }
}