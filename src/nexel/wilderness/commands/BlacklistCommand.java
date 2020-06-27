package nexel.wilderness.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import nexel.wilderness.CommandHandler;

public class BlacklistCommand {

	private CommandHandler main;
	
	public BlacklistCommand(CommandHandler main) {
	    this.main = main;
	}
	
	public boolean blacklistCommand(Player currentPlayer, String[] args) {
		
		if (!(currentPlayer.hasPermission("nexelwilderness.admin.blacklist") && currentPlayer.hasPermission("nexelwilderness.admin.*"))) 
			return false;
		
		String prefix = main.getConfig().getString("prefix") + "&r ";
		// List all blacklisted blocks
		if (args.length == 1) {
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "Blacklisted blocks:"));
				
			if (!(main.getConfig().isSet("blacklistedBlocks"))) {
				currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + main.getConfig().getString("noBlacklistedBlocks")));
				return true;
			}
			if (main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false).size() == 0) {
				currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + main.getConfig().getString("noBlacklistedBlocks")));
				return true;
			}
			
			for(String block : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) 
				currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + block));
			
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + main.getConfig().getString("removeBlacklistedBlock")));
			return true;
			
		}
		
		// Checking if the usage is wrong
		if (!(args[1].equalsIgnoreCase("add")) && !(args[1].equalsIgnoreCase("remove"))) {
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("insufficientDetails").replace("%usage%", "/wild blacklist add/remove <block>")));
			return true;
		}
		
		// Add a new blacklist block
		if (args[1].equalsIgnoreCase("add")) {
			
			if (main.errorCatcher(args.length, 3, "/wild blacklist add <block>", currentPlayer) == true) return false;
			
			try {
				Material.valueOf(args[2].toUpperCase());
			} catch(IllegalArgumentException ex) { 
				currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("blockDoesntExist")));
				return false;	
			}
			
			main.getConfig().set("blacklistedBlocks." + args[2].toUpperCase(), 1);
			main.saveConfig();
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("succesfullBlacklist").replace("%blacklistedblock%", args[2].toUpperCase())));
			return true;
			
		}
		
		// Remove a blacklist block
		if (args[1].equalsIgnoreCase("remove")) {
			
			if (main.errorCatcher(args.length, 3, "/wild blacklist remove <block>", currentPlayer) == true) return false;
			
			try {
				Material.valueOf(args[2].toUpperCase());
			} catch(IllegalArgumentException ex) { 
				currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("blockDoesntExist")));
				return false;	
			}
			
			main.getConfig().set("blacklistedBlocks." + args[2].toUpperCase(), null);
			main.saveConfig();
			
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("removedFromBlacklist").replace("%removedblock%", args[2].toUpperCase())));
			return true;
			
		}
		return false;
		
	}
	
}
