package net.mizugashi.mizugashiFish.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Shop {

    static public String guiPrefix = "§bFishing Shop§Ｆ§Ｉ§Ｓ§Ｈ";

    public Inventory createShopGui() {
        Inventory inventory = Bukkit.createInventory(null, 45, guiPrefix);
        ItemStack dummy = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta_dummy = dummy.getItemMeta();
        Objects.requireNonNull(meta_dummy).setDisplayName("");
        dummy.setItemMeta(meta_dummy);
        ItemStack sell = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta meta_sell = sell.getItemMeta();
        Objects.requireNonNull(meta_sell).setDisplayName("§e売却する");
        sell.setItemMeta(meta_sell);
        int[] i = {36,37,38,42,43,44};
        int[] j = {39,40,41};
        for (int k : i) {
            inventory.setItem(k, dummy);
        }
        for (int k : j) {
            inventory.setItem(k, sell);
        }
        return inventory;
    }
}
