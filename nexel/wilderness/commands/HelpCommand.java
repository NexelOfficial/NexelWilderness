package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import org.bukkit.entity.Player;

public class HelpCommand {
    private final CommandHandler main;

    public HelpCommand(CommandHandler main) {
        this.main = main;
    }

    public boolean helpCommand(Player currentPlayer, String[] args) {
        if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.help")) {
			return false;
		}

		String[] messages = {
				"&aAll commands for /wild:",
				"&7/wild (The main Wild command.)",
				"&7/wild size <size> (Sets the size of the wild region.)",
				"&7/wild biome add/remove <biome> <icon> (Add / remove a new biome to the biome picker.)",
				"&7/wild blacklist (Add and show blacklisted blocks.)",
				"&aThere are more options in the config. Customize the plugin to your needs."
		};

		for (String message : messages) {
			currentPlayer.sendMessage(main.coloredString(message));
		}

        return true;
    }
}