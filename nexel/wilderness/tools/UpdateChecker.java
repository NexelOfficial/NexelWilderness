package nexel.wilderness.tools;

import nexel.wilderness.CommandHandler;
import nexel.wilderness.WildInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Consumer;

public class UpdateChecker implements Listener {
    private final CommandHandler main;

    public UpdateChecker(CommandHandler main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("nexelwilderness.admin.help") || player.hasPermission("nexelwilderness.admin.*")) {
            // Check for update
            getVersion(version -> {
                if (!main.getDescription().getVersion().equals(version)) {
                    main.sendColoredMessage(player, "&aGood news! NexelWilderness can be updated to a newer version (" + version + "). Be sure to check it out on Spigot!");
                }
            });
        }
    }

    // Snippet from https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates/
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=80595").openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                main.sendColoredMessage(Messages.prefix + "&cUnable to check for updates!");
            }
        });
    }
}