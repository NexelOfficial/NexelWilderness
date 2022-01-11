package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;

public class BiomeCommand {

	private CommandHandler main;
	
	public BiomeCommand(CommandHandler main) {
	    this.main = main;
	}

	private CheckObject checkObject = new CheckObject();

	@SuppressWarnings("deprecation")
	public boolean biomeCommand(Player currentPlayer, String[] args)
	{
		// Check if user has permission
		if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.biome"))
			return false;

		// Catch errors in the command
		if (main.errorCatcher(args.length, 4, "/wild biome add/remove <biome> <icon>", currentPlayer))
			return false;

		// Declare variables
		boolean addBiome = args[1].equalsIgnoreCase("add");
		boolean removeBiome = args[1].equalsIgnoreCase("remove");

		String biome = args[2].toUpperCase();
		String material = args[3].toUpperCase();
		String prefix = main.messages.prefix;

		// Check if material exists
		if (!checkObject.materialExists(material))
		{
			currentPlayer.sendMessage(main.coloredString(prefix + main.messages.blockDoesntExist));
			return false;
		}
		
		// Checking the biome
		if (checkObject.biomeExists(biome) != null)
		{
			currentPlayer.sendMessage(main.coloredString(prefix + main.messages.biomeDoesntExist));
			return false;
		}

		// Remove command
		if (removeBiome)
		{
			removeBiome(biome);
			currentPlayer.sendMessage(main.coloredString(prefix + main.messages.biomeRemoved
					.replace("%biome%", biome)));
			return true;
		}
		// Add command
		else if (addBiome)
		{
			addBiome(biome, material);
			currentPlayer.sendMessage(main.coloredString(prefix + main.messages.biomeAdded
					.replace("%biome%", biome)
					.replace("%block%", material)));
			return true;
		}
		// Wrong usage
		else
		{
			currentPlayer.sendMessage(main.coloredString( prefix + main.messages.insufficientDetails
					.replace("%usage%", "/wild biome add/remove <biome> <icon>")));
			return true;
		}
	}

	private void removeBiome(String biome)
	{
		main.getConfig().set("Biomes." + biome, null);
		main.saveConfig();
	}

	private void addBiome(String biome, String material)
	{
		main.getConfig().set("Biomes." + biome, material);
		main.saveConfig();
	}
}
