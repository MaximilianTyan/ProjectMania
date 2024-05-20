package com.zondayland.registry;

import com.zondayland.ZondayLand;
import com.zondayland.commands.LocateBlocksCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {

    public static void register() {
        ZondayLand.LOGGER.info("Registering commands");
        CommandRegistrationCallback.EVENT.register(LocateBlocksCommand::register);
    }

}
