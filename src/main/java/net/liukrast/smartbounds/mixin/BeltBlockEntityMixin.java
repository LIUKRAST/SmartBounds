package net.liukrast.smartbounds.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeltBlockEntity.class)
public class BeltBlockEntityMixin {
    @Shadow public int beltLength;

    @Inject(method = "createRenderBoundingBox", at = @At("RETURN"), cancellable = true)
    private void createRenderBoundingBox(CallbackInfoReturnable<AABB> cir) {
        var that = BeltBlockEntity.class.cast(this);
        var l = that.beltLength-1;
        var dir = that.getBlockState().getValue(BeltBlock.HORIZONTAL_FACING);
        var slope = that.getBlockState().getValue(BeltBlock.SLOPE);
        var aabb = new AABB(that.getBlockPos());
        if(slope == BeltSlope.VERTICAL) {
             cir.setReturnValue(aabb.expandTowards(0, dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? l : -l, 0));
             return;
        }
        var n = dir.getNormal();
        cir.setReturnValue(aabb.expandTowards(n.getX()*l, switch (slope) {
            case UPWARD -> l;
            case DOWNWARD -> -l;
            default -> 0;
        }, n.getZ()*l));
    }

    @Unique
    private int smart_bounds$storedLength = 0;

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/belt/BeltBlockEntity;invalidateRenderBoundingBox()V"))
    private boolean tick(BeltBlockEntity instance) {
        if(smart_bounds$storedLength != beltLength) {
            smart_bounds$storedLength = beltLength;
            return true;
        }
        return false;
    }
}
