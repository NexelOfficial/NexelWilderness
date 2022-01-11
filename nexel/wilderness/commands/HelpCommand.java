package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand {

	private CommandHandler main;

	public HelpCommand(CommandHandler main) {
	    this.main = main;
	}
	
	public boolean helpCommand(Player currentPlayer, String[] args)
	{
		if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.help"))
			return false;

		currentPlayer.sendMessage(main.coloredString("&aAll commands for /wild:"));
		currentPlayer.sendMessage(main.coloredString("&7/wild (The main Wild command.)"));
		currentPlayer.sendMessage(main.coloredString("&7/wild size <size> (Sets the size of the wild region.)"));
		currentPlayer.sendMessage(main.coloredString("&7/wild biome add/remove <biome> <icon> (Add / remove a new biome to the biome picker.)"));
		currentPlayer.sendMessage(main.coloredString("&7/wild blacklist (Add and show blacklisted blocks.)"));
		currentPlayer.sendMessage(main.coloredString("&aThere are so much more options in the config! Customize the plugin to your needs."));
		return true;
	}
}