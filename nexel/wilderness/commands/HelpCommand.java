package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

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

        List<String> commands = new ArrayList<>(List.of(
                "&7/wild (Open the Wild menu)",
                "&7/wild size <size> (Sets the size of the wild region.)",
                "&7/wild biome add/remove <biome> <icon> (Add / remove a new biome to the biome picker.)",
                "&7/wild blacklist (Add and show blacklisted blocks.)",
                "&7/wild retries <retries> (Set the amount of biome finding attempts.)"
        ));

        if (sender instanceof ConsoleCommandSender) {
            commands.remove(0);
            commands.add("&7/wild <player> (Randomly teleport a player.)");
        }

        String message = "&aAll commands for /wild:\n";
        for (String command : commands) {
            message += command + "\n";
        }
        message += "&aThere are more options in the config. Customize the plugin to your needs.";

        sender.sendMessage(main.coloredString(message));
        return true;
    }
}