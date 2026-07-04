package eu.mikart.cleanrtp.references.messages;

import eu.mikart.cleanrtp.references.messages.placeholder.PlaceholderAnalyzer;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface Message {
    MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    static void sms(CommandSender sender, String legacyMiniMessage) {
        if (legacyMiniMessage != null && !legacyMiniMessage.isEmpty()) {
            AsyncHandler.sync(() -> sender.sendMessage(component(legacyMiniMessage, sender)));
        }
    }

    static void sms(CommandSender sender, String legacyMiniMessage, Object info) {
        if (legacyMiniMessage != null && !legacyMiniMessage.isEmpty()) {
            AsyncHandler.sync(() -> sender.sendMessage(component(legacyMiniMessage, sender, info)));
        }
    }

    static void sms(CommandSender sender, ComponentLike component) {
        AsyncHandler.sync(() -> sender.sendMessage(component.asComponent()));
    }

    static void sms(CommandSender sender, List<ComponentLike> components) {
        if (components != null && !components.isEmpty()) {
            AsyncHandler.sync(() -> components.forEach(component -> sender.sendMessage(component.asComponent())));
        }
    }

    static Component translatable(CommandSender sender, String key, @Nullable Object info) {
        List<ComponentLike> args = PlaceholderAnalyzer.arguments(sender, info);
        return prefixed(Component.translatable(key, args));
    }

    static Component translatableRaw(CommandSender sender, String key, @Nullable Object info) {
        return Component.translatable(key, PlaceholderAnalyzer.arguments(sender, info));
    }

    static Component prefixed(ComponentLike component) {
        return Component.translatable("cleanrtp.messages.prefix").append(component.asComponent());
    }

    static Component component(String value, @Nullable CommandSender sender, Object... namedValues) {
        String mini = legacyToMini(value);
        if (namedValues.length == 1) {
            return MINI_MESSAGE.deserialize(mini, PlaceholderAnalyzer.tagResolver(sender, namedValues[0]));
        }
        return MINI_MESSAGE.deserialize(mini, PlaceholderAnalyzer.tagResolver(sender, null, namedValues));
    }

    static List<ComponentLike> components(Collection<String> values, @Nullable CommandSender sender) {
        return values.stream().map(value -> component(value, sender)).map(ComponentLike.class::cast).toList();
    }

    static String legacyString(String value, @Nullable CommandSender sender) {
        return LegacyComponentSerializer.legacySection().serialize(component(value, sender));
    }

    static String legacyToMini(String value) {
        String result = value.replace("<", "\\<").replace(">", "\\>");
        result = result.replaceAll("#([A-Fa-f0-9]{6})", "<#$1>");
        result = result.replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>");
        return result.replaceAll("%([A-Za-z0-9_]+)%", "<$1>");
    }
}
