package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckTools;
import nexel.wilderness.tools.Messages;
import org.bukkit.entity.Player;

public class BiomeCommand {
    private final CommandHandler main;

	public BiomeCommand(CommandHandler main) {
		this.main = main;
	}

    public boolean biomeCommand(Player currentPlayer, String[] args) {
        // Check if user has permission
        if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.biome")) {
			return false;
		}

        // Catch errors in the command
        if (main.errorCatcher(args.length, 4, "/wild biome add/remove <biome> <icon>", currentPlayer)) {
			return false;
		}

        // Declare variables
        boolean addBiome = args[1].equalsIgnoreCase("add");
        boolean removeBiome = args[1].equalsIgnoreCase("remove");

        String biome = args[2].toUpperCase();
        String material = args[3].toUpperCase();
        String prefix = Messages.prefix;

        // Check if material exists
        if (!CheckTools.materialExists(material)) {
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.blockDoesntExist));
            return false;
        }

        // Checking the biome
        if (CheckTools.biomeExists(biome) != null) {
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.biomeDoesntExist));
            return false;
        }


        if (removeBiome) {
			// Remove command
            removeBiome(biome);
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.biomeRemoved
                    .replace("%biome%", biome)));
            return true;
        } else if (addBiome) {
			// Add command
            addBiome(biome, material);
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.biomeAdded
                    .replace("%biome%", biome)
                    .replace("%block%", material)));
            return true;
        } else {
			// Wrong usage
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.insufficientDetails
                    .replace("%usage%", "/wild biome add/remove <biome> <icon>")));
            return true;
        }
    }

    private void removeBiome(String biome) {
        main.getConfig().set("Biomes." + biome, null);
        main.saveConfig();
    }

    private void addBiome(String biome, String material) {
        main.getConfig().set("Biomes." + biome, material);
        main.saveConfig();
    }
}
