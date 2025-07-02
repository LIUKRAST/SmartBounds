package net.liukrast.smartbounds.mixin;

import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {PortableStorageInterfaceBlockEntity.class, DeployerBlockEntity.class})
public abstract class PSIAndDeployerBlockEntitiesMixin extends SmartBlockEntity {
    public PSIAndDeployerBlockEntitiesMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "createRenderBoundingBox", at = @At("HEAD"), cancellable = true)
    private void createRenderBoundingBox(CallbackInfoReturnable<AABB> cir) {
        var n = getBlockState().getValue(BlockStateProperties.FACING).getNormal();
        cir.setReturnValue(new AABB(getBlockPos()).expandTowards(n.getX()*2, n.getY()*2, n.getZ()*2));
    }
}
