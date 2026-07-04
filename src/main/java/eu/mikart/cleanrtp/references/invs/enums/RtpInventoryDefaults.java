package eu.mikart.cleanrtp.references.invs.enums;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.mikart.cleanrtp.references.invs.RtpInventorySettings;
import eu.mikart.cleanrtp.references.messages.Message;
import eu.mikart.cleanrtp.references.player.HelperPlayer;
import eu.mikart.cleanrtp.references.player.playerdata.PlayerData;

public interface RtpInventoryDefaults {

    void show(Player p);

    void clickEvent(InventoryClickEvent event);

    default ItemStack createItem(String item, int amount, String name, List<String> lore) {
        Material mat = Material.valueOf(item.toUpperCase());
        ItemStack _stack = new ItemStack(mat, amount);
        ItemMeta _meta = _stack.getItemMeta();
        if (_meta != null) {
            if (lore != null)
                _meta.setLore(lore);
            if (name != null)
                _meta.setDisplayName(Message.color(name));
        }
        _stack.setItemMeta(_meta);
        return _stack;
    }

    default void cacheInv(Player p, Inventory inv, RtpInventorySettings type) {
        PlayerData info = HelperPlayer.getData(p);
        info.getMenu().setInv(inv);
        info.getMenu().setInvType(type);
    }

    default Inventory createInv(int size, String title) {
        title = Message.color(title);
        return Bukkit.createInventory(null, size, title);
    }
}
