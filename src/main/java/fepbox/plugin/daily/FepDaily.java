package fepbox.plugin.daily;

import fepbox.plugin.daily.commands.DailyCommand;
import fepbox.plugin.daily.utils.GuiHandler;
import fepbox.plugin.daily.utils.PlayersStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public final class FepDaily extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        List<String> players = new ArrayList<String>();
        players.add(null);
        PlayersStorage pss = new PlayersStorage(this, "players.yml");
        if (!pss.getConfig().contains("players")) {
            pss.getConfig().set("players", players);
        }
        if (!pss.getConfig().contains("date")) {
            pss.getConfig().set("date", LocalDate.now().toString());
        }
        pss.save();


        getServer().getPluginManager().registerEvents(new GuiHandler(this), this);
        getCommand("daily").setExecutor(new DailyCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
