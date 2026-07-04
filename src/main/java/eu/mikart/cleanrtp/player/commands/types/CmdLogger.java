package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.web.LogUploader;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CmdLogger implements RTPCommand {

    public String getName() {
        return "logger";
    }
    public void execute(CommandSender sender, String label, String[] args) {
        get(sender, label, args, args.length >= 2 && args[args.length - 1].equalsIgnoreCase("_UPLOAD_"));
    }

    private void get(CommandSender sender, String label, String[] args, boolean upload) {
        String cmd = "/" + label + " " + String.join(" ", args);
        if (!upload) {
            if (sender instanceof Player player) {
                TextComponent component = new TextComponent(Message.color("&7- &7Click to upload log to &flogs.ronanplugins.com"));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd + " _UPLOAD_"));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Message.color("&6Suggested command&f: &7" + "/betterrtp " + String.join(" ", args) + " _UPLOAD_")).create()));
                player.spigot().sendMessage(component);
            } else {
                sender.sendMessage("Execute `" + cmd + " _UPLOAD_`" + " to upload log to https://logs.ronanplugins.com");
            }
        } else {
            CompletableFuture.runAsync(() -> {
                String key = LogUploader.post(BetterRTP.getInstance().getRtpLogger().getFile());
                if (key == null) {
                    Message.sms(sender, new ArrayList<>(Collections.singletonList("&cAn error occured attempting to upload log!")), null);
                } else {
                    try {
                        JSONObject json = (JSONObject) new JSONParser().parse(key);
                        Message.sms(sender, Arrays.asList(" ", Message.getPrefix(RtpMessage.msg) + "&aLog uploaded! &fView&7: &6https://logs.ronanplugins.com/" + json.get("key")), null);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @NotNull public PermissionNode permission() {
        return PermissionNode.ADMIN;
    }

    public void usage(CommandSender sendi, String label) {
    }
}
