package com.changeme.lnq.queue.mixins;

import com.changeme.lnq.queue.QueueManger;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class QueueServerWorldMixin extends World {

    protected QueueServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    // Remove unnecessary things in queue world tick.
    @Inject(at=@At("HEAD"), method = "tick", cancellable = true)
    private void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (this.getDimension() == QueueManger.QUEUE_WORLD.getDimension()) {
                ci.cancel();

        }
    }
}
