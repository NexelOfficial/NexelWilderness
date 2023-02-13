package nexel.wilderness;

import nexel.wilderness.commands.*;
import nexel.wilderness.tools.*;
import nexel.wilderness.tools.wild.WildChosenBiome;
import nexel.wilderness.tools.wild.WildRandomBiome;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CommandHandler extends JavaPlugin {
    private final CooldownHandler cooldown = new CooldownHandler(this);
    private final SizeCommand sizeCommand = new SizeCommand(this);
    private final RetriesCommand retriesCommand = new RetriesCommand(this);
    private final BlacklistCommand blacklistCommand = new BlacklistCommand(this);
    private final BiomeCommand biomeCommand = new BiomeCommand(this);
    private final HelpCommand helpCommand = new HelpCommand(this);
    private final WildChosenBiome chosenBiome = new WildChosenBiome(this, cooldown);
    private final WildRandomBiome randomBiome = new WildRandomBiome(this, cooldown);
    private final TeleportHandler teleport = new TeleportHandler(this, cooldown, chosenBiome, randomBiome);
    private final InventoryHandler inventory = new InventoryHandler(this, cooldown, teleport);
    private final WorldWildCommand worldWildCommand = new WorldWildCommand(this, cooldown, teleport);

    public boolean worldBorderFound = false;

    @Override
    public void onEnable() {
        // Setup events
        getServer().getPluginManager().registerEvents(inventory, this);
        getServer().getPluginManager().registerEvents(teleport, this);

        // Check if the user is using a supported version
        String version = Bukkit.getServer().getClass().getPackage().getName().replace('_', '.');
        List<String> supportedVersions = getConfig().getStringList("supported-versions");

        for (String supportedVersion : supportedVersions) {
            if (version.contains(supportedVersion)) {
                // Log the welcome messages
                sendColoredMessage("&5Nexel&fWilderness &7> &fNexelWilderness has been enabled!");
                sendColoredMessage("&5Nexel&fWilderness &7> &aCreated with &clove&a by Nexel");

                // Initialize other required things
                saveDefaultConfig();
                cooldown.startTimer();
                Messages.init(this);

                // Add support for WorldBorder if in the server
                if (getServer().getPluginManager().getPlugin("WorldBorder") != null) {
                    sendColoredMessage("&5Nexel&fWilderness &7> &aWorldBorder plugin has been detected!");
                    worldBorderFound = true;
                } else {
                    sendColoredMessage("&5Nexel&fWilderness &7> &aNo external border plugins were found.");
                }
                return;
            }
        }

        // The version is not supported. Disable the plugin.
        sendColoredMessage("&5Nexel&fWilderness &7> &cYou are using an unsupported version of Minecraft: '" + version + "'");
        sendColoredMessage("&5Nexel&fWilderness &7> &cYou can add this version in the configuration file, but no support will be provided for it.");
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        String commandName = command.getName();

        if (commandName.equalsIgnoreCase("wild")) {
            // Check if a command has been given
            if (args.length == 0) {
                // Show help screen for console, open the wild menu for player
                if (sender instanceof ConsoleCommandSender) {
                    return helpCommand.helpCommand(sender, args);
                } else if (sender instanceof Player) {
                    Player player = Bukkit.getPlayer(sender.getName());

                    // Check if player can use Wild in this world
                    if (getConfig().isSet("blacklistedWorlds")) {
                        String[] worlds = getConfig().getString("blacklistedWorlds").trim().split(",");
                        for (String worldString : worlds) {
                            World world = getServer().getWorld(worldString);
                            if (player.getWorld() == world) {
                                sendColoredMessage(player, Messages.prefix + Messages.noWildAllowed);
                                return false;
                            }
                        }
                    }

                    // Open the wild menu
                    WildInventory playerInventory = inventory.getInventory(player);
                    playerInventory.openMainMenu();
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("biome")) {
                return biomeCommand.biomeCommand(sender, args);
            } else if (args[0].equalsIgnoreCase("size")) {
                return sizeCommand.sizeCommand(sender, args);
            } else if (args[0].equalsIgnoreCase("retries")) {
                return retriesCommand.retriesCommand(sender, args);
            } else if (args[0].equalsIgnoreCase("blacklist")) {
                return blacklistCommand.blacklistCommand(sender, args);
            } else if (args[0].equalsIgnoreCase("help")) {
                return helpCommand.helpCommand(sender, args);
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!(hasPermission(sender, "nexelwilderness.admin.reload"))) {
                    return false;
                }

                reloadConfig();
                sendColoredMessage(sender, Messages.prefix + Messages.succesfullReload);
                return true;
            } else if (sender instanceof Player && args[0].equalsIgnoreCase("world")) {
                Player player = Bukkit.getPlayer(sender.getName());
                return worldWildCommand.worldWild(player, args);
            } else if (sender instanceof ConsoleCommandSender) {
                // Check for player on first argument
                Player player = Bukkit.getPlayer(args[0]);

                if (player != null) {
                    // Teleport player randomly
                    randomBiome.normalWild(player, player.getWorld());
                    sendColoredMessage(sender, Messages.prefix + Messages.succesfulTeleport.replace("%player%", player.getDisplayName()));
                } else {
                    return helpCommand.helpCommand(sender, args);
                }
            }
        } else if (commandName.equalsIgnoreCase("biometp") && sender instanceof Player) {
            Player player = Bukkit.getPlayer(sender.getName());

            // Check if a player was found
            if (player == null) {
                return false;
            }

            // Check if the player has permissions
            if (!(hasPermission(sender, "nexelwilderness.biomewild"))) {
                sendColoredMessage(sender, Messages.prefix + Messages.noPermissions);
                return false;
            }

            int currentCooldown = cooldown.getCooldown(player);

            if (currentCooldown <= 0) {
                player.closeInventory();
                teleport.startDelay(args[0], player, null);
                String delayTime = TimeConverter.formatTime(getConfig().getInt("teleportDelay"));

                sendColoredMessage(sender, Messages.prefix + Messages.delayedTeleport.replace("%time%", delayTime));
            } else {
                player.closeInventory();
                String cooldownTime = TimeConverter.formatTime(currentCooldown);

                sendColoredMessage(sender, Messages.prefix + Messages.cooldownNotOver.replace("%cooldown%", cooldownTime));
            }
        }
        return false;
    }

    public boolean errorCatcher(int argsLength, int argsRequired, String usage, CommandSender sender) {
        if (argsLength != argsRequired) {
            sendColoredMessage(sender, Messages.prefix + Messages.insufficientDetails.replace("%usage%", usage));
            return true;
        }

        return false;
    }

    public String capitalBiome(String biomeString) {
        if (biomeString == null || biomeString.isEmpty()) {
            return biomeString;
        }

        return biomeString.substring(0, 1).toUpperCase() + biomeString.substring(1).toLowerCase();
    }

    public void sendColoredMessage(String text) {
        sendColoredMessage(getServer().getConsoleSender(), text);
    }

    public void sendColoredMessage(CommandSender sender, String text) {
        String coloredText = ChatColor.translateAlternateColorCodes('&', text);
        sender.sendMessage(coloredText);
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }

        Player permPlayer = (Player) sender;

        if (permPlayer.hasPermission(permission) || permPlayer.hasPermission("nexelwilderness.admin.*")) {
            return true;
        } else {
            sendColoredMessage(sender, Messages.prefix + Messages.noPermissions);
            return false;
        }
    }
}