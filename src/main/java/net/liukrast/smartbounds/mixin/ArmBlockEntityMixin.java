package net.liukrast.smartbounds.mixin;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmBlockEntity.class)
public class ArmBlockEntityMixin {
    @Inject(method = "createRenderBoundingBox", at = @At("RETURN"), cancellable = true)
    private void createRenderBoundingBox(CallbackInfoReturnable<AABB> cir) {
        cir.setReturnValue(cir.getReturnValue().deflate(1, 1, 1).move(0, 1, 0));

    }
}
