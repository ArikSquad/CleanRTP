package eu.mikart.cleanrtp.references.file;

import eu.mikart.cleanrtp.BetterRTP;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FileLanguage {
    private MiniMessageTranslationStore translationStore;

    public void load() {
        if (translationStore != null) {
            GlobalTranslator.translator().removeSource(translationStore);
        }

        translationStore = MiniMessageTranslationStore.create(
                Key.key("cleanrtp", "translations"),
                MiniMessage.builder().tags(translationTags()).build());

        for (Locale locale : supportedLocales()) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(
                        "translations.cleanrtp",
                        locale,
                        BetterRTP.getInstance().getClass().getClassLoader());
                translationStore.registerAll(locale, bundle, false);
            } catch (MissingResourceException ignored) {
                BetterRTP.getInstance().getLogger().warning("Missing translation bundle for " + locale);
            }
        }

        GlobalTranslator.translator().addSource(translationStore);
    }

    private TagResolver translationTags() {
        try {
            return TagResolver.resolver(TagResolver.standard(), io.github.miniplaceholders.api.MiniPlaceholders.audienceGlobalPlaceholders());
        } catch (NoClassDefFoundError ignored) {
            return TagResolver.standard();
        }
    }

    private Locale[] supportedLocales() {
        return new Locale[] {
                Locale.US,
                Locale.of("pt", "BR"),
                Locale.of("zh", "CN"),
                Locale.of("zh", "TW"),
                Locale.of("cs", "CZ"),
                Locale.of("da", "DK"),
                Locale.GERMANY,
                Locale.of("es", "ES"),
                Locale.FRANCE,
                Locale.of("he", "IL"),
                Locale.of("hu", "HU"),
                Locale.ITALY,
                Locale.JAPAN,
                Locale.of("nl", "NL"),
                Locale.of("no", "NO"),
                Locale.of("pl", "PL"),
                Locale.of("ro", "RO"),
                Locale.of("ru", "RU"),
                Locale.of("tr", "TR"),
                Locale.of("uk", "UA"),
                Locale.of("vi", "VN")
        };
    }
}
