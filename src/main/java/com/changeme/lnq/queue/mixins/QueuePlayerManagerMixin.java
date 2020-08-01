package com.changeme.lnq.queue.mixins;

import com.changeme.lnq.queue.QueueManger;
import com.changeme.lnq.util.ModUtil;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.GameMode;
import net.minecraft.world.biome.source.BiomeAccess;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerManager.class)
public abstract class QueuePlayerManagerMixin {

    @Shadow @Final private MinecraftServer server;

    @Shadow private int viewDistance;

    @Shadow public abstract int getMaxPlayerCount();

    @Shadow @Final private RegistryTracker.Modifiable registryTracker;

    @Shadow public abstract void sendWorldInfo(ServerPlayerEntity player, ServerWorld world);

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private List<ServerPlayerEntity> players;

    @Shadow @Final private Map<UUID, ServerPlayerEntity> playerMap;


    // On connect, send to queue
    @Inject(at=@At("HEAD"), method="onPlayerConnect", cancellable = true)
    private void onPlayerConnectMixin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if (!QueueManger.hasPlayer(player)) {
            this.addPlayerToQueue(connection, player);
            ci.cancel();
        } else {
            // send to main.
        }


    }

    @Inject(at=@At("HEAD"), method="remove")
    private void removeMixin(ServerPlayerEntity player, CallbackInfo ci) {
        if (QueueManger.hasPlayer(player)) {
//            QueueManger.removePlayer(player);
        }

    }


    private void addPlayerToQueue(ClientConnection connection, ServerPlayerEntity player) {
        ServerWorld queueWorld = QueueManger.QUEUE_WORLD;
        queueWorld.savingDisabled = true; // NOTE: need to move this?

        player.setWorld(queueWorld);
        player.interactionManager.setWorld(queueWorld);
        player.interactionManager.setGameMode(GameMode.SPECTATOR, GameMode.SPECTATOR);

        String playerHost = "local";
        if (connection.getAddress() != null) {
            playerHost = connection.getAddress().toString();
        }
        LOGGER.info("{}[{}] joined queue with entity id {} at ({}, {}, {})", player.getName().getString(), playerHost, player.getEntityId(), player.getX(), player.getY(), player.getZ());

        ServerPlayNetworkHandler serverPlayNetworkHandler = new ServerPlayNetworkHandler(this.server, connection, player);

        serverPlayNetworkHandler.sendPacket(
                new GameJoinS2CPacket(
                        player.getEntityId(),
                        GameMode.SPECTATOR,
                        player.interactionManager.method_30119(),
                        BiomeAccess.hashSeed(queueWorld.getSeed()),
                        false,
                        ModUtil.getDedicatedServer().getWorldRegistryKeys(),
                        this.registryTracker,
                        queueWorld.getDimensionRegistryKey(),
                        queueWorld.getRegistryKey(),
                        this.getMaxPlayerCount(),
                        this.viewDistance,
                        false,
                        false,
                        queueWorld.isDebugWorld(),
                        queueWorld.isFlat()
                )
        );
        serverPlayNetworkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, player));

        queueWorld.onPlayerConnected(player);

        this.players.add(player);
        this.playerMap.put(player.getUuid(), player);

        this.sendWorldInfo(player, queueWorld);

        player.onSpawn();
    }
}
