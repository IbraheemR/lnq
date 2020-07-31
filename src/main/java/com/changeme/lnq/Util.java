package com.changeme.lnq;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class Util {

    public static float getServerTickTime() {
        return ((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance()).getTickTime();
    }

    public static String getServerTPSString() {
        float tickTime = getServerTickTime();
        float tps = 1000/tickTime;

        tps = tps > 20 ? 20: tps;

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

    private static String[] adminUUIDs = {
            "2b320790-fb08-4e62-8d50-aa12a40850b1" // dYdXplusC
    };
}
