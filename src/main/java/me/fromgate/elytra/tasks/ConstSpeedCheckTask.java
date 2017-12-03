package me.fromgate.elytra.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.util.Util;

public class ConstSpeedCheckTask extends BukkitRunnable {	
	@Override
	public void run() {
		if(Bukkit.getServer().getOnlinePlayers()!=null && !Bukkit.getServer().getOnlinePlayers().isEmpty()){
			
			for(Player player : Bukkit.getServer().getOnlinePlayers())
			{		
		        if (!player.hasPermission("elytra.const-flight")) return;
		        if (!player.isGliding()) return;
		        if (!Util.isElytraWeared(player)) return;
		        if(player.hasMetadata("swimming") || player.hasMetadata("falling")) return;
		        Vector vector = player.getVelocity();
		        if (vector.length() > Elytra.getCfg().isConstSpeed) return;
		        player.setVelocity(vector.normalize().multiply(Elytra.getCfg().isConstSpeed));
			}
		}			
	}	
}
