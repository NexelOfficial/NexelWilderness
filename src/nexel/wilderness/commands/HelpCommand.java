package nexel.wilderness.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nexel.wilderness.CommandHandler;

public class HelpCommand {

	private CommandHandler main;

	public HelpCommand(CommandHandler main) {
	    this.main = main;
	}
	
	public boolean helpCommand(Player currentPlayer, String[] args) {
	
		if (!(currentPlayer.hasPermission("nexelwilderness.admin.help") && currentPlayer.hasPermission("nexelwilderness.admin.*"))) 
			return false;
			
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAll commands for /wild:"));
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/wild (The main Wild command.)"));
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/wild size <size> (Sets the size of the wild region.)"));
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/wild biome add/remove <biome> <icon> (Add / remove a new biome to the biome picker.)"));
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7/wild blacklist (Add and show blacklisted blocks.)"));
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aThere are so much more options in the config! Customize the plugin to your needs."));
		return true;
		
	}
	
	
}
