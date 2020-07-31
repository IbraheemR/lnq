package com.changeme.lnq.mixin;

import com.changeme.lnq.CustomPlayerList;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class SendPlayerListHeaderOnSpawn {

    @Inject(at=@At("HEAD"), method = "onSpawn")
    public void onSpawn(CallbackInfo ci) {
        CustomPlayerList.sendList();
    }
}
