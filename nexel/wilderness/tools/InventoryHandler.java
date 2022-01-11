package nexel.wilderness.tools;

import nexel.wilderness.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryHandler implements Listener{

	private CommandHandler main;

	public InventoryHandler(CommandHandler main){
	    this.main = main;
	}

	private TimeConverter timeConverter = new TimeConverter();
	private CheckObject checkObject = new CheckObject();

	@EventHandler
    public void onInventoryClick(InventoryClickEvent event)
	{
		// Declare variables
		Inventory clickedInventory = event.getInventory();
		Inventory menuInventory = main.inventoryClass.menuInventory;

		if (clickedInventory.getHolder() != checkObject.inventoryHasHolder(menuInventory))
			return;

		if (!event.getView().getTitle().equals(main.getConfig().getString("menuprefix")))
			return;

		event.setCancelled(true);

		if (!isClickedItemValid(event.getCurrentItem()))
			return;

		// Declare more variables
		Player currentPlayer = (Player) event.getWhoClicked();
		String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
		String prefix = main.messages.prefix;

		if (displayName.contains("Random biome"))
		{
			if (!(currentPlayer.hasPermission("nexelwilderness.wild") || currentPlayer.hasPermission("nexelwilderness.*")))
			{
				currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("noPermissions")));
				return;
			}

			int currentCooldown = main.cooldown.getPlayerCooldown(currentPlayer.getUniqueId());

			if (currentCooldown <= 0)
			{
				currentPlayer.closeInventory();
				String delayTime = timeConverter.formatTime(main.getConfig().getInt("teleportDelay"));
				currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("delayedTeleport").replace("%time%", delayTime)));
				main.delayedTeleport.startDelay("randomBiome", currentPlayer, null);
				return;
			}
			else
			{
				String cooldownTime = timeConverter.formatTime(currentCooldown);
				currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("cooldownNotOver").replace("%cooldown%", cooldownTime)));
				currentPlayer.closeInventory();
				return;
			}
		}

		if (displayName.contains("Close"))
		{
			currentPlayer.closeInventory();
			return;
		}

		if (displayName.contains("Pick a biome"))
		{
			main.inventoryClass.biomeChooser(currentPlayer);
			return;
		}

		if (displayName.contains("Back"))
		{
			main.inventoryClass.mainWildMenu(currentPlayer);
			return;
		}

		if (displayName.contains("Use /wild help for more options"))
		{
			currentPlayer.closeInventory();
			currentPlayer.performCommand("wild help");
			return;
 		}

		if (!(currentPlayer.hasPermission("nexelwilderness.biomewild") || currentPlayer.hasPermission("nexelwilderness.*")))
		{
			currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("noPermissions")));
			return;
		}

		int currentCooldown = main.cooldown.getPlayerCooldown(currentPlayer.getUniqueId());

		if (currentCooldown <= 0)
		{
			currentPlayer.closeInventory();
			main.delayedTeleport.startDelay(displayName, currentPlayer, null);
			String delayTime = timeConverter.formatTime(main.getConfig().getInt("teleportDelay"));
			currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("delayedTeleport").replace("%time%", delayTime)));
		}
		else
		{
			currentPlayer.closeInventory();
			String cooldownTime = timeConverter.formatTime(currentCooldown);
			currentPlayer.sendMessage(main.coloredString(prefix + main.getConfig().getString("cooldownNotOver").replace("%cooldown%", cooldownTime)));
		}
	}

	private boolean isClickedItemValid(ItemStack clickedItem)
	{
		if (clickedItem == null)
			return false;

		if (!clickedItem.getItemMeta().hasDisplayName())
			return false;

		return !clickedItem.getItemMeta().getDisplayName().isEmpty();
	}
}