package eu.mikart.cleanrtp.references.invs.types;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;
import eu.mikart.cleanrtp.references.invs.enums.RTPInventory;
import eu.mikart.cleanrtp.references.invs.enums.RtpInventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RTPInvWorlds extends RTPInventory {

    public void show(Player p) {
        int slots = (Bukkit.getWorlds().size() - (Bukkit.getWorlds().size() % 9) + 1) * 9;
        if (slots < 6 * 9)
            slots += 9;
        Inventory inv = this.createInv(slots, "&lSelect a world to edit!");
        int _index = 0;
        for (World world : Bukkit.getWorlds()) {
            if (_index > 9 * 5)
                break;
            ItemStack _item = createItem(RtpInventoryItem.NORMAL.item, RtpInventoryItem.NORMAL.amt, world.getName(), null);
            inv.setItem(_index, _item);
            _index ++;
        }
        ItemStack _item = createItem(RtpInventoryItem.BACK.item, RtpInventoryItem.BACK.amt, RtpInventoryItem.BACK.name, null);
        inv.setItem(inv.getSize() - 9 + RtpInventoryItem.BACK.slot, _item);
        p.openInventory(inv);
        this.cacheInv(p, inv, this.type);
    }

    @Override
    public void clickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        int slot = e.getSlot();
        for (RtpInventoryItem type : RtpInventoryItem.values()) {
            if (type.slot != -1) {
                switch (type) {
                    case BACK:
                        if (slot == e.getInventory().getSize() - 9 + type.slot) {
                            BetterRTP.getInstance().getInvs().getInv(RtpInventorySettings.MAIN).show(p);
                            return;
                        }
                    default:
                        break;
                }
            }
        }
        int _index = 0;
        for (World world : Bukkit.getWorlds()) {
            if (_index == slot) {
                BetterRTP.getInstance().getPInfo().setInvWorld(p, world);
                BetterRTP.getInstance().getInvs().getInv(BetterRTP.getInstance().getPInfo().getInvNextInv().get(p)).show(p);
            }
            _index ++;
        }
    }
}

