package nexel.wilderness.tools.wild;

import java.util.Random;

import nexel.wilderness.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WildRandomBiome {

	private CommandHandler main;

	public WildRandomBiome(CommandHandler main){
	    this.main = main;
	}

	private Random rand = new Random();

	public void normalWild(Player currentPlayer, World currentWorld)
	{
		// Declare variables
		int size = main.getConfig().getInt("size");
		int retries = main.messages.retries;
		int cooldown = main.messages.wildCooldown;

		if (currentWorld == null)
			currentWorld = currentPlayer.getWorld();

		boolean hasBlacklistedBlocks = main.getConfig().isSet("blacklistedBlocks");
		
		loop:
		for (int i = 0; i < retries; i++)
		{
			// Create new wild location
			Location wildLocation = newWildLocation(currentWorld, size);

			// Check if there are blacklisted blocks, and if there are, retry.
			if (hasBlacklistedBlocks)
				for(String blacklistedBlock : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false))
					if (wildLocation.add(0, -1, 0).getBlock().getType() == Material.valueOf(blacklistedBlock))
						continue loop;

			// Teleport player and send the title.
			currentPlayer.teleport(wildLocation.add(0, 2, 0));
			currentPlayer.sendTitle(main.coloredString("&cWilderness"), "Teleported you to " + wildLocation.getBlockX() + ", " + wildLocation.getBlockZ(), 10, 50, 10);

			// Put the cooldown for the player
    		main.cooldown.cooldownTimeForPlayer.put(currentPlayer.getUniqueId(), cooldown);
			return;
		}

		// Ran out of retries, give error.
		String prefix = main.messages.prefix;

		currentPlayer.sendMessage(main.coloredString(prefix + main.messages.noSafeSpot));
		currentPlayer.closeInventory();
		return;
		
	}

	private Location newWildLocation(World world, int size)
	{
		int wildX = (rand.nextInt(size + 1) - size / 2);
		int wildZ = (rand.nextInt(size + 1) - size / 2);
		int wildY = world.getHighestBlockYAt(wildX, wildZ);

		return new Location(world, wildX, wildY, wildZ);
	}
}
