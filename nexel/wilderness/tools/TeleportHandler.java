package nexel.wilderness.tools;

import java.util.*;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.wild.WildChosenBiome;
import nexel.wilderness.tools.wild.WildRandomBiome;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TeleportHandler implements Listener {

    private final CommandHandler main;
    private final CooldownHandler cooldown;
    private final WildChosenBiome chosenBiome;
    private final WildRandomBiome randomBiome;

    private final List<UUID> teleportingPlayers = new ArrayList<>();
    private BukkitTask countdownStarter;

    public TeleportHandler(CommandHandler main, CooldownHandler cooldown, WildChosenBiome chosenBiome, WildRandomBiome randomBiome) {
        this.main = main;
        this.cooldown = cooldown;
        this.chosenBiome = chosenBiome;
        this.randomBiome = randomBiome;
    }

    public void startDelay(String displayName, Player currentPlayer, World world) {
        teleportingPlayers.add(currentPlayer.getUniqueId());
        String prefix = Messages.prefix;

        countdownStarter = new BukkitRunnable() {
            @Override
            public void run() {
                int currentCooldown = cooldown.getCooldown(currentPlayer);

                // Check if the player has a cooldown or not
                if (currentCooldown <= 0) {
                    // Give teleporting message
                    currentPlayer.sendMessage(main.coloredString(prefix + Messages.teleporting));

                    // Wild to random biome
                    if (displayName.equals("randomBiome")) {
						// Wild to random biome
                        randomBiome.normalWild(currentPlayer, world);
                    } else {
						// Wild to chosen biome
                        // Check if biome exists
                        Biome potentialBiome = CheckTools.biomeExists(displayName);

                        if (potentialBiome == null) {
                            currentPlayer.sendMessage(main.coloredString(prefix + Messages.errorTeleporting));
                            teleportingPlayers.remove(currentPlayer.getUniqueId());
                            return;
                        }

                        chosenBiome.biomeWild(potentialBiome, currentPlayer, world);
                    }

                    // Cancel the task and stop player from teleporting
                    teleportingPlayers.remove(currentPlayer.getUniqueId());
                    cancel();
                } else {
					// Player has a cooldown, don't wild.
                    teleportingPlayers.remove(currentPlayer.getUniqueId());
                    String cooldownTime = TimeConverter.formatTime(currentCooldown);
                    currentPlayer.sendMessage(main.coloredString(prefix + Messages.cooldownNotOver
                            .replace("%cooldown%", cooldownTime)));
                }
            }
        }.runTaskLater(main, main.getConfig().getInt("teleportDelay") * 20L);
    }

    @EventHandler
    public void onMovement(PlayerMoveEvent event) {
        // Get variables
        Player currentPlayer = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        // Check if player is teleporting
        if (!teleportingPlayers.contains(currentPlayer.getUniqueId())) {
            return;
        }

        // Check if player moves atleast a block
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        // Cancel the teleport, and give player a message.
        String prefix = Messages.prefix;
        currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("interuptedTeleport")));
        teleportingPlayers.remove(currentPlayer.getUniqueId());
        countdownStarter.cancel();
    }
}
