package com.bananaarch.minecraftevacuation.interactions.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
    public static Inventory cloneInventory(Inventory originalInventory, String title) {
        int size = originalInventory.getSize();

        Inventory copiedInventory = Bukkit.createInventory(null, size, title);
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = originalInventory.getItem(i);
            if (itemStack != null) {
                copiedInventory.setItem(i, itemStack.clone());
            }
        }
        return copiedInventory;
    }
}
