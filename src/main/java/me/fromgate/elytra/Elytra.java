package me.fromgate.elytra;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Elytra extends JavaPlugin implements Listener{
    private ElytraConfig cfg;
    private Particle particle;
    private Sound sound;

    public void onEnable(){
        cfg = new ElytraConfig(this);
        cfg.load();
        this.particle = parseParticle(cfg.particleType);
        cfg.particleType = particle.name();
        this.sound = parseSound(cfg.soundType);
        cfg.soundType =sound.name();
        cfg.save();
        getServer().getPluginManager().registerEvents(this,this);
    }

    @SuppressWarnings("deprecation")
    @EventHandler (ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSpeedup(PlayerMoveEvent event){
        if (!cfg.boostEnable) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("elytra.speedup")) return;
        if (player.isFlying()) return;
        if (!isBoostAngle(event.getFrom().getPitch())) return;
        if (!isBoostAngle(event.getTo().getPitch())) return;
        Vector vector = player.getVelocity();
        if (vector.length()<cfg.activationSpeedMin) return;
        if (vector.length()>cfg.activationSpeedMax) return;
        if (player.getInventory().getChestplate()==null||
                player.getInventory().getChestplate().getType()!= Material.ELYTRA) return;
        player.setVelocity(vector.multiply(cfg.speedUpMult));
        playParticles(player);
        playSound(player);
        processGForce(player);
    }


    private boolean isBoostAngle(double pitch){
        if (cfg.minAngle>(-1*pitch)) return false;
        if (cfg.maxAngle<(-1*pitch)) return true;
        return true;
    }

    private void playSound(final Player player){
        if (!cfg.soundEnable) return;
        player.getWorld().playSound(player.getLocation(),sound,cfg.soundVolume,cfg.soundPitch);
        if (cfg.soundRepeatCount<=0) return;
        for (int i=0;i<cfg.soundRepeatCount;i++)
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @SuppressWarnings("deprecation")
                public void run() {
                    if (player.isDead()||!player.isOnline()||player.isFlying()||player.isOnGround()) return;
                    player.getWorld().playSound(player.getLocation(),sound,cfg.soundVolume,cfg.soundPitch);
                }
            },i*cfg.soundDelay);
    }

    @SuppressWarnings("deprecation")
    private void playParticles(final Player player){
        if (!cfg.particles) return;
        playParticle (player);
        if (cfg.particlesCount<=0) return;
        for (int i=1;i<=cfg.particlesCount;i++)
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                if (player.isDead()||!player.isOnline()||player.isFlying()||player.isOnGround()) return;
                playParticle (player);
            }
        },i);
    }

    private void playParticle (Player player){
        player.getWorld().spawnParticle(particle,player.getLocation(),cfg.particleAmount,cfg.particleRadius,cfg.particleRadius,cfg.particleRadius,cfg.particleExtra);
    }

    private Particle parseParticle (String particleStr){
        for (Particle pt : Particle.values())
            if (pt.name().equalsIgnoreCase(particleStr)) return pt;
        return Particle.SPELL_WITCH;
    }

    private Sound parseSound (String soundStr){
        for (Sound s : Sound.values())
            if (s.name().equalsIgnoreCase(soundStr)) return s;
        return Sound.ENTITY_BAT_TAKEOFF;
    }

    private void processGForce(Player player){
        if (player.getGameMode()== GameMode.CREATIVE||player.getGameMode()== GameMode.SPECTATOR) return;
        if (cfg.gforceBreakElytra>0){
            ItemStack elytra = player.getInventory().getChestplate();
            int durability = elytra.getDurability()+cfg.gforceBreakElytra;
            int maxDur =elytra.getType().getMaxDurability()==0? 431 : elytra.getType().getMaxDurability();
            if (durability>=maxDur) durability = maxDur-1;
            elytra.setDurability((short) durability);
            player.getInventory().setChestplate(elytra);
        }
        if (cfg.gforceDamagePlayer>0){
            double health = player.getHealth()-cfg.gforceDamagePlayer;
            if (health<0) health=0;
            player.damage(player.getHealth()-health);
        }
    }
}
