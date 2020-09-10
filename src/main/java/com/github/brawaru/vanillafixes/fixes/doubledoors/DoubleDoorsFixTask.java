package com.github.brawaru.vanillafixes.fixes.doubledoors;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.brawaru.vanillafixes.fixes.doubledoors.DoubleDoorsCommon.*;

/**
 * Represents a task to fix linked door
 */
public final class DoubleDoorsFixTask extends BukkitRunnable {
    private final Block block;

    public DoubleDoorsFixTask(Block $door) {
        block = $door;
    }

    @Override
    public void run() {
        Door door = (Door) block.getState().getBlockData();

        BlockFace relativeFace = getRelativeFace(door);

        if (relativeFace == null) return;

        Block relativeBlock = block.getRelative(relativeFace);

        if (!isDoor(relativeBlock)) return;

        BlockData relativeBlockData = relativeBlock.getBlockData();

        Door relativeDoor = (Door) relativeBlockData;

        if (!doorsLinked(door, relativeDoor)) return;

        if (door.isOpen() == relativeDoor.isOpen()) return;

        relativeDoor.setOpen(door.isOpen());
        relativeBlock.setBlockData(relativeBlockData);
    }
}
