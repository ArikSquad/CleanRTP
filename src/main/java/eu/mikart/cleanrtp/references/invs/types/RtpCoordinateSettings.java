package eu.mikart.cleanrtp.references.invs.types;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.file.FileOther;
import eu.mikart.cleanrtp.references.invs.SettingsType;

import java.util.ArrayList;

public enum RtpCoordinateSettings {
    CENTER_Z(   SettingsType.INTEGER, FileOther.Filetype.CONFIG, "Settings.Importance.Enabled",
            new Object[]{true, "Center Z", "&7Toggle Categories system", "paper"}),
    CENTER_X(   SettingsType.INTEGER, FileOther.Filetype.CONFIG, "Settings.Cooldown.Enabled",
            new Object[]{true, "Center X", "&7Toggle Cooldown system", "paper"}),
    USE_WORLDBORDER( SettingsType.BOOLEAN, FileOther.Filetype.CONFIG, "Settings.Cooldown.Time",
            new Object[]{true, "Cooldown Time", new ArrayList<String>() {{
                add("&7Set the time (in minutes)");
                add("&7between making a new ticket");
            }}, "paper"}),
    /*DEBUG(      SettingsType.BOOLEAN, FILETYPE.CONFIG, "Settings.Debug",
            new Object[]{true, "Debug", "&7Toggle debugger", "paper"}),
    TEMPLATE(   SettingsType.BOOLEAN, FILETYPE.CONFIG, "Template.Enabled",
            new Object[]{true, "Templates", "&7Toggle Templates system", "paper"});*/;

    @Getter
    SettingsType type;
    @Getter
    FileOther.Filetype filetype;
    String path;
    String[] condition = null;
    @Getter
    Object[] info; // = new Object[]{false}; //ENABLED, NAME, DESCRIPTION, ITEM

    RtpCoordinateSettings(SettingsType type, FileOther.Filetype filetype, String path, Object[] info) {
        this.type = type;
        this.filetype = filetype;
        this.path = path;
        this.info = info;
    }

    RtpCoordinateSettings(SettingsType type, FileOther.Filetype filetype, String[] arry, Object[] info) {
        this.type = type;
        this.filetype = filetype;
        this.path = null;
        this.info = info;
        //Condition
        this.condition = arry;
    }

    void setValue(Object value) {
        BetterRTP.getInstance().getFiles().getType(filetype).setValue(path, value);
    }

    public Object getValue() {
        String path = this.path;
        if (path == null && condition != null) {
            if (BetterRTP.getInstance().getFiles().getType(filetype).getBoolean(condition[0]))
                path = condition[1];
            else
                path = condition[2];
        }
        return getValuePath(path);
    }

    private Object getValuePath(String path) {
        if (path != null) {
            if (getType() == SettingsType.BOOLEAN)
                return BetterRTP.getInstance().getFiles().getType(filetype).getBoolean(path);
            else if (getType() == SettingsType.STRING)
                return BetterRTP.getInstance().getFiles().getType(filetype).getString(path);
            else if (getType() == SettingsType.INTEGER)
                return BetterRTP.getInstance().getFiles().getType(filetype).getInt(path);
        }
        return null;
    }

}