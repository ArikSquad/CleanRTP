package eu.mikart.cleanrtp.references;

import lombok.Getter;
import eu.mikart.cleanrtp.BetterRTP;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class RTPLogger {

    @Getter private String format;
    @Getter private File file;
    private FileHandler handler;
    //private ConsoleHandler consoleHandler_rtp, consoleHandler_logger;

    public void setup(BetterRTP plugin) {
        var config = plugin.getSettings().getGeneral().getLogger();
        boolean enabled = config.isEnabled();
        Logger logger = plugin.getLogger();
        logger.setUseParentHandlers(true);
        if (handler != null) {
            logger.removeHandler(handler);
            handler.close();
        }
        if (!enabled) return;
        this.format = config.getFormat();
        boolean toConsole = config.isLogToConsole();
        try {
            this.file = new File(plugin.getDataFolder() + File.separator + "log.txt");
            Files.deleteIfExists(file.toPath());
            this.handler = new FileHandler(file.getPath(), true);
            handler.setFormatter(new MyFormatter());
            logger.setUseParentHandlers(toConsole); //Disable logging to console
            logger.addHandler(handler);
        } catch (IOException e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE, "Could not open RTP log file", e);
        }
    }

    private static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat(BetterRTP.getInstance().getRtpLogger().getFormat());
        return format.format(new Date());
    }

    public void unload() {
        if (handler != null)
            handler.close();
    }

    static class MyFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            return getDate() + " [" + record.getLevel().getName() + "]: " + record.getMessage() + '\n';
        }
    }
}
