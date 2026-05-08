package fepbox.plugin.daily.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public record DailyGuiSession(Inventory page1,
        Inventory page2,
        HashMap<Integer, ItemStack> items1,
        HashMap<Integer, ItemStack> items2) {}
