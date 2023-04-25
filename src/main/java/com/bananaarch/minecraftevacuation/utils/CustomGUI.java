package com.bananaarch.minecraftevacuation.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum CustomGUI {
    MENU_GUI(
            27,
            ChatColor.YELLOW + "Menu",
            new HashMap<Integer, ItemStack>() {{
                put(3, CustomItems.START_REPLAY.getItemStack());
                put(4, CustomItems.START_TRAINING.getItemStack());
                put(5, CustomItems.SHOW_STUDENTS.getItemStack());
                put(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());
                put(11, CustomItems.FRESHMAN_ITEM.getItemStack());
                put(12, CustomItems.SOPHOMORE_ITEM.getItemStack());
                put(13, CustomItems.JUNIOR_ITEM.getItemStack());
                put(14, CustomItems.SENIOR_ITEM.getItemStack());
                put(15, CustomItems.TEACHER_ITEM.getItemStack());
                put(22, CustomItems.EXIT_ITEM.getItemStack());
            }}
    ),
    BOT_GUI(
            9,
            ChatColor.YELLOW + "Edit Bot",
            new HashMap<Integer, ItemStack>() {{
                put(8, CustomItems.EXIT_ITEM.getItemStack());
            }}
    );

    private Inventory inventory;

    CustomGUI(int size, String name, HashMap<Integer, ItemStack> itemSlot) {

        Inventory inventory = Bukkit.createInventory(null, size, name);

        for (Integer key : itemSlot.keySet()) {
            ItemStack item = itemSlot.get(key);

            inventory.setItem(key, item);
        }

        this.inventory = inventory;

    }

    public Inventory getInventory() { return inventory; }

    public void setItem(int index, ItemStack item) {
        inventory.setItem(index, item);
    }

}
