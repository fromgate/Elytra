package me.fromgate.elytra;

import me.fromgate.elytra.util.Time;
import me.fromgate.elytra.util.Util;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ElytraCooldown {

    private static Map<String, Long> cooldowns = new HashMap<String, Long>();

    static void clearCoolDown(Player player, Type type) {
        if (type == null) {
            for (Type t : Type.values()) clearCoolDown(player, t);
        } else if (cooldowns.containsKey(player.getName() + "." + type.name()))
            cooldowns.remove(player.getName() + "." + type.name());
    }


    static boolean checkAndUpdate(Player player, Type type) {
        if (!type.isEnabled()) return true;
        String name = player.getName() + "." + type.name();
        long time = cooldowns.containsKey(name) ? cooldowns.get(name) : 0;
        long currentTime = System.currentTimeMillis();
        if (currentTime - time < type.delay()) {
            Util.playCooldownSound(player);
            return false;
        }
        cooldowns.put(name, currentTime);
        return true;
    }

    enum Type {
        RUN_UP,
        SPEED_UP,
        SHIFT;

        public boolean isEnabled() {
            switch (this) {
                case RUN_UP:
                    return Elytra.getCfg().runUpCooldown;
                case SHIFT:
                    return Elytra.getCfg().shiftCooldownEnable;
            }
            return Elytra.getCfg().cooldownEnable; //SPEED_UP
        }

        public long delay() {
            switch (this) {
                case RUN_UP:
                    return Time.parseTime(Elytra.getCfg().runUpCooldownTime);
                case SHIFT:
                    return Time.parseTime(Elytra.getCfg().shiftCooldownTime);
            }
            return Time.parseTime(Elytra.getCfg().cooldownTime); //SPEED_UP
        }

    }

}