package eu.mikart.cleanrtp.references.settings;

import lombok.Getter;
import eu.mikart.cleanrtp.references.file.FileOther;

public class Settings {

    @Getter private boolean debug;
    @Getter private boolean delayEnabled;
    @Getter private int delayTime;
    @Getter private boolean rtpOnFirstJoin_Enabled;
    @Getter private String rtpOnFirstJoin_World;
    @Getter private boolean rtpOnFirstJoin_SetAsRespawn;
    @Getter private boolean statusMessages; //Send more information about rtp
    @Getter private int preloadRadius; //Amount of chunks to load around a safe rtp location (clamped (0 - 16))
    //Dependencies
    private final SoftDepends depends = new SoftDepends();
    @Getter private boolean protocolLibSounds;
    @Getter private boolean locationEnabled;
    @Getter private boolean useLocationIfAvailable;
    @Getter private boolean locationNeedPermission;
    @Getter private boolean useLocationsInSameWorld;
    @Getter private boolean permissionGroupEnabled;
    @Getter private boolean queueEnabled;
    //Placeholders
    @Getter private String placeholder_true;
    @Getter private String placeholder_nopermission;
    @Getter private String placeholder_cooldown;
    @Getter private String placeholder_balance;
    @Getter private String placeholder_hunger;
    @Getter private String placeholder_timeDays;
    @Getter private String placeholder_timeHours;
    @Getter private String placeholder_timeMinutes;
    @Getter private String placeholder_timeSeconds;
    @Getter private String placeholder_timeZero;
    @Getter private String placeholder_timeInf;
    @Getter private String placeholder_timeSeparator_middle;
    @Getter private String placeholder_timeSeparator_last;


    public void load() { //Load Settings
        FileOther.Filetype config = FileOther.Filetype.CONFIG;
        debug = config.getBoolean("Settings.Debugger");
        delayEnabled = config.getBoolean("Settings.Delay.Enabled");
        delayTime = config.getInt("Settings.Delay.Time");
        rtpOnFirstJoin_Enabled = config.getBoolean("Settings.RtpOnFirstJoin.Enabled");
        rtpOnFirstJoin_World = config.getString("Settings.RtpOnFirstJoin.World");
        rtpOnFirstJoin_SetAsRespawn = config.getBoolean("Settings.RtpOnFirstJoin.SetAsRespawn");
        preloadRadius = config.getInt("Settings.PreloadRadius");
        statusMessages = config.getBoolean("Settings.StatusMessages");
        permissionGroupEnabled = config.getBoolean("PermissionGroup.Enabled");
        queueEnabled = config.getBoolean("Settings.Queue.Enabled");
        protocolLibSounds = FileOther.Filetype.EFFECTS.getBoolean("Sounds.ProtocolLibSound");
        locationEnabled = FileOther.Filetype.LOCATIONS.getBoolean("Enabled");
        useLocationIfAvailable = FileOther.Filetype.LOCATIONS.getBoolean("UseLocationIfAvailable");
        locationNeedPermission = FileOther.Filetype.LOCATIONS.getBoolean("RequirePermission");
        useLocationsInSameWorld = FileOther.Filetype.LOCATIONS.getBoolean("UseLocationsInSameWorld");
        //Placeholders
        placeholder_true = FileOther.Filetype.PLACEHOLDERS.getString("Config.CanRTP.Success");
        placeholder_nopermission = FileOther.Filetype.PLACEHOLDERS.getString("Config.CanRTP.NoPermission");
        placeholder_cooldown = FileOther.Filetype.PLACEHOLDERS.getString("Config.CanRTP.Cooldown");
        placeholder_balance = FileOther.Filetype.PLACEHOLDERS.getString("Config.CanRTP.Price");
        placeholder_hunger = FileOther.Filetype.PLACEHOLDERS.getString("Config.CanRTP.Hunger");
        placeholder_timeDays = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Days");
        placeholder_timeHours = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Hours");
        placeholder_timeMinutes = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Minutes");
        placeholder_timeSeconds = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Seconds");
        placeholder_timeZero = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.ZeroAll");
        placeholder_timeInf = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Infinite");
        placeholder_timeSeparator_middle = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Separator.Middle");
        placeholder_timeSeparator_last = FileOther.Filetype.PLACEHOLDERS.getString("Config.TimeFormat.Separator.Last");
        depends.load();
    }

    public SoftDepends getsDepends() {
        return depends;
    }
}
