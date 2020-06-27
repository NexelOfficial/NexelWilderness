package nexel.wilderness;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryClass {

	private CommandHandler main;

	public InventoryClass(CommandHandler main){
	    this.main = main;
	}
	
	ItemStack currentItem;
	Inventory mapChooserInventory;
	
	public void biomeChooser(Player currentPlayer) {
		
		mapChooserInventory = Bukkit.createInventory(null, 36, main.getConfig().getString("menuprefix"));
		inventoryClearer(currentPlayer, 36);
		
		String backClose;
		if (main.getConfig().isSet("mainIcons.backClose")) 
			backClose = main.getConfig().getString("mainIcons.backClose");
		else
			backClose = "ARROW";
		
		if (main.getConfig().isSet("Biomes")) {
			int i = 0;
			for (String biome : main.getConfig().getConfigurationSection("Biomes").getKeys(false)) {
				currentItem = new ItemStack(Material.valueOf(main.getConfig().getString("Biomes." + biome)));
				ArrayList<String> biomeList = (ArrayList<String>) main.getConfig().getStringList("mainText.biome");
				String biomeCapital = main.capitalBiome(biome).replace("_", " ");
				biomeList.set(0, biomeList.get(0).replace("%biome%", biomeCapital));
				mapChooserInventory.setItem(i, newItem(currentItem, biomeList));
				i++;
			}
		} else {
			currentItem = new ItemStack(Material.BARRIER);
			ArrayList<String> noItemsList = (ArrayList<String>) main.getConfig().getStringList("mainText.noBiomes");
			mapChooserInventory.setItem(0, newItem(currentItem, noItemsList));
		}
		
		currentItem = new ItemStack(Material.valueOf(backClose));
		ArrayList<String> backCloseList = (ArrayList<String>) main.getConfig().getStringList("mainText.backClose");
		mapChooserInventory.setItem(27, newItem(currentItem, backCloseList));
		currentPlayer.openInventory(mapChooserInventory);
		
	}
	
	public void mainWildMenu(Player currentPlayer) {
		
		String helpCommand;
		if (main.getConfig().isSet("mainIcons.helpCommand")) 
			helpCommand = main.getConfig().getString("mainIcons.helpCommand");
		else
			helpCommand = "BOOK";
		String randomBiome;
		if (main.getConfig().isSet("mainIcons.randomBiome")) 
			randomBiome = main.getConfig().getString("mainIcons.randomBiome");
		else
			randomBiome = "DIAMOND";
		String backClose;
		if (main.getConfig().isSet("mainIcons.backClose")) 
			backClose = main.getConfig().getString("mainIcons.backClose");
		else
			backClose = "ARROW";
		String biomePicker;
		if (main.getConfig().isSet("mainIcons.biomePicker")) 
			biomePicker = main.getConfig().getString("mainIcons.biomePicker");
		else
			biomePicker = "GRASS";
		
		
		ArrayList<String> helpCommandList = (ArrayList<String>) main.getConfig().getStringList("mainText.helpCommand");
		ArrayList<String> randomBiomeList = (ArrayList<String>) main.getConfig().getStringList("mainText.randomBiome");
		ArrayList<String> backCloseList = (ArrayList<String>) main.getConfig().getStringList("mainText.backClose");
		ArrayList<String> biomePickerList = (ArrayList<String>) main.getConfig().getStringList("mainText.biomePicker");
			
		
		mapChooserInventory = Bukkit.createInventory(null, 27, main.getConfig().getString("menuprefix"));
		inventoryClearer(currentPlayer, 27);
		
		currentItem = new ItemStack(Material.valueOf(randomBiome));
		mapChooserInventory.setItem(14, newItem(currentItem, randomBiomeList));
		currentItem = new ItemStack(Material.valueOf(biomePicker));
		mapChooserInventory.setItem(12, newItem(currentItem, biomePickerList));
		currentItem = new ItemStack(Material.valueOf(backClose));
		mapChooserInventory.setItem(18, newItem(currentItem, backCloseList));
		if (currentPlayer.hasPermission("nexelwilderness.admin.help")) {
			currentItem = new ItemStack(Material.valueOf(helpCommand));
			mapChooserInventory.setItem(26, newItem(currentItem, helpCommandList));
		}
		currentPlayer.openInventory(mapChooserInventory);
		
	}
	
	public void inventoryClearer(Player currentPlayer, int length) {
		
		ItemStack emptyItem = new ItemStack(Material.AIR);
		for(int i = 0; i < length; i++) mapChooserInventory.setItem(i, emptyItem);
		
	}
	
	public ItemStack newItem(ItemStack item, ArrayList<String> Lore) {
		 
		ItemMeta itemMeta = item.getItemMeta();
		 
		for (int i = 0; i < Lore.size(); i++)
			Lore.set(i, ChatColor.translateAlternateColorCodes('&', Lore.get(i)));
		
		itemMeta.setDisplayName(Lore.get(0));
		Lore.set(0, null);
	    itemMeta.setLore(Lore);
	    item.setItemMeta(itemMeta);
	     
	    return item;
	     
	}
	
}
