package com.changeme.lnq.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class ModUtil {

    public static ModMetadata getLNQModMetadata() {
        return FabricLoader.getInstance().getModContainer("lnq").get().getMetadata();
    }
}
