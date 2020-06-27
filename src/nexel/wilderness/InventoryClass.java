package nexel.wilderness;

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
				mapChooserInventory.setItem(i, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&a" + main.capitalBiome(biome).replace("_", " ")), ChatColor.translateAlternateColorCodes('&', "&7&oClick to teleport.")));
				i++;
			}
		} else {
			currentItem = new ItemStack(Material.BARRIER);
			mapChooserInventory.setItem(0, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&cNo biomes have been set."), ChatColor.translateAlternateColorCodes('&', "&7&oUse the diamond to wild to a random biome.")));
		}
		
		currentItem = new ItemStack(Material.valueOf(backClose));
		mapChooserInventory.setItem(27, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&cBack"), ChatColor.translateAlternateColorCodes('&', "&7&oClick to go back.")));
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
			
		
		mapChooserInventory = Bukkit.createInventory(null, 27, main.getConfig().getString("menuprefix"));
		inventoryClearer(currentPlayer, 27);
		
		currentItem = new ItemStack(Material.valueOf(randomBiome));
		mapChooserInventory.setItem(14, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&6Random biome"), ChatColor.translateAlternateColorCodes('&', "&7&oClick to teleport.")));
		currentItem = new ItemStack(Material.valueOf(biomePicker));
		mapChooserInventory.setItem(12, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&aPick a biome"), ChatColor.translateAlternateColorCodes('&', "&7&oClick to open biome picker.")));
		currentItem = new ItemStack(Material.valueOf(backClose));
		mapChooserInventory.setItem(18, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&cClose"), ChatColor.translateAlternateColorCodes('&', "&7&oClick to close.")));
		if (currentPlayer.hasPermission("nexelwilderness.admin.help")) {
			currentItem = new ItemStack(Material.valueOf(helpCommand));
			mapChooserInventory.setItem(26, newItem(currentItem, ChatColor.translateAlternateColorCodes('&', "&fUse /wild help for more options"), ChatColor.translateAlternateColorCodes('&', "&7&oClick to close and run /wild help.")));
		}
		currentPlayer.openInventory(mapChooserInventory);
		
	}
	
	public void inventoryClearer(Player currentPlayer, int length) {
		
		ItemStack emptyItem = new ItemStack(Material.AIR);
		for(int i = 0; i < length; i++) mapChooserInventory.setItem(i, emptyItem);
		
	}
	
	public ItemStack newItem(ItemStack item, String Title, String... Lore) {
		 
		ItemMeta itemMeta = item.getItemMeta();
		 
		itemMeta.setDisplayName(Title);
	    itemMeta.setLore(Arrays.asList(Lore));
	    item.setItemMeta(itemMeta);
	     
	    return item;
	     
	}
	
}
