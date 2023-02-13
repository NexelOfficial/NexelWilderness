package nexel.wilderness;

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

import nexel.wilderness.commands.BiomeCommand;
import nexel.wilderness.commands.BlacklistCommand;
import nexel.wilderness.commands.HelpCommand;
import nexel.wilderness.commands.SizeCommand;
import nexel.wilderness.commands.WorldWildCommand;

public class CommandHandler extends JavaPlugin {
    private final CooldownHandler cooldown = new CooldownHandler(this);
    private final SizeCommand sizeCommand = new SizeCommand(this);
    private final BlacklistCommand blacklistCommand = new BlacklistCommand(this);
    private final BiomeCommand biomeCommand = new BiomeCommand(this);
    private final HelpCommand helpCommand = new HelpCommand(this);
    private final WildChosenBiome chosenBiome = new WildChosenBiome(this, cooldown);
    private final WildRandomBiome randomBiome = new WildRandomBiome(this, cooldown);
    private final TeleportHandler teleport = new TeleportHandler(this, cooldown, chosenBiome, randomBiome);
    private final InventoryHandler inventory = new InventoryHandler(this, cooldown, teleport);
    private final WorldWildCommand worldWildCommand = new WorldWildCommand(this, cooldown, teleport);

    public FileConfiguration config = null;

    @Override
    public void onEnable() {
        config = getConfig();
        getServer().getPluginManager().registerEvents(inventory, this);
        getServer().getPluginManager().registerEvents(teleport, this);

        getServer().getConsoleSender().sendMessage(coloredString("&5Nexel&fWilderness &7> &fNexelWilderness has been enabled!"));
        getServer().getConsoleSender().sendMessage(coloredString("&5Nexel&fWilderness &7> &aCreated with &clove &aby Nexel"));
        saveDefaultConfig();
        cooldown.secondTimer();
        Messages.init(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            getServer().getConsoleSender().sendMessage(coloredString("&5Nexel&fWilderness &7> &cConsole commands are not supported yet. :("));
            return false;
        }

        if (command.getName().equalsIgnoreCase("wild")) {
            Player player = Bukkit.getPlayer(sender.getName());

            // Check if a command has been given
            if (args.length == 0) {
                if (config.isSet("blacklistedWorlds")) {
                    String[] worlds = config.getString("blacklistedWorlds").trim().split(",");
                    for (String worldString : worlds) {
                        World world = getServer().getWorld(worldString);
                        if (player.getWorld() == world) {
                            player.sendMessage(coloredString(Messages.prefix + Messages.noWildAllowed));
                            return false;
                        }
                    }
                }

                // Open the wild menu
                WildInventory playerInventory = inventory.getInventory(player);
                playerInventory.openMainMenu();
                return true;
            }

            if (args[0].equalsIgnoreCase("biome")) {
                return biomeCommand.biomeCommand(player, args);
            } else if (args[0].equalsIgnoreCase("size")) {
                return sizeCommand.sizeCommand(player, args);
            } else if (args[0].equalsIgnoreCase("blacklist")) {
                return blacklistCommand.blacklistCommand(player, args);
            } else if (args[0].equalsIgnoreCase("help")) {
                return helpCommand.helpCommand(player, args);
            } else if (args[0].equalsIgnoreCase("world")) {
                return worldWildCommand.worldWild(player, args);
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!(hasPermission(player, "nexelwilderness.admin.reload"))) {
                    return false;
                }

                reloadConfig();
                player.sendMessage(coloredString(Messages.prefix + Messages.succesfullReload));
                return true;
            }
        } else if (command.getName().equalsIgnoreCase("biometp")) {
            Player currentPlayer = Bukkit.getPlayer(sender.getName());

            if (!(currentPlayer.hasPermission("nexelwilderness.biomewild") || currentPlayer.hasPermission("nexelwilderness.*"))) {
                currentPlayer.sendMessage(coloredString(Messages.prefix + Messages.noPermissions));
                return false;
            }

            int currentCooldown = cooldown.getCooldown(currentPlayer);

            if (currentCooldown <= 0) {
                currentPlayer.closeInventory();
                teleport.startDelay(args[0], currentPlayer, null);
                String delayTime = TimeConverter.formatTime(config.getInt("teleportDelay"));

                currentPlayer.sendMessage(coloredString(Messages.prefix + Messages.delayedTeleport
                        .replace("%time%", delayTime)));
            } else {
                currentPlayer.closeInventory();
                String cooldownTime = TimeConverter.formatTime(currentCooldown);

                currentPlayer.sendMessage(coloredString(Messages.prefix + Messages.cooldownNotOver
                        .replace("%cooldown%", cooldownTime)));
            }
        }
        return false;
    }

    public boolean errorCatcher(int argsLength, int argsRequired, String usage, Player currentPlayer) {
        if (argsLength != argsRequired) {
            currentPlayer.sendMessage(coloredString(Messages.prefix + Messages.insufficientDetails.replace("%usage%", usage)));
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

    public String coloredString(String textToTranslate) {
        return ChatColor.translateAlternateColorCodes('&', textToTranslate);
    }

    public boolean hasPermission(Player permPlayer, String permission) {
        if (permPlayer.hasPermission(permission) || permPlayer.hasPermission("nexelwilderness.admin.*")) {
            return true;
        } else {
            permPlayer.sendMessage(coloredString(Messages.prefix + Messages.noPermissions));
            return false;
        }
    }
}