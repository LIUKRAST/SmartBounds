package net.liukrast.smartbounds.mixin;

import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBehaviour;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlock;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelBlockEntity;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelPosition;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.stream.Stream;

@Mixin(FactoryPanelBlockEntity.class)
public abstract class FactoryPanelBlockEntityMixin extends SmartBlockEntity {
    @Shadow public EnumMap<FactoryPanelBlock.PanelSlot, FactoryPanelBehaviour> panels;

    public FactoryPanelBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "createRenderBoundingBox", at = @At("HEAD"), cancellable = true)
    private void createRenderBoundingBox(CallbackInfoReturnable<AABB> cir) {
        var that = FactoryPanelBlockEntity.class.cast(this);
        var pos = that.getBlockPos();
        double minX = pos.getX();
        double minY = pos.getY();
        double minZ = pos.getZ();
        double maxX = pos.getX()+1;
        double maxY = pos.getY()+1;
        double maxZ = pos.getZ()+1;
        for(var panel : that.panels.values()) {
            for(BlockPos fPos : Stream.concat(
                    panel.targetedBy.keySet().stream().map(FactoryPanelPosition::pos),
                    panel.targetedByLinks.keySet().stream()
            ).toList()) {
                if(fPos.getX() < minX) minX = fPos.getX();
                if(fPos.getY() < minY) minY = fPos.getY();
                if(fPos.getZ() < minZ) minZ = fPos.getZ();
                if(fPos.getX() > maxX) maxX = fPos.getX()+1;
                if(fPos.getY() > maxY-1) maxY = fPos.getY()+1;
                if(fPos.getZ() > maxZ) maxZ = fPos.getZ()+1;
            }
        }
        cir.setReturnValue(new AABB(minX, minY, minZ, maxX, maxY, maxZ));
    }

    @Unique
    private int smart_bounds$connectionsSize;

    @Inject(method = "read", at = @At("HEAD"))
    private void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        smart_bounds$connectionsSize = 0;
        for(var panel : panels.values()) {
            smart_bounds$connectionsSize+= panel.targetedBy.size();
            smart_bounds$connectionsSize+=panel.targetedByLinks.size();
        }
    }

    @Inject(method = "read", at = @At("TAIL"))
    private void read$1(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket, CallbackInfo ci) {
        int k = 0;
        for(var panel : panels.values()) {
            k+= panel.targetedBy.size();
            k+=panel.targetedByLinks.size();
        }
        if(k != smart_bounds$connectionsSize && level != null && level.isClientSide)
            invalidateRenderBoundingBox();
    }
}
