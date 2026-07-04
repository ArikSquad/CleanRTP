package eu.mikart.cleanrtp.references;

import eu.mikart.cleanrtp.BetterRTP;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface PermissionCheck {

    static String getPrefix() {
        return "betterrtp.";
    }

    default boolean check(CommandSender sender) {
        return BetterRTP.getInstance().getPerms().checkPerm(getNode(), sender);
    }

    static boolean check(CommandSender sender, String check) {
        return BetterRTP.getInstance().getPerms().checkPerm(check, sender);
    }

    static boolean getAWorld(CommandSender sender, String world) {
        return getAWorldText(sender, world).passed;
    }

    static PermissionResult getAWorldText(CommandSender sender, @NotNull String world) {
        String perm = getPrefix() + "world.*";
        if (check(sender, perm)) {
            return new PermissionResult(perm, true);
        } else {
            perm = getPrefix() + "world." + world;
            if (check(sender, perm))
                return new PermissionResult(perm, true);
        }
        return new PermissionResult(perm, false);
    }

    static boolean getLocation(CommandSender sendi, String location) {
        return check(sendi, getPrefix() + "location." + location);
    }

    static boolean getPermissionGroup(CommandSender sendi, String group) {
        return check(sendi, getPrefix() + "group." + group);
    }

    String getNode();

    record PermissionResult(boolean passed, String string) {
            PermissionResult(String string, boolean passed) { // ???
                this(passed, string);
            }
        }
}