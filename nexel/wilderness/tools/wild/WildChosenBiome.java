package nexel.wilderness.tools.wild;

import java.util.Random;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CooldownHandler;
import nexel.wilderness.tools.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class WildChosenBiome {
    private final Random rand = new Random();
    private final CommandHandler main;
    private final CooldownHandler cooldown;

    public WildChosenBiome(CommandHandler main, CooldownHandler cooldown) {
        this.main = main;
        this.cooldown = cooldown;
    }

    public void biomeWild(Biome biome, Player currentPlayer, World currentWorld) {
        // Declare variables
        int size = main.getConfig().getInt("size");
        int retries = Messages.retries;
        int wildCooldown = Messages.wildCooldown;

        if (currentWorld == null) {
			currentWorld = currentPlayer.getWorld();
		}

        boolean advancedBiomes = Messages.advancedBiomes;
        boolean hasBlacklistedBlocks = main.getConfig().isSet("blacklistedBlocks");

        loop:
        for (int i = 0; i < retries; i++) {
            // Create a new wild location and biome
            Location wildLocation = newWildLocation(currentWorld, size);
            Biome randomBiome = getBiomeFromLocation(wildLocation);

            main.getServer().getConsoleSender().sendMessage(randomBiome.name() + ", " + biome.name());

            // If advancedBiomes is on, the biome must match the chosen biome exactly in name. (OCEAN is just OCEAN not DEEP_OCEAN)
            if (!advancedBiomes) {
				if (!(randomBiome.name().contains(biome.name()))) {
					continue;
				}
			} else if (randomBiome != biome) {
                continue;
            }

            // Check if there are blacklisted blocks, and if there are, retry.
            if (hasBlacklistedBlocks) {
				for (String blacklistedBlock : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) {
					if (wildLocation.getBlock().getType() == Material.valueOf(blacklistedBlock)) {
						continue loop;
					}
				}
			}

            // Teleport player and send the title.
            currentPlayer.teleport(wildLocation.add(0, 2, 0));
            currentPlayer.sendTitle(main.coloredString("&cWilderness"), "Teleported you to " + wildLocation.getBlockX() + ", " + wildLocation.getBlockZ(), 10, 50, 10);

            // Put the cooldown for the player
            cooldown.setCooldown(currentPlayer, wildCooldown);
            return;
        }

        // Ran out of retries, give error.
        String prefix = Messages.prefix;

        currentPlayer.sendMessage(main.coloredString(prefix + Messages.noBiomeSpot));
        currentPlayer.closeInventory();
    }

    private Location newWildLocation(World world, int size) {
        int wildX = (rand.nextInt(size + 1) - size / 2);
        int wildZ = (rand.nextInt(size + 1) - size / 2);
        int wildY = world.getHighestBlockYAt(wildX, wildZ);

        return new Location(world, wildX, wildY, wildZ);
    }

    private Biome getBiomeFromLocation(Location biomeLocation) {
        return biomeLocation.getWorld().getBiome(biomeLocation.getBlockX(), biomeLocation.getBlockZ());
    }
}
