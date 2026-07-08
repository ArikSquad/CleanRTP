package eu.mikart.cleanrtp.config;

import de.exlll.configlib.ConfigurationElementFilter;
import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import eu.mikart.cleanrtp.BetterRTP;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigProvider {

    @NotNull
    static YamlConfigurationProperties.Builder<?> YAML_CONFIGURATION_PROPERTIES = YamlConfigurationProperties.newBuilder()
        .addPostProcessor(ConfigurationElementFilter.byPostProcessKey("lowercase"), (String value) -> value.toLowerCase())
        .charset(StandardCharsets.UTF_8)
        .setNameFormatter(NameFormatters.LOWER_UNDERSCORE);

    @Setter
    public static Settings settings;

    static SoftDepends softDepends = new SoftDepends();

    public static void loadSettings() {
        setSettings(YamlConfigurations.update(
            Path.of(BetterRTP.getInstance().getDataFolder().toURI()).resolve("config.yml"),
            Settings.class,
            YAML_CONFIGURATION_PROPERTIES.build()
        ));

        softDepends.load();
    }

    public static void saveSettings() {
        YamlConfigurations.save(
            Path.of(BetterRTP.getInstance().getDataFolder().toURI()).resolve("config.yml"),
            Settings.class,
            settings,
            YAML_CONFIGURATION_PROPERTIES.build()
        );
    }

}
