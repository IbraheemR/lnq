package com.changeme.lnq.commands;

import com.changeme.lnq.util.ModUtil;
import com.changeme.lnq.util.ServerUtil;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

public class TPSCommand {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("tps").executes(context -> {
                context.getSource().sendFeedback(new LiteralText("Server Reports: " + ServerUtil.getServerTPSString()), false);
                return 1;
            }));
        });
    }
}
