package me.fromgate.elytra.tasks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.ElytraConfig;
import me.fromgate.elytra.util.Util;

public class RunUpCheckTask extends BukkitRunnable {
	
	private HashMap<Player, Location> oldLocale = new HashMap<Player, Location>();	
	private Map<String, Integer> runners = new HashMap<>();
	private ElytraConfig cfg;
	
	public RunUpCheckTask(){
		cfg = Elytra.getCfg();
	}
	
	@Override
	public void run() {
		if(Bukkit.getServer().getOnlinePlayers()!=null && !Bukkit.getServer().getOnlinePlayers().isEmpty()){			
			for(Player player : Bukkit.getServer().getOnlinePlayers())
			{
				if (!player.hasPermission("elytra.runup")) return;
				if (!Util.isElytraWeared(player)) return;
				if (player.isGliding()) return;
				if (player.isFlying()) return;
				Location l = player.getLocation();			
				if(oldLocale.containsKey(player)){
					if (Util.isSameBlocks(oldLocale.get(player), l)) return;
					if (!player.isSprinting()) {
						setRunUpMode(player, false);
						return;
			        }
					if (!timeToJump(player)){
						oldLocale.remove(player);
					    oldLocale.put(player, l);
					    return;
					}
					
			        if (!Util.checkAngle(l.getPitch(), cfg.runUpMinAngle, cfg.runUpMaxAngle)){
			        	oldLocale.remove(player);
					    oldLocale.put(player, l);
					    return;
			        }
			        if (!ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.RUN_UP)){
			        	oldLocale.remove(player);
					    oldLocale.put(player, l);
					    return;
			        }
			        setRunUpMode(player, false);
			        player.setSprinting(false);
			        player.teleport(l.add(0, 1, 0));
			        Vector v = player.getLocation().getDirection();
			        v.multiply(cfg.runUpBoost);
			        player.setVelocity(v);
			        Util.playParticles(player);
			        Util.playSound(player);
			        Bukkit.getScheduler().runTaskLater(Elytra.getPlugin(), () -> player.setGliding(true), 5);
			        oldLocale.remove(player);
					oldLocale.put(player, l);
				}else{
					oldLocale.put(player, l);
				}
			}
		}
		
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