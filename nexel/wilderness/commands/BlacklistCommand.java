package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckTools;
import nexel.wilderness.tools.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlacklistCommand {
    private final CommandHandler main;

	public BlacklistCommand(CommandHandler main) {
		this.main = main;
	}

    public boolean blacklistCommand(CommandSender sender, String[] args) {
        // Check for permissions
        if (!main.hasPermission(sender, "nexelwilderness.admin.blacklist")) {
			return false;
		}

        String prefix = Messages.prefix;

        // List all blacklisted blocks
        if (args.length == 1) {
            sender.sendMessage(main.coloredString(prefix + "Blacklisted blocks:"));

            if (!(main.getConfig().isSet("blacklistedBlocks"))) {
                sender.sendMessage(main.coloredString("&7" + Messages.noBlacklistedBlocks));
                return true;
            }

            if (main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false).size() == 0) {
                sender.sendMessage(main.coloredString("&7" + Messages.noBlacklistedBlocks));
                return true;
            }

            for (String block : main.getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + block));
			}

            sender.sendMessage(main.coloredString("&7" + Messages.removeBlacklistedBlock));
            return true;
        }

        if (main.errorCatcher(args.length, 3, "/wild blacklist add/remove <block>", sender)) {
			return false;
		}

        // Declare variables
        boolean shouldAdd = args[1].equalsIgnoreCase("add");
        boolean shouldRemove = args[1].equalsIgnoreCase("remove");

        String material = args[2].toUpperCase();

        if (shouldAdd) {
			// Add a new blacklist block
            if (!CheckTools.materialExists(material)) {
                sender.sendMessage(main.coloredString(prefix + Messages.blockDoesntExist));
                return false;
            }

            addBlacklist(material);
            sender.sendMessage(main.coloredString(prefix + Messages.succesfullBlacklist
                    .replace("%blacklistedblock%", material)));
            return true;
        } else if (shouldRemove) {
			// Remove a blacklist block
            if (!CheckTools.materialExists(material)) {
                sender.sendMessage(main.coloredString(prefix + Messages.blockDoesntExist));
                return false;
            }

            removeBlacklist(material);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + Messages.removedFromBlacklist
                    .replace("%removedblock%", material)));
            return true;
        } else {
            sender.sendMessage(main.coloredString(prefix + Messages.insufficientDetails
                    .replace("%usage%", "/wild blacklist add/remove <block>")));
            return true;
        }
    }

    private void addBlacklist(String material) {
        main.getConfig().set("blacklistedBlocks." + material, 1);
        main.saveConfig();
    }

    private void removeBlacklist(String material) {
        main.getConfig().set("blacklistedBlocks." + material, null);
        main.saveConfig();
    }
}