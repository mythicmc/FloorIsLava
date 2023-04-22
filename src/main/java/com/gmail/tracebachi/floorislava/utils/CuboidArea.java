/*
 * This file is part of FloorIsLava.
 *
 * FloorIsLava is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FloorIsLava is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FloorIsLava.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gmail.tracebachi.floorislava.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 8/26/16.
 */
public class CuboidArea {

    private static final Random RANDOM = new Random();

    private Point upper;
    private Point lower;
    private List<Location> savedSafeBlocks = new ArrayList<>();
    private List<Location> safeBlocks = new ArrayList<>();

    public CuboidArea(ConfigurationSection alpha, ConfigurationSection beta, String worldName, boolean addSafeBlocks) {
        Preconditions.checkNotNull(alpha, "Point was null.");
        Preconditions.checkNotNull(beta, "Point was null.");

        // Add as class property?
        World world = Bukkit.getWorld(worldName);

        int alphaX = alpha.getInt("x");
        int alphaY = alpha.getInt("y");
        int alphaZ = alpha.getInt("z");
        int betaX = beta.getInt("x");
        int betaY = beta.getInt("y");
        int betaZ = beta.getInt("z");

        int highestX = Math.max(alphaX, betaX);
        int highestY = Math.max(alphaY, betaY);
        int highestZ = Math.max(alphaZ, betaZ);

        int lowestX = Math.min(alphaX, betaX);
        int lowestY = Math.min(alphaY, betaY);
        int lowestZ = Math.min(alphaZ, betaZ);

        upper = new Point(highestX, highestY, highestZ);
        lower = new Point(lowestX, lowestY, lowestZ);

        if (addSafeBlocks) {
            for (int x = lowestX; x <= highestX; x++) {
                for (int z = lowestZ; z <= highestZ; z++ ) {
                    for (int y = highestY; y >= lowestY; y--) {
                        if (!world.getBlockAt(x, y, z).getType().isSolid()) {
                            continue;
                        }

                        savedSafeBlocks.add(new Location(world, x, y ,z));
                        safeBlocks.add(new Location(world, x, y, z));
                        break;
                    }
                }
            }
        }
    }

    public Point getUpper() {
        return upper;
    }

    public Point getLower() {
        return lower;
    }

    public boolean isInside(Location loc) {
        return isInside(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public boolean isInside(int locX, int locY, int locZ) {
        if (locY > upper.y())
            return false;
        if (locY < lower.y())
            return false;

        if (locX > upper.x())
            return false;
        if (locX < lower.x())
            return false;

        if (locZ > upper.z())
            return false;
        if (locZ < lower.z())
            return false;
        return true;
    }

    public Location getRandomLocationInside(World world) {
        int randomX;
        int randomZ;

        if (upper.x() == lower.x()) {
            randomX = upper.x();
        } else {
            randomX = lower.x() + 1 + RANDOM.nextInt(upper.x() - lower.x() - 1);
        }

        if (upper.z() == lower.z()) {
            randomZ = upper.z();
        } else {
            randomZ = lower.z() + 1 + RANDOM.nextInt(upper.z() - lower.z() - 1);
        }

        return new Location(
                world,
                randomX + 0.5,
                upper.y() - 1,
                randomZ + 0.5
        );
    }

    public Location getRandomSafeLocationInside() {
        if (safeBlocks.size() == 0) {
            return null;
        }

        return safeBlocks.get(new Random().nextInt(safeBlocks.size()));
    }

    public void removeSafeBlock(Block block, boolean removedByDegradation) {
        safeBlocks.remove(block.getLocation());

        // If the block was removed because the arena shrunk, it won't loop through the blocks below.
        if (removedByDegradation) return;

        // Starting from the block below from the broken block, check if it's solid. If it is, add it to the
        // safe blocks list. If it isn't, don't do anything.
        for (int y = block.getY() - 1; y >= lower.y(); y--) {
            Block blockBelow = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
            if (blockBelow.getType().isSolid()) {
                safeBlocks.add(blockBelow.getLocation());
                break;
            }
        }
    }

    // Use this when an arena ends. Otherwise, blocks broken in a game won't count as safe blocks in next game.
    public void restoreSavedBlocks() {
        safeBlocks = new ArrayList<>(savedSafeBlocks);
    }
}