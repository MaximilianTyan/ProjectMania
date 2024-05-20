package com.zondayland.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LocateBlocksCommand implements Command<ServerCommandSource> {

    private static final int MAX_RADIUS = 1_000;
    private static final int DEFAULT_RADIUS = 100;

    public static void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    ) {
        dispatcher.register(
                literal("locateblock")
                        .requires(commandSource -> commandSource.hasPermissionLevel(PermissionLevels.GAMEMASTER))
                        .then(argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(context -> executes(context, true))
                                .then(argument("radius", IntegerArgumentType.integer(0, MAX_RADIUS))
                                        .executes(context -> executes(context, false))
                                )
                        )
        );
    }

    private static int executes(CommandContext<ServerCommandSource> context, boolean isDefaultDistance) {
        BlockState refBlockState = BlockStateArgumentType.getBlockState(context, "block").getBlockState();
        Identifier refId = Registries.BLOCK.getId(refBlockState.getBlock());

        int radius = DEFAULT_RADIUS;
        if (!isDefaultDistance) {
            radius = IntegerArgumentType.getInteger(context, "radius");
        }

        ServerCommandSource source = context.getSource();
        World world = source.getWorld();
        BlockPos centerPos = BlockPos.ofFloored(source.getPosition());

        int min_dx = centerPos.getX() - (radius / 2);
        int max_dx = centerPos.getX() + (radius / 2);

        int min_dy = max(centerPos.getY() - (radius / 2), world.getBottomY());
        int max_dy = min(centerPos.getY() + (radius / 2), world.getTopY());

        int min_dz = centerPos.getZ() - (radius / 2);
        int max_dz = centerPos.getZ() + (radius / 2);

        source.sendMessage(Text.of("Searching for block %s in radius %d (%d:%d %d:%d %d:%d)".formatted(
                refId,
                radius,
                min_dx,
                max_dx,
                min_dy,
                max_dy,
                min_dz,
                max_dz
        )));

        int blockCount = 0;
        for (int x_pos = min_dx; x_pos < max_dx; x_pos++) {
            for (int z_pos = min_dz; z_pos < max_dz; z_pos++) {
                for (int y_pos = min_dy; y_pos < max_dy; y_pos++) {

                    BlockPos worldPos = new BlockPos(x_pos, y_pos, z_pos);
                    BlockState worldBlockState = world.getBlockState(worldPos);

                    if (worldBlockState == refBlockState) {
                        foundBlock(source, refId, worldPos);
                        blockCount++;
                    }
                }
            }
        }

        if (blockCount == 0) {
            source.sendError(Text.literal("Could not find any block %s in radius %d".formatted(refId, radius)));
            return 0;
        } else {
            source.sendMessage(Text.of("Found %d blocks %s in radius %d".formatted(blockCount, refId, radius)));
            return 1;
        }
    }

    private static void foundBlock(
            ServerCommandSource source, Identifier blockId, BlockPos blockPos
    ) {
        // Inspired by class LocateCommand

        String tpCommand = "/tp @s %d %d %d".formatted(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());

        MutableText coordsText = (MutableText) Text.of(blockPos.toShortString());
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, tpCommand);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("Click to teleport"));
        Style coordsStyle = Style.EMPTY
                .withFormatting(Formatting.AQUA)
                .withClickEvent(clickEvent)
                .withHoverEvent(hoverEvent);
        coordsText.setStyle(coordsStyle);

        MutableText feedback = (MutableText) Text.of(blockId.toString());
        source.sendFeedback(() -> feedback.append(" (").append(coordsText).append(")"), false);
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return 0;
    }

}
