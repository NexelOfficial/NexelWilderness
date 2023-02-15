package nexel.wilderness.tools.wild;

import java.util.Random;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CooldownHandler;
import nexel.wilderness.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;

public class WildChosenBiome {
    private final Random rand = new Random();
    private final CommandHandler main;
    private final CooldownHandler cooldown;

    public WildChosenBiome(CommandHandler main, CooldownHandler cooldown) {
        this.main = main;
        this.cooldown = cooldown;
    }

    public void biomeWild(Biome biome, Player player, World currentWorld) {
        boolean hasBlacklistedBlocks = main.getConfig().isSet("blacklistedBlocks");
        int size = main.getConfig().getInt("size");
        int biomeRetries = Messages.biomeRetries;
        int blacklistRetries = Messages.blacklistRetries;
        int wildCooldown = Messages.wildCooldown;

        if (currentWorld == null) {
            currentWorld = player.getWorld();
        }

        blacklistLoop:
        for (int i = 0; i < blacklistRetries; i++) {
            // Create a new wild location and biome
            Location wildLocation = newWildLocation(currentWorld, size, biome, biomeRetries);

            // Check if there are blacklisted blocks, and if there are, retry.
            if (hasBlacklistedBlocks) {
				for (String blacklistedBlock : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) {
					if (wildLocation.getBlock().getType() == Material.valueOf(blacklistedBlock)) {
						continue blacklistLoop;
					}
				}
			}

            // Teleport player and send the title.
            player.teleport(wildLocation.add(0, 2, 0));
            player.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cWilderness"), "Teleported you to " + wildLocation.getBlockX() + ", " + wildLocation.getBlockZ(), 10, 50, 10);

            // Put the cooldown for the player
            cooldown.setCooldown(player, wildCooldown);
            return;
        }

        // Ran out of retries, give error.
        String prefix = Messages.prefix;

        main.sendColoredMessage(player, prefix + Messages.noBiomeSpot);
        player.closeInventory();
    }

    private Location newWildLocation(World world, int size, Biome biomeToFind, int retries) {
        boolean advancedBiomes = Messages.advancedBiomes;
        int wildX = (rand.nextInt(size + 1) - size / 2);
        int wildZ = (rand.nextInt(size + 1) - size / 2);

        for (int i = 0; i < retries; i++) {
            // Check for WorldBorder and update wildX and wildZ
            if (main.worldBorderFound) {
                BorderData border = Config.Border(world.getName());

                if (border != null) {
                    Location wildLocation;

                    do {
                        // Get random position in WorldBorder
                        int sizeX = border.getRadiusX();
                        int sizeZ = border.getRadiusZ();

                        wildX = (rand.nextInt(sizeX * 2) - sizeX) + (int) border.getX();
                        wildZ = (rand.nextInt(sizeZ * 2) - sizeZ) + (int) border.getZ();

                        wildLocation = new Location(world, wildX, 64, wildZ);
                    } while (!border.insideBorder(wildLocation));
                }
            }

            // If advancedBiomes is on, the biome must match the chosen biome exactly in name. (OCEAN is just OCEAN not DEEP_OCEAN)
            Biome randomBiome = getBiomeFromLocation(new Location(world, wildX, 64, wildZ));

            if (!advancedBiomes) {
                if (randomBiome.name().contains(biomeToFind.name())) {
                    break;
                }
            } else if (randomBiome.name().equals(biomeToFind.name())) {
                break;
            }
        }

        int wildY = world.getHighestBlockYAt(wildX, wildZ);
        return new Location(world, wildX, wildY, wildZ);
    }

    private Biome getBiomeFromLocation(Location biomeLocation) {
        return biomeLocation.getWorld().getBiome(biomeLocation.getBlockX(), biomeLocation.getBlockZ());
    }
}
