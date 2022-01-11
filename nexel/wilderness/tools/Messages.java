package nexel.wilderness.tools;

import nexel.wilderness.CommandHandler;

public class Messages
{
    private CommandHandler main;

    public Messages(CommandHandler main) {
        this.main = main;
    }

    public String insufficientDetails;
    public String noWildAllowed;
    public String noPermissions;
    public String cooldownNotOver;
    public String blockDoesntExist;
    public String delayedTeleport;
    public String prefix;
    public String menuprefix;
    public String succesfullReload;
    public String noBiomeSpot;
    public String noSafeSpot;
    public String teleporting;
    public String errorTeleporting;

    // Biome Strings
    public String biomeDoesntExist;
    public String biomeRemoved;
    public String biomeAdded;

    // Blacklist Strings
    public String noBlacklistedBlocks;
    public String removeBlacklistedBlock;
    public String succesfullBlacklist;
    public String removedFromBlacklist;

    // Size String
    public String wildSizeSet;

    // Bools
    public boolean advancedBiomes;
    public boolean biomePicker;

    // Ints
    public int wildCooldown;
    public int retries;

    public void init()
    {
        insufficientDetails = main.getConfig().getString("insufficientDetails");
        noWildAllowed = main.getConfig().getString("noWildAllowed");
        noPermissions = main.getConfig().getString("noPermissions");
        cooldownNotOver = main.getConfig().getString("cooldownNotOver");
        blockDoesntExist = main.getConfig().getString("blockDoesntExist");
        delayedTeleport = main.getConfig().getString("delayedTeleport");
        prefix = main.getConfig().getString("prefix") + "&r ";
        menuprefix = main.getConfig().getString("menuprefix");
        succesfullReload = main.getConfig().getString("succesfullReload");
        noBiomeSpot = main.getConfig().getString("noBiomeSpot");
        noSafeSpot = main.getConfig().getString("noSafeSpot");
        teleporting = main.getConfig().getString("teleporting");
        errorTeleporting = main.getConfig().getString("errorTeleporting");

        biomeDoesntExist = main.getConfig().getString("biomeDoesntExist");
        biomeRemoved = main.getConfig().getString("biomeRemoved");
        biomeAdded = main.getConfig().getString("biomeAdded");

        noBlacklistedBlocks = main.getConfig().getString("noBlacklistedBlocks");
        removeBlacklistedBlock = main.getConfig().getString("removeBlacklistedBlock");
        succesfullBlacklist = main.getConfig().getString("succesfullBlacklist");
        removedFromBlacklist = main.getConfig().getString("removedFromBlacklist");
        wildSizeSet = main.getConfig().getString("wildSizeSet");

        advancedBiomes = main.getConfig().getBoolean("advancedBiomes");
        biomePicker = main.getConfig().getBoolean("biomePicker");

        wildCooldown = main.getConfig().getInt("wildCooldown");
        retries = main.getConfig().getInt("retries");
    }
}
