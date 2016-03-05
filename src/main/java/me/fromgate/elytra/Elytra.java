package me.fromgate.elytra;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class Elytra extends JavaPlugin {
    private ElytraConfig cfg;
    Particle particle;
    Sound sound;

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
        this.particle = Util.parseParticle(cfg.particleType);
        cfg.particleType = particle.name();
        this.sound = Util.parseSound(cfg.soundType);
        cfg.soundType =sound.name();
        cfg.save();
        getServer().getPluginManager().registerEvents(new ElytraListener(),this);
    }

}
