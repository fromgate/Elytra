package me.fromgate.elytra.tasks;

import org.bukkit.Bukkit;
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
		        if (!player.isSneaking()) return;
		        if (!player.isGliding()) return;
		        if (!Util.isElytraWeared(player)) return;
		        Vector vector = player.getVelocity();
		        if (vector.length() > Elytra.getCfg().shiftActSpeed) return;
		        if (!ElytraCooldown.checkAndUpdate(player, ElytraCooldown.Type.SHIFT)) return;
		        player.setVelocity(vector.multiply(Elytra.getCfg().shiftMult));
		        Util.playParticles(player);
		        Util.playSound(player);
		        Util.processGForce(player);
			}
		}
	}
}
