package me.fromgate.elytra;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Elytra extends JavaPlugin implements Listener{
    private ElytraConfig cfg;

    public void onEnable(){
        cfg = new ElytraConfig(this);
        cfg.load();
        cfg.save();
        getServer().getPluginManager().registerEvents(this,this);
    }

    @SuppressWarnings("deprecation")
    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSpeedup(PlayerMoveEvent event){
        if (!cfg.boostEnable) return;
        Player player = event.getPlayer();
        if (player.isFlying()) return;
        if (!isBoostAngle(event.getFrom().getPitch())) return;
        if (!isBoostAngle(event.getTo().getPitch())) return;
        Vector vector = player.getVelocity();
        if (vector.length()<cfg.activationSpeedMin) return;
        if (vector.length()>cfg.activationSpeedMax) return;
        if (player.getInventory().getChestplate()==null||
                player.getInventory().getChestplate().getType()!= Material.ELYTRA) return;
        player.setVelocity(vector.multiply(cfg.speedUpMult));
    }


    private boolean isBoostAngle(double pitch){
        if (cfg.minAngle<(-1*pitch)) return false;
        if (cfg.maxAngle>(-1*pitch)) return true;
        return true;
    }
}
