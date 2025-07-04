package net.liukrast.smartbounds.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.logistics.packagePort.frogport.FrogportBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrogportBlockEntity.class)
public class FrogPortBlockEntityMixin {

    @Definition(id = "target", field = "Lcom/simibubi/create/content/logistics/packagePort/frogport/FrogportBlockEntity;target:Lcom/simibubi/create/content/logistics/packagePort/PackagePortTarget;")
    @Expression("this.target != null")
    @ModifyExpressionValue(method = "getRenderBoundingBox", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean getRenderBoundingBox(boolean original) {
        var that = FrogportBlockEntity.class.cast(this);
        return original && that.target.getExactTargetLocation(that, that.getLevel(), that.getBlockPos()) != Vec3.ZERO;
    }
}
