package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckTools;
import nexel.wilderness.tools.Messages;
import org.bukkit.command.CommandSender;

public class RetriesCommand {
    private final CommandHandler main;

	public RetriesCommand(CommandHandler main) {
		this.main = main;
	}

    public boolean retriesCommand(CommandSender sender, String[] args) {
        // Check of permissions
        if (!main.hasPermission(sender, "nexelwilderness.admin.retries")) {
			return false;
		}

        // Catch errors
        if (main.errorCatcher(args.length, 2, "/wild retries <retries>", sender)) {
			return false;
		}

        // Check if size is a number
        if (!CheckTools.isNumber(args[1])) {
			return false;
		}

        int retries = Integer.parseInt(args[1]);
        String message = Messages.prefix + Messages.retriesSet.replace("%retries%", args[1]);

        if (retries > 20) {
            message += "\n&eIncreasing this value beyond 20 when your world is not pre-loaded could cause serious lag spikes.";
        }

        if (retries < 10) {
            message += "\n&eDecreasing this value below 10 will grealy reduce the amount of times the biome will be found.";
        }

        setRetries(retries);
        sender.sendMessage(main.coloredString(message));
        return true;
    }

    private void setRetries(int retries) {
        main.getConfig().set("retries", retries);
        main.saveConfig();
    }
}
