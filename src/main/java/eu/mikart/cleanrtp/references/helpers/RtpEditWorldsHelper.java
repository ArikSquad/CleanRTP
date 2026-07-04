package eu.mikart.cleanrtp.references.helpers;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.EditCommandType;
import eu.mikart.cleanrtp.player.commands.EditCommandSetting;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldType;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.command.CommandSender;

public class RtpEditWorldsHelper {

    public static void editCustomWorld(CommandSender sendi, EditCommandSetting cmd, String world, String val) {
        Object value = parseEditValue(sendi, cmd, val);
        if (value == null) return;
        BetterRTP.getInstance().getSettings().setCustomWorldValue(world, cmd, value);
        BetterRTP.getInstance().getRTP().loadWorlds();
        MessagesCore.EDIT_SET.send(sendi, Argument.string("type", cmd.get()), Argument.string("value", val));
    }

    public static void editLocation(CommandSender sendi, EditCommandSetting cmd, String location, String val) {
        if (editSingleMap(sendi, cmd, location, val))
            BetterRTP.getInstance().getRTP().loadLocations();
    }

    private static boolean editSingleMap(CommandSender sendi, EditCommandSetting cmd, String field, String val) {
        Object value = parseEditValue(sendi, cmd, val);
        if (value == null) return false;

        BetterRTP.getInstance().getSettings().setLocationValue(field, cmd, value);
        MessagesCore.EDIT_SET.send(sendi, Argument.string("type", cmd.get()), Argument.string("value", val));
        return true;
    }

    public static void editPermissionGroup(CommandSender sendi, EditCommandSetting cmd, String group, String world, String val) {
        Object value = parseEditValue(sendi, cmd, val);
        if (value == null) return;

        if (BetterRTP.getInstance().getSettings().setPermissionGroupValue(group, world, cmd, value)) {
            BetterRTP.getInstance().getRTP().loadPermissionGroups();
            MessagesCore.EDIT_SET.send(sendi, Argument.string("type", cmd.get()), Argument.string("value", val));
        } else {
            MessagesCore.EDIT_ERROR.send(sendi);
        }
    }

    public static void editDefault(CommandSender sendi, EditCommandSetting cmd, String val) {
        Object value = parseEditValue(sendi, cmd, val);
        if (value == null) return;

        BetterRTP.getInstance().getSettings().setDefaultValue(cmd, value);
        BetterRTP.getInstance().getRTP().loadWorlds();
        MessagesCore.EDIT_SET.send(sendi, Argument.string("type", cmd.get()), Argument.string("value", val));
    }

    public static void editWorldtype(CommandSender sendi, String world, String val) {
        WorldType type;
        try {
            type = WorldType.valueOf(val.toUpperCase());
        } catch (Exception e) {
            MessagesCore.EDIT_ERROR.send(sendi);
            return;
        }

        BetterRTP.getInstance().getSettings().setWorldType(world, type.name());
        BetterRTP.getInstance().getRTP().load();
        MessagesCore.EDIT_SET.send(sendi, Argument.string("type", EditCommandType.WorldType.name()), Argument.string("value", val));
    }

    public static void editOverride(CommandSender sendi, String world, String val) {
        String messageValue = val;
        BetterRTP.getInstance().getSettings().setOverride(world, val);
        if (val.equals("REMOVE_OVERRIDE")) {
            messageValue = "(removed override)";
        }

        BetterRTP.getInstance().getRTP().load();
        MessagesCore.EDIT_SET.send(sendi, Argument.string("type", EditCommandType.OVERRIDE.name()), Argument.string("value", messageValue));
    }

    public static void editBlacklisted(CommandSender sendi, String block, boolean add) {
        BetterRTP.getInstance().getSettings().setBlacklistedBlock(block, add);
        String messageValue = add ? block : "(removed " + block + ")";

        BetterRTP.getInstance().getRTP().load();
        MessagesCore.EDIT_SET.send(sendi, Argument.string("type", EditCommandType.BLACKLISTEDBLOCKS.name()), Argument.string("value", messageValue));
    }

    private static Object parseEditValue(CommandSender sendi, EditCommandSetting cmd, String val) {
        try {
            Object value = cmd.getResult(val);
            if (value != null) return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessagesCore.EDIT_ERROR.send(sendi);
        return null;
    }
}
