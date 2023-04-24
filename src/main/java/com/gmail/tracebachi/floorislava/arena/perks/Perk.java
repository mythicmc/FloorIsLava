package com.gmail.tracebachi.floorislava.arena.perks;

import com.gmail.tracebachi.floorislava.utils.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.gmail.tracebachi.floorislava.utils.Prefixes.BAD;
import static com.gmail.tracebachi.floorislava.utils.Prefixes.GOOD;

public abstract class Perk {

    private long delay;
    private final Map<UUID, Long> playerDelayCache = new HashMap<>();

    public abstract boolean onPerkActivation(PlayerInteractEvent interactEvent, PlayerInteractEntityEvent entityEvent);

    public abstract Material getItem();

    public abstract String getCooldownMessage();

    public abstract boolean isOffensive();

    public void activate(PlayerInteractEvent interactEvent, PlayerInteractEntityEvent entityEvent, Map<String, PlayerState> playersPlaying) {
        long now = System.currentTimeMillis();
        Player player = entityEvent == null ? interactEvent.getPlayer() : entityEvent.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (!playerDelayCache.containsKey(player.getUniqueId()))
            this.playerDelayCache.put(player.getUniqueId(), 0L);

        if (now - playerDelayCache.get(player.getUniqueId()) < delay // At this point, the key ALWAYS exists.
                && this.getCooldownMessage() != null) {
            player.sendMessage(BAD + this.getCooldownMessage());
            return;
        }

        // Consumable anti perk behaviour for non-offensive perks
        if (!isOffensive()) {
            List<Player> candidates = new ArrayList<>();
            // If we're in end-game, we know someone has an anti perk totem
            Player endGameAntiPerkHolder = null;
            for (Map.Entry<String, PlayerState> entry : playersPlaying.entrySet()) {
                Player playingPlayer = Bukkit.getPlayer(entry.getKey());
                if (playingPlayer == null) {
                    continue;
                }

                if (playingPlayer.getInventory().contains(Material.TOTEM_OF_UNDYING)) {
                    endGameAntiPerkHolder = playingPlayer;
                    if (playingPlayer.getLocation().distance(player.getLocation()) <= 4) {
                        candidates.add(playingPlayer);
                    }
                }
            }

            boolean endGameAntiPerk = playersPlaying.size() == 2 && endGameAntiPerkHolder != null;

            // If there's players in range with the anti perk, or we're in the end game stage and
            // someone has an anti perk
            if (!candidates.isEmpty() || endGameAntiPerk) {
                Player chosenPlayer = null;
                if (endGameAntiPerk) chosenPlayer = endGameAntiPerkHolder;
                else {
                    for (Player candidate : candidates) {
                        Location playerLocation = player.getLocation();
                        double closestPlayerDistance = chosenPlayer == null ? Double.MAX_VALUE
                                : chosenPlayer.getLocation().distance(playerLocation);
                        double playerInRangeDistance = candidate.getLocation().distance(playerLocation);
                        if (closestPlayerDistance > playerInRangeDistance) {
                            chosenPlayer = candidate;
                        }
                    }
                }

                // Consume the chosen player's Anti Perk totem
                assert chosenPlayer != null;
                chosenPlayer.sendMessage(GOOD + "Your Consumable Anti Perk just stopped " + player.getName() + "'s perk!");
                chosenPlayer.playSound(chosenPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                for (ItemStack item : chosenPlayer.getInventory()) {
                    if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
                        item.setAmount(item.getAmount() - 1);
                    }
                }

                // Inform the perk user about what happened
                if (endGameAntiPerk) {
                    player.sendMessage(BAD + "Since it's end-game and " + chosenPlayer.getName()
                            + " has a Consumable Anti Perk, it just stopped you!");
                } else {
                    player.sendMessage(BAD + "Looks like " + chosenPlayer.getName()
                            + " has a Consumable Anti Perk and it stopped you because you're near them!");
                }
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                heldItem.setAmount(heldItem.getAmount() - 1);
                return;
            }
        } else if (entityEvent != null && entityEvent.getRightClicked() instanceof Player) {
            // Offensive Anti Perk behaviour for both anti perk totem users and non-perkers
            Player rightClicked = (Player) entityEvent.getRightClicked();
            // TODO: Check if the player is an anti-perker (future feature)
            if (rightClicked.getInventory().contains(Material.TOTEM_OF_UNDYING)) {
                // Inform the perk user about what happened
                // TODO: Different message if the player is an anti-perker
                player.sendMessage(BAD + "Looks like " + rightClicked.getName()
                        + " has a Consumable Anti Perk and your perk failed!");
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                heldItem.setAmount(heldItem.getAmount() - 1);

                // Tell the consumable anti perk owner the good news
                rightClicked.sendMessage(GOOD + "Your Consumable Anti Perk just saved you from " + player.getName()
                        + "'s offensive perk!");
                rightClicked.playSound(rightClicked.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                // Take one anti perk totem
                for (ItemStack item : rightClicked.getInventory()) {
                    if (item.getType() == Material.TOTEM_OF_UNDYING) {
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }
        }


        /*
           interactEvent is null if the perk is activated through a PlayerEntityInteractEvent
           entityEvent is null if the perk is activated through a PlayerEntityInteractEvent
        */
        if (!this.onPerkActivation(interactEvent, entityEvent))
            return;
        if (player.getInventory().getItemInMainHand().getType() != Material.EGG)
            decrementAmountOfItemStack(player.getInventory(), player.getInventory().getItemInMainHand());
        playerDelayCache.put(player.getUniqueId(), now);
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    private void decrementAmountOfItemStack(Inventory inventory, ItemStack itemStack) {
        if (itemStack.getAmount() == 1)
            inventory.remove(itemStack);
        else
            itemStack.setAmount(itemStack.getAmount() - 1);
    }
}
