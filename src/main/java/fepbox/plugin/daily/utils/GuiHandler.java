package fepbox.plugin.daily.utils;

import fepbox.plugin.daily.FepDaily;
import fepbox.plugin.daily.commands.DailyCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class GuiHandler implements Listener {
    FepDaily plugin;
    DailyCommand daily;
    public GuiHandler(FepDaily plugin, DailyCommand daily) {
        this.plugin = plugin;
        this.daily = daily;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            var clickdItem = e.getCurrentItem();
            if (clickdItem == null) {return;}

            DailyGuiSession session = daily.getSession(p.getUniqueId());

            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Daily")) {
                e.setCancelled(true);
                PlayersStorage pss = new PlayersStorage(plugin, "players.yml");
                List<String> players = pss.getConfig().getStringList("players");
                if (clickdItem.getType() == Material.CHEST_MINECART) {
                    if (!players.contains(p.getUniqueId().toString())) {
                        String day = String.valueOf(LocalDate.now().getDayOfWeek().getValue());
                        List<String> cmds = plugin.getConfig().getStringList("commands."+day);
                        for (String cmd : cmds) {
                            String parsedCommand = cmd.replace("%player%", e.getWhoClicked().getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
                        }
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        String message = plugin.getConfig().getString("broadcast");
                        String parsedMessage = message.replace("%player%", e.getWhoClicked().getName());
                        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', parsedMessage));
                    } else {
                        p.sendMessage(ChatColor.RED + "Już odebrałeś Daily!!");
                    }
                    p.closeInventory();
                    players.add(p.getUniqueId().toString());
                    pss.getConfig().set("players", players);
                    pss.save();
                } else if (clickdItem.getType() == Material.ARROW) {
                    p.closeInventory();
                    p.openInventory(session.page2());
                    int[] daySlotsp2 = {
                            10, 11, 12, 13, 14, 15, 16,
                            19, 20, 21, 22, 23, 24, 25,
                    };
                    Inventory inventory = session.page2();
                    HashMap<Integer, ItemStack> items = session.items2();
                    for (int i = 0; i < inventory.getSize(); i++) {
                        if (items.containsKey(i)) {
                            session.page2().setItem(i, items.get(i));
                        }
                    }
                } else {
                    p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
                }
            } else if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Daily (2/2)")) {
                e.setCancelled(true);
                PlayersStorage pss = new PlayersStorage(plugin, "players.yml");
                List<String> players = pss.getConfig().getStringList("players");

                switch (clickdItem.getType()) {
                    case ARROW -> {
                        p.closeInventory();
                        p.openInventory(session.page1());
                    }
                    case CHEST_MINECART -> {
                        if (!players.contains(p.getUniqueId().toString())) {
                            String day = String.valueOf(LocalDate.now().getDayOfWeek().getValue());
                            List<String> cmds = plugin.getConfig().getStringList("commands."+day);
                            for (String cmd : cmds) {
                                String parsedCommand = cmd.replace("%player%", e.getWhoClicked().getName());
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        } else {
                            p.sendMessage(ChatColor.RED + "Już odebrałeś Daily!!");
                        }
                        p.closeInventory();
                        players.add(p.getUniqueId().toString());
                        pss.getConfig().set("players", players);
                        pss.save();
                    }

                }
            }
        }
    }
}
