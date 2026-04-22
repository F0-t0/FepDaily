package fepbox.plugin.daily.utils;

import fepbox.plugin.daily.FepDaily;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.LocalDate;
import java.util.List;

public class GuiHandler implements Listener {
    FepDaily plugin;
    public GuiHandler(FepDaily plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            var clickdItem = e.getCurrentItem();
            if (clickdItem == null) {return;}

            if (e.getView().getTitle().equalsIgnoreCase(ChatColor.GREEN + "Daily")) {
                e.setCancelled(true);
                PlayersStorage pss = new PlayersStorage(plugin, "players.yml");
                List<String> players = pss.getConfig().getStringList("players");
                if (clickdItem.getType() == Material.CHEST_MINECART) {
                    if (!players.contains(p.getUniqueId().toString())) {
                        String day = String.valueOf(LocalDate.now().getDayOfMonth());
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
                } else {
                    p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, 1.0f, 1.0f);
                }
            }
        }
    }
}
