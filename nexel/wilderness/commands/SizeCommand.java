package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckTools;
import nexel.wilderness.tools.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SizeCommand {
    private final CommandHandler main;

	public SizeCommand(CommandHandler main) {
		this.main = main;
	}

    public boolean sizeCommand(CommandSender sender, String[] args) {
        // Check of permissions
        if (!main.hasPermission(sender, "nexelwilderness.admin.size")) {
			return false;
		}

        // Catch errors
        if (main.errorCatcher(args.length, 2, "/wild size <size>", sender)) {
			return false;
		}

        // Check if size is a number
        if (!CheckTools.isNumber(args[1])) {
			return false;
		}

        int size = Integer.parseInt(args[1]);

        setSize(size);
        main.sendColoredMessage(sender, Messages.prefix + Messages.wildSizeSet
                .replace("%wildsize%", size / 2 + ", -" + size / 2));
        return true;
    }

    private void setSize(int size) {
        main.getConfig().set("size", size);
        main.saveConfig();
    }
}
