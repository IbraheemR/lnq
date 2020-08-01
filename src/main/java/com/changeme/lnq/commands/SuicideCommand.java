package com.changeme.lnq.commands;

import com.changeme.lnq.queue.QueueManger;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class SuicideCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("suicide").requires(SuicideCommand::nonQueueSource).executes(SuicideCommand::handleSuicideCommand));
        });

        // Duplicate command without alias
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("epic").requires(SuicideCommand::nonQueueSource).executes(SuicideCommand::handleSuicideCommand));
        });
    }

    private static boolean nonQueueSource(ServerCommandSource source) {
        try {
            return QueueManger.hasPlayer(source.getPlayer());
        } catch (CommandSyntaxException e) {
            return false;
        }
    }

    private static int handleSuicideCommand(CommandContext<ServerCommandSource> context) {
        Entity e = context.getSource().getEntity();

        e.kill();
        context.getSource().sendFeedback(new TranslatableText("commands.kill.success.single", new Object[]{e.getDisplayName()}), true);

        return 1;
    }
}
