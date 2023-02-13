package nexel.wilderness.tools;

import nexel.wilderness.CommandHandler;

public class Messages {
    private static CommandHandler main;

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
    public static String wildSizeSet;

    // Bools
    public static boolean advancedBiomes;
    public static boolean biomePicker;

    // Ints
    public static int wildCooldown;
    public static int retries;

    public static void init(CommandHandler main) {
        Messages.main = main;

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