package nexel.wilderness.tools;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.WildInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InventoryHandler implements Listener {
    private final HashMap<UUID, WildInventory> inventories = new HashMap<>();
    private final CommandHandler main;
    private final CooldownHandler cooldown;
    private final TeleportHandler teleport;

	public WildInventory getInventory(Player player) {
		UUID uuid = player.getUniqueId();

        inventories.putIfAbsent(uuid, new WildInventory(main, player));
		return inventories.get(uuid);
	}

    public InventoryHandler(CommandHandler main, CooldownHandler cooldown, TeleportHandler teleport) {
        this.main = main;
        this.cooldown = cooldown;
        this.teleport = teleport;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get player's WildInventory
        Player whoClicked = (Player) event.getWhoClicked();
        WildInventory playerInventory = getInventory(whoClicked);

        // Declare variables
        Inventory clickedInventory = event.getInventory();

        if (!clickedInventory.equals(playerInventory.inventory)) {
            return;
        }

        // Prevent taking the item
        event.setCancelled(true);

        if (!isClickedItemValid(event.getCurrentItem())) {
			return;
		}

        // Declare more variables
        Player player = (Player) event.getWhoClicked();
        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();
        String prefix = Messages.prefix;

        if (displayName.contains("Random biome")) {
            if (!(player.hasPermission("nexelwilderness.wild") || player.hasPermission("nexelwilderness.*"))) {
                main.sendColoredMessage(player, prefix + main.getConfig().getString("noPermissions"));
                return;
            }

            int currentCooldown = cooldown.getCooldown(player);

            if (currentCooldown <= 0) {
                player.closeInventory();
                String delayTime = TimeConverter.formatTime(main.getConfig().getInt("teleportDelay"));
                main.sendColoredMessage(player, prefix + Messages.delayedTeleport.replace("%time%", delayTime));
                teleport.startDelay("randomBiome", player, null);
                return;
            } else {
                String cooldownTime = TimeConverter.formatTime(currentCooldown);
                main.sendColoredMessage(player, prefix + Messages.cooldownNotOver.replace("%cooldown%", cooldownTime));
                player.closeInventory();
                return;
            }
        }

        if (displayName.contains("Close")) {
            player.closeInventory();
            return;
        }

        if (displayName.contains("Pick a biome")) {
            playerInventory.openBiomePicker();
            return;
        }

        if (displayName.contains("Back")) {
            playerInventory.openMainMenu();
            return;
        }

        if (displayName.contains("Use /wild help for more options")) {
            player.closeInventory();
            player.performCommand("wild help");
            return;
        }

        if (!(player.hasPermission("nexelwilderness.biomewild") || player.hasPermission("nexelwilderness.*"))) {
            main.sendColoredMessage(player, prefix + main.getConfig().getString("noPermissions"));
            return;
        }

        int currentCooldown = cooldown.getCooldown(player);

        if (currentCooldown <= 0) {
            player.closeInventory();
            teleport.startDelay(displayName, player, null);
            String delayTime = TimeConverter.formatTime(main.getConfig().getInt("teleportDelay"));
            main.sendColoredMessage(player, prefix + Messages.delayedTeleport.replace("%time%", delayTime));
        } else {
            player.closeInventory();
            String cooldownTime = TimeConverter.formatTime(currentCooldown);
            main.sendColoredMessage(player, prefix + Messages.cooldownNotOver.replace("%cooldown%", cooldownTime));
        }
    }

    private boolean isClickedItemValid(ItemStack clickedItem) {
        if (clickedItem == null || !clickedItem.hasItemMeta()) {
			return false;
		}

        if (!clickedItem.getItemMeta().hasDisplayName()) {
			return false;
		}

        return !clickedItem.getItemMeta().getDisplayName().isEmpty();
    }
}