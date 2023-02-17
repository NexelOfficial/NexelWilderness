package nexel.wilderness.tools;

import nexel.wilderness.CommandHandler;

public class Messages {
    public static String insufficientDetails;
    public static String noWildAllowed;
    public static String noPermissions;
    public static String cooldownNotOver;
    public static String blockDoesntExist;
    public static String delayedTeleport;
    public static String prefix;
    public static String menuprefix;
    public static String succesfullReload;
    public static String noBiomeSpot;
    public static String noSafeSpot;
    public static String teleporting;
    public static String errorTeleporting;
    public static String succesfulTeleport;

    // Biome Strings
    public static String biomeDoesntExist;
    public static String biomeRemoved;
    public static String biomeAdded;

    // Blacklist Strings
    public static String noBlacklistedBlocks;
    public static String removeBlacklistedBlock;
    public static String succesfullBlacklist;
    public static String removedFromBlacklist;

    // Size String
    public static String wildSizeSet, retriesSet, retriesWarning;

    // Bools
    public static boolean advancedBiomes;
    public static boolean biomePicker;

    // Ints
    public static int wildCooldown;
    public static int biomeRetries, blacklistRetries;

    public static void init(CommandHandler main) {
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
        succesfulTeleport = main.getConfig().getString("succesfulTeleport");

        biomeDoesntExist = main.getConfig().getString("biomeDoesntExist");
        biomeRemoved = main.getConfig().getString("biomeRemoved");
        biomeAdded = main.getConfig().getString("biomeAdded");

        noBlacklistedBlocks = main.getConfig().getString("noBlacklistedBlocks");
        removeBlacklistedBlock = main.getConfig().getString("removeBlacklistedBlock");
        succesfullBlacklist = main.getConfig().getString("succesfullBlacklist");
        removedFromBlacklist = main.getConfig().getString("removedFromBlacklist");
        wildSizeSet = main.getConfig().getString("wildSizeSet");
        retriesSet = main.getConfig().getString("retriesSet");
        retriesWarning = main.getConfig().getString("retriesWarning");

        advancedBiomes = main.getConfig().getBoolean("advancedBiomes");
        biomePicker = main.getConfig().getBoolean("biomePicker");

        wildCooldown = main.getConfig().getInt("wildCooldown");
        biomeRetries = main.getConfig().getInt("biomeRetries");
        blacklistRetries = main.getConfig().getInt("blacklistRetries");
    }
}