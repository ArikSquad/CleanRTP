package eu.mikart.cleanrtp.references.messages;

import eu.mikart.cleanrtp.references.file.FileData;

public interface MessageData {

    String section();

    String prefix();

    FileData file();

    default String get() {
        return file().getString(prefix() + section());
    }
}
