package fepbox.plugin.daily.commands;

import fepbox.plugin.daily.FepDaily;
import fepbox.plugin.daily.utils.DailyGuiSession;
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
import java.util.*;

public class DailyCommand implements CommandExecutor {
    FepDaily plugin;
    private final Map<UUID, DailyGuiSession> sessions = new HashMap<>();

    public DailyCommand(FepDaily plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            boolean isPolish;
            String language = plugin.getConfig().getString("language");
            assert language != null;
            if (language.equals("pl")) {isPolish = true;} else {isPolish = false;}

            Inventory page2 = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Daily (2/2)");
            Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Daily");

            HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
            HashMap<Integer, ItemStack> items2 = new HashMap<Integer, ItemStack>();

            PlayersStorage pss = new PlayersStorage(plugin, "players.yml");
            if (!pss.getConfig().getString("date").equalsIgnoreCase(LocalDate.now().toString())) {
                List<String> players = pss.getConfig().getStringList("date");
                players.clear();
                pss.getConfig().set("date", LocalDate.now().toString());
                pss.getConfig().set("players", players);
                pss.save();
            }

            inventory.clear();
            page2.clear();
            items.clear();
            items2.clear();

            ItemStack filler = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            ItemStack fillerMonday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerTuesday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerWednesday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerThursday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerFriday = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemStack fillerSaturday = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            ItemStack fillerSunday = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);

            // Meta thingies
            ItemMeta fillerMondayMeta = fillerMonday.getItemMeta();
            ItemMeta fillerTuesdayMeta = fillerTuesday.getItemMeta();
            ItemMeta fillerWednesdayMeta = fillerWednesday.getItemMeta();
            ItemMeta fillerThursdayMeta = fillerThursday.getItemMeta();
            ItemMeta fillerFridayMeta = fillerFriday.getItemMeta();
            ItemMeta fillerSaturdayMeta = fillerSaturday.getItemMeta();
            ItemMeta fillerSundayMeta = fillerSunday.getItemMeta();

            if (isPolish) {
                fillerMondayMeta.setDisplayName(ChatColor.GREEN + "Poniedziałek ↓");
                fillerTuesdayMeta.setDisplayName(ChatColor.GREEN + "Wtorek ↓");
                fillerWednesdayMeta.setDisplayName(ChatColor.GREEN + "Środa ↓");
                fillerThursdayMeta.setDisplayName(ChatColor.GREEN + "Czwartek ↓");
                fillerFridayMeta.setDisplayName(ChatColor.GREEN + "Piątek ↓");
                fillerSaturdayMeta.setDisplayName(ChatColor.GOLD + "Sobota ↓");
                fillerSundayMeta.setDisplayName(ChatColor.GOLD + "Niedziela ↓");
            } else {
                fillerMondayMeta.setDisplayName(ChatColor.GREEN + "Monday ↓");
                fillerTuesdayMeta.setDisplayName(ChatColor.GREEN + "Tuesday ↓");
                fillerWednesdayMeta.setDisplayName(ChatColor.GREEN + "Wednesday ↓");
                fillerThursdayMeta.setDisplayName(ChatColor.GREEN + "Thursday ↓");
                fillerFridayMeta.setDisplayName(ChatColor.GREEN + "Friday ↓");
                fillerSaturdayMeta.setDisplayName(ChatColor.GOLD + "Saturday ↓");
                fillerSundayMeta.setDisplayName(ChatColor.GOLD + "Sunday ↓");
            }

            fillerMonday.setItemMeta(fillerMondayMeta);
            fillerTuesday.setItemMeta(fillerTuesdayMeta);
            fillerWednesday.setItemMeta(fillerWednesdayMeta);
            fillerThursday.setItemMeta(fillerThursdayMeta);
            fillerFriday.setItemMeta(fillerFridayMeta);
            fillerSaturday.setItemMeta(fillerSaturdayMeta);
            fillerSunday.setItemMeta(fillerSundayMeta);

            ItemStack previous = new ItemStack(Material.ARROW);
            ItemMeta previousMeta = previous.getItemMeta();
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            if (isPolish) {previousMeta.setDisplayName(ChatColor.GRAY + "Poprzednia strona");
                nextMeta.setDisplayName(ChatColor.GRAY + "Następna strona");}
            else {previousMeta.setDisplayName(ChatColor.GRAY + "Previous Page");
                nextMeta.setDisplayName(ChatColor.GRAY + "Next Page");}


            previous.setItemMeta(previousMeta);
            next.setItemMeta(nextMeta);

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
            inventory.setItem(53, next);

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

            String dailyBefore;
            String dailyAfter;
            String dailyRightNow;
            String dailyClaimed;
            if (isPolish) {
                dailyBefore = "§lOdebrałeś lub przegapiłeś już to daily!";
                dailyAfter = "§lJeszcze nie czas na odebranie tego daily!";
                dailyRightNow = "§lKliknij aby odebrać to daily!";
                dailyClaimed = "§lOdebrałeś już to daily!";
            } else {
                dailyBefore = "§lYou already claimed this daily or you missed it!";
                dailyAfter = "§lIt's not time yet to claim the daily!";
                dailyRightNow = "§lClick here to claim the daily";
                dailyClaimed = "§lYou already claimed the daily!";
            }

            for (int i = 1; i <= days && i <= daySlots.length; i++) {
                int Todaynr = LocalDate.now().getDayOfMonth();
                int index = offset + (i - 1);
                LocalDate date = LocalDate.now().withDayOfMonth(i);
                int slot = 0;
                if (index < 35) {
                    slot = daySlots[index];
                } else {
                    break;
                }
                if (Todaynr > i) {
                    ItemStack item = new ItemStack(Material.MINECART);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + date.toString());
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.RED + dailyBefore);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    items.put(slot, item);
                } else if (Todaynr == i) {
                    if (usedPlayers.contains(p.getUniqueId().toString())) {
                        ItemStack item = new ItemStack(Material.HOPPER_MINECART);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.GOLD + date.toString());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GOLD + dailyClaimed);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items.put(slot, item);
                    } else {
                        ItemStack item = new ItemStack(Material.CHEST_MINECART);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + date.toString());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.GREEN + dailyRightNow);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items.put(slot, item);
                    }
                } else if (Todaynr < i) {
                    ItemStack item = new ItemStack(Material.FURNACE_MINECART);
                    List<String> lore = new ArrayList<>();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.GRAY + date.toString());
                    lore.add(ChatColor.GRAY + dailyAfter);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    items.put(slot, item);
                }
            }
            if (offset > 5) {
                int[] daySlotsp2 = {
                        19,
                        20
                };




                page2.setItem(0, filler);
                page2.setItem(1, fillerMonday);
                page2.setItem(2, fillerTuesday);
                page2.setItem(3, fillerWednesday);
                page2.setItem(4, fillerThursday);
                page2.setItem(5, fillerFriday);
                page2.setItem(6, fillerSaturday);
                page2.setItem(7, fillerSunday);
                page2.setItem(8, filler);
                page2.setItem(9, filler);
                page2.setItem(17, filler);
                page2.setItem(18, previous);
                page2.setItem(26, filler);
                days = days - 29;
                int i2 = 30;
                for (int i = 1; i <= days && i <= daySlotsp2.length; i++) {
                    int Todaynr = LocalDate.now().getDayOfMonth();
                    LocalDate date = LocalDate.now().withDayOfMonth(i+29);
                    int slot = 0;
                    if (i == 1) {
                        slot = 10;
                    } else if (i == 2) {
                        slot = 11;
                    }
                    if (Todaynr > i2) {
                        ItemStack item = new ItemStack(Material.MINECART);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.RED + date.toString());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.RED + dailyBefore);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items2.put(slot, item);
                    } else if (Todaynr == i2) {
                        if (usedPlayers.contains(p.getUniqueId().toString())) {
                            ItemStack item = new ItemStack(Material.HOPPER_MINECART);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.GOLD + date.toString());
                            List<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GOLD + dailyClaimed);
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            items2.put(slot, item);
                        } else {
                            ItemStack item = new ItemStack(Material.CHEST_MINECART);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN + date.toString());
                            List<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GREEN + dailyRightNow);
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            items2.put(slot, item);
                        }
                    } else if (Todaynr < i2) {
                        ItemStack item = new ItemStack(Material.FURNACE_MINECART);
                        List<String> lore = new ArrayList<>();
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.GRAY + date.toString());
                        lore.add(ChatColor.GRAY + dailyAfter);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items2.put(slot, item);
                    }
                    i2++;
                }
            } else if (offset > 4) {
                int[] daySlotsp2 = {
                        10,
                };




                page2.setItem(0, filler);
                page2.setItem(1, fillerMonday);
                page2.setItem(2, fillerTuesday);
                page2.setItem(3, fillerWednesday);
                page2.setItem(4, fillerThursday);
                page2.setItem(5, fillerFriday);
                page2.setItem(6, fillerSaturday);
                page2.setItem(7, fillerSunday);
                page2.setItem(8, filler);
                page2.setItem(9, filler);
                page2.setItem(17, filler);
                page2.setItem(18, previous);
                page2.setItem(26, filler);
                days = days - 29;
                int i2 = 30;
                for (int i = 1; i <= days && i <= daySlotsp2.length; i++) {
                    int Todaynr = LocalDate.now().getDayOfMonth();
                    int index = (i - 1);
                    LocalDate date = LocalDate.now().withDayOfMonth(i+29);
                    int slot = 0;
                    if (i == 1) {
                        slot = 10;
                    }
                    if (Todaynr > i2) {
                        ItemStack item = new ItemStack(Material.MINECART);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.RED + date.toString());
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.RED + dailyBefore);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items2.put(slot, item);
                    } else if (Todaynr == i2) {
                        if (usedPlayers.contains(p.getUniqueId().toString())) {
                            ItemStack item = new ItemStack(Material.HOPPER_MINECART);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.GOLD + date.toString());
                            List<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GOLD + dailyClaimed);
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            items2.put(slot, item);
                        } else {
                            ItemStack item = new ItemStack(Material.CHEST_MINECART);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.GREEN + date.toString());
                            List<String> lore = new ArrayList<>();
                            lore.add(ChatColor.GREEN + dailyRightNow);
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            items2.put(slot, item);
                        }
                    } else if (Todaynr < i2) {
                        ItemStack item = new ItemStack(Material.FURNACE_MINECART);
                        List<String> lore = new ArrayList<>();
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.GRAY + date.toString());
                        lore.add(ChatColor.GRAY + dailyAfter);
                        meta.setLore(lore);
                        item.setItemMeta(meta);
                        items2.put(slot, item);
                    }
                    i2++;
                }
            }
            else {
                inventory.setItem(53, filler);
            }
            sessions.put(p.getUniqueId(), new DailyGuiSession(inventory, page2, items, items2));

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

    public DailyGuiSession getSession(UUID uuid) {
        return sessions.get(uuid);
    }
}