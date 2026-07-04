package eu.mikart.cleanrtp.references.invs.types;

import eu.mikart.cleanrtp.references.invs.enums.RTPInventory;
import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;
import eu.mikart.cleanrtp.references.invs.enums.RtpInventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RTPInvCoordinates extends RTPInventory {

    public void show(Player p) {
        if (BetterRTP.getInstance().getPInfo().getInvWorld().get(p) == null) {
            BetterRTP.getInstance().getPInfo().setNextInv(p, this.type);
            BetterRTP.getInstance().getInvs().getInv(RtpInventorySettings.WORLDS).show(p);
            return;
        }
        int slots = (RtpCoordinateSettings.values().length - (RtpCoordinateSettings.values().length % 9) + 1) * 9;
        if (slots < 6 * 9)
            slots += 9;
        Inventory inv = this.createInv(slots, "Settings: &lCoordinates");
        int index = 0;
        for (RtpCoordinateSettings set : RtpCoordinateSettings.values()) {
            ItemStack _item = createItem(RtpInventoryItem.NORMAL.item, RtpInventoryItem.NORMAL.amt, "&a&l" + set.getInfo()[1], null);
            inv.setItem(index, _item);
            index++;
        }
        ItemStack _item = createItem(RtpInventoryItem.BACK.item, RtpInventoryItem.BACK.amt, RtpInventoryItem.BACK.name, null);
        inv.setItem(inv.getSize() - 9 + RtpInventoryItem.BACK.slot, _item);
        p.openInventory(inv);
        this.cacheInv(p, inv, this.type);
    }

    @Override
    public void clickEvent(InventoryClickEvent e) {
        int slot = e.getSlot();
        for (RtpCoordinateSettings set : RtpCoordinateSettings.values()) {

        }
        for (RtpInventoryItem type : RtpInventoryItem.values()) {
            if (type.slot != -1) {
                if (type == RtpInventoryItem.BACK) {
                    if (slot == e.getInventory().getSize() - 9 + type.slot)
                        BetterRTP.getInstance().getInvs().getInv(RtpInventorySettings.MAIN).show((Player) e.getWhoClicked());
                }
            }
        }
    }
}