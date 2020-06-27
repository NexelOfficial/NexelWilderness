package nexel.wilderness.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nexel.wilderness.CommandHandler;

public class SizeCommand {

	private CommandHandler main;

	public SizeCommand(CommandHandler main) {
	    this.main = main;
	}
	
	public boolean sizeCommand(Player currentPlayer, String[] args) {
	
		if (!(currentPlayer.hasPermission("nexelwilderness.admin.size") && currentPlayer.hasPermission("nexelwilderness.admin.*"))) 
			return false;
			
		String prefix = main.getConfig().getString("prefix") + "&r ";
		if (main.errorCatcher(args.length, 2, "/wild size <size>", currentPlayer) == true) 
			return false;
		
		main.getConfig().set("size", Integer.parseInt(args[1]));
		main.saveConfig();
		
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.getConfig().getString("wildSizeSet").replace("%wildsize%", Integer.parseInt(args[1])/2 + ", -" + Integer.parseInt(args[1])/2)));
		return true; 
		
	}
	
	
}
