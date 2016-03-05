package me.fromgate.elytra;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class ElytraListener implements Listener{


    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSpeedup(PlayerMoveEvent event){
        if (!Elytra.getCfg().boostEnable) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("elytra.speedup")) return;
        if (!Util.isPlayerGliding(player)) return;
        if (!Util.isElytraWeared (player)) return;
        if (!Util.isBoostAngle(event.getFrom().getPitch())) return;
        if (!Util.isBoostAngle(event.getTo().getPitch())) return;
        Vector vector = player.getVelocity();
        if (vector.length()<Elytra.getCfg().activationSpeedMin) return;
        if (vector.length()>Elytra.getCfg().activationSpeedMax) return;
        player.setVelocity(vector.multiply(Elytra.getCfg().speedUpMult));
        Util.playParticles(player);
        Util.playSound(player);
        Util.processGForce(player);
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onAutoGlide (PlayerMoveEvent event){
        if (!Elytra.getCfg().autoElytra) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("elytra.auto")) return;
        if (!Util.isElytraWeared (player)) return;
        if (Util.isPlayerGliding(player)) return;
        if (player.isFlying()) return;
        if (!Util.checkEmptyBlocks(event.getFrom(),event.getTo())) return;
        Util.setGlide(player);
    }

}
