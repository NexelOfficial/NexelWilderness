package nexel.wilderness.tools.wild;

import java.util.Random;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CooldownHandler;
import nexel.wilderness.tools.Messages;
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

    public void normalWild(Player currentPlayer, World currentWorld) {
        // Declare variables
        int size = main.getConfig().getInt("size");
        int retries = Messages.retries;
        int wildCooldown = Messages.wildCooldown;

        if (currentWorld == null) {
            currentWorld = currentPlayer.getWorld();
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
            currentPlayer.teleport(wildLocation.add(0, 2, 0));
            currentPlayer.sendTitle(main.coloredString("&cWilderness"), "Teleported you to " + wildLocation.getBlockX() + ", " + wildLocation.getBlockZ(), 10, 50, 10);

            // Put the cooldown for the player
            cooldown.setCooldown(currentPlayer, wildCooldown);
            return;
        }

        // Ran out of retries, give error.
        String prefix = Messages.prefix;

        currentPlayer.sendMessage(main.coloredString(prefix + Messages.noSafeSpot));
        currentPlayer.closeInventory();
    }

    private Location newWildLocation(World world, int size) {
        int wildX = (rand.nextInt(size + 1) - size / 2);
        int wildZ = (rand.nextInt(size + 1) - size / 2);
        int wildY = world.getHighestBlockYAt(wildX, wildZ);

        return new Location(world, wildX, wildY, wildZ);
    }
}
