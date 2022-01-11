package nexel.wilderness;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryClass 
{
	private CommandHandler main;

	public InventoryClass(CommandHandler main){
	    this.main = main;
	}

	public Inventory menuInventory;
	
	public void biomeChooser(Player currentPlayer)
	{
		// Create new inventory and clear it
		menuInventory = Bukkit.createInventory(null, 36, main.messages.menuprefix);
		inventoryClearer(menuInventory);

		// Add all biomes to the inventory
		if (main.getConfig().isSet("Biomes"))
		{
			int i = 0;
			for (String biome : main.getConfig().getConfigurationSection("Biomes").getKeys(false))
			{
				ItemStack biomeItem = new ItemStack(getMaterialCrossVersion(main.getConfig().getString("Biomes." + biome)));
				ArrayList<String> biomeList = (ArrayList<String>) main.getConfig().getStringList("mainText.biome");

				String biomeCapital = main.capitalBiome(biome).replace("_", " ");
				biomeList.set(0, biomeList.get(0).replace("%biome%", biomeCapital));

				menuInventory.setItem(i, newItem(biomeItem, biomeList));
				i++;
			}
		}
		// No biomes were found
		else
			addItemToInventory(0, "noBiomes", "BARRIER");

		// Back button
		addItemToInventory(27, "backClose", "ARROW");

		// Finish up and open the inventory for the player
		currentPlayer.openInventory(menuInventory);
	}
	
	public void mainWildMenu(Player currentPlayer)
	{
		// Set the inventory to a new one.
		menuInventory = Bukkit.createInventory(null, 27, main.messages.menuprefix);
		inventoryClearer(menuInventory);
		
		if (!main.messages.biomePicker)
			addItemToInventory(13, "randomBiome", "DIAMOND");
		else
		{
			addItemToInventory(12, "biomePicker", "GRASS");
			addItemToInventory(14, "randomBiome", "DIAMOND");
		}

		addItemToInventory(18, "backClose", "ARROW");

		if (currentPlayer.hasPermission("nexelwilderness.admin.help") || currentPlayer.hasPermission("nexelwilderness.admin.*"))
			addItemToInventory(26, "helpCommand", "BOOK");

		currentPlayer.openInventory(menuInventory);
	}
	
	public void inventoryClearer(Inventory inventory)
	{
		int inventorySize = menuInventory.getContents().length;
		ItemStack emptyItem = new ItemStack(Material.AIR);

		for(int i = 0; i < inventorySize; i++)
			menuInventory.setItem(i, emptyItem);
	}
	
	@SuppressWarnings("deprecation")
	public Material getMaterialCrossVersion(String materialName)
	{
		if (materialName.contains(":"))
		{
			String[] argsSplit = materialName.toUpperCase().split(":");
			String matName = argsSplit[0];
			int matData = Integer.parseInt(argsSplit[1]);
			return Bukkit.getUnsafe().getMaterial(matName, matData);
		}
		else
			return Material.matchMaterial(materialName);
	}

	public ItemStack newItem(ItemStack item, ArrayList<String> lore)
	{
		ItemMeta itemMeta = item.getItemMeta();

		for (int i = 0; i < lore.size(); i++)
			lore.set(i, main.coloredString(lore.get(i)));

		itemMeta.setDisplayName(lore.get(0));
		lore.set(0, null);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

		return item;
	}

	public void addItemToInventory(int position, String itemName, String defaultIcon)
	{
		// Get the icon string
		String icon;
		if (main.getConfig().isSet("mainIcons." + itemName))
			icon = main.getConfig().getString("mainIcons." + itemName);
		else
			icon = defaultIcon;

		ArrayList<String> detailsList = (ArrayList<String>) main.getConfig().getStringList("mainText." + itemName);
		ItemStack newItem = new ItemStack(getMaterialCrossVersion(icon));

		menuInventory.setItem(position, newItem(newItem, detailsList));
	}
}
