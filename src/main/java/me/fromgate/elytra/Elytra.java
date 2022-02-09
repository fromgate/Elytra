package me.fromgate.elytra;

import me.fromgate.elytra.tasks.AutoGlideCheckTask;
import me.fromgate.elytra.tasks.BoostCheckTask;
import me.fromgate.elytra.tasks.ConstSpeedCheckTask;
import me.fromgate.elytra.tasks.RunUpCheckTask;
import me.fromgate.elytra.tasks.ShiftActivationCheckTask;
import me.fromgate.elytra.util.ElytraConfig;
import me.fromgate.elytra.util.Util;

import org.bukkit.plugin.java.JavaPlugin;

public class Elytra extends JavaPlugin {
    private ElytraConfig cfg;

    private static Elytra plugin;

    public static Elytra getPlugin() {
        return plugin;
    }

    public static ElytraConfig getCfg() {
        return plugin.cfg;
    }

    public void onEnable() {
        plugin = this;
        cfg = new ElytraConfig(this);
        cfg.load();
        Util.init();
        cfg.update();
        if(cfg.constSpeedEnable){
        	ConstSpeedCheckTask task = new ConstSpeedCheckTask();
            task.runTaskTimer(plugin, 20, 13);
        }
        if(cfg.boostEnable){
        	BoostCheckTask task = new BoostCheckTask();
        	task.runTaskTimer(getPlugin(), 20, 8);
        }
        if(cfg.shiftActivation){
        	ShiftActivationCheckTask task = new ShiftActivationCheckTask();
        	task.runTaskTimer(plugin, 20, 10);
        }
        if(cfg.autoElytra){
        	AutoGlideCheckTask task = new AutoGlideCheckTask();
        	task.runTaskTimer(plugin, 20, 8);
        }
        if (cfg.runUpEnable){
        	RunUpCheckTask task = new RunUpCheckTask();
        	task.runTaskTimer(plugin, 20, 6);
        }       
    }
}
