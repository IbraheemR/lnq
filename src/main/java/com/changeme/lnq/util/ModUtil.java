package com.changeme.lnq.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public class ModUtil {
    public static final ModMetadata LNQModMetadata = FabricLoader.getInstance().getModContainer("lnq").get().getMetadata();

    public static MinecraftDedicatedServer getDedicatedServer() {
        return((MinecraftDedicatedServer) FabricLoader.getInstance().getGameInstance());
    }
}
