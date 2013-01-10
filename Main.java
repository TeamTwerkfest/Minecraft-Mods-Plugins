
package me.eMe.ThrowCDs;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin
{

    public Main()
    {
    }

    public void onDisable()
    {
        PluginDescriptionFile pdfFile = getDescription();
        logger.info((new StringBuilder(String.valueOf(pdfFile.getName()))).append(" version ").append(pdfFile.getVersion()).append(" by ").append(pdfFile.getAuthors()).append(" has been disabled").toString());
    }

    public void onEnable()
    {
        PluginDescriptionFile pdfFile = getDescription();
        authorname = pdfFile.getAuthors();
        logger.info((new StringBuilder(String.valueOf(pdfFile.getName()))).append(" version ").append(pdfFile.getVersion()).append(" by ").append(pdfFile.getAuthors()).append(" has been enabled").toString());
        logger.info((new StringBuilder(String.valueOf(pdfFile.getName()))).append(" for information how the use the configuration files from ThrowDisks, visit: http://dev.bukkit.org/server-mods/throwdisks/").toString());
        Folder = getDataFolder();
        if(!Folder.exists())
            Folder.mkdir();
        getConfig().options().copyDefaults(true);
        saveConfig();
        PlayerConfiguration = new File((new StringBuilder()).append(Folder).append("\\Players.yml").toString());
        if(!PlayerConfiguration.exists())
            try
            {
                PlayerConfiguration.createNewFile();
            }
            catch(IOException ioexception) { }
        PlayerConfig = YamlConfiguration.loadConfiguration(PlayerConfiguration);
        black1 = getConfig().getInt("CraftingRecipes.BlackDisk.1");
        black2 = getConfig().getInt("CraftingRecipes.BlackDisk.2");
        gold1 = getConfig().getInt("CraftingRecipes.GoldDisk.1");
        gold2 = getConfig().getInt("CraftingRecipes.GoldDisk.2");
        green1 = getConfig().getInt("CraftingRecipes.GreenDisk.1");
        green2 = getConfig().getInt("CraftingRecipes.GreenDisk.2");
        white1 = getConfig().getInt("CraftingRecipes.WhiteDisk.1");
        white2 = getConfig().getInt("CraftingRecipes.WhiteDisk.2");
        Gold_Explosion = getConfig().getInt("Disks.GoldDisk.Explosionpower");
        Gold_Range = getConfig().getInt("Disks.GoldDisk.Maximumrange");
        Gold_Fire = getConfig().getInt("Disks.GoldDisk.FireTicks");
        Green_velocity = getConfig().getInt("Disks.GreenDisk.Velocity");
        Green_Range = getConfig().getInt("Disks.GreenDisk.Maximumrange");
        White_Explosion = getConfig().getInt("Disks.WhiteDisk.Explosionpower");
        White_Amount = getConfig().getInt("Disks.WhiteDisk.LightningAmount");
        White_Range = getConfig().getInt("Disks.WhiteDisk.MaximumRange");
        AutoPlayer = getConfig().getBoolean("AutoAssign");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new ThrowListener(), this);
        ItemStack gold = new ItemStack(2256);
        ShapedRecipe golddisc = (new ShapedRecipe(gold)).shape(new String[] {
            " * ", "*#*", " * "
        }).setIngredient('*', Material.getMaterial(gold1)).setIngredient('#', Material.getMaterial(gold2));
        getServer().addRecipe(golddisc);
        ItemStack green = new ItemStack(2257);
        ShapedRecipe greendisc = (new ShapedRecipe(green)).shape(new String[] {
            " * ", "*#*", " * "
        }).setIngredient('*', Material.getMaterial(green1)).setIngredient('#', Material.getMaterial(green2));
        getServer().addRecipe(greendisc);
        ItemStack white = new ItemStack(2264);
        ShapedRecipe whitedisc = (new ShapedRecipe(white)).shape(new String[] {
            " * ", "*#*", " * "
        }).setIngredient('*', Material.getMaterial(white1)).setIngredient('#', Material.getMaterial(white2));
        getServer().addRecipe(whitedisc);
        ItemStack black = new ItemStack(2263);
        ShapedRecipe blackdisc = (new ShapedRecipe(black)).shape(new String[] {
            "***", "*#*", "***"
        }).setIngredient('*', Material.getMaterial(black1)).setIngredient('#', Material.getMaterial(black2));
        getServer().addRecipe(blackdisc);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
    {
        Player player = (Player)sender;
        if(commandLabel.equalsIgnoreCase("give/TD"))
            if(player.isOp() || PlayerConfig.getString((new StringBuilder("Players.")).append(player.getName()).toString()).equals("DiskSpawner") || PlayerConfig.getString((new StringBuilder("Players.")).append(player.getName()).toString()).equals("DiskManager"))
            {
                if(args.length == 1)
                {
                    if(args[0].equals("explosive"))
                        player.getInventory().addItem(new ItemStack[] {
                            Functions.setname(new ItemStack(Material.GOLD_RECORD, 1), "Explosive throwingdisk")
                        });
                    if(args[0].equals("air"))
                        player.getInventory().addItem(new ItemStack[] {
                            Functions.setname(new ItemStack(Material.getMaterial(2257), 1), "Air throwingdisk")
                        });
                    if(args[0].equals("lightning"))
                        player.getInventory().addItem(new ItemStack[] {
                            Functions.setname(new ItemStack(Material.getMaterial(2264), 1), "Lightning throwingdisk")
                        });
                } else
                {
                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Invalid arguments!").toString());
                }
            } else
            {
                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You have to be an op, diskspawner or diskmanager for this command!").toString());
            }
        if(commandLabel.equalsIgnoreCase("give/BlackDisk"))
            if(player.isOp())
                player.getInventory().addItem(new ItemStack[] {
                    new ItemStack(Material.RECORD_8, 1)
                });
            else
                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You have to be an op, diskspawner or diskmanager for this command!").toString());
        if(commandLabel.equalsIgnoreCase("TDhelp"))
        {
            List message_L = getConfig().getStringList("Help.Normal");
            List op_L = getConfig().getStringList("Help.Op");
            String message = message_L.toString();
            String op = op_L.toString();
            if(player.isOp())
                player.sendMessage((new StringBuilder()).append(ChatColor.DARK_PURPLE).append(op).append(" ").append(ChatColor.GREEN).append(message).toString());
            else
                player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append(message).toString());
        }
        if(commandLabel.equalsIgnoreCase("TDadd"))
            if(PlayerConfig.getString((new StringBuilder("Players.")).append(player.getName()).toString()).equals("DiskManager") || player.isOp())
            {
                if(args.length == 2)
                {
                    if(player.getServer().getPlayer(args[1]) == null)
                        player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Player is not online! Added the player").append(ChatColor.DARK_GREEN).append(" (").append(args[1]).append(") ").append(ChatColor.GREEN).append("anyway.").toString());
                    if(args[0].toLowerCase().equalsIgnoreCase("player"))
                    {
                        PlayerConfig.set((new StringBuilder("Players.")).append(player.getName()).toString(), "DiskPlayer");
                        player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("The player ").append(ChatColor.DARK_GREEN).append(args[1]).append(ChatColor.GREEN).append(" has been added to the list: DiskPlayers.").toString());
                        if(player.getServer().getPlayer(args[1]) != null)
                            player.getServer().getPlayer(args[1]).sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You've been added to the list 'DiskPlayers' by: ").append(ChatColor.DARK_GREEN).append(player.getName()).toString());
                        try
                        {
                            PlayerConfig.save(PlayerConfiguration);
                        }
                        catch(Exception e)
                        {
                            System.out.println("Didn't succeed to save file players: ");
                            System.out.println(e.getMessage());
                        }
                    }
                    if(args[0].toLowerCase().equalsIgnoreCase("spawner"))
                    {
                        PlayerConfig.set((new StringBuilder("Players.")).append(player.getName()).toString(), "DiskSpawner");
                        player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("The player ").append(ChatColor.DARK_GREEN).append(args[1]).append(ChatColor.GREEN).append(" has been added to the list: DiskSpawners.").toString());
                        if(player.getServer().getPlayer(args[1]) != null)
                            player.getServer().getPlayer(args[1]).sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You've been added to the list 'DiskSpawners' by: ").append(ChatColor.DARK_GREEN).append(player.getName()).toString());
                        try
                        {
                            PlayerConfig.save(PlayerConfiguration);
                        }
                        catch(Exception e)
                        {
                            System.out.println("Didn't succeed to save file players: ");
                            System.out.println(e.getMessage());
                        }
                    }
                    if(args[0].toLowerCase().equalsIgnoreCase("manager"))
                    {
                        PlayerConfig.set((new StringBuilder("Players.")).append(player.getName()).toString(), "DiskManager");
                        player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("The player ").append(ChatColor.DARK_GREEN).append(args[1]).append(ChatColor.GREEN).append(" has been added to the list: DiskManagers.").toString());
                        if(player.getServer().getPlayer(args[1]) != null)
                            player.getServer().getPlayer(args[1]).sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You've been added to the list 'DiskManagers' by: ").append(ChatColor.DARK_GREEN).append(player.getName()).toString());
                        try
                        {
                            PlayerConfig.save(PlayerConfiguration);
                        }
                        catch(Exception e)
                        {
                            System.out.println("Didn't succeed to save file players: ");
                            System.out.println(e.getMessage());
                        }
                    }
                } else
                {
                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Invalid amount of arguments!").toString());
                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Usage: /TDadd <type> <player>").toString());
                }
            } else
            {
                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You are not allowed to use this command: you aren't a ThrowDisks manager or an op.").toString());
            }
        if(commandLabel.equalsIgnoreCase("TDremove"))
            if(PlayerConfig.getString((new StringBuilder("Players.")).append(player.getName()).toString()).equals("DiskManager") || player.isOp())
            {
                if(args.length == 2)
                {
                    if(args[0].toLowerCase().equalsIgnoreCase("player"))
                        if(PlayerConfig.getString((new StringBuilder("Players.")).append(args[1]).toString()) != null)
                        {
                            if(PlayerConfig.getString((new StringBuilder("Players.")).append(args[1]).toString()).equals("DiskPlayer"))
                            {
                                PlayerConfig.set((new StringBuilder("Players.")).append(player.getName()).toString(), "None");
                                player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("The player ").append(ChatColor.DARK_GREEN).append(args[1]).append(ChatColor.GREEN).append(" has been removed from the list: DiskPlayers.").toString());
                                if(player.getServer().getPlayer(args[1]) != null)
                                    player.getServer().getPlayer(args[1]).sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You've been removed from the list: DiskPlayers").toString());
                                else
                                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Player is not online! Removed the player").append(ChatColor.DARK_GREEN).append(" (").append(args[1]).append(") ").append(ChatColor.GREEN).append("anyway.").toString());
                                try
                                {
                                    PlayerConfig.save(PlayerConfiguration);
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Didn't succeed to save file players: ");
                                    System.out.println(e.getMessage());
                                }
                            } else
                            {
                                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("the player").append(ChatColor.DARK_RED).append(" '").append(args[1]).append("' ").append(ChatColor.RED).append("isn't in the list: DiskPlayers").toString());
                            }
                        } else
                        {
                            player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("That player doesn't exist!").toString());
                        }
                    if(args[0].toLowerCase().equalsIgnoreCase("spawner"))
                        if(PlayerConfig.getString((new StringBuilder("Players.")).append(args[1]).toString()) != null)
                        {
                            if(PlayerConfig.getString((new StringBuilder("Players.")).append(args[1]).toString()).equals("DiskSpawner"))
                            {
                                PlayerConfig.set((new StringBuilder("Players.")).append(player.getName()).toString(), "DiskPlayer");
                                player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("The player ").append(ChatColor.DARK_GREEN).append(args[1]).append(ChatColor.GREEN).append(" has been removed from the list: DiskSpawners.").toString());
                                if(player.getServer().getPlayer(args[1]) != null)
                                    player.getServer().getPlayer(args[1]).sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You've been removed from the list: DiskSpawners").toString());
                                else
                                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Player is not online! Removed the player").append(ChatColor.DARK_GREEN).append(" (").append(args[1]).append(") ").append(ChatColor.GREEN).append("anyway.").toString());
                                try
                                {
                                    PlayerConfig.save(PlayerConfiguration);
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Didn't succeed to save file players: ");
                                    System.out.println(e.getMessage());
                                }
                            } else
                            {
                                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("the player").append(ChatColor.DARK_RED).append(" '").append(args[1]).append("' ").append(ChatColor.RED).append("isn't in the list: DiskSpawners").toString());
                            }
                        } else
                        {
                            player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("That player doesn't exist!").toString());
                        }
                    if(args[0].toLowerCase().equalsIgnoreCase("manager"))
                        if(PlayerConfig.getString((new StringBuilder("Players.")).append(args[1]).toString()) != null)
                        {
                            if(PlayerConfig.getString((new StringBuilder("Players.")).append(args[1]).toString()).equals("DiskManager"))
                            {
                                PlayerConfig.set((new StringBuilder("Players.")).append(player.getName()).toString(), "DiskSpawner");
                                player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("The player ").append(ChatColor.DARK_GREEN).append(args[1]).append(ChatColor.GREEN).append(" has been removed from the list: DiskManagers.").toString());
                                if(player.getServer().getPlayer(args[1]) != null)
                                    player.getServer().getPlayer(args[1]).sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You've been removed from the list: DiskManagers").toString());
                                else
                                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Player is not online! Removed the player").append(ChatColor.DARK_GREEN).append(" (").append(args[1]).append(") ").append(ChatColor.GREEN).append("anyway.").toString());
                                try
                                {
                                    PlayerConfig.save(PlayerConfiguration);
                                }
                                catch(Exception e)
                                {
                                    System.out.println("Didn't succeed to save file players: ");
                                    System.out.println(e.getMessage());
                                }
                            } else
                            {
                                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("the player").append(ChatColor.DARK_RED).append(" '").append(args[1]).append("' ").append(ChatColor.RED).append("isn't in the list: DiskManagers").toString());
                            }
                        } else
                        {
                            player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("That player doesn't exist!").toString());
                        }
                } else
                {
                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Invalid amount of arguments!").toString());
                    player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("Usage: /TDremove <type> <player>").toString());
                }
            } else
            {
                player.sendMessage((new StringBuilder()).append(ChatColor.RED).append("You are not allowed to use this command: you aren't a ThrowDisks manager or an op.").toString());
            }
        if(commandLabel.equalsIgnoreCase("TDcheck"))
            player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("you are an: ").append(PlayerConfig.getString((new StringBuilder("Players.")).append(player.getName()).toString())).toString());
        return false;
    }

    public final Logger logger = Logger.getLogger("Minecraft");
    public static File Folder;
    public static File PlayerConfiguration;
    public static FileConfiguration PlayerConfig;
    public static boolean AutoPlayer;
    public static int black1;
    public static int black2;
    public static int gold1;
    public static int gold2;
    public static int green1;
    public static int green2;
    public static int white1;
    public static int white2;
    public static int Gold_Explosion;
    public static int Gold_Range;
    public static int Gold_Fire;
    public static int Green_velocity;
    public static int Green_Range;
    public static int White_Explosion;
    public static int White_Amount;
    public static int White_Range;
    public static List authorname;
}
