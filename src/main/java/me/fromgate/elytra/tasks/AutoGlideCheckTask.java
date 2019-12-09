package me.fromgate.elytra.tasks;

import java.util.HashMap;
import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoGlideCheckTask extends BukkitRunnable {	
	private Map<Player, Location> oldLocale = new HashMap<>();
	@Override
	public void run() {
		if(Bukkit.getServer().getOnlinePlayers()!=null && !Bukkit.getServer().getOnlinePlayers().isEmpty()){ 
			for(Player player : Bukkit.getServer().getOnlinePlayers())
			{
				if (player.hasPermission("elytra.auto")){
					if(!player.hasMetadata("swimming") && !player.hasMetadata("falling")){
						Location l = player.getLocation();
						if(oldLocale.containsKey(player)){
							if(!Util.isSameBlocks(l, oldLocale.get(player))){
								if(Util.checkEmptyBlocks(oldLocale.get(player), l)){
									if(!Util.isElytraWeared(player)){
										if(Elytra.getCfg().autoElytraEquip)
										{
											if(player.hasPermission("elytra.auto-equip")){
												if(Util.hasElytraStorage(player)) {
													Util.autoEquip(player);
												}else {
													//no elytra to autoequip (maybe a message)
												}											
											}
										}        	
									}
									if (!player.isGliding()){
										if (!player.isFlying()){
											player.setGliding(true);
										}
									}
								}
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
	}
	
}
