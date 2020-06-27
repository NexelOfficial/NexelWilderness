package nexel.wilderness.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import nexel.wilderness.CommandHandler;

public class BiomeCommand {

	private CommandHandler main;
	
	public BiomeCommand(CommandHandler main) {
	    this.main = main;
	}
	
	public boolean biomeCommand(Player currentPlayer, String[] args) {
		
		if (!(currentPlayer.hasPermission("nexelwilderness.admin.biome") && currentPlayer.hasPermission("nexelwilderness.admin.*"))) 
			return false;
		
		String prefix = main.getConfig().getString("prefix") + "&r ";
		if (main.errorCatcher(args.length, 4, "/wild biome add/remove <biome> <icon>", currentPlayer) == true) 
			return false;
		
		// Checking the biome
		try {
			Material.valueOf(args[3].toUpperCase());
		} catch(IllegalArgumentException ex) { 
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("blockDoesntExist")));
			return false;	
		}
		
		// Checking the block
		try {
			Biome.valueOf(args[2].toUpperCase());
		} catch(IllegalArgumentException ex) { 
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("blockDoesntExist")));
			return false;	
		}
		
		// Checking if the usage is wrong
		if (!(args[1].equalsIgnoreCase("add")) && !(args[1].equalsIgnoreCase("remove"))) {
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("insufficientDetails").replace("%usage%", "/wild biome add/remove <biome> <icon>")));
			return true;
		}
		
		// Add command
		if (args[1].equalsIgnoreCase("add")) {
		    main.getConfig().set("Biomes." + args[2].toUpperCase(), args[3].toUpperCase());
			main.saveConfig();
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("biomeAdded").replace("%biome%", args[2].toUpperCase()).replace("%block%", args[3].toUpperCase())));
			return true;
		} 
		
		// Remove command
		if (args[1].equalsIgnoreCase("remove")) {
		    main.getConfig().set("Biomes." + args[2].toUpperCase(), null);
			main.saveConfig();
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("biomeRemoved").replace("%biome%", args[2].toUpperCase())));
			return true;
		}
		return false;
		
	}
	
}
