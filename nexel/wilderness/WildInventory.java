package nexel.wilderness;

import java.util.ArrayList;
import java.util.Set;

import nexel.wilderness.tools.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WildInventory {
	private final CommandHandler main;
	private final Player holder;

	public Inventory inventory;

	public WildInventory(CommandHandler main, Player holder) {
		this.main = main;
		this.holder = holder;
	}

	public void openBiomePicker() {
		// Create new inventory and clear it
		inventory = Bukkit.createInventory(null, 36, Messages.menuprefix);
		clearInventory(inventory);

		// Add all biomes to the inventory
		if (main.getConfig().isSet("biomes")) {
			ConfigurationSection biomes = main.getConfig().getConfigurationSection("biomes");
			assert biomes != null;
			Set<String> keys = biomes.getKeys(false);

			int i = 0;
			for (String biome : keys) {
				String biomeItemString = main.getConfig().getString("biomes." + biome);

				assert biomeItemString != null;
				ItemStack biomeItem = new ItemStack(getMaterialFromString(biomeItemString));
				ArrayList<String> biomeList = (ArrayList<String>) main.getConfig().getStringList("mainText.biome");

				String biomeCapital = main.capitalBiome(biome).replace("_", " ");
				biomeList.set(0, biomeList.get(0).replace("%biome%", biomeCapital));

				inventory.setItem(i, newItem(biomeItem, biomeList));
				i++;
			}
		} else {
			// No biomes were found
			addItemToInventory(0, "noBiomes", "BARRIER");
		}

		// Back button
		addItemToInventory(27, "backClose", "ARROW");
		holder.openInventory(inventory);
	}

	public void openMainMenu() {
		// Set the inventory to a new one.
		inventory = Bukkit.createInventory(null, 27, Messages.menuprefix);
		clearInventory(inventory);

		if (!Messages.biomePicker) {
			addItemToInventory(13, "randomBiome", "DIAMOND");
		} else {
			addItemToInventory(12, "biomePicker", "GRASS");
			addItemToInventory(14, "randomBiome", "DIAMOND");
		}

		// Back button
		addItemToInventory(18, "backClose", "ARROW");

		// Help button
		if (holder.hasPermission("nexelwilderness.admin.help") || holder.hasPermission("nexelwilderness.admin.*")) {
			addItemToInventory(26, "helpCommand", "BOOK");
		}

		holder.openInventory(inventory);
	}

	private void clearInventory(Inventory inventory) {
		int inventorySize = this.inventory.getContents().length;
		ItemStack emptyItem = new ItemStack(Material.AIR);

		for (int i = 0; i < inventorySize; i++) {
			this.inventory.setItem(i, emptyItem);
		}
	}

	private Material getMaterialFromString(String materialString) {
		Material match = Material.matchMaterial(materialString);
		return match != null ? match : Material.matchMaterial("BARRIER");
	}

	public ItemStack newItem(ItemStack item, ArrayList<String> lore) {
		ItemMeta itemMeta = item.getItemMeta();

		lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));

		itemMeta.setDisplayName(lore.get(0));
		lore.set(0, null);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

		return item;
	}

	public void addItemToInventory(int position, String itemName, String defaultIcon) {
		// Get the icon string
		String icon;
		if (main.getConfig().isSet("mainIcons." + itemName)) {
			icon = main.getConfig().getString("mainIcons." + itemName);
		} else {
			icon = defaultIcon;
		}

		ArrayList<String> detailsList = (ArrayList<String>) main.getConfig().getStringList("mainText." + itemName);
		ItemStack newItem = new ItemStack(getMaterialFromString(icon));

		inventory.setItem(position, newItem(newItem, detailsList));
	}
}