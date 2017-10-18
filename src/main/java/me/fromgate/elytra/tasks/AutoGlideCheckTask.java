package me.fromgate.elytra.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import me.fromgate.elytra.Elytra;
import me.fromgate.elytra.util.Util;

public class AutoGlideCheckTask extends BukkitRunnable {	
	private HashMap<Player, Location> oldLocale = new HashMap<Player, Location>();	
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
												if(Util.hasElytraStorage(player)){
													autoEquip(player);
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
	
	private void autoEquip(Player player){
		PlayerInventory inv = player.getInventory();
		List<ItemStack> storage = new ArrayList<ItemStack>();
		ItemStack chestplate = new ItemStack(Material.AIR);
		ItemStack elytra = new ItemStack(Material.AIR);       	
		if(inv.getChestplate()!=null && inv.getChestplate().getType()!=Material.AIR){
			chestplate = inv.getChestplate();
			inv.setChestplate(new ItemStack(Material.AIR));
		}
		for(ItemStack item : inv.getStorageContents()){
			storage.add(item);        		
		}
		for(ItemStack item : storage){
			if(item!=null){
				if(item.getType().equals(Material.ELYTRA)){
					elytra = item;
					break;
				}
			}   		
		}
		storage.remove(elytra);
		if(chestplate.getType()!=Material.AIR){
			storage.add(chestplate);
		}
		player.getInventory().setStorageContents(Util.listToArray(storage));
		inv.setChestplate(elytra);
		player.sendMessage(ChatColor.GREEN + "Elytra Auto-Equipped");
	}
}
