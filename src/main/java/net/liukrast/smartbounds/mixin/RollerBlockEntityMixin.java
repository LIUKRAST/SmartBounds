package net.liukrast.smartbounds.mixin;

import com.simibubi.create.content.contraptions.actors.roller.RollerBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RollerBlockEntity.class)
public class RollerBlockEntityMixin {
    @Inject(method = "createRenderBoundingBox", at = @At("HEAD"), cancellable = true)
    private void createRenderBoundingBox(CallbackInfoReturnable<AABB> cir) {
        var that = RollerBlockEntity.class.cast(this);
        var n = that.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
        cir.setReturnValue(new AABB(that.getBlockPos()).expandTowards(n.getX(), n.getY()-1, n.getZ()));
    }
}
