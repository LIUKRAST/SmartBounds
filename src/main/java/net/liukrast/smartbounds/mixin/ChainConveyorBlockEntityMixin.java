package net.liukrast.smartbounds.mixin;

import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChainConveyorBlockEntity.class)
public class ChainConveyorBlockEntityMixin {
    @Inject(method = "createRenderBoundingBox", at = @At("HEAD"), cancellable = true)
    private void createRenderBoundingBox(CallbackInfoReturnable<AABB> cir) {
        ChainConveyorBlockEntity that = ChainConveyorBlockEntity.class.cast(this);
        var pos = that.getBlockPos();
        double minX = pos.getX() - 1;
        double minY = pos.getY();
        double minZ = pos.getZ() - 1;
        double maxX = pos.getX() + 2;
        double maxY = pos.getY() + 1;
        double maxZ = pos.getZ() + 2;
        for(BlockPos pos1 : that.connections) {
            var fPos = new BlockPos(pos.getX() + pos1.getX(), pos.getY() + pos1.getY(), pos.getZ() + pos1.getZ());
            if(fPos.getX() < minX+1) minX = fPos.getX() - 1;
            if(fPos.getY() < minY) minY = fPos.getY();
            if(fPos.getZ() < minZ+1) minZ = fPos.getZ() - 1;
            if(fPos.getX() > maxX-1) maxX = fPos.getX() + 2;
            if(fPos.getY() > maxY) maxY = fPos.getY() + 1;
            if(fPos.getZ() > maxZ-1) maxZ = fPos.getZ() + 2;
        }
        cir.setReturnValue(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }
}
