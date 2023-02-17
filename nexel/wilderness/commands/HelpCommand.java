package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand {
    private final CommandHandler main;

    public HelpCommand(CommandHandler main) {
        this.main = main;
    }

    public boolean helpCommand(CommandSender sender, String[] args) {
        if (!main.hasPermission(sender, "nexelwilderness.admin.help")) {
            return false;
        }


        if (args.length < 2) {
            // General help page
            List<String> commands = new ArrayList<String>();
            commands.add("&7/wild (Open the Wild menu)");
            commands.add("&7/wild size <size> (Sets the size of the wild region.)");
            commands.add("&7/wild biome add/remove <biome> <icon> (Add / remove a new biome to the biome picker.)");
            commands.add("&7/wild blacklist (Show all blacklisted blocks.)");
            commands.add("&7/wild blacklist add/remove <block> (Add / remove blacklisted blocks.)");
            commands.add("&7/wild retries <retries> (Set the amount of biome finding attempts.)");

            if (sender instanceof ConsoleCommandSender) {
                commands.remove(0);
                commands.add("&7/wild <player> (Randomly teleport a player.)");
            }

            String message = "&aAll commands for /wild:\n";
            for (String command : commands) {
                message += command + "\n";
            }
            message += "&aUse /wild help <command> to get more help for that command.\nThere are more options in the configuration file. Customize the plugin even further there.";

            main.sendColoredMessage(sender, message);
        } else {
            // More specific help for a command
            if (args[1].equalsIgnoreCase("size")) {
                main.sendColoredMessage(sender, Messages.prefix + "&7The &lsize&7 command is used to set the area in which players can wild. Setting this to 1000, will allow players to wild between -500 and 500.");
            } else if (args[1].equalsIgnoreCase("biome")) {
                main.sendColoredMessage(sender, Messages.prefix + "&7The &lbiome&7 command is used to add more biomes to the biome picker. When adding 'FOREST', biomes like 'BIRCH_FOREST' are also included. You can enable advancedBiomes in the configuration file to disable this behaviour.");
            } else if (args[1].equalsIgnoreCase("blacklist")) {
                main.sendColoredMessage(sender, Messages.prefix + "&7The &lblacklist&7 command is used to blacklist blocks. Players are unable to teleport on top of these blocks. Blocks like 'LAVA', 'WATER' and 'CACTUS' are some good examples.");
            } else if (args[1].equalsIgnoreCase("retries")) {
                main.sendColoredMessage(sender, Messages.prefix + "&7The &lretries&7 command is used to set the amount of retries taken to find the given biome. Lower values will result in not being able to find the biome, but higher values will result in big lag spikes. (Default: 50)");
            }
        }

        return true;
    }
}