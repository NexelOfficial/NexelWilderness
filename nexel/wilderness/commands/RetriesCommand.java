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

        boolean advancedBiomes = Messages.advancedBiomes;
        int retries = Integer.parseInt(args[1]);
        String message = Messages.prefix + Messages.retriesSet.replace("%retries%", args[1]);

        if (!advancedBiomes && retries > 100) {
            message += "\n&eWhen not using advanced biomes, setting this value above 100 is unnecessary most of the times and could cause lag spikes.";
        } else if (advancedBiomes && retries > 250){
            message += "\n&eWhen using advanced biomes, setting this value above 250 is unnecessary most of the times and could cause lag spikes.";
        }

        if (retries < 30) {
            message += "\n&eDecreasing this value below 30 will grealy reduce the amount of times the biome will be found.";
        }

        setRetries(retries);
        main.sendColoredMessage(sender, message);
        return true;
    }

    private void setRetries(int retries) {
        main.getConfig().set("retries", retries);
        main.saveConfig();
    }
}