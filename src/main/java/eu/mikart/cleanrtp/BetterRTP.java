package eu.mikart.cleanrtp;

import eu.mikart.cleanrtp.config.ConfigProvider;
import lombok.Getter;
import eu.mikart.cleanrtp.player.PlayerInfo;
import eu.mikart.cleanrtp.player.commands.CommandRegistrar;
import eu.mikart.cleanrtp.player.events.EventListener;
import eu.mikart.cleanrtp.player.rtp.RTP;
import eu.mikart.cleanrtp.references.Permissions;
import eu.mikart.cleanrtp.references.RTPLogger;
import eu.mikart.cleanrtp.references.database.DatabaseHandler;
import eu.mikart.cleanrtp.references.depends.DepEconomy;
import eu.mikart.cleanrtp.references.depends.DepMiniPlaceholders;
import eu.mikart.cleanrtp.references.file.Files;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.player.playerdata.PlayerDataManager;
import eu.mikart.cleanrtp.references.rtpinfo.CooldownHandler;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.config.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterRTP extends JavaPlugin {
    @Getter private final Permissions perms = new Permissions();
    @Getter private final DepEconomy eco = new DepEconomy();
    @Getter private final CommandRegistrar cmd = new CommandRegistrar(this);
    @Getter private final RTP RTP = new RTP();
    private final EventListener listener = new EventListener();
    @Getter private static BetterRTP instance;
    @Getter private final Files files = new Files();
    @Getter private final PlayerInfo pInfo = new PlayerInfo();
    @Getter private final PlayerDataManager playerDataManager = new PlayerDataManager();
    @Getter private final CooldownHandler cooldowns = new CooldownHandler();
    @Getter private final QueueHandler queue = new QueueHandler();
    @Getter private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private Runnable unregisterMiniPlaceholders = () -> {};
    @Getter private final RTPLogger rtpLogger = new RTPLogger();

    public Settings getSettings() {
        return ConfigProvider.settings;
    }

    @Override
    public void onEnable() {
        instance = this;
        ConfigProvider.loadSettings();
        loadAll();
        cmd.registerCommands();
        listener.registerEvents(this);
        queue.registerEvents(this);
        registerMiniPlaceholders();
    }

    @Override
    public void onDisable() {
        queue.unload();
        rtpLogger.unload();
        unregisterMiniPlaceholders.run();
    }

    private void registerMiniPlaceholders() {
        try {
            DepMiniPlaceholders miniPlaceholders = new DepMiniPlaceholders();
            miniPlaceholders.register();
            unregisterMiniPlaceholders = miniPlaceholders::unregister;
        } catch (NoClassDefFoundError ignored) {
            unregisterMiniPlaceholders = () -> {};
        }
    }

    public void reload(CommandSender sender) {
        loadAll();
        MessagesCore.RELOAD.send(sender);
    }

    //(Re)Load all plugin systems/files/cache
    private void loadAll() {
        playerDataManager.clear();
        files.loadAll();
        cooldowns.load();
        databaseHandler.load();
        rtpLogger.setup(this);
        RTP.load();
        listener.load();
        eco.load();
        perms.register();
        queue.load();
    }

    public static void debug(String str) {
        getInstance().getLogger().info(str);
    }
}
