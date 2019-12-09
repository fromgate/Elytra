package me.fromgate.elytra.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.ElytraCooldown;
import me.fromgate.elytra.util.Util;

public class ShiftActivationCheckTask extends BukkitRunnable {	
	@Override
	public void run() {
		if(Bukkit.getServer().getOnlinePlayers()!=null && !Bukkit.getServer().getOnlinePlayers().isEmpty()){			
			for(Player player : Bukkit.getServer().getOnlinePlayers())
			{
		        if (!player.hasPermission("elytra.shift-activation")) return;
		        if(player.hasMetadata("swimming") || player.hasMetadata("falling")) return;
		        if (!Util.isElytraWeared(player)) return;
		        if (!player.isGliding()) return;
		        if (!player.isSneaking()) return;
		        Vector vector = player.getVelocity();
		        if (vector.length() > Elytra.getCfg().shiftActSpeed) return;
		        if (!ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.SHIFT)) return;
		        if(Elytra.getCfg().chargeFirework>0) {
			    	if(Util.containsFireworks(player)) {
			    		Util.removeFirework(player, Elytra.getCfg().chargeFirework);
			    		player.setVelocity(vector.multiply(Elytra.getCfg().shiftMult));
					    Util.playParticles(player);
					    Util.playSound(player);
					    Util.processGForce(player);
					    return;
			    	}
			    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNot Enough Fireworks to Boost"));
			    	return;
			    }
		        player.setVelocity(vector.multiply(Elytra.getCfg().shiftMult));
		        Util.playParticles(player);
		        Util.playSound(player);
		        Util.processGForce(player);
			}
		}
	}
}
