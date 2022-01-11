package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.CheckObject;
import nexel.wilderness.tools.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldWildCommand {

	private CommandHandler main;
	
	public WorldWildCommand(CommandHandler main){
	    this.main = main;
	}

	private TimeConverter timeConverter = new TimeConverter();
	private CheckObject checkObject = new CheckObject();

	public boolean worldWild(Player currentPlayer, String[] args)
	{
		// Permissions check
		if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.worldwild"))
			return false;

		// Catch command error
		if (main.errorCatcher(args.length, 2, "/wild world <world>", currentPlayer))
			return false;

		// Declare variables
		String prefix = main.messages.prefix;

		// Check if world exists
		if (checkObject.worldExists(args[1]))
			return false;

		World world = Bukkit.getWorld(args[1]);

		// Check if player is on cooldown
     	int currentCooldown = main.cooldown.getPlayerCooldown(currentPlayer.getUniqueId());

     	if (currentCooldown <= 0)
     	{
	    	currentPlayer.closeInventory(); 
		    String delayTime = timeConverter.formatTime(main.getConfig().getInt("teleportDelay"));
		    currentPlayer.sendMessage(main.coloredString(prefix + main.messages.delayedTeleport.replace("%time%", delayTime)));
		    main.delayedTeleport.startDelay("randomBiome", currentPlayer, world);
		    return true;
     	}
     	else
     	{
     		String cooldownTime = timeConverter.formatTime(currentCooldown);
			currentPlayer.sendMessage(main.coloredString(prefix + main.messages.cooldownNotOver.replace("%cooldown%", cooldownTime)));
     		currentPlayer.closeInventory();
     		return true;
     	}
	}
}
