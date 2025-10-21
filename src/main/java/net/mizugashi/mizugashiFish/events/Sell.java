package net.mizugashi.mizugashiFish.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static net.mizugashi.mizugashiFish.MizugashiFish.*;

public class Sell implements Listener {

    static public String guiPrefix = "§bSelling Fish§Ｆ§Ｉ§Ｓ§Ｈ";

    @EventHandler
    public void onClickSell(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(guiPrefix)) {
            if (event.getCurrentItem() == null) return;
            if (!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(key_fish) || event.getClick().isKeyboardClick()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseSell(InventoryCloseEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(guiPrefix)) {
            if (event.getPlayer() instanceof Player player) {
                Inventory inventory = event.getInventory();
                double price = 0;
                for (ItemStack item : inventory.getContents()) {
                    if (!item.hasItemMeta()) continue;
                    if (item.getItemMeta().getPersistentDataContainer().has(key_fish_price)) {
                        price += item.getItemMeta().getPersistentDataContainer().get(key_fish_price, PersistentDataType.DOUBLE);
                    } else player.getInventory().addItem(item);
                }
                vault.nologdeposit(player, price);
            }
        }
    }

    public Inventory createShopGui() {
        return Bukkit.createInventory(null, 45, guiPrefix);
    }
}
