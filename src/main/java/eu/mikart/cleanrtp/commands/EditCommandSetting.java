package eu.mikart.cleanrtp.commands;

import eu.mikart.cleanrtp.player.rtp.RtpShape;
import java.util.Collections;

public enum EditCommandSetting {
    CENTER_X("CenterX", "INT"),
    CENTER_Z("CenterZ", "INT"),
    MAXRAD("MaxRadius", "INT"),
    MINRAD("MinRadius", "INT"),
    MAXY("MaxY", "INT"),
    MINY("MinY", "INT"),
    PRICE("Price", "INT"),
    SHAPE("Shape", "SHAPE"),
    USEWORLDBORDER("UseWorldBorder", "BOL");

    private final String str;
    private final String type;

    EditCommandSetting(String str, String type) {
        this.str = str;
        this.type = type;
    }

    public String get() {
        return str;
    }

    public Object getResult(String input) {
        if (type.equalsIgnoreCase("INT")) {
            return Integer.parseInt(input);
        } else if (type.equalsIgnoreCase("BOL")) {
            return Boolean.valueOf(input);
        } else if (type.equalsIgnoreCase("STR")) {
            return Collections.singletonList(input);
        } else if (type.equalsIgnoreCase("SHAPE")) {
            try {
                return RtpShape.valueOf(input.toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
}
