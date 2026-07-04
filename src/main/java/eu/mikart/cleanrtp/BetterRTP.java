package eu.mikart.cleanrtp;

import lombok.Getter;
import eu.mikart.cleanrtp.player.PlayerInfo;
import eu.mikart.cleanrtp.player.commands.Commands;
import eu.mikart.cleanrtp.player.events.EventListener;
import eu.mikart.cleanrtp.player.rtp.RTP;
import eu.mikart.cleanrtp.references.Permissions;
import eu.mikart.cleanrtp.references.RTPLogger;
import eu.mikart.cleanrtp.references.WarningHandler;
import eu.mikart.cleanrtp.references.database.DatabaseHandler;
import eu.mikart.cleanrtp.references.depends.DepEconomy;
import eu.mikart.cleanrtp.references.depends.DepPlaceholderApi;
import eu.mikart.cleanrtp.references.file.Files;
import eu.mikart.cleanrtp.references.invs.RTPInventories;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.player.playerdata.PlayerDataManager;
import eu.mikart.cleanrtp.references.rtpinfo.CooldownHandler;
import eu.mikart.cleanrtp.references.rtpinfo.QueueHandler;
import eu.mikart.cleanrtp.references.settings.Settings;
import eu.mikart.cleanrtp.references.web.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterRTP extends JavaPlugin {
    @Getter private final Permissions perms = new Permissions();
    @Getter private final DepEconomy eco = new DepEconomy();
    @Getter private final Commands cmd = new Commands(this);
    @Getter private final RTP RTP = new RTP();
    private final EventListener listener = new EventListener();
    @Getter private static BetterRTP instance;
    @Getter private final Files files = new Files();
    @Getter private final RTPInventories invs = new RTPInventories();
    @Getter private final PlayerInfo pInfo = new PlayerInfo();
    @Getter private final PlayerDataManager playerDataManager = new PlayerDataManager();
    @Getter private final Settings settings = new Settings();
    @Getter private final CooldownHandler cooldowns = new CooldownHandler();
    @Getter private final QueueHandler queue = new QueueHandler();
    @Getter private final DatabaseHandler databaseHandler = new DatabaseHandler();
    @Getter private final WarningHandler warningHandler = new WarningHandler();
    @Getter private boolean PlaceholderAPI;
    @Getter private final RTPLogger rtpLogger = new RTPLogger();

    @Override
    public void onEnable() {
        instance = this;
        registerDependencies();
        loadAll();
        cmd.registerPaperCommands();
        new Metrics(this);
        listener.registerEvents(this);
        queue.registerEvents(this);
        try {
            new DepPlaceholderApi().register();
        } catch (NoClassDefFoundError e) {
            //No placeholder api :(
        }
    }

    @Override
    public void onDisable() {
        invs.closeAll();
        queue.unload();
        rtpLogger.unload();
    }

    private void registerDependencies() {
        PlaceholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public void reload(CommandSender sendi) {
        invs.closeAll();
        loadAll();
        MessagesCore.RELOAD.send(sendi);
    }

    //(Re)Load all plugin systems/files/cache
    private void loadAll() {
        playerDataManager.clear();
        files.loadAll();
        settings.load();
        cooldowns.load();
        databaseHandler.load();
        rtpLogger.setup(this);
        invs.load();
        RTP.load();
        cmd.load();
        listener.load();
        eco.load();
        perms.register();
        queue.load();
    }

    public static void debug(String str) {
        getInstance().getLogger().info(str);
    }
}
