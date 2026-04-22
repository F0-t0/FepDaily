package fepbox.plugin.daily.commands;

import fepbox.plugin.daily.FepDaily;
import fepbox.plugin.daily.utils.PlayersStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyCommand implements CommandExecutor {
    FepDaily plugin;
    public DailyCommand(FepDaily plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            PlayersStorage pss = new PlayersStorage(plugin, "players.yml");
            if (!pss.getConfig().getString("date").equalsIgnoreCase(LocalDate.now().toString())) {
                List<String> players = pss.getConfig().getStringList("date");
                players.clear();
                pss.getConfig().set("date", LocalDate.now().toString());
                pss.getConfig().set("players", players);
                pss.save();
            }


            Inventory inventory = Bukkit.createInventory(p, 54, ChatColor.GREEN + "Daily");
            ItemStack filler = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            ItemStack fillerMonday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerTuesday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerWednesday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerThursday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerFriday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerSaturday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerSunday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta fillerMondayMeta = fillerMonday.getItemMeta();
            fillerMondayMeta.setDisplayName(ChatColor.GREEN + "Poniedziałek ↓");
            fillerMonday.setItemMeta(fillerMondayMeta);
            ItemMeta fillerTuesdayMeta = fillerTuesday.getItemMeta();
            fillerTuesdayMeta.setDisplayName(ChatColor.GREEN + "Wtorek ↓");
            fillerTuesday.setItemMeta(fillerTuesdayMeta);
            ItemMeta fillerwednesdayMeta = fillerWednesday.getItemMeta();
            fillerwednesdayMeta.setDisplayName(ChatColor.GREEN+"Środa ↓");
            fillerWednesday.setItemMeta(fillerwednesdayMeta);
            ItemMeta fillerThursdayMeta = fillerThursday.getItemMeta();
            fillerThursdayMeta.setDisplayName(ChatColor.GREEN+"Czwartek ↓");
            fillerThursday.setItemMeta(fillerThursdayMeta);
            ItemMeta fillerFridayMeta = fillerThursday.getItemMeta();
            fillerFridayMeta.setDisplayName(ChatColor.GREEN+"Piątek ↓");
            fillerFriday.setItemMeta(fillerFridayMeta);
            ItemMeta fillerSaturdayMeta = fillerThursday.getItemMeta();
            fillerSaturdayMeta.setDisplayName(ChatColor.GREEN+"Sobota ↓");
            fillerSaturday.setItemMeta(fillerSaturdayMeta);
            ItemMeta fillerSundayMeta = fillerSaturday.getItemMeta();
            fillerSundayMeta.setDisplayName(ChatColor.GREEN + "Niedziela ↓");
            fillerSunday.setItemMeta(fillerSundayMeta);
            inventory.setItem(0, filler);
            inventory.setItem(1, fillerMonday);
            inventory.setItem(2, fillerTuesday);
            inventory.setItem(3, fillerWednesday);
            inventory.setItem(4, fillerThursday);
            inventory.setItem(5, fillerFriday);
            inventory.setItem(6, fillerSaturday);
            inventory.setItem(7, fillerSunday);
            inventory.setItem(8, filler);
            inventory.setItem(9, filler);
            inventory.setItem(17, filler);
            inventory.setItem(18, filler);
            inventory.setItem(26, filler);
            inventory.setItem(27, filler);
            inventory.setItem(35, filler);
            inventory.setItem(36, filler);
            inventory.setItem(44, filler);
            inventory.setItem(45, filler);
            inventory.setItem(53, filler);

            Month month = LocalDate.now().getMonth();
            int monthNumber = month.getValue();
            int days = 0;


            switch (monthNumber) {
                case 1, 3, 5, 7, 8, 10, 12:
                    days = 31;
                    break;
                case 2:
                    if (LocalDate.now().getChronology().isLeapYear(LocalDate.now().getYear())) {
                        days = 29;
                    } else {
                        days = 28;
                    }
                    break;
                case 4, 6, 11, 9:
                    days = 30;
                    break;
            }
            HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
            List<String> usedPlayers = pss.getConfig().getStringList("players");
            LocalDate firstDayofMonth = LocalDate.now().withDayOfMonth(1);
            DayOfWeek firstDayOfWeek = firstDayofMonth.getDayOfWeek();
            Integer dayofWeek = firstDayOfWeek.getValue();
            int offset = dayofWeek-1;

            int[] daySlots = {
                    10, 11, 12, 13, 14, 15, 16,
                    19, 20, 21, 22, 23, 24, 25,
                    28, 29, 30, 31, 32, 33, 34,
                    37, 38, 39, 40, 41, 42, 43,
                    46, 47, 48, 49, 50, 51, 52
            };
            for (int i = 1; i <= days && i <= daySlots.length; i++) {
                int Todaynr = LocalDate.now().getDayOfMonth();
                int index = offset + (i - 1);
                LocalDate date = LocalDate.now().withDayOfMonth(i);
                int slot = daySlots[index];
                if (Todaynr > i) {
                    ItemStack item = new ItemStack(Material.MINECART);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + date.toString());
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + "§lOdebrałeś lub przegapiłeś to daily!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    items.put(slot, item);
                } else if (Todaynr == i) {
                    if (usedPlayers.contains(p.getUniqueId().toString())) {
                        ItemStack item = new ItemStack(Material.HOPPER_MINECART);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.GOLD + date.toString());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + "§lOdebrałeś już to daily!");
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items.put(slot, item);
                    } else {
                        ItemStack item = new ItemStack(Material.CHEST_MINECART);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + date.toString());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GREEN + "§lKliknij aby odebrać daily!");
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items.put(slot, item);
                    }
                } else if (Todaynr < i) {
                    ItemStack item = new ItemStack(Material.FURNACE_MINECART);
                    List<String> lore = new ArrayList<>();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GRAY + date.toString());
                    lore.add(ChatColor.GRAY + "§lJeszcze nie czas na odebranie tego!");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    items.put(slot, item);
                }

            }
            p.openInventory(inventory);
            for (int i = 0; i < daySlots.length; i += 2) {
                final int firstSlot = daySlots[i];
                final ItemStack firstItem = items.get(firstSlot);

                final int secondSlot = (i + 1 < daySlots.length) ? daySlots[i + 1] : -1;
                final ItemStack secondItem = (secondSlot != -1) ? items.get(secondSlot) : null;

                long delay = (i / 2) + 1L;

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (firstItem != null) {
                        inventory.setItem(firstSlot, firstItem);
                    }

                    if (secondSlot != -1 && secondItem != null) {
                        inventory.setItem(secondSlot, secondItem);
                    }
                }, delay);
            }
        }

        return true;
    }
}
