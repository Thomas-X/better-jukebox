package dev.thomaszwarts.betterjukebox.mixins;

import dev.thomaszwarts.betterjukebox.IJukeboxBlockTileEntityMixin;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.JukeboxTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JukeboxTileEntity.class)
public abstract class JukeboxBlockTileEntityMixin implements ITickableTileEntity, IJukeboxBlockTileEntityMixin {
    public boolean isPlaying = false;
    public int maxPlayTime = 0;
    public int playTime = 0;

    @Override
    public void reset() {
        System.out.println("RESET");
        this.isPlaying = false;
        this.maxPlayTime = 0;
        this.playTime = 0;
    }

    public boolean getIsPlaying() {
        System.out.println(String.format("getIsPlaying %s", this.isPlaying));
        return this.isPlaying;
    }

    @Override
    public int getMaxPlayTime() {
        return this.maxPlayTime;
    }

    @Override
    public int getPlayTime() {
        return this.playTime;
    }

    @Shadow
    ItemStack record;

    public void tick() {
        if (this.isPlaying) {
            this.playTime += 50; // 50ms per tick on average (with TPS 20)
            if (this.playTime > this.maxPlayTime) {
                ((JukeboxTileEntity) (Object) this).setChanged();
                // This triggers reset in the JukeboxBlockMixin class inside getAnalogOutputSignal
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "setRecord(Lnet/minecraft/item/ItemStack;)V", cancellable = true)
    private void setRecord(ItemStack itemStack, CallbackInfo info) {
        info.cancel();
        this.record = itemStack;
        this.isPlaying = true;
        String path = itemStack.getItem().getRegistryName().getPath();

        this.maxPlayTime = getMaxPlayTime(path);

        ((JukeboxTileEntity) (Object) this).setChanged();
        System.out.println(path);
    }

    private int getMaxPlayTime(String path) {
        switch (path) {
            case "music_disc_13":
                return 178 * 1000; // s -> ms
            case "music_disc_cat":
                return 185 * 1000;
            case "music_disc_blocks":
                return 345 * 1000;
            case "music_disc_chirp":
                //noinspection DuplicateBranchesInSwitch
                return 185 * 1000;
            case "music_disc_far":
                return 174 * 1000;
            case "music_disc_mall":
                return 197 * 1000;
            case "music_disc_mellohi":
                return 96 * 1000;
            case "music_disc_stal":
                return 150 * 1000;
            case "music_disc_strad":
                return 188 * 1000;
            case "music_disc_ward":
                return 251 * 1000;
            case "music_disc_11":
                return 71 * 1000;
            case "music_disc_wait":
                return 238 * 1000;
            case "music_disc_pigstep":
                return 148 * 1000;
            // TODO: add otherside and 5 when porting to 1.18
            default:
                System.out.println("Music disc which isn't mapped was inserted, going to set maxplaytime to 0. MusicDisc path: " + path);
                return 1;
        }
    }
}
