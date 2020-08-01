package com.changeme.lnq.queue;

import com.changeme.lnq.util.ModUtil;
import com.changeme.lnq.util.ServerUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameMode;
import net.minecraft.world.biome.source.BiomeAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class QueueManger {
    public static final ServerWorld QUEUE_WORLD = ModUtil.getDedicatedServer().getWorld(RegistryKey.of(Registry.DIMENSION, new Identifier("lnq", "queue")));

    private static Map<UUID, Entry> players = new HashMap<>();

    public static void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tick();
        });
    }

    @Deprecated
    public static void addPlayer(ClientConnection connection, ServerPlayerEntity player) {
        // TODO: move add logic to here?

        players.put(player.getUuid(), new Entry(connection, player));
    }

    @Deprecated
    public static void removePlayer(ServerPlayerEntity player) {
        // TODO: move add logic to here?
        players.get(player.getUuid()).toRemove = true;
    }

    public static boolean hasPlayer(ServerPlayerEntity player) {
        return QUEUE_WORLD.getPlayers().contains(player);
    }

    public static int getSize() {
        return QUEUE_WORLD.getPlayers().size();
    }

    public static void tick() {
//        QUEUE_WORLD.getPlayers().forEach(player -> {
//            if (players.get(player.getUuid()).toRemove) {
//                //
//            } else {
//                if (ServerUtil.getIngamePlayerCount() < ServerUtil.getMaxPlayerCount()){
//                    ModUtil.getDedicatedServer().getPlayerManager().onPlayerConnect(player);
//                }
//            }
//        });
        players.forEach((uuid, entry) -> {
            if (entry.toRemove) {
                players.remove(uuid);
            } else {
                if (ServerUtil.getIngamePlayerCount() < ServerUtil.getMaxPlayerCount()) {

                } else {
//                    send queue position
                }
            }
        });
    }
    
    private static class Entry {
        public boolean toRemove;
        private final ServerPlayerEntity player;
        private final ClientConnection connection;

        public Entry(ClientConnection connection, ServerPlayerEntity player) {
            this.connection = connection;
            this.player = player;
            this.toRemove = false;
        }
    }
}
