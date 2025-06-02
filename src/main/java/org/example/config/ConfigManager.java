package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {

    private static final Path CFG_PATH = Path.of("src/main/resources/config.json");
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private static volatile ConfigManager instance;
    private final GameConfig config;

    private ConfigManager() {
        this.config = load();
        System.out.println("Loaded config → " + this.config);
    }

    public static ConfigManager getInstance() {
        ConfigManager local = instance;
        if (local == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) instance = new ConfigManager();
                local = instance;
            }
        }
        return local;
    }

    public GameConfig config() {
        return config;
    }

    public synchronized void save(GameConfig fresh) {
        try {
            MAPPER.writeValue(CFG_PATH.toFile(), fresh);
            instance = new ConfigManager();  // hot-swap
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot save config", ex);
        }
    }

    private GameConfig load() {
        try (var in = Files.newInputStream(CFG_PATH)) {
            return MAPPER.readValue(in, GameConfig.class);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot load config", ex);
        }
    }
}
