package com.changeme.lnq;

import com.changeme.lnq.mixin.PlayerListHeaderAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;

public class CustomPlayerList {

    static public void register() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            MinecraftServer server = world.getServer();

            if(server.getTicks() % 200 == 0) { // Every 10 seconds

                PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket();

                ((PlayerListHeaderAccessor) packet).setHeader(new LiteralText(""));
                ((PlayerListHeaderAccessor) packet).setFooter(
                        new LiteralText("§e" + server.getCurrentPlayerCount() + "§r online\n" + Util.getServerTPSString())
                );

                world.getPlayers().forEach(player -> {
                    player.networkHandler.sendPacket(packet);
                });
            }
        });
    }
}
