package eu.mikart.cleanrtp.references.invs;

public enum SettingsType {
    BOOLEAN(Boolean.class), STRING(String.class), INTEGER(Integer.class);

    private java.io.Serializable type;

    SettingsType(java.io.Serializable type) {
        this.type = type;
    }

    java.io.Serializable getType() {
        return type;
    }
}