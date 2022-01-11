package nexel.wilderness.tools;

import java.util.*;

import nexel.wilderness.CommandHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DelayedTeleport implements Listener {

	private final CommandHandler main;
	
	public DelayedTeleport(CommandHandler main){
	    this.main = main;
	}

	private final TimeConverter timeConverter = new TimeConverter();
	private final CheckObject checkObject = new CheckObject();

	private List<UUID> teleportingPlayers = new ArrayList<>();
	BukkitTask countdownStarter;
	
	public void startDelay(String displayName, Player currentPlayer, World world)
	{
		teleportingPlayers.add(currentPlayer.getUniqueId());
		String prefix = main.messages.prefix;

		countdownStarter = new BukkitRunnable()
		{
            @Override
            public void run()
			{
				int currentCooldown = main.cooldown.getPlayerCooldown(currentPlayer.getUniqueId());

				// Check if the player has a cooldown or not
            	if (currentCooldown <= 0)
				{
					// Give teleporting message
					currentPlayer.sendMessage(main.coloredString(prefix + main.messages.teleporting));

					// Wild to random biome
	            	if (displayName.equals("randomBiome"))
					{
						main.wildRandomBiome.normalWild(currentPlayer, world);
					}
	            	// Wild to selected biome
            		else
            		{
            			// Check if biome exists
            			Biome potentialBiome = checkObject.biomeExists(displayName);
						if (potentialBiome == null)
						{
							currentPlayer.sendMessage(main.coloredString(prefix + main.messages.errorTeleporting));
							teleportingPlayers.remove(currentPlayer.getUniqueId());
							return;
						}

						main.wildChosenBiome.biomeWild(potentialBiome, currentPlayer, world);
	            	}

            		// Cancel the task and stop player from teleporting
					teleportingPlayers.remove(currentPlayer.getUniqueId());
					cancel();
            	}
            	// Player has a cooldown, don't wild.
            	else
            	{
					teleportingPlayers.remove(currentPlayer.getUniqueId());
            		String cooldownTime = timeConverter.formatTime(currentCooldown);
            		currentPlayer.sendMessage(main.coloredString(prefix + main.messages.cooldownNotOver
							.replace("%cooldown%", cooldownTime)));
            	}
            }
        }.runTaskLater(main, main.getConfig().getInt("teleportDelay") * 20L);
	}
	
	@EventHandler
	public void onMovement(PlayerMoveEvent event)
	{
		// Get variables
		Player currentPlayer = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();

		// Check if player is teleporting
		if (!teleportingPlayers.contains(currentPlayer.getUniqueId()))
			return;

		// Check if player moves
		if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())
			return;

		// Cancel the teleport, and give player a message.
		String prefix = main.messages.prefix;
		currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("interuptedTeleport")));
		teleportingPlayers.remove(currentPlayer.getUniqueId());
		countdownStarter.cancel();
	}
}
