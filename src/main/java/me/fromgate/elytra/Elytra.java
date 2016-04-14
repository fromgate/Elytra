package me.fromgate.elytra;

import me.fromgate.elytra.util.ElytraConfig;
import me.fromgate.elytra.util.Util;
import org.bukkit.plugin.java.JavaPlugin;

public class Elytra extends JavaPlugin {
    private ElytraConfig cfg;

    private static Elytra plugin;

    public static Elytra getPlugin(){
        return plugin;
    }
    public static ElytraConfig getCfg(){
        return plugin.cfg;
    }

    public void onEnable(){
        plugin = this;
        cfg = new ElytraConfig(this);
        cfg.load();
        Util.init();
        cfg.update();
        if (cfg.runUpEnable) getServer().getPluginManager().registerEvents(new RunUp(),this);
        getServer().getPluginManager().registerEvents(new ElytraListener(),this);
    }
}
