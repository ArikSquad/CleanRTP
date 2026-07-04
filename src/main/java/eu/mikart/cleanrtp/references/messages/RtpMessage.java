package eu.mikart.cleanrtp.references.messages;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RtpMessage {

    public static void sms(CommandSender sender, String msg) {
        Message.sms(sender, msg);
    }

    public static void sms(CommandSender sender, String msg, Object info) {
        Message.sms(sender, msg, info);
    }

    public static void sms(CommandSender sender, ComponentLike msg) {
        Message.sms(sender, msg);
    }

    public static void sms(CommandSender sender, List<ComponentLike> msg) {
        Message.sms(sender, msg);
    }
}
