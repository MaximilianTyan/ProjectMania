package io.github.maximiliantyan.projectmania.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.maximiliantyan.projectmania.ModConfig;
import io.github.maximiliantyan.projectmania.ModMain;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;

public class VoteLinkCommand implements Command<CommandSourceStack> {
    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        LiteralArgumentBuilder<CommandSourceStack> command = LiteralArgumentBuilder.literal("vote");
        dispatcher.register(command.executes(VoteLinkCommand::executes));
    }

    private static int executes(CommandContext<CommandSourceStack> objectCommandContext) {
        CommandSourceStack source = objectCommandContext.getSource();
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            ModMain.LOGGER.warn("Vote Command: player is null!");
            return 0;
        }

        MutableComponent urlText = (MutableComponent) Component.literal(ModConfig.voteAddress);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, ModConfig.voteAddress);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to open"));
        urlText.setStyle(Style.EMPTY.applyFormats(ChatFormatting.AQUA, ChatFormatting.UNDERLINE)
                                    .withClickEvent(clickEvent)
                                    .withHoverEvent(hoverEvent));

        source.sendSuccess(() -> Component.literal("Vote here: ").append(urlText), false);
        return 1;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return 0;
    }
}
