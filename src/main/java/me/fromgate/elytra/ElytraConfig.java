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
    public double activationSpeedMin = 0.5;

    @Path (value = "speed-up.activation.speed-max")
    public double activationSpeedMax = 1.5;

    @Path (value = "speed-up.multiplier")
    public double speedUpMult = 1.3;

    @Path (value = "g-force.damage-elytra")
    public int gforceBreakElytra=10;

    @Path (value = "g-force.damage-player")
    public double gforceDamagePlayer=2;

    @Path(value = "particles.enable")
    public boolean particles = true;

    @Path(value = "particles.type")
    public String particleType = "SPELL_WITCH";

    @Path(value = "particles.radius")
    public double particleRadius = 0.3;

    @Path(value = "particles.amount")
    public int particleAmount = 15;

    @Path(value = "particles.extra-param")
    public double particleExtra= 0;

    @Path(value = "particles.play-repeat")
    public int particlesCount = 3;

    @Path(value = "sound.enable")
    public boolean soundEnable=true;

    @Path(value = "sound.type")
    public String soundType="ENTITY_BAT_TAKEOFF";

    @Path(value = "sound.pitch")
    public float soundPitch=0.8f;

    @Path(value = "sound.volume")
    public float soundVolume=0.5f;

    @Path(value = "sound.play-repeat")
    public int soundRepeatCount=3;

    @Path(value = "sound.play-delay-ticks")
    public int soundDelay=3;


}
