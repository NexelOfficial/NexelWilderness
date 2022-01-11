package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckObject;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;


public class BlacklistCommand
{
	private CommandHandler main;
	
	public BlacklistCommand(CommandHandler main) {
	    this.main = main;
	}

	private CheckObject checkObject = new CheckObject();

	public boolean blacklistCommand(Player currentPlayer, String[] args)
	{
		// Check for permissions
		if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.blacklist"))
			return false;

		String prefix = main.messages.prefix;

		// List all blacklisted blocks
		if (args.length == 1)
		{
			currentPlayer.sendMessage(main.coloredString(prefix + "Blacklisted blocks:"));
				
			if (!(main.getConfig().isSet("blacklistedBlocks")))
			{
				currentPlayer.sendMessage(main.coloredString("&7" + main.messages.noBlacklistedBlocks));
				return true;
			}

			if (main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false).size() == 0)
			{
				currentPlayer.sendMessage(main.coloredString("&7" + main.messages.noBlacklistedBlocks));
				return true;
			}
			
			for(String block : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) 
				currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + block));
			
			currentPlayer.sendMessage(main.coloredString("&7" + main.messages.removeBlacklistedBlock));
			return true;
		}

		if (main.errorCatcher(args.length, 3, "/wild blacklist add/remove <block>", currentPlayer))
			return false;

		// Declare variables
		boolean addbl = args[1].equalsIgnoreCase("add");
		boolean removebl = args[1].equalsIgnoreCase("remove");

		String material = args[2].toUpperCase();

		// Add a new blacklist block
		if (addbl)
		{
			if (!checkObject.materialExists(material))
			{
				currentPlayer.sendMessage(main.coloredString(prefix + main.messages.blockDoesntExist));
				return false;	
			}

			addBlacklist(material);
			currentPlayer.sendMessage(main.coloredString(prefix + main.messages.succesfullBlacklist
					.replace("%blacklistedblock%", material)));
			return true;
		}
		// Remove a blacklist block
		else if (removebl)
		{
			if (!checkObject.materialExists(material))
			{
				currentPlayer.sendMessage(main.coloredString(prefix + main.messages.blockDoesntExist));
				return false;
			}
			
			removeBlacklist(material);
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + main.messages.removedFromBlacklist
					.replace("%removedblock%", material)));
			return true;
		}
		else
		{
			currentPlayer.sendMessage(main.coloredString( prefix + main.messages.insufficientDetails
					.replace("%usage%", "/wild blacklist add/remove <block>")));
			return true;
		}
	}

	private void addBlacklist(String material)
	{
		main.getConfig().set("blacklistedBlocks." + material, 1);
		main.saveConfig();
	}

	private void removeBlacklist(String material)
	{
		main.getConfig().set("blacklistedBlocks." + material, null);
		main.saveConfig();
	}
}