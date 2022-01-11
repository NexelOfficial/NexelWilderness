package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckObject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SizeCommand
{
	private CommandHandler main;

	public SizeCommand(CommandHandler main) {
	    this.main = main;
	}

	private CheckObject checkObject = new CheckObject();

	public boolean sizeCommand(Player currentPlayer, String[] args)
	{
		// Check of permissions
		if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.size"))
			return false;

		String prefix = main.messages.prefix;

		// Catch errors
		if (main.errorCatcher(args.length, 2, "/wild size <size>", currentPlayer))
			return false;

		// Check if size is a number
		if (!checkObject.isNumber(args[1]))
			return false;

		int size = Integer.parseInt(args[1]);

		setSize(size);
		currentPlayer.sendMessage(main.coloredString(prefix + main.messages.wildSizeSet
				.replace("%wildsize%", size / 2 + ", -" + size / 2)));
		return true;
	}

	private void setSize(int size)
	{
		main.getConfig().set("size", size);
		main.saveConfig();
	}
}
