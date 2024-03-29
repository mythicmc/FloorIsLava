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
package com.gmail.tracebachi.floorislava;

import com.gmail.tracebachi.floorislava.arena.Arena;
import com.gmail.tracebachi.floorislava.commands.FloorBoosterCommand;
import com.gmail.tracebachi.floorislava.commands.FloorCommand;
import com.gmail.tracebachi.floorislava.commands.FloorHoloCommand;
import com.gmail.tracebachi.floorislava.commands.ManageFloorCommand;
import com.gmail.tracebachi.floorislava.gui.FloorGuiMenuListener;
import com.gmail.tracebachi.floorislava.gui.FloorModeVoteMenuListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

/**
 * Created by Trace Bachi (BigBossZee) on 8/20/2015.
 */
public class FloorIsLavaPlugin extends JavaPlugin {

    private static FloorIsLavaPlugin instance;
    private Arena arena;
    private FloorGuiMenuListener guiMenuListener;
    private FloorModeVoteMenuListener voteMenuListener;
    private Economy economy;

    @Override
    public void onLoad() {
        instance = this;
        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists())
            saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        reloadConfig();

        RegisteredServiceProvider<Economy> economyProvider = getServer()
                .getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider == null) {
            getLogger().severe("Economy provider not found! FloorIsLava will not be enabled.");
            return;
        } else {
            economy = economyProvider.getProvider();
        }

        arena = new Arena(this);
        arena.loadConfig(getConfig());
        getServer().getPluginManager().registerEvents(arena, this);
        guiMenuListener = new FloorGuiMenuListener(arena);
        voteMenuListener = new FloorModeVoteMenuListener(arena);
        getServer().getPluginManager().registerEvents(guiMenuListener, this);
        getServer().getPluginManager().registerEvents(voteMenuListener, this);

        Objects.requireNonNull(getCommand("floor"), "floor command not found.")
                .setExecutor(new FloorCommand(arena));
        Objects.requireNonNull(getCommand("floorbooster"), "floorbooster command not found.")
                .setExecutor(new FloorBoosterCommand(arena));
        Objects.requireNonNull(getCommand("floorholo"), "floorholo command not found.")
                .setExecutor(new FloorHoloCommand(arena.getFloorLeaderboard()));
        Objects.requireNonNull(getCommand("mfloor"), "mfloor command not found.")
                .setExecutor(new ManageFloorCommand(this, arena));
    }

    @Override
    public void onDisable() {
        /*
        getCommand("mfloor").setExecutor(null);
        getCommand("floorbooster").setExecutor(null);
        getCommand("floorholo").setExecutor(null);
        getCommand("floor").setExecutor(null);
         */
        guiMenuListener = null;
        arena.forceStop(Bukkit.getConsoleSender(), false);
        arena.getFloorLeaderboard().save();
        arena.getFloorLeaderboard().clear();
    }

    public Economy getEconomy() {
        return economy;
    }

    public static FloorIsLavaPlugin getInstance() {
        return instance;
    }
}

/* TODO
[S▪G]|[Admin]|[Zorioux] Ast I got 4 ideas for floor
[S▪G]|[Admin]|[Zorioux] 2 perks, 1. one that teleport you randomly to existing block away from your location
[S▪G]|[Admin]|[Zorioux] 2. fake tnt, to distract people
[S▪G]|[Admin]|[Zorioux] 3d withering for blocks
[S▪G]|[Admin]|[Zorioux] and if possible to detect who dropped who
[S▪G]|[Admin]|[Zorioux] I guess 3d withering could be the most useful
 */
