package me.fromgate.elytra.tasks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.Util;

public class BoostCheckTask extends BukkitRunnable {
	
	private HashMap<Player, Location> oldLocale = new HashMap<Player, Location>();
	
	@Override
	public void run() {
		if(Bukkit.getServer().getOnlinePlayers()!=null && !Bukkit.getServer().getOnlinePlayers().isEmpty()){			
			for(Player player : Bukkit.getServer().getOnlinePlayers())
			{
				if (!player.hasPermission("elytra.speedup")) return;
		        if (!player.isGliding()) return;
		        if (!Util.isElytraWeared(player)) return;
		        if(player.hasMetadata("swimming") || player.hasMetadata("falling")) return;
				Location l = player.getLocation();				
				if(oldLocale.containsKey(player)){
					if(!Util.isSameBlocks(oldLocale.get(player), l)){
						Vector vector = player.getVelocity();				
					    if (!Util.isBoostAngle(oldLocale.get(player).getPitch())){
					    	oldLocale.remove(player);
						    oldLocale.put(player, l);
					    	return;
					    } 
					    if (!Util.isBoostAngle(l.getPitch())){
					    	oldLocale.remove(player);
						    oldLocale.put(player, l);
					    	return;
					    }
					    if (vector.length() < Elytra.getCfg().activationSpeedMin){
					    	oldLocale.remove(player);
						    oldLocale.put(player, l);
					    	return;
					    } 
					    if (vector.length() > Elytra.getCfg().activationSpeedMax){
					    	oldLocale.remove(player);
						    oldLocale.put(player, l);
					    	return;
					    }
					    if (!ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.SPEED_UP)){
					    	oldLocale.remove(player);
						    oldLocale.put(player, l);
					    	return;
					    }
					    if(Elytra.getCfg().chargeFirework>0) {
					    	if(Util.containsFireworks(player)) {
					    		Util.removeFirework(player, Elytra.getCfg().chargeFirework);
					    		player.setVelocity(vector.multiply(Elytra.getCfg().speedUpMult));
							    Util.playParticles(player);
							    Util.playSound(player);
							    Util.processGForce(player);
							    return;
					    	}
					    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot Enough Fireworks to Boost"));
					    	return;
					    }
					    player.setVelocity(vector.multiply(Elytra.getCfg().speedUpMult));
					    Util.playParticles(player);
					    Util.playSound(player);
					    Util.processGForce(player);
					    oldLocale.remove(player);
					    oldLocale.put(player, l);
					}									
				}else{
					oldLocale.put(player, l);
				}
			}
		}
	}
}
