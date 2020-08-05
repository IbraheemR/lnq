package com.changeme.lnq.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.Set;

public class OpenInventoryCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("oi").requires(source -> source.hasPermissionLevel(3)).executes(OpenInventoryCommand::showDummyContainer));
        });
    }

    private static int showDummyContainer(CommandContext<ServerCommandSource> context){
        ServerCommandSource source = context.getSource();

        try {
            ServerPlayerEntity player = source.getPlayer();
            player.openHandledScreen(new PlayerInventoryScreenFactory(player, player));
        } catch (CommandSyntaxException e) {
            return 0;
        }

        return 1;
    }

    private static class PlayerInventoryScreenFactory implements NamedScreenHandlerFactory {

        private final ServerPlayerEntity lookingPlayer;
        private final ServerPlayerEntity subjectPlayer;

        public PlayerInventoryScreenFactory(ServerPlayerEntity lookingPlayer, ServerPlayerEntity subjectPlayer) {
            this.lookingPlayer = lookingPlayer;
            this.subjectPlayer = subjectPlayer;
        }

        @Override
        public Text getDisplayName() {
            return this.subjectPlayer.getName();
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            try {
                return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X5, syncId, this.lookingPlayer.inventory, new PlayerInventoryProxy(this.subjectPlayer.inventory), 5);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
    }

    private static class PlayerInventoryProxy implements Inventory {

        private final PlayerInventory playerInventory;

        public PlayerInventoryProxy(PlayerInventory playerInventory) {
            this.playerInventory = playerInventory;
        }

        private boolean isNullSlot(int proxySlot) {
            return proxySlot == 0 || proxySlot == 5 || proxySlot == 6 || proxySlot == 7;
        }

        private int fromProxySlot(int proxySlot) {
            if (proxySlot < 9) { // Armour + shield to top
                return proxySlot + 36;
            } else if (proxySlot < 36) { // Main inv as is
                return proxySlot;
            } else { // Move hotbar at bottom
                return proxySlot - 36;
            }
        }

        @Override
        public int size() {
            return 54;
        }

        @Override
        public boolean isEmpty() {
            return playerInventory.isEmpty();
        }

        @Override
        public ItemStack getStack(int proxySlot) {
            if (isNullSlot(proxySlot)) {
                return ItemStack.EMPTY;
            } else {
                return playerInventory.getStack(fromProxySlot(proxySlot));
            }
        }

        @Override
        public ItemStack removeStack(int proxySlot, int amount) {
            if (isNullSlot(proxySlot)) {
                return ItemStack.EMPTY;
            } else {
                return playerInventory.removeStack(fromProxySlot(proxySlot), amount);
            }
        }

        @Override
        public ItemStack removeStack(int proxySlot) {
            if (isNullSlot(proxySlot)) {
                return ItemStack.EMPTY;
            } else {
                return playerInventory.removeStack(fromProxySlot(proxySlot));
            }
        }

        @Override
        public void setStack(int proxySlot, ItemStack stack) {
            if (!isNullSlot(proxySlot)) {
                playerInventory.setStack(fromProxySlot(proxySlot), stack);
            }
        }

        @Override
        public void markDirty() {
            playerInventory.markDirty();
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

        @Override
        public void clear() {
            playerInventory.clear();
        }

        @Override
        public boolean isValid(int proxySlot, ItemStack stack) {
            return !isNullSlot(fromProxySlot(proxySlot));
        }
    }
}
