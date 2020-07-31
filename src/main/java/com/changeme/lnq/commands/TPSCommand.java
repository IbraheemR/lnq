package com.changeme.lnq.commands;

import com.changeme.lnq.Util;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

public class TPSCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("tps").executes(context -> {
                context.getSource().sendFeedback(new LiteralText("Server Reports: " + Util.getServerTPSString()), false);
                return 1;
            }));
        });
    }
}
