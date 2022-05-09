package dev.thomaszwarts.betterjukebox.block;

import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class CapProviderJukeBox implements ICapabilityProvider {
    private final LazyOptional inventoryHolder;

    public CapProviderJukeBox(JukeboxTileEntity jukebox) {
        this.inventoryHolder = LazyOptional.of(() -> new ItemHandlerJukeBox(jukebox));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap.orEmpty(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventoryHolder);
    }
}