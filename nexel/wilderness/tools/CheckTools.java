package nexel.wilderness.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class CheckTools {
    public static boolean materialExists(String materialString) {
        // Checking the material
        if (materialString.contains(":")) {
            return false;
        } else {
            return Material.matchMaterial(materialString) != null;
        }
    }

    public static Biome biomeExists(String biome) {
        try {
            String biomeNoRichtext = ChatColor.stripColor(biome.toUpperCase().replace(" ", "_"));
            return Biome.valueOf(biomeNoRichtext);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static boolean isNumber(String potentialNumber) {
        try {
            Integer.parseInt(potentialNumber);
            return true;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    public static boolean worldExists(String world) {
        if (Bukkit.getWorld(world) == null)
            return true;
        else
            return false;
    }
}