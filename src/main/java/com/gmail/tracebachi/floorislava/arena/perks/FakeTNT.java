package com.gmail.tracebachi.floorislava.arena.perks;

import com.gmail.tracebachi.floorislava.FloorIsLavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.logging.Level;

public class FakeTNT extends Perk{

    private final FloorIsLavaPlugin plugin;

    public FakeTNT(FloorIsLavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onPerkActivation(PlayerInteractEvent e, PlayerInteractEntityEvent entityEvent) {
        if (e == null)
            return false;
        Bukkit.getLogger().log(Level.SEVERE, "Called!");
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location location = Objects.requireNonNull(e.getClickedBlock(),
                    "If a block was right clicked, why'd this be null?").getLocation();
            TNTPrimed tnt = e.getPlayer().getWorld().spawn(location.add(0, 1, 0), TNTPrimed.class);
            tnt.setMetadata("FIL", new FixedMetadataValue(plugin, "FIL"));
            tnt.setMetadata("FakeTNT", new FixedMetadataValue(plugin, "FIL"));
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            Location location = e.getPlayer().getLocation();
            TNTPrimed tnt = e.getPlayer().getWorld().spawn(location.add(0, 1, 0), TNTPrimed.class);
            tnt.setMetadata("FIL", new FixedMetadataValue(plugin, "FIL"));
            tnt.setMetadata("FakeTNT", new FixedMetadataValue(plugin, true));
            Vector vector = e.getPlayer().getLocation().getDirection();
            vector.add(new Vector(0.0, 0.15, 0.0));
            tnt.setVelocity(vector);
        }
        return true;
    }

    @Override
    public Material getItem() {
        return Material.REDSTONE_LAMP;
    }

    @Override
    public String getCooldownMessage() {
        return "You cannot throw fake TNT yet.";
    }
}
