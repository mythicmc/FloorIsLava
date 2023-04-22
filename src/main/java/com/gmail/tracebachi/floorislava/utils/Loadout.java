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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.*;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 2/17/16.
 */
public class Loadout {

    public static final ItemStack FAKE_TNT_ITEM = new ItemStack(Material.REDSTONE_LAMP);
    public static final ItemStack TNT_ITEM = new ItemStack(Material.TNT);
    public static final ItemStack HOOK_ITEM = new ItemStack(Material.TRIPWIRE_HOOK);
    public static final ItemStack WEB_ITEM = new ItemStack(Material.COBWEB);
    public static final ItemStack INVIS_ITEM = new ItemStack(Material.BLAZE_ROD);
    public static final ItemStack BOOST_ITEM = new ItemStack(Material.FEATHER);
    public static final ItemStack CHIKUN_ITEM = new ItemStack(Material.EGG);
    public static final ItemStack STEAL_ITEM = new ItemStack(Material.FLINT_AND_STEEL);
    public static final ItemStack RANDOM_TP_ITEM = new ItemStack(Material.CHORUS_FRUIT);

    static {
        ItemMeta fakeTntMeta = FAKE_TNT_ITEM.getItemMeta();
        if (fakeTntMeta == null)
            throw new NullPointerException("The fake TNT item meta is null for some reasons.");
        fakeTntMeta.setDisplayName(GOLD + "Fake TNT");
        FAKE_TNT_ITEM.setItemMeta(fakeTntMeta);

        ItemMeta tntMeta = TNT_ITEM.getItemMeta();
        if (tntMeta == null)
            throw new NullPointerException("The tnt item meta is null for some reasons.");
        tntMeta.setDisplayName(DARK_RED + "\u2622"
                + GOLD + " Throwing TNT "
                + DARK_RED + "\u2622");
        TNT_ITEM.setItemMeta(tntMeta);

        ItemMeta hookMeta = HOOK_ITEM.getItemMeta();
        if (hookMeta == null)
            throw new NullPointerException("The hook item meta is null for some reasons.");
        hookMeta.setDisplayName(AQUA + "Player Launcher");
        HOOK_ITEM.setItemMeta(hookMeta);

        ItemMeta webMeta = WEB_ITEM.getItemMeta();
        if (webMeta == null)
            throw new NullPointerException("The web item meta is null for some reasons.");
        webMeta.setDisplayName(GREEN + "Webber");
        WEB_ITEM.setItemMeta(webMeta);

        ItemMeta invisMeta = INVIS_ITEM.getItemMeta();
        if (invisMeta == null)
            throw new NullPointerException("The invis item meta is null for some reasons.");
        invisMeta.setDisplayName(GRAY + "Rod of Invisibility");
        INVIS_ITEM.setItemMeta(invisMeta);

        ItemMeta boostMeta = BOOST_ITEM.getItemMeta();
        if (boostMeta == null)
            throw new NullPointerException("The boost item meta is null for some reasons.");
        boostMeta.setDisplayName(YELLOW + "Boost");
        BOOST_ITEM.setItemMeta(boostMeta);

        ItemMeta chikunMeta = CHIKUN_ITEM.getItemMeta();
        if (chikunMeta == null)
            throw new NullPointerException("The chikun item meta is null for some reasons.");
        chikunMeta.setDisplayName(LIGHT_PURPLE + "Chikun Bomb");
        CHIKUN_ITEM.setItemMeta(chikunMeta);

        ItemMeta stealMeta = STEAL_ITEM.getItemMeta();
        if (stealMeta == null)
            throw new NullPointerException("The steal item meta is null for some reasons.");
        stealMeta.setDisplayName(BLUE + "Steal");
        STEAL_ITEM.setItemMeta(stealMeta);

        ItemMeta randomTpMeta = RANDOM_TP_ITEM.getItemMeta();
        if (randomTpMeta == null)
            throw new NullPointerException("The random tp item meta is null for some reasons.");
        randomTpMeta.setDisplayName(DARK_PURPLE + "Random Teleport");
        RANDOM_TP_ITEM.setItemMeta(randomTpMeta);
    }

    public int faketnt;
    public int tnt;
    public int hook;
    public int web;
    public int invis;
    public int boost;
    public int chikun;
    public int steal;
    public int randomTp;

    public Loadout() {
        faketnt = 0;
        tnt = 0;
        hook = 0;
        web = 0;
        invis = 0;
        boost = 0;
        chikun = 0;
        steal = 0;
        randomTp = 0;
    }

    public int countSum() {
        return faketnt + tnt + hook + web + invis + boost + chikun + steal + randomTp;
    }
}
