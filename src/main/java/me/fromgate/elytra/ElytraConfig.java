package me.fromgate.elytra;

import org.bukkit.plugin.java.JavaPlugin;

public class ElytraConfig extends SimpleConfig {
    public ElytraConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Path (value = "speed-up.enable")
    public boolean boostEnable = true;

    @Path (value = "speed-up.activation.angle-min")
    public int minAngle = 40;


    @Path (value = "speed-up.activation.angle-max")
    public int maxAngle = 80;

    @Path (value = "speed-up.activation.speed-min")
    public double activationSpeedMin = 0.7;

    @Path (value = "speed-up.activation.speed-max")
    public double activationSpeedMax = 1.5;

    @Path (value = "speed-up.multiplier")
    public double speedUpMult = 1.3;





}
