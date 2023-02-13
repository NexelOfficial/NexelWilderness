package nexel.wilderness.tools.wild;

import java.util.Random;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CooldownHandler;
import nexel.wilderness.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WildRandomBiome {
    private final Random rand = new Random();
    private final CommandHandler main;
    private final CooldownHandler cooldown;

    public WildRandomBiome(CommandHandler main, CooldownHandler cooldown) {
        this.main = main;
        this.cooldown = cooldown;
    }

    public void normalWild(Player player, World currentWorld) {
        // Declare variables
        int size = main.getConfig().getInt("size");
        int retries = Messages.retries;
        int wildCooldown = Messages.wildCooldown;

        if (currentWorld == null) {
            currentWorld = player.getWorld();
        }

        boolean hasBlacklistedBlocks = main.getConfig().isSet("blacklistedBlocks");

        loop:
        for (int i = 0; i < retries; i++) {
            // Create new wild location
            Location wildLocation = newWildLocation(currentWorld, size);

            // Check if there are blacklisted blocks, and if there are, retry.
            if (hasBlacklistedBlocks) {
                for (String blacklistedBlock : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) {
                    if (wildLocation.add(0, -1, 0).getBlock().getType() == Material.valueOf(blacklistedBlock)) {
                        continue loop;
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

        main.sendColoredMessage(player, prefix + Messages.noSafeSpot);
        player.closeInventory();
    }

    private Location newWildLocation(World world, int size) {
        int wildX = (rand.nextInt(size + 1) - size / 2);
        int wildZ = (rand.nextInt(size + 1) - size / 2);

        // Check for WorldBorder and update wildX and wildZ
        if (main.worldBorderFound) {
            BorderData border = Config.Border(world.getName());

            if (border != null)
            {
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

        int wildY = world.getHighestBlockYAt(wildX, wildZ);
        return new Location(world, wildX, wildY, wildZ);
    }
}
