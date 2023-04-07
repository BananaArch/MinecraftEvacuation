package com.bananaarch.minecraftevacuation.settings.command;

import com.bananaarch.minecraftevacuation.settings.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public enum CustomGUI {

    SETTINGS_GUI(27,
            ChatColor.WHITE + "Settings",
            new HashMap<Integer, ItemStack>() {{
                put(11, CustomItems.FRESHMAN_ITEM.getItemStack());
                put(12, CustomItems.SOPHOMORE_ITEM.getItemStack());
                put(13, CustomItems.JUNIOR_ITEM.getItemStack());
                put(14, CustomItems.SENIOR_ITEM.getItemStack());
                put(15, CustomItems.TEACHER_ITEM.getItemStack());
                put(22, CustomItems.EXIT_ITEM.getItemStack());
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

}
