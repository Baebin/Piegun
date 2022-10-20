package kr.piebin.piegun.db;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private static ConfigManager instance = null;
    public static ConfigManager getInstance() {
        if (instance == null) instance = new ConfigManager();
        return instance;
    }

    public static String PATH;

    private Map<String, YamlConfiguration> yamlMap = new HashMap<>();

    public YamlConfiguration getYaml(String file) {
        return yamlMap.get(file.toLowerCase());
    }

    public void setYaml(String file, YamlConfiguration yaml) {
        yamlMap.put(file, yaml);
    }

    public void load(String file) {
        yamlMap.put(file.toLowerCase(), YamlConfiguration.loadConfiguration(checkAndCreateFile(getFile(file))));
    }

    public void save(String file) throws IOException {
        yamlMap.get(file.toLowerCase()).save(getFile(file));
    }

    public File getFile(String name) {
        return new File(PATH, name);
    }

    public static File checkAndCreateFile(File file) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
