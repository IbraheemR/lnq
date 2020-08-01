package com.changeme.lnq.playerlist;

import com.changeme.lnq.playerlist.mixins.PlayerListHeaderAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.LiteralText;

import static com.changeme.lnq.util.ServerUtil.getIngamePlayerCount;
import static com.changeme.lnq.util.ServerUtil.getMaxPlayerCount;
import static com.changeme.lnq.util.ServerUtil.getServerTPSString;

public class CustomPlayerList {

    static public void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if(server.getTicks() % 200 == 0) { // Every 10 seconds
                sendList();
            }
        });
    }

    static public void sendList() {
        MinecraftDedicatedServer server = ((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance());

        PlayerListHeaderS2CPacket packet = new PlayerListHeaderS2CPacket();

        ((PlayerListHeaderAccessor) packet).setHeader(new LiteralText("§e" + getIngamePlayerCount() + "§r/" + getMaxPlayerCount() + " online"));
        ((PlayerListHeaderAccessor) packet).setFooter(new LiteralText(getServerTPSString()));

         server.getPlayerManager().getPlayerList().forEach(player -> {
            player.networkHandler.sendPacket(packet);
        });
    }
}
