package com.bananaarch.minecraftevacuation.settings.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public enum CustomItems {

    EXIT_ITEM(
            "Exit",
            ChatColor.DARK_RED,
            "test",
            false,
            Material.BARRIER
    ),
    FRESHMAN_ITEM(
            "Freshman",
            ChatColor.GREEN,
            "test",
            true,
            Material.EMERALD_BLOCK
    ),
    SOPHOMORE_ITEM(
            "Sophomore",
            ChatColor.YELLOW,
            "test",
            true,
            Material.GOLD_BLOCK
    ),
    JUNIOR_ITEM(
            "Juniors",
            ChatColor.BLUE,
            "test",
            true,
            Material.DIAMOND_BLOCK
    ),
    SENIOR_ITEM(
            "Seniors",
            ChatColor.DARK_PURPLE,
            "test",
            true,
            Material.NETHERITE_BLOCK
    ),
    TEACHER_ITEM(
            "Teachers",
            ChatColor.WHITE,
            "test",
            true,
            Material.BEDROCK
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
