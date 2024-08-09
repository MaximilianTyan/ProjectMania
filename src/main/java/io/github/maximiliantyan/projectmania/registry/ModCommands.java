package io.github.maximiliantyan.projectmania.registry;

import io.github.maximiliantyan.projectmania.ModMain;
import io.github.maximiliantyan.projectmania.commands.BlackjackCommand;
import io.github.maximiliantyan.projectmania.commands.CoinMachineCommand;
import io.github.maximiliantyan.projectmania.commands.LocateBlocksCommand;
import io.github.maximiliantyan.projectmania.commands.VoteLinkCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {

    public static void register() {
        ModMain.LOGGER.info("Registering commands");
        CommandRegistrationCallback.EVENT.register(LocateBlocksCommand::register);
        CommandRegistrationCallback.EVENT.register(CoinMachineCommand::register);
        CommandRegistrationCallback.EVENT.register(BlackjackCommand::register);
        CommandRegistrationCallback.EVENT.register(VoteLinkCommand::register);
    }
}
