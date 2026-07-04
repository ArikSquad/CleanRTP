package eu.mikart.cleanrtp.references.helpers;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.RTPCommand;

public class RtpCommandHelper {

    public static void registerCommand(RTPCommand cmd, boolean forced) {
        BetterRTP.getInstance().getCmd().registerCommand(cmd, forced);
    }

}
