package com.changeme.lnq.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class ServerUtil {
    public static float getServerTickTime() {
        return ((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance()).getTickTime();
    }

    public static float getServerTPS() {
        float tickTime = getServerTickTime();
        float tps = 1000/tickTime;

        tps = tps > 20 ? 20: tps;

        return tps;
    }

    public static String getServerTPSString() {
        float tickTime = getServerTickTime();
        float tps = getServerTPS();

        char color;
        if (tps > 15) {
            color = 'a';
        } else if (tps > 10) {
            color = 'e';
        } else {
            color = '4';
        }

        return String.format("§%s%.1fTPS§r (%.2f ms)", color, tps, tickTime);
    }



    public static int getPlayerCount() {
        return ((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance()).getCurrentPlayerCount();
    }

    public static int getMaxPlayerCount() {
        return ((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance()).getMaxPlayerCount();
    }
}
