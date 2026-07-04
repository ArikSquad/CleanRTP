package eu.mikart.cleanrtp.player.rtp.effects;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.config.Settings;
import eu.mikart.cleanrtp.references.messages.Message;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class RtpEffectTitles {

    boolean enabled = false;
    private final HashMap<RtpTitleType, RtpTitle> titles = new HashMap<>();

    void load() {
        titles.clear();
        enabled = BetterRTP.getInstance().getSettings().getGeneral().getTitles().enabled;
        if (enabled)
            for (RtpTitleType type : RtpTitleType.values())
                titles.put(type, new RtpTitle(type.message(BetterRTP.getInstance().getSettings().getGeneral().getTitles())));
    }

    public void showTitle(RtpTitleType type, Player p, Location loc, int attempts, int delay) {
        if (titles.containsKey(type)) {
            String title = getPlaceholders(titles.get(type).title, p, loc, attempts, delay);
            String sub = getPlaceholders(titles.get(type).subTitle, p, loc, attempts, delay);
            show(p, title, sub);
        }
    }

    public boolean sendMsg(RtpTitleType type) {
        return titles.containsKey(type) && titles.get(type).shouldSendMessage || !enabled;
    }

    private String getPlaceholders(String str, Player p, Location loc, int attempts, int delay) {
        return str.replace("%player%", p.getName())
                .replace("%x%", String.valueOf(loc.getBlockX()))
                .replace("%y%", String.valueOf(loc.getBlockY()))
                .replace("%z%", String.valueOf(loc.getBlockZ()))
                .replace("%attempts%", String.valueOf(attempts))
                .replace("%time%", String.valueOf(delay));
    }

    private void show(Player p, String title, String sub) {
        p.showTitle(Title.title(Message.component(title, p), Message.component(sub, p)));
    }

    public enum RtpTitleType {
        NODELAY, TELEPORT, DELAY, CANCEL, LOADING, FAILED;

        Settings.GeneralSettings.TitleMessage message(Settings.GeneralSettings.Titles titles) {
            return switch (this) {
                case NODELAY -> titles.noDelay;
                case TELEPORT -> titles.teleport;
                case DELAY -> titles.delay;
                case CANCEL -> titles.cancelled;
                case LOADING -> titles.loading;
                case FAILED -> titles.failed;
            };
        }
    }

    private static class RtpTitle {
        String title, subTitle;
        boolean shouldSendMessage;

        RtpTitle(Settings.GeneralSettings.TitleMessage titleMessage) {
            title = titleMessage.title;
            subTitle = titleMessage.subtitle;
            shouldSendMessage = titleMessage.sendMessage;
        }

    }
}
