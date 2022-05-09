package dev.thomaszwarts.betterjukebox;

public interface IJukeboxBlockTileEntityMixin {
    void reset();

    boolean getIsPlaying();

    int getMaxPlayTime();

    int getPlayTime();
}
