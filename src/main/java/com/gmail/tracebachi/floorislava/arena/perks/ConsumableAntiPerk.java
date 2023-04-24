package com.gmail.tracebachi.floorislava.arena.perks;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ConsumableAntiPerk extends Perk {
    @Override
    public boolean onPerkActivation(PlayerInteractEvent interactEvent, PlayerInteractEntityEvent entityEvent) {
        return false;
    }

    @Override
    public Material getItem() {
        return Material.TOTEM_OF_UNDYING;
    }

    @Override
    public String getCooldownMessage() {
        return null; // No cooldown
    }

    @Override
    public boolean isOffensive() {
        return false;
    }
}
