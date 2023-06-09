package com.bananaarch.minecraftevacuation.interactions.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public enum CustomItems {

    START_REPLAY(
            "Start Replay",
            ChatColor.GREEN,
            "Click here to replay the pre-trained model",
            false,
            Material.LIME_CONCRETE_POWDER
    ),
    STOP_REPLAY(
            "Stop Replay",
            ChatColor.RED,
            "Click here to stop the replay",
            false,
            Material.RED_CONCRETE_POWDER
    ),
    START_TRAINING(
            "Start Training",
            ChatColor.WHITE,
            "Click here to start the training",
            false,
            Material.FIREWORK_STAR
    ),
    STOP_TRAINING(
            "Stop Training",
            ChatColor.WHITE,
            "Click here to stop the training",
            true,
            Material.NETHER_STAR
    ),
    STUDENTS_SHOWN(
            "Visibility: Revealed",
            ChatColor.GREEN,
            "Click here to hide Student NPCs",
            false,
            Material.LIME_CANDLE
    ),
    STUDENTS_HIDDEN(
            "Visibility: Hidden",
            ChatColor.RED,
            "Click here to show Student NPCs",
            false,
            Material.RED_CANDLE
    ),
    DESTROY_ALL_BOTS(
            "Destroy all bots",
            ChatColor.DARK_RED,
            "Click here to destroy all bots",
            false,
            Material.TNT_MINECART
    ),
    CONFIRM_DESTROY_ALL_BOTS(
            "THIS ACTION CANNOT BE UNDONE!",
            ChatColor.DARK_RED,
            "Click to confirm that you want to " + ChatColor.RED + "DELETE ALL BOTS",
            true,
            Material.SCULK_SENSOR
    ),
    FRESHMAN_ITEM(
            "Freshman",
            ChatColor.GREEN,
            "Click here to get a Freshman block",
            true,
            Material.EMERALD_BLOCK
    ),
    SOPHOMORE_ITEM(
            "Sophomore",
            ChatColor.YELLOW,
            "Click here to get a Sophomore block",
            true,
            Material.GOLD_BLOCK
    ),
    JUNIOR_ITEM(
            "Juniors",
            ChatColor.BLUE,
            "Click here to get a Junior block",
            true,
            Material.DIAMOND_BLOCK
    ),
    SENIOR_ITEM(
            "Seniors",
            ChatColor.DARK_PURPLE,
            "Click here to get a Senior block",
            true,
            Material.NETHERITE_BLOCK
    ),
    TEACHER_ITEM(
            "Teachers",
            ChatColor.WHITE,
            "Click here to get a Teacher block",
            true,
            Material.BEDROCK
    ),
    TARGET_BLOCK(
            "Target",
            ChatColor.RED,
            "Click here to obtain a Target Initialization block",
            true,
            Material.TARGET
    ),
    GENERATE_PATH(
            "Generate path",
            ChatColor.YELLOW,
            "Click here to generate all the paths for the bots",
            false,
            Material.COMMAND_BLOCK_MINECART
    ),
    FOLLOW_PATH(
            "Follow path",
            ChatColor.YELLOW,
            "Click here to have all the bots follow the path (Make sure the paths are generated first)",
            false,
            Material.HOPPER_MINECART
    ),
    BASE_BOT_INFO(
            "Bot Info",
            ChatColor.WHITE,
            ChatColor.DARK_RED.toString() + ChatColor.BOLD + "ERROR: Invalid Bot",
            true,
            Material.ARMOR_STAND
    ),
    CHANGE_GENDER(
            "Change Gender",
            ChatColor.WHITE,
            "Click here to change the gender",
            false,
            Material.LEVER
    ),
    SHOW_PATH(
            "Show Path",
            ChatColor.WHITE,
            "Click here to show the Bot's optimal path for " + ChatColor.GREEN + "15 seconds",
            false,
            Material.COMMAND_BLOCK_MINECART

    ),
    DELETE_BOT(
            "Delete bot",
            ChatColor.DARK_RED,
            "Click here to " + ChatColor.RED + "delete" + ChatColor.GRAY + " this bot",
            false,
            Material.TNT_MINECART
    ),
    EXIT_GUI(
            "Exit",
            ChatColor.RED,
            "Click here to exit the GUI",
            false,
            Material.BARRIER
    );


    private ItemStack itemStack;

    CustomItems(String name, ChatColor chatColor, String description, Boolean enchanted, Material material) {

        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (enchanted) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemMeta.setDisplayName(chatColor.toString() + ChatColor.BOLD + name);
        itemMeta.setLore(Arrays.asList(ChatColor.GRAY + description));
        itemStack.setItemMeta(itemMeta);

        this.itemStack = itemStack;

    }


    public ItemStack getItemStack() { return itemStack; }

}
