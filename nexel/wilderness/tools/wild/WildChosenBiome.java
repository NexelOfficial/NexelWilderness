package nexel.wilderness.tools.wild;

import java.util.Random;

import nexel.wilderness.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class WildChosenBiome {

	private CommandHandler main;

	public WildChosenBiome(CommandHandler main){
	    this.main = main;
	}

	private Random rand = new Random();

	public void biomeWild(Biome biome, Player currentPlayer, World currentWorld)
	{
		// Declare variables
		int size = main.getConfig().getInt("size");
		int retries = main.messages.retries;
		int cooldown = main.messages.wildCooldown;

		if (currentWorld == null)
			currentWorld = currentPlayer.getWorld();

		boolean advancedBiomes = main.messages.advancedBiomes;
		boolean hasBlacklistedBlocks = main.getConfig().isSet("blacklistedBlocks");

		loop:
		for (int i = 0; i < retries; i++)
		{
			// Create a new wild location and biome
			Location wildLocation = newWildLocation(currentWorld, size);
			Biome randomBiome = getBiomeFromLocation(wildLocation);

			// If advancedBiomes is on, the biome must match the chosen biome exactly in name. (OCEAN is just OCEAN not DEEP_OCEAN)
			if (!advancedBiomes)
				if (!(randomBiome.toString().contains(biome.name())))
					continue;
			else
				if (randomBiome != biome)
					continue;

			// Check if there are blacklisted blocks, and if there are, retry.
			if (hasBlacklistedBlocks)
				for(String blacklistedBlock : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false))
					if (wildLocation.getBlock().getType() == Material.valueOf(blacklistedBlock))
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

		currentPlayer.sendMessage(main.coloredString(prefix + main.messages.noBiomeSpot));
		currentPlayer.closeInventory();
	}

	private Location newWildLocation(World world, int size)
	{
		int wildX = (rand.nextInt(size + 1) - size / 2);
		int wildZ = (rand.nextInt(size + 1) - size / 2);
		int wildY = world.getHighestBlockYAt(wildX, wildZ);

		return new Location(world, wildX, wildY, wildZ);
	}

	private Biome getBiomeFromLocation(Location biomeLocation)
	{
		return biomeLocation.getWorld().getBiome(biomeLocation.getBlockX(), biomeLocation.getBlockZ(), biomeLocation.getBlockZ());
	}
}
