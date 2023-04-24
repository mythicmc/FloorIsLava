package com.gmail.tracebachi.floorislava.arena.perks;

import com.gmail.tracebachi.floorislava.utils.CuboidArea;
import com.gmail.tracebachi.floorislava.utils.Prefixes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.gmail.tracebachi.floorislava.utils.Prefixes.BAD;

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

        // When no blocks are left.
        if (randomLocation == null) {
            e.getPlayer().sendMessage(Prefixes.BAD + "No solid blocks were found in the arena.");
            return false;
        }

        // Chance of failure (nerf)
        if (Math.random() <= 0.33) {
            e.getPlayer().sendMessage(BAD + "Oh, no! Sounds like your random teleport failed.");
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            return true;
        }

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

    @Override
    public boolean isOffensive() {
        return false;
    }
}
