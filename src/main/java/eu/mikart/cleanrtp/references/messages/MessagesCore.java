package eu.mikart.cleanrtp.references.messages;

import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;

public enum MessagesCore {

    SUCCESS_PAID("Success.Paid"),
    SUCCESS_BYPASS("Success.Bypass"),
    SUCCESS_LOADING("Success.Loading"),
    SUCCESS_TELEPORT("Success.Teleport"),
    FAILED_NOTSAFE("Failed.NotSafe"),
    FAILED_PRICE("Failed.Price"),
    FAILED_HUNGER("Failed.Hunger"),
    OTHER_NOTSAFE("Other.NotSafe"),
    OTHER_SUCCESS("Other.Success"),
    OTHER_BIOME("Other.Biome"),
    NOTEXIST("NotExist"),
    RELOAD("Reload"),
    UPDATE("Update"),
    NOPERMISSION("NoPermission.Basic"),
    NOPERMISSION_WORLD("NoPermission.World"),
    DISABLED_WORLD("DisabledWorld"),
    COOLDOWN("Cooldown"),
    INVALID("Invalid"),
    NOTONLINE("NotOnline"),
    DELAY("Delay"),
    SIGN("Sign"),
    MOVED("Moved"),
    ALREADY("Already"),
    //EDIT
    EDIT_ERROR("Edit.Error"),
    EDIT_SET("Edit.Set"),
    EDIT_REMOVE("Edit.Remove"),
    ;

    final String section;

    MessagesCore(String section) {
        this.section = section;
    }

    private static final String pre = "cleanrtp.messages.";

    public void send(CommandSender sendi) {
        RtpMessage.sms(sendi, getComponent(sendi, null));
    }

    public void send(CommandSender sendi, Object placeholderInfo) {
        RtpMessage.sms(sendi, getComponent(sendi, placeholderInfo));
    }

    public void send(CommandSender sendi, List<Object> placeholderInfo) {
        RtpMessage.sms(sendi, getComponent(sendi, placeholderInfo));
    }

    public void send(CommandSender sendi, ComponentLike... args) {
        RtpMessage.sms(sendi, Message.prefixed(Component.translatable(key(), args)));
    }

    public Component getComponent(CommandSender p, Object placeholderInfo) {
        return Message.translatable(p, key(), placeholderInfo);
    }

    public Component getComponentRaw(CommandSender p, Object placeholderInfo) {
        return Message.translatableRaw(p, key(), placeholderInfo);
    }

    public void send(CommandSender sendi, HashMap<String, String> placeholder_values) {
        Component component = Component.translatable(key(),
                placeholder_values.entrySet().stream()
                        .map(entry -> Argument.string(entry.getKey(), entry.getValue()))
                        .toList());
        RtpMessage.sms(sendi, Message.prefixed(component));
    }

    public String key() {
        return pre + section.toLowerCase();
    }
}
