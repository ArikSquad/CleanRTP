package eu.mikart.cleanrtp.references.depends;

import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class DepEconomy {
    private Economy e;
    private boolean checked = false;

    public boolean charge(CommandSender sender, WorldPlayer pWorld) {
        check(false);
        Player player = pWorld.getPlayer();
        //Economy Stuff
        if (e != null
                && pWorld.getPrice() != 0
                && pWorld.getPlayerInfo().isTakeMoney()
                && !PermissionNode.BYPASS_ECONOMY.check(player)) {
            try {
                EconomyResponse r = e.withdrawPlayer(player, pWorld.getPrice());
                boolean passed_economy = r.transactionSuccess();
                if (!passed_economy) {
                    MessagesCore.FAILED_PRICE.send(sender, pWorld.getPrice());
                } //else
                    //pWorld.eco_money_taken = true;
                return passed_economy;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Default value
        return true;
    }

    public boolean hasBalance(WorldPlayer pWorld) {
        check(false);
        //Economy Stuff
        float price = pWorld.getPrice();
        if (e != null && price != 0 && !PermissionNode.BYPASS_ECONOMY.check(pWorld.getPlayer())) {
            try {
                return e.getBalance(pWorld.getPlayer()) >= price;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Default value
        return true;
    }

    public void load() {
        check(true);
    }

    private void check(boolean force) {
        if (!checked || force)
            registerEconomy();
    }

    private void registerEconomy() {
        try {
            if (BetterRTP.getInstance().getSettings().getGeneral().getEconomy().isEnabled())
                if (BetterRTP.getInstance().getServer().getPluginManager().isPluginEnabled("Vault")) {
                    RegisteredServiceProvider<Economy> rsp = BetterRTP.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
                    e = rsp.getProvider();
                }
        } catch (NullPointerException e) {
            //
        }
        checked = true;
    }
}
