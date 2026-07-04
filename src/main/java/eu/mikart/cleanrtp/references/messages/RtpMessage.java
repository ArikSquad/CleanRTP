package eu.mikart.cleanrtp.references.messages;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.file.FileData;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RtpMessage implements Message {

    public static RtpMessage msg = new RtpMessage();

    public static FileData getLang() {
        return BetterRTP.getInstance().getFiles().getLang();
    }

    @Override
    public FileData lang() {
        return getLang();
    }

    public static void sms(CommandSender sendi, String msg) {
        Message.sms(RtpMessage.msg, sendi, msg);
    }

    public static void sms(CommandSender sendi, String msg, Object placeholderInfo) {
        Message.sms(RtpMessage.msg, sendi, msg, placeholderInfo);
    }

    public static void sms(CommandSender sendi, String msg, List<Object> placeholderInfo) {
        Message.sms(RtpMessage.msg, sendi, msg, placeholderInfo);
    }

    public static void sms(CommandSender sendi, List<String> msg, List<Object> placeholderInfo) {
        Message.sms(sendi, msg, placeholderInfo);
    }

    public static String getPrefix() {
        return Message.getPrefix(RtpMessage.msg);
    }
}
