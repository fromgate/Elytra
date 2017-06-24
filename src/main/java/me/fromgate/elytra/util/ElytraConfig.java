package me.fromgate.elytra.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ElytraConfig extends SimpleConfig {

    public ElytraConfig(JavaPlugin plugin) {
        super(plugin);
    }

    // Auto-glide    
    @Path(value = "auto-glide.enable")
    public boolean autoElytra = true;

    @Path(value = "auto-glide.required-air-under-player")
    public int autoElytraEmpty = 3;

    // Constant-flight 
    @Path(value = "constant-flight.enable")
    public boolean constSpeedEnable = false;

    @Path(value = "constant-flight.velocity")
    public double isConstSpeed = 1.1;

    // Activation by "shift-key"
    @Path(value = "shift-speed-up.enable")
    public boolean shiftActivation = true;

    @Path(value = "shift-speed-up.max-activation-speed")
    public double shiftActSpeed = 2;

    @Path(value = "shift-speed-up.velocity-multiplier")
    public double shiftMult = 1.5;

    @Path(value = "shift-speed-up.cooldown.enable")
    public boolean shiftCooldownEnable = true;

    @Path(value = "shift-speed-up.cooldown.time")
    public String shiftCooldownTime = "1s";

    // Speed-up (angle)
    @Path(value = "speed-up.enable")
    public boolean boostEnable = true;

    @Path(value = "speed-up.cooldown.enable")
    public boolean cooldownEnable = true;

    @Path(value = "speed-up.cooldown.time")
    public String cooldownTime = "3s";

    @Path(value = "speed-up.activation.angle-min")
    public int minAngle = 40;

    @Path(value = "speed-up.activation.angle-max")
    public int maxAngle = 80;

    @Path(value = "speed-up.activation.speed-min")
    public double activationSpeedMin = 0.5;

    @Path(value = "speed-up.activation.speed-max")
    public double activationSpeedMax = 1.5;

    @Path(value = "speed-up.activation.multiplier")
    public double speedUpMult = 1.3;

    // Run-up
    @Path(value = "run-up.enable")
    public boolean runUpEnable = true;

    @Path(value = "run-up.activation.angle-min")
    public int runDistance = 8;

    @Path(value = "run-up.activation.angle-min")
    public int runUpMinAngle = 30;

    @Path(value = "run-up.activation.angle-max")
    public int runUpMaxAngle = 70;

    @Path(value = "run-up.boost")
    public double runUpBoost = 1.5;

    @Path(value = "run-up.cooldown.enable")
    public boolean runUpCooldown = true;

    @Path(value = "run-up.cooldown.time")
    public String runUpCooldownTime = "3s";

    // Debuff
    @Path(value = "g-force.damage-elytra")
    public int gforceBreakElytra = 10;

    @Path(value = "g-force.damage-player")
    public double gforceDamagePlayer = 2;


    // Visual effects
    @Path(value = "particles.enable")
    public boolean particles = true;

    @Path(value = "particles.type")
    public String particleType = "SPELL_WITCH";

    @Path(value = "particles.radius")
    public double particleRadius = 0.3;

    @Path(value = "particles.amount")
    public int particleAmount = 15;

    @Path(value = "particles.extra-param")
    public double particleExtra = 0;

    @Path(value = "particles.play-repeat")
    public int particlesCount = 3;


    // Sound effects
    @Path(value = "sound.enable")
    public boolean soundEnable = true;

    @Path(value = "sound.activation.type")
    public String soundType = "ENTITY_BAT_TAKEOFF";

    @Path(value = "sound.activation.pitch")
    public float soundPitch = 0.8f;

    @Path(value = "sound.activation.volume")
    public float soundVolume = 0.5f;

    @Path(value = "sound.activation.play-repeat")
    public int soundRepeatCount = 3;

    @Path(value = "sound.activation.play-delay-ticks")
    public int soundDelay = 3;

    @Path(value = "sound.cooldown-fail.type")
    public String cooldownFailSound = "UI_BUTTON_CLICK";

    @Path(value = "sound.cooldown-fail.pitch")
    public float cooldownFailPitch = 0.8f;

    @Path(value = "sound.cooldown-fail.volume")
    public float cooldownFailVolume = 0.5f;


    public void update() {
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(this.configFile);
        } catch (Exception e) {
            return;
        }
        if (cfg.get("constant-flight.enable") == null) this.save();
    }

}
