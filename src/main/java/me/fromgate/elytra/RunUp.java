package me.fromgate.elytra;

import me.fromgate.elytra.util.ElytraConfig;
import me.fromgate.elytra.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class RunUp implements Listener {
    private ElytraConfig cfg;
    private Map<String, Integer> runners = new HashMap<String, Integer>();

    public RunUp() {
        this.cfg = Elytra.getCfg();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onRunUp(PlayerMoveEvent event) {
        if (!cfg.runUpEnable) return;
        final Player player = event.getPlayer();
        if (!player.hasPermission("elytra.runup")) return;
        if (!Util.isElytraWeared(player)) return;
        if (player.isGliding()) return;
        if (player.isFlying()) return;
        if (Util.isSameBlocks(event.getFrom(), event.getTo())) return;
        if (!player.isSprinting()) {
            setRunUpMode(player, false);
            return;
        }
        if (!timeToJump(player)) return;
        if (!Util.checkAngle(event.getPlayer().getLocation().getPitch(), cfg.runUpMinAngle, cfg.runUpMaxAngle)) return;
        if (!ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.RUN_UP)) return;
        setRunUpMode(player, false);
        player.setSprinting(false);
        event.setTo(event.getTo().add(0, 1, 0));
        Vector v = player.getLocation().getDirection();
        v.multiply(cfg.runUpBoost);
        player.setVelocity(v);
        Util.playParticles(player);
        Util.playSound(player);
        Bukkit.getScheduler().runTaskLater(Elytra.getPlugin(), new Runnable() {
            public void run() {
                player.setGliding(true);
            }
        }, 5);
    }

    private boolean timeToJump(Player player) {
        if (!runners.containsKey(player.getName())) setRunUpMode(player, true);
        int count = runners.get(player.getName());
        if (count < cfg.runDistance) {
            runners.put(player.getName(), count + 1);
            return false;
        }
        return true;
    }

    private void setRunUpMode(Player player, boolean mode) {
        if (mode) runners.put(player.getName(), 0);
        else if (runners.containsKey(player.getName())) runners.remove(player.getName());
    }

}
