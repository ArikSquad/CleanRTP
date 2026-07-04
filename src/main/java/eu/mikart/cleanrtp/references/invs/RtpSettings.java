package eu.mikart.cleanrtp.references.invs;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.file.FileOther.Filetype;

public enum RtpSettings {
    BLACKLIST(   SettingsType.BOOLEAN, Filetype.CONFIG, "Template.Enabled",
            new Object[]{true, "Templates", "&7Toggle Templates system", "paper"});

    SettingsType type;
    Filetype filetype;
    String path;
    String[] condition = null;
    Object[] info; // = new Object[]{false}; //ENABLED, NAME, DESCRIPTION, ITEM

    RtpSettings(SettingsType type, Filetype filetype, String path, Object[] info) {
        this.type = type;
        this.filetype = filetype;
        this.path = path;
        this.info = info;
    }

    RtpSettings(SettingsType type, Filetype filetype, String[] arry, Object[] info) {
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

    public Object[] getInfo() {return info;}

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

    public SettingsType getType() {
        return type;
    }

    public Filetype getFiletype() {
        return filetype;
    }
}