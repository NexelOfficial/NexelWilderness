package nexel.wilderness;




import nexel.wilderness.tools.*;
import nexel.wilderness.tools.wild.WildChosenBiome;
import nexel.wilderness.tools.wild.WildRandomBiome;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import nexel.wilderness.commands.BiomeCommand;
import nexel.wilderness.commands.BlacklistCommand;
import nexel.wilderness.commands.HelpCommand;
import nexel.wilderness.commands.SizeCommand;
import nexel.wilderness.commands.WorldWildCommand;

import java.util.Locale;


public class CommandHandler extends JavaPlugin {

	public FileConfiguration config = null;
	public InventoryClass inventoryClass = new InventoryClass(this);
	public SizeCommand sizeCommand = new SizeCommand(this);
	public BlacklistCommand blacklistCommand = new BlacklistCommand(this);
	public BiomeCommand biomeCommand = new BiomeCommand(this);
	public HelpCommand helpCommand = new HelpCommand(this);
	public Cooldown cooldown = new Cooldown(this);
	public WildRandomBiome wildRandomBiome = new WildRandomBiome(this);
	public WildChosenBiome wildChosenBiome = new WildChosenBiome(this);
	public DelayedTeleport delayedTeleport = new DelayedTeleport(this);
	public InventoryHandler inventoryHandler = new InventoryHandler(this);
	public WorldWildCommand worldWildCommand = new WorldWildCommand(this);
	public Messages messages = new Messages(this);

	private TimeConverter timeConverter = new TimeConverter();
	private CheckObject checkObject = new CheckObject();

	@Override
    public void onEnable()
	{
		config = getConfig();
		getServer().getPluginManager().registerEvents(inventoryHandler, this);
		getServer().getPluginManager().registerEvents(delayedTeleport, this);

    	getServer().getConsoleSender().sendMessage(coloredString("&5Nexel&fWilderness &7> &fNexelWilderness has been enabled!"));
    	getServer().getConsoleSender().sendMessage(coloredString("&5Nexel&fWilderness &7> &aCreated with &clove &aby Nexel"));
    	saveDefaultConfig();
    	cooldown.secondTimer();
    	
        new Metrics(this, 7969);
        messages.init();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args)
	{
		if (sender instanceof ConsoleCommandSender)
		{
			getServer().getConsoleSender().sendMessage(coloredString("&5Nexel&fWilderness &7> &cConsole commands are not supported yet. :("));
			return false;
		}

		if (command.getName().equalsIgnoreCase("wild")) {

			Player player = Bukkit.getPlayer(sender.getName());

			// Check if a command has been given
			if (args.length == 0)
			{
				if (config.isSet("blacklistedWorlds"))
				{
					String[] worlds = config.getString("blacklistedWorlds").trim().split(",");
					for (String worldString : worlds)
					{
						World world = getServer().getWorld(worldString);
						if (player.getWorld() == world)
						{
							player.sendMessage(coloredString(messages.prefix + messages.noWildAllowed));
							return false;						
						}
					}
				}
				inventoryClass.mainWildMenu(player);
				return true;
			}

			if (args[0].equalsIgnoreCase("biome"))
				if (biomeCommand.biomeCommand(player, args))
					return true;
				
			if (args[0].equalsIgnoreCase("size"))
				if (sizeCommand.sizeCommand(player, args))
					return true;
			
			if (args[0].equalsIgnoreCase("blacklist"))
				if (blacklistCommand.blacklistCommand(player, args))
					return true;
			
			if (args[0].equalsIgnoreCase("help"))
				if (helpCommand.helpCommand(player, args))
					return true;
			
			if (args[0].equalsIgnoreCase("world"))
				if (worldWildCommand.worldWild(player, args))
					return true;
			
			if (args[0].equalsIgnoreCase("reload"))
			{
				if (!(hasPermission(player, "nexelwilderness.admin.reload")))
					return false;

				reloadConfig();
				messages.init();
				player.sendMessage(coloredString(messages.prefix + messages.succesfullReload));
				return true;
			}
		}
		else if (command.getName().equalsIgnoreCase("biometp"))
		{
			Player currentPlayer = Bukkit.getPlayer(sender.getName());

			if (!(currentPlayer.hasPermission("nexelwilderness.biomewild") || currentPlayer.hasPermission("nexelwilderness.*")))
	 		{
	 			currentPlayer.sendMessage(coloredString(messages.prefix + messages.noPermissions));
	 			return false;
	 		}

			int currentCooldown = cooldown.getPlayerCooldown(currentPlayer.getUniqueId());

	      	if (currentCooldown <= 0)
	      	{
			    currentPlayer.closeInventory(); 
			    delayedTeleport.startDelay(args[0], currentPlayer, null);
			    String delayTime = timeConverter.formatTime(config.getInt("teleportDelay"));

			    currentPlayer.sendMessage(coloredString(messages.prefix + messages.delayedTeleport
						.replace("%time%", delayTime)));
	     	}
	      	else
	      	{
	     		currentPlayer.closeInventory();
	     		String cooldownTime = timeConverter.formatTime(currentCooldown);

	     		currentPlayer.sendMessage(coloredString(messages.prefix + messages.cooldownNotOver
						.replace("%cooldown%", cooldownTime)));
	    	}
		}
		return false;
	}
	
	public boolean errorCatcher(int argsLength, int argsRequired, String usage, Player currentPlayer)
	{
		if (argsLength != argsRequired)
		{
			currentPlayer.sendMessage(coloredString(messages.prefix + messages.insufficientDetails.replace("%usage%", usage)));
			return true;	
		}
		return false;
	}
	
	public String capitalBiome(String biomeString)
	{
	    if(biomeString == null || biomeString.isEmpty())
	        return biomeString;

	    return biomeString.substring(0, 1).toUpperCase() + biomeString.substring(1).toLowerCase();
	}

	public String coloredString(String textToTranslate)
	{
		return ChatColor.translateAlternateColorCodes('&', textToTranslate);
	}

	public boolean hasPermission(Player permPlayer, String permission)
	{
		if (permPlayer.hasPermission(permission) || permPlayer.hasPermission("nexelwilderness.admin.*"))
			return true;
		else
		{
			permPlayer.sendMessage(coloredString(messages.prefix + messages.noPermissions));
			return false;
		}
	}
}