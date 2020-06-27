package nexel.wilderness;



import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import nexel.wilderness.Metrics;
import nexel.wilderness.commands.BiomeCommand;
import nexel.wilderness.commands.BlacklistCommand;
import nexel.wilderness.commands.HelpCommand;
import nexel.wilderness.commands.SizeCommand;


public class CommandHandler extends JavaPlugin implements Listener {
	
	InventoryClass inventoryClass = new InventoryClass(this);
	SizeCommand sizeCommand = new SizeCommand(this);
	BlacklistCommand blacklistCommand = new BlacklistCommand(this);
	BiomeCommand biomeCommand = new BiomeCommand(this);
	HelpCommand helpCommand = new HelpCommand(this);
	
	@Override
    public void onEnable() {	
		
		getServer().getPluginManager().registerEvents(this, this);
		
    	getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Nexel&fWilderness &7> &fNexelWilderness BETA has been enabled!"));
    	getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Nexel&fWilderness &7> &fThis is a Beta build. Things will break!"));
    	getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Nexel&fWilderness &7> &aCreated with &clove &aby Nathan Diepeveen"));
    	
    	saveDefaultConfig();
    	
        Metrics metrics = new Metrics(this, 7969);
    	
	}
	
	@Override
    public void onDisable() {	
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

		if (!(command.getName().equalsIgnoreCase("wild"))) return false;

		Player currentPlayer = Bukkit.getPlayer(sender.getName());
		
		if (args.length == 0) {
			
			if (getConfig().isSet("blacklistedWorlds")) {
				String[] worlds = getConfig().getString("blacklistedWorlds").trim().split(",");
				for (String worldString : worlds) {
					World world = getServer().getWorld(worldString);
					if (currentPlayer.getWorld() == world) {
						String prefix = getConfig().getString("prefix") + "&r ";
						currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("noWildAllowed")));
						return false;						
					}
				}
			}
			inventoryClass.mainWildMenu(currentPlayer);
			return true;
			
		}
		
		if (args[0].equalsIgnoreCase("biome")) {
			
			if (biomeCommand.biomeCommand(currentPlayer, args) == true);
			return true;
			
		}
			
		if (args[0].equalsIgnoreCase("size")) {
			
			if (sizeCommand.sizeCommand(currentPlayer, args) == true);
				return true;
				
		}
		
		if (args[0].equalsIgnoreCase("blacklist")) {
				
			if (blacklistCommand.blacklistCommand(currentPlayer, args) == true);
				return true;	
			
		} 
		if (args[0].equalsIgnoreCase("help")) {
			
			if (helpCommand.helpCommand(currentPlayer, args) == true);
				return true;
			
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			
			if (!(currentPlayer.hasPermission("nexelwilderness.admin.reload") && currentPlayer.hasPermission("nexelwilderness.admin.*"))) 
				return false;
			String prefix = getConfig().getString("prefix") + "&r ";
			reloadConfig();
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("succesfullReload")));
			return true;
			
			
		}
	
		return false;
	
	}
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
		
		 InventoryHolder inventoryHolder = event.getInventory().getHolder();
		 try {
			 if (inventoryHolder != inventoryClass.mapChooserInventory.getHolder()) return;
		 } catch(NullPointerException ex) {
			 return;
		 }
		 if (event.getView().getTitle()!=getConfig().getString("menuprefix")) return;
		 event.setCancelled(true);
		 if (event.getCurrentItem() == null) return;
		 String displayName = null;
		 try {
			 displayName = event.getCurrentItem().getItemMeta().getDisplayName();
		 } catch(NullPointerException ex) { return; }
	     if (displayName==null || displayName==" ") return;
	     Player currentPlayer = (Player) event.getWhoClicked();

	     try {
	    	 Biome.valueOf(ChatColor.stripColor(displayName.toUpperCase().replace(" ", "_")));
	    	 biomeWild(ChatColor.stripColor(displayName.toUpperCase().replace(" ", "_")), currentPlayer);
	    	 currentPlayer.closeInventory(); return;
	     } catch (Exception ex) {

	     } 
	     
	     if (displayName.contains("Random biome")) {
		     normalWild(currentPlayer);
		     currentPlayer.closeInventory(); return;
		 } if (displayName.contains("Close")) {
		     currentPlayer.closeInventory();
		     return;
		 } if (displayName.contains("Pick a biome")) {
			 inventoryClass.biomeChooser(currentPlayer);
			 return;
		 } if (displayName.contains("Back")) {
			 inventoryClass.mainWildMenu(currentPlayer);
		     return;
		 } if (displayName.contains("Use /wild help for more options")) {
		     currentPlayer.closeInventory();
		     currentPlayer.performCommand("wild help"); return;
		 }
		 
	}
	
	public boolean errorCatcher(int argsLength, int argsRequired, String usage, Player currentPlayer) {
		
		String prefix = getConfig().getString("prefix") + "&r ";
		
		if (!(currentPlayer.hasPermission("nexelwilderness.admin"))) {
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("noPermissions")));
			return true;	
		}
		
		if (argsLength != argsRequired) {
			currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("insufficientDetails").replace("%usage%", usage)));
			return true;	
		}
		return false;
		
	}
	
	
	public void biomeWild(String biome, Player currentPlayer) {
		
		Random randomNumber = new Random(); 

		int configSize = Integer.parseInt(getConfig().getString("size"));
		World currentWorld = currentPlayer.getWorld();
		
		for (int i = 0; i < 200; i++) {
			
			int wildX = (randomNumber.nextInt(configSize + 1)-configSize/2); 
			int wildZ = (randomNumber.nextInt(configSize + 1)-configSize/2); 
			
			@SuppressWarnings("deprecation")
			Biome currentBiome = currentWorld.getBiome(wildX, wildZ);
			if (currentBiome != Biome.valueOf(biome.toUpperCase())) 
				continue;
			
			for (int wildY = 255; wildY > 0; wildY--) {
				
				Location wildBlock = new Location(currentWorld, wildX, wildY, wildZ);
				if (wildBlock.getBlock().getType() == Material.AIR) 
					continue;
					
				if (getConfig().isSet("blacklistedBlocks"))
					for(String blacklistedBlock : getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) {
						if (wildBlock.getBlock().getType() == Material.valueOf(blacklistedBlock)) {
							String prefix = getConfig().getString("prefix") + "&r ";
							currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("noSafeSpot")));
							currentPlayer.closeInventory();
							return;
						}
					}
				
				Location teleportBlock = new Location(currentWorld, wildX, wildY + 2, wildZ);
				currentPlayer.teleport(teleportBlock);
				currentPlayer.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cWilderness"), "Teleported you to " + wildX + ", " + wildZ, 10, 50, 10);
				return;
				
			}
				
		}
		String prefix = getConfig().getString("prefix") + "&r ";
		currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("noBiomeSpot")));
		currentPlayer.closeInventory();
		
	}

	public void normalWild(Player currentPlayer) {
		
		Random randomNumber = new Random(); 

		int configSize = Integer.parseInt(getConfig().getString("size"));
		World currentWorld = currentPlayer.getWorld();
			
		int wildX = (randomNumber.nextInt(configSize + 1)-configSize/2); 
		int wildZ = (randomNumber.nextInt(configSize + 1)-configSize/2); 
		
		for (int wildY = 255; wildY > 0; wildY--) {
			
			Location wildBlock = new Location(currentWorld, wildX, wildY, wildZ);
			if (wildBlock.getBlock().getType() == null || wildBlock.getBlock().getType() == Material.AIR) 
				continue;
				
			if (getConfig().isSet("blacklistedBlocks"))
				for(String blacklistedBlock : getConfig().getConfigurationSection("blacklistedBlocks").getKeys(false)) {
					if (wildBlock.getBlock().getType() == Material.valueOf(blacklistedBlock)) {
						String prefix = getConfig().getString("prefix") + "&r ";
						currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + getConfig().getString("noSafeSpot")));
						currentPlayer.closeInventory();
						return;
					}
				}
			
			Location teleportBlock = new Location(currentWorld, wildX, wildY + 2, wildZ);
			currentPlayer.teleport(teleportBlock);
			currentPlayer.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cWilderness"), "Teleported you to " + wildX + ", " + wildZ, 10, 50, 10);
			return;
			
		}
		
	}
	
	public String capitalBiome(String biomeString) {
	    if(biomeString == null || biomeString.isEmpty()) {
	        return biomeString;
	    }

	    return biomeString.substring(0, 1).toUpperCase() + biomeString.substring(1).toLowerCase();
	}

	
}
