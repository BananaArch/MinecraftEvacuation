package com.bananaarch.minecraftevacuation.interactions.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum CustomGUI {
    MENU_GUI(
            27,
            ChatColor.DARK_GRAY + "Menu",
            new HashMap<Integer, ItemStack>() {{
                put(2, CustomItems.START_REPLAY.getItemStack());
                put(3, CustomItems.START_TRAINING.getItemStack());
                put(4, CustomItems.TARGET_BLOCK.getItemStack());
                put(5, CustomItems.STUDENTS_SHOWN.getItemStack());
                put(6, CustomItems.DESTROY_ALL_BOTS.getItemStack());
                put(11, CustomItems.FRESHMAN_ITEM.getItemStack());
                put(12, CustomItems.SOPHOMORE_ITEM.getItemStack());
                put(13, CustomItems.JUNIOR_ITEM.getItemStack());
                put(14, CustomItems.SENIOR_ITEM.getItemStack());
                put(15, CustomItems.TEACHER_ITEM.getItemStack());
                put(21, CustomItems.GENERATE_PATH.getItemStack());
                put(22, CustomItems.EXIT_GUI.getItemStack());
                put(23, CustomItems.FOLLOW_PATH.getItemStack());
            }}
    ),
    BOT_GUI(
            9,
            ChatColor.DARK_GRAY + "Edit Bot",
            new HashMap<Integer, ItemStack>() {{
                put(0, CustomItems.BASE_BOT_INFO.getItemStack());
                put(1, CustomItems.CHANGE_GENDER.getItemStack());
                put(2, CustomItems.SHOW_PATH.getItemStack());
                put(7, CustomItems.DELETE_BOT.getItemStack());
                put(8, CustomItems.EXIT_GUI.getItemStack());
            }}
    );

    private Inventory inventory;
    private String title;

    CustomGUI(int size, String title, HashMap<Integer, ItemStack> itemSlot) {

        Inventory inventory = Bukkit.createInventory(null, size, title);

        for (Integer key : itemSlot.keySet()) {
            ItemStack item = itemSlot.get(key);

            inventory.setItem(key, item);
        }

        this.inventory = inventory;
        this.title = title;

    }

    public Inventory getInventory() { return inventory; }

    public void setItem(int index, ItemStack item) {
        inventory.setItem(index, item);
    }

    public String getTitle() {
        return title;
    }


}
