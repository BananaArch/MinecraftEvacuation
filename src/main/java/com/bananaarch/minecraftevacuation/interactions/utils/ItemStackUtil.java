package com.bananaarch.minecraftevacuation.interactions.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtil {

    public static ItemStack setCustomLore(ItemStack itemStack, List<String> lore) {
        ItemStack clonedItemStack = itemStack.clone();
        ItemMeta itemMeta = clonedItemStack.getItemMeta();

        List<String> loreList = new ArrayList<>();
        lore.forEach(line -> loreList.add(line));

        itemMeta.setLore(loreList);

        // Set the modified ItemMeta back to the cloned ItemStack
        clonedItemStack.setItemMeta(itemMeta);

        return clonedItemStack;

    }
}
