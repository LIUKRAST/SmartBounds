package net.liukrast.smartbounds.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.logistics.packagePort.PackagePortBlockEntity;
import com.simibubi.create.content.logistics.packagePort.frogport.FrogportBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PackagePortBlockEntity.class)
public class PackagePortBlockEntityMixin {
    @WrapWithCondition(method = "read", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/logistics/packagePort/PackagePortBlockEntity;invalidateRenderBoundingBox()V"))
    private boolean read(PackagePortBlockEntity instance) {
        return !(instance instanceof FrogportBlockEntity);
    }
}
