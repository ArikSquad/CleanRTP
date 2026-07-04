package eu.mikart.cleanrtp.references.messages;

import net.kyori.adventure.text.Component;

public interface MessageData {

    String section();

    String prefix();

    default String key() {
        return "cleanrtp." + prefix().toLowerCase() + section().toLowerCase();
    }

    default Component getComponent() {
        return Component.translatable(key());
    }
}
