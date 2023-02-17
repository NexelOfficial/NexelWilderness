package nexel.wilderness.tools;

import java.util.HashMap;
import java.util.UUID;

import nexel.wilderness.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownHandler {
    private final CommandHandler main;
    private final HashMap<UUID, Integer> cooldowns = new HashMap<>();

    public Integer getCooldown(Player player) {
        UUID uuid = player.getUniqueId();

        cooldowns.putIfAbsent(uuid, 0);
        return cooldowns.get(uuid);
    }

    public Integer getCooldown(UUID uuid) {
        cooldowns.putIfAbsent(uuid, 0);
        return cooldowns.get(uuid);
    }

    public void setCooldown(UUID uuid, Integer cooldown) {
        cooldowns.put(uuid, cooldown);
    }

    public void setCooldown(Player player, Integer cooldown) {
        UUID uuid = player.getUniqueId();
        cooldowns.put(uuid, cooldown);
    }

    public CooldownHandler(CommandHandler main) {
        this.main = main;
    }

    public void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("nexelwilderness.admin.bypasscooldown")) {
                        setCooldown(player, 0);
                        continue;
                    }

                    int currentCooldown = getCooldown(player);

                    if (currentCooldown == 0) {
                        continue;
                    }

                    setCooldown(player, currentCooldown - 1);
                }
            }
        }.runTaskTimer(main, 0L, 20L);
    }
}
