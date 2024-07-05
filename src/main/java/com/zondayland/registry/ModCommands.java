package com.zondayland.registry;

import com.zondayland.ZondayLand;
import com.zondayland.commands.BlackjackCommand;
import com.zondayland.commands.CoinMachineCommand;
import com.zondayland.commands.LocateBlocksCommand;
import com.zondayland.commands.VoteLinkCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {

    public static void register() {
        ZondayLand.LOGGER.info("Registering commands");
        CommandRegistrationCallback.EVENT.register(LocateBlocksCommand::register);
        CommandRegistrationCallback.EVENT.register(CoinMachineCommand::register);
        CommandRegistrationCallback.EVENT.register(BlackjackCommand::register);
        CommandRegistrationCallback.EVENT.register(VoteLinkCommand::register);
    }
}
