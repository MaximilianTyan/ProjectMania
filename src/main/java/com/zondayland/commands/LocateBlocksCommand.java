package com.zondayland.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class LocateBlocksCommand implements Command<CommandSourceStack> {

    private static final int MAX_RADIUS = 1_000;
    private static final int DEFAULT_RADIUS = 100;

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess,
            Commands.CommandSelection commandSelection
    ) {
        // Forced to declare arguments outside cause generics DON'T FUCKING WORK with this builder pattern
        // Makes for cleaner code though (yeah I HATE the builder pattern formatting)

        // Arguments
        LiteralArgumentBuilder<CommandSourceStack> literalArg = LiteralArgumentBuilder.literal("locateblock");
        RequiredArgumentBuilder<CommandSourceStack, ?> blockArg =
                argument("block", BlockStateArgument.block(registryAccess));
        RequiredArgumentBuilder<CommandSourceStack, ?> radiusArg =
                argument("radius", IntegerArgumentType.integer(0, MAX_RADIUS));

        // Structure
        LiteralArgumentBuilder<CommandSourceStack> command =
                literalArg.requires(commandSource -> commandSource.hasPermission(PermissionLevels.GAMEMASTER))
                          .then(blockArg
                                  .executes(context -> executes(context, true))
                                  .then(radiusArg
                                          .executes(context -> executes(context, false))
                                  )
                          );
        dispatcher.register(command);
    }

    private static int executes(CommandContext<CommandSourceStack> context, boolean isDefaultDistance) {
        BlockState refBlockState = BlockStateArgument.getBlock(context, "block").getState();
        ResourceLocation refId = BuiltInRegistries.BLOCK.getKey(refBlockState.getBlock());

        int radius = DEFAULT_RADIUS;
        if (!isDefaultDistance) {
            radius = IntegerArgumentType.getInteger(context, "radius");
        }

        CommandSourceStack source = context.getSource();
        Level world = source.getLevel();
        BlockPos centerPos = BlockPos.containing(source.getPosition());

        int min_dx = centerPos.getX() - (radius / 2);
        int max_dx = centerPos.getX() + (radius / 2);

        int min_dy = max(centerPos.getY() - (radius / 2), world.getMinBuildHeight());
        int max_dy = min(centerPos.getY() + (radius / 2), world.getMaxBuildHeight());

        int min_dz = centerPos.getZ() - (radius / 2);
        int max_dz = centerPos.getZ() + (radius / 2);

        source.sendSystemMessage(Component.literal("Searching for block %s in radius %d (%d:%d %d:%d %d:%d)".formatted(refId,
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
            source.sendFailure(Component.literal("Could not find any block %s in radius %d".formatted(refId,
                    radius
            )));
            return 0;
        } else {
            source.sendSystemMessage(Component.literal("Found %d blocks %s in radius %d".formatted(blockCount,
                    refId,
                    radius
            )));
            return 1;
        }
    }

    /** See original feedback in {@link LocateCommand} */
    private static void foundBlock(
            CommandSourceStack source, ResourceLocation blockId, BlockPos blockPos
    ) {

        String tpCommand = "/tp @s %d %d %d".formatted(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());

        MutableComponent coordsText = (MutableComponent) Component.literal(blockPos.toShortString());
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, tpCommand);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to teleport"));
        Style coordsStyle =
                Style.EMPTY.applyFormat(ChatFormatting.AQUA).withClickEvent(clickEvent).withHoverEvent(hoverEvent);
        coordsText.setStyle(coordsStyle);

        MutableComponent feedback = (MutableComponent) Component.literal(blockId.toString());
        source.sendSuccess(() -> feedback.append(" (").append(coordsText).append(")"), false);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return 0;
    }
}
