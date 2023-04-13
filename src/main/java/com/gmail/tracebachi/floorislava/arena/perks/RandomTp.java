package com.gmail.tracebachi.floorislava.arena.perks;

import com.gmail.tracebachi.floorislava.utils.CuboidArea;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class RandomTp extends Perk {

    private final CuboidArea arenaArea;

    public RandomTp(CuboidArea arenaArea) {
        this.arenaArea = arenaArea;
    }

    @Override
    public boolean onPerkActivation(PlayerInteractEvent e, PlayerInteractEntityEvent entityEvent) {
        if (e == null)
            return false;

        Location randomLocation = arenaArea.getRandomSafeLocationInside();
        e.getPlayer().teleport(new Location(randomLocation.getWorld(), randomLocation.getX() + 0.5, randomLocation.getY() + 1, randomLocation.getZ() + 0.5));
        return true;
    }

    @Override
    public Material getItem() {
        return Material.CHORUS_FRUIT;
    }

    @Override
    public String getCooldownMessage() {
        return "You cannot be teleported yet.";
    }
}
