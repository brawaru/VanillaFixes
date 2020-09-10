package com.github.brawaru.vanillafixes.fixes.doubledoors;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Door;

public final class DoubleDoorsCommon {
    /**
     * Checks whether block is a door
     *
     * @param block Block to check
     * @return Boolean indicating whether block is a door
     */
    public static boolean isDoor(Block block) {
        return block != null && Tag.DOORS.isTagged(block.getType());
    }

    /**
     * Checks whether two doors are linked together
     *
     * @param usedDoor     Door for which event was called
     * @param relativeDoor Door placed closely
     * @return Boolean indicating whether two doors are linked together
     */
    public static boolean doorsLinked(Door usedDoor, Door relativeDoor) {
        // Material must be equal
        if (!usedDoor.getMaterial().equals(relativeDoor.getMaterial())) {
            return false;
        }

        // Hinges must be unequal (opposite to each other)
        if (usedDoor.getHinge().equals(relativeDoor.getHinge())) {
            return false;
        }

        // Facing must be equal
        if (!usedDoor.getFacing().equals(relativeDoor.getFacing())) {
            return false;
        }

        // Halves must be equal
        return usedDoor.getHalf().equals(relativeDoor.getHalf());
    }

    /**
     * Gets facing for a possible relative door location depending on facing of used door and it's hinge
     *
     * @param usedDoor Door for which event was called
     * @return Facing for a possible relative door location
     */
    public static BlockFace getRelativeFace(Door usedDoor) {
        final boolean isLeftDoor = usedDoor.getHinge().equals(Door.Hinge.LEFT);

        switch (usedDoor.getFacing()) {
            case NORTH: {
                //   S
                // E ↑ W
                //   N
                return isLeftDoor ? BlockFace.EAST : BlockFace.WEST;
            }

            case WEST: {
                //   S
                // E → W
                //   N
                return isLeftDoor ? BlockFace.NORTH : BlockFace.SOUTH;
            }

            case EAST: {
                //   S
                // E ← W
                //   N
                return isLeftDoor ? BlockFace.SOUTH : BlockFace.NORTH;
            }

            case SOUTH: {
                //   S
                // E ↓ W
                //   N
                return isLeftDoor ? BlockFace.WEST : BlockFace.EAST;
            }

            default: {
                // ???
                return null;
            }
        }
    }
}
