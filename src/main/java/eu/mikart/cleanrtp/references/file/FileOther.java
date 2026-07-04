package eu.mikart.cleanrtp.references.file;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileOther {

    List<Filetype> types = new ArrayList<>();

    void load() {
        types.clear();
        for (Filetype type : Filetype.values()) {
            type.load();
            types.add(type);
        }
    }

    public enum Filetype implements FileData {
        CONFIG("config"),
        ECO("economy"),
        SIGNS("signs"),
        EFFECTS("effects"),
        LOCATIONS("locations"),
        PLACEHOLDERS("placeholders")
        ;

        private final String fileName;
        private final YamlConfiguration config = new YamlConfiguration();
        @Getter
        private final File file;

        Filetype(String str) {
            this.fileName = str + ".yml";
            this.file = new File(plugin().getDataFolder(), fileName);
        }

        @Override
        public Plugin plugin() {
            return BetterRTP.getInstance();
        }

        //PUBLIC
        @Override
        public YamlConfiguration getConfig() {
            return config;
        }

        @Override
        public String fileName() {
            return fileName;
        }
    }
}
