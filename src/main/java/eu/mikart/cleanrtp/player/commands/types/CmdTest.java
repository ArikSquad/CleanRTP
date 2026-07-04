package eu.mikart.cleanrtp.player.commands.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommandHelpable;
import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.player.rtp.RtpType;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.messages.MessagesHelp;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//Meant to just test particles and effects without actually rtp'ing around the world
public class CmdTest implements RTPCommand, RTPCommandHelpable {

    public String getName() {
        return "test";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            BetterRTP.getInstance().getRTP().getTeleport().afterTeleport(p, p.getLocation(),
                    RtpHelper.getPlayerWorld(new RTPSetupInformation(p.getWorld(), p, p, false)), 0, p.getLocation(), RtpType.TEST);
        } else
            sender.sendMessage("Console is not able to execute this command! Try '/rtp help'");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @NotNull @Override
    public PermissionNode permission() {
        return PermissionNode.ADMIN;
    }

    @Override
    public String getHelp() {
        return MessagesHelp.TEST.get();
    }

    @Override
    public boolean isDebugOnly() {
        return true;
    }
}
