package nexel.wilderness.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Arrays;

public class CheckObject
{
    public boolean materialExists(String material)
    {
        // Checking the material
        if (material.contains(":"))
            try
            {
                String[] argsSplit = material.split(":");
                String matName = argsSplit[0];
                int matData = Integer.parseInt(argsSplit[1]);

                if (Bukkit.getUnsafe().getMaterial(matName, matData) == null)
                    return false;

                return true;
            }
            catch (NumberFormatException ex)
            {
                return false;
            }
        else if (Material.matchMaterial(material) != null)
            return true;

        return false;
    }

    public Biome biomeExists(String biome)
    {
        try 
        {
            String biomeNoRichtext = ChatColor.stripColor(biome.toUpperCase().replace(" ", "_"));
            return Biome.valueOf(biomeNoRichtext);
        }
        catch (NumberFormatException ex)
        {
            return null;
        }
    }

    public boolean isNumber(String potentialNumber)
    {
        try
        {
            Integer.parseInt(potentialNumber);
            return true;
        }
        catch (NullPointerException ex)
        {
            return false;
        }
    }

    public boolean worldExists(String world)
    {
        if (Bukkit.getWorld(world) == null)
            return true;
        else
            return false;
    }

    public InventoryHolder inventoryHasHolder(Inventory inventory)
    {
        try
        {
            return inventory.getHolder();
        }
        catch (NullPointerException ex)
        {
            return null;
        }
    }
}
