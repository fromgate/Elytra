package me.fromgate.elytra.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class SimpleConfig {

    File configFile;

    public SimpleConfig(JavaPlugin plugin) {
        this(plugin, "config.yml");
    }

    public SimpleConfig(JavaPlugin plugin, String fileName) {
        this(new File(plugin.getDataFolder() + File.separator + fileName));
        try {
            plugin.saveResource(fileName,false);
        } catch (Exception e){
            plugin.getLogger().info("Resource "+fileName+" was not found. Default value will be used.");
        }
    }

    public SimpleConfig(File file) {
        this.configFile = file;
        configFile.getParentFile().mkdirs();
    }

    public boolean save() {
        if (configFile.exists()) try {
            configFile.createNewFile();
        } catch (Exception e) {
            return false;
        }
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(configFile);
        } catch (Exception ignore) {
        }
        for (Field field : this.getClass().getDeclaredFields()) {
            String path = getPath(field);
            try {
                if (path != null) cfg.set(path, field.get(this));
            } catch (Exception e) {
                return false;
            }
        }
        try {
            cfg.save(configFile);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean load() {
        if (!this.configFile.exists()) return false;
        YamlConfiguration cfg = new YamlConfiguration();
        try {
            cfg.load(configFile);
        } catch (Exception e) {
            return false;
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getName().equals("configFile")) continue;
            String path = getPath(field);
            if (path == null) continue;
            if (path.isEmpty()) continue;
            try {
                if (field.getType() == int.class || field.getType() == Integer.class)
                    field.set(this, cfg.getInt(path, field.getInt(this)));
                else if (field.getType() == boolean.class || field.getType() == Boolean.class)
                    field.set(this, cfg.getBoolean(path, field.getBoolean(this)));
                else if (field.getType() == long.class || field.getType() == Long.class)
                    field.set(this, cfg.getLong(path, field.getLong(this)));
                else if (field.getType() == double.class || field.getType() == Double.class)
                    field.set(this, cfg.getDouble(path, field.getDouble(this)));
                else if (field.getType() == float.class || field.getType() == Float.class)
                    field.set(this, (float) cfg.getDouble(path, field.getDouble(this)));
                else if (field.getType() == String.class)
                    field.set(this, cfg.getString(path, (String) field.get(this)));
                else if (field.getType() == List.class) {
                    Type genericFieldType = field.getGenericType();
                    if (genericFieldType instanceof ParameterizedType) {
                        ParameterizedType aType = (ParameterizedType) genericFieldType;
                        Class fieldArgClass = (Class) aType.getActualTypeArguments()[0];
                        if (fieldArgClass == Integer.class) field.set(this, cfg.getIntegerList(path));
                        else if (fieldArgClass == Boolean.class) field.set(this, cfg.getBooleanList(path));
                        else if (fieldArgClass == Double.class) field.set(this, cfg.getDoubleList(path));
                        else if (fieldArgClass == Character.class) field.set(this, cfg.getCharacterList(path));
                        else if (fieldArgClass == Byte.class) field.set(this, cfg.getByteList(path));
                        else if (fieldArgClass == Float.class) field.set(this, cfg.getFloatList(path));
                        else if (fieldArgClass == Short.class) field.set(this, cfg.getFloatList(path));
                        else if (fieldArgClass == String.class) field.set(this, cfg.getStringList(path));
                    } else field.set(this, cfg.getList(path)); // Hell knows what's kind of List was found :)
                } else
                    throw new IllegalStateException("SimpleConfig did not supports class: " + field.getType().getName() + " for config field " + configFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getPath(Field field) {
        String path = null;
        if (field.isAnnotationPresent(Path.class)) {
            Path pathDefine = field.getAnnotation(Path.class);
            path = pathDefine.value();
        }
        if (path == null || path.isEmpty()) path = field.getName().replaceAll("_", ".");
        if (Modifier.isFinal(field.getModifiers())) return null;
        if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
        return path;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Path {
        String value() default "";
    }
}
