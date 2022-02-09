package me.fromgate.elytra.util;

import me.fromgate.elytra.Elytra;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Util {

    private static ElytraConfig cfg;
    private static Particle particle;
    private static Sound sound;
    static long cooldownTime;


    public static void init() {
        cfg = Elytra.getCfg();
        particle = Util.parseParticle(cfg.particleType);
        cfg.particleType = particle.name();
        sound = Util.parseSound(cfg.soundType);
        cfg.soundType = sound.name();
        cooldownTime = Time.parseTime(cfg.cooldownTime);
    }

    public static boolean isBoostAngle(double pitch) {
        return checkAngle(pitch, cfg.minAngle, cfg.maxAngle);
    }

    public static boolean checkAngle(double pitch, int min, int max) {
        pitch = -pitch;
        if (min > (pitch)) return false;
        if (max < (pitch)) return false;
        return true;
    }

    public static void playSound(final Player player) {
        if (!cfg.soundEnable) return;
        player.getWorld().playSound(player.getLocation(), sound, cfg.soundVolume, cfg.soundPitch);
        if (cfg.soundRepeatCount <= 0) return;
        for (int i = 0; i < cfg.soundRepeatCount; i++)
            Bukkit.getScheduler().runTaskLater(Elytra.getPlugin(), () -> {
                if (player.isDead() || !player.isOnline() || player.isFlying() || player.isOnGround()) return;
                player.getWorld().playSound(player.getLocation(), sound, cfg.soundVolume, cfg.soundPitch);
            }, i * cfg.soundDelay);
    }

    public static void playCooldownSound(Player player) {
        if (!cfg.soundEnable) return;
        Sound s = parseSound(cfg.cooldownFailSound);
        player.getWorld().playSound(player.getLocation(), s, cfg.cooldownFailVolume, cfg.cooldownFailPitch);
    }

    public static void playParticles(final Player player) {
        if (!cfg.particles) return;
        playParticle(player);
        if (cfg.particlesCount <= 0) return;
        for (int i = 1; i <= cfg.particlesCount; i++)
            Bukkit.getScheduler().runTaskLater(Elytra.getPlugin(), () -> {
                if (player.isDead() || !player.isOnline() || player.isFlying() || player.isOnGround()) return;
                playParticle(player);
            }, i);
    }

    static void playParticle(Player player) {
        player.getWorld().spawnParticle(particle, player.getLocation(),
                cfg.particleAmount,
                cfg.particleRadius,
                cfg.particleRadius,
                cfg.particleRadius,
                cfg.particleExtra);
    }

    static Particle parseParticle(String particleStr) {
        for (Particle pt : Particle.values())
            if (pt.name().equalsIgnoreCase(particleStr)) return pt;
        return Particle.SPELL_WITCH;
    }

    static Sound parseSound(String soundStr) {
        for (Sound s : Sound.values())
            if (s.name().equalsIgnoreCase(soundStr)) return s;
        return Sound.ENTITY_BAT_TAKEOFF;
    }

    public static void processGForce(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
        if (cfg.gforceBreakElytra > 0 && !player.hasPermission("elytra.damage-elytra.bypass")) {

            ItemStack elytra = player.getInventory().getChestplate();

            int durability = elytra.getDurability() + cfg.gforceBreakElytra;
            int maxDur = elytra.getType().getMaxDurability() == 0 ? 431 : elytra.getType().getMaxDurability();
            if (durability >= maxDur) durability = maxDur - 1;
            elytra.setDurability((short) durability);
            player.getInventory().setChestplate(elytra);
        }
        if (cfg.gforceDamagePlayer > 0 && !player.hasPermission("elytra.damage-player.bypass")) {
            double health = player.getHealth() - cfg.gforceDamagePlayer;
            if (health < 0) health = 0;
            player.damage(player.getHealth() - health);
        }
    }

    public static boolean isElytraWeared(Player player) {
        if (player.getInventory().getChestplate() == null) return false;
        if (player.getInventory().getChestplate().getType() != Material.ELYTRA) return false;
        if (player.getInventory().getChestplate().getDurability() >= 431) return false;
        return true;
    }
    
    public static boolean hasElytraStorage(Player player) {
    	PlayerInventory inv = player.getInventory();
    	if(inv.getContents()!=null){
    		for(ItemStack item : inv.getContents()){
    			if(item!=null){
    				if(!item.getType().equals(Material.AIR)){
            			if(item.getType().equals(Material.ELYTRA)){
            				if(item.getDurability() <= 431)
            					return true;
            			}
            		}
    			}     		
        	}
    	}
    	return false;
    }
    
    public static boolean containsFireworks(Player player) {
    	if(player.getInventory().getContents()!=null){
    		if(player.getInventory().contains(Material.FIREWORK_ROCKET)) {
    			for(ItemStack item : player.getInventory().getContents()){
        			if(item!=null && !item.getType().equals(Material.AIR)){
                		if(item.getType().equals(Material.FIREWORK_ROCKET)){
                			if(item.getAmount()>=Elytra.getCfg().chargeFirework) {
                	    		return true;
                	    	}
                		}
        			}
    			}
    		}
    	}  	
		return false;  	
    }
    
    public static void removeFirework(Player player, int amount) {
    	PlayerInventory inv = player.getInventory();
    	for(ItemStack item : inv.getContents()){
        	if(item!=null){
        		if(!item.getType().equals(Material.AIR)){
           			if(item.getType().equals(Material.FIREWORK_ROCKET)){
           				if(item.getAmount()>=amount) {
           					inv.remove(item);
           					if(item.getAmount()-amount > 0) {
           						item.setAmount(item.getAmount()-amount);
           						inv.addItem(item);
           					}
           					return;
           				}
           			}
        		}
        	}   		
        }
    }
    
    public static void autoEquip(Player player){
		PlayerInventory inv = player.getInventory();
		ItemStack chestplate = new ItemStack(Material.AIR);
		ItemStack elytra = new ItemStack(Material.AIR);       	
		if(inv.getChestplate()!=null && inv.getChestplate().getType()!=Material.AIR){
			chestplate = inv.getChestplate();
			inv.setChestplate(new ItemStack(Material.AIR));
		}	
		for(ItemStack item : inv.getContents()){
			if(item!=null){
				if(item.getType().equals(Material.ELYTRA)){
					elytra = item;
					break;
				}
			}   		
		}
		inv.remove(elytra);
		if((elytra.getAmount()-1)>0) {
			elytra.setAmount(elytra.getAmount() -1);
			inv.addItem(elytra);
		}
		if(chestplate.getType()!=Material.AIR){
			inv.addItem(chestplate);
		}
		elytra.setAmount(1);
		inv.setChestplate(elytra);
		player.sendMessage(ChatColor.GREEN + "Elytra Auto-Equipped");
	}
    
    public static boolean checkEmptyBlocks(Location from, Location to) {
        if (from.getBlockY() - to.getBlockY() < 1) return false;
        Block bf = from.getBlock();
        Block tf = to.getBlock();
        for (int i = 0; i <= cfg.autoElytraEmpty; i++) {
            bf = bf.getRelative(0, i == 0 ? 0 : -1, 0);
            if (bf.getType() != Material.AIR) return false;
            tf = tf.getRelative(0, i == 0 ? 0 : -1, 0);
            if (tf.getType() != Material.AIR) return false;
        }
        return true;
    }

    public static boolean isSameBlocks(Location loc1, Location loc2) {
        if (loc1.getBlockX() != loc2.getBlockX()) return false;
        if (loc1.getBlockZ() != loc2.getBlockZ()) return false;
        if (loc1.getBlockY() != loc2.getBlockY()) return false;
        if (loc1.getWorld() != loc2.getWorld()) return false;
        return true;
    }
}
