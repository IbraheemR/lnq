package com.changeme.lnq.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class SuicideCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("suicide").executes(SuicideCommand::handleSuicideCommand));
        });

        // Duplicate command without alias
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("epic").executes(SuicideCommand::handleSuicideCommand));
        });
    }

    private static int handleSuicideCommand(CommandContext<ServerCommandSource> context) {
        Entity e = context.getSource().getEntity();

        e.kill();
        context.getSource().sendFeedback(new TranslatableText("commands.kill.success.single", new Object[]{e.getDisplayName()}), true);

        return 1;
    }
}
