package com.github.shinjoy991.armorautoswap.command;

import com.github.shinjoy991.armorautoswap.ArmorAutoSwap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = ArmorAutoSwap.MOD_ID)
public class CommandRegister {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {

        new ReloadCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}
