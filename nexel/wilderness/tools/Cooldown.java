package nexel.wilderness.tools;

import java.util.HashMap;
import java.util.UUID;

import nexel.wilderness.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Cooldown
{
	private final CommandHandler main;

	public Cooldown(CommandHandler main){
	    this.main = main;
	}

	public HashMap<UUID, Integer> cooldownTimeForPlayer = new HashMap<>();
	
	public void secondTimer()
	{
		new BukkitRunnable()
		{
			@Override
            public void run()
			{
            	for (Player currentPlayer : Bukkit.getOnlinePlayers())
            	{
            		if (currentPlayer.hasPermission("nexelwilderness.admin.bypasscooldown"))
            			cooldownTimeForPlayer.put(currentPlayer.getUniqueId(), 0);

            		if (!(cooldownTimeForPlayer.containsKey(currentPlayer.getUniqueId())))
            			continue;
            		if (cooldownTimeForPlayer.get(currentPlayer.getUniqueId()) <= 0)
            			continue;

            		int currentCooldown = cooldownTimeForPlayer.get(currentPlayer.getUniqueId());
            		cooldownTimeForPlayer.put(currentPlayer.getUniqueId(), currentCooldown - 1);
            		currentCooldown = cooldownTimeForPlayer.get(currentPlayer.getUniqueId());

            		if (!(currentCooldown <= 0))
            			continue;

            		cooldownTimeForPlayer.put(currentPlayer.getUniqueId(), 0);
            	}
            }
        }.runTaskTimer(main, 0L, 20L);
	}

	public int getPlayerCooldown(UUID playerUniqueId)
	{
		try
		{
			return cooldownTimeForPlayer.get(playerUniqueId);
		}
		catch(NullPointerException ex)
		{
			return 0;
		}
	}
}
