package dev.thomaszwarts.betterjukebox.mixins;

import dev.thomaszwarts.betterjukebox.IJukeboxBlockTileEntityMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxBlock.class)
public abstract class JukeboxBlockMixin {

    @Inject(at = @At("HEAD"), method = "getAnalogOutputSignal(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)I", cancellable = true)
    public void getAnalogOutputSignal(BlockState blockState, World world, BlockPos blockPos, CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof JukeboxTileEntity) {
            IJukeboxBlockTileEntityMixin jukeboxTileEntity = ((IJukeboxBlockTileEntityMixin) tileEntity);
            if (jukeboxTileEntity.getIsPlaying()) {
                if (jukeboxTileEntity.getPlayTime() >= jukeboxTileEntity.getMaxPlayTime()) {
                    jukeboxTileEntity.reset();
                    cir.setReturnValue(0);
                    return;
                }
                cir.setReturnValue(15);
                return;
            } else {
                cir.setReturnValue(0);
                return;
            }
        }
        cir.setReturnValue(0);
    }
}
