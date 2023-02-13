package nexel.wilderness.commands;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.tools.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldWildCommand {
    private final CommandHandler main;
    private final CooldownHandler cooldown;
    private final TeleportHandler teleport;

	public WorldWildCommand(CommandHandler main, CooldownHandler cooldown, TeleportHandler teleport) {
		this.main = main;
        this.cooldown = cooldown;
        this.teleport = teleport;
	}

    public boolean worldWild(Player currentPlayer, String[] args) {
        // Permissions check
        if (!main.hasPermission(currentPlayer, "nexelwilderness.admin.worldwild")) {
			return false;
		}

        // Catch command error
        if (main.errorCatcher(args.length, 2, "/wild world <world>", currentPlayer)) {
			return false;
		}

        // Declare variables
        String prefix = Messages.prefix;

        // Check if world exists
        if (CheckTools.worldExists(args[1])) {
			return false;
		}

        World world = Bukkit.getWorld(args[1]);

        // Check if player is on cooldown
        int currentCooldown = cooldown.getCooldown(currentPlayer);

        if (currentCooldown <= 0) {
            currentPlayer.closeInventory();
            String delayTime = TimeConverter.formatTime(main.getConfig().getInt("teleportDelay"));
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.delayedTeleport.replace("%time%", delayTime)));
            teleport.startDelay("randomBiome", currentPlayer, world);
            return true;
        } else {
            String cooldownTime = TimeConverter.formatTime(currentCooldown);
            currentPlayer.sendMessage(main.coloredString(prefix + Messages.cooldownNotOver.replace("%cooldown%", cooldownTime)));
            currentPlayer.closeInventory();
            return true;
        }
    }
}
