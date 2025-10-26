package net.mizugashi.mizugashiFish.events;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

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
                Inventory inventory = event.getView().getTopInventory();
                double price = 0;
                for (ItemStack item : inventory.getContents()) {
                    if (item == null) continue;
                    if (!item.hasItemMeta()) continue;
                    if (item.getItemMeta().getPersistentDataContainer().has(key_fish_price)) {
                        price += item.getItemMeta().getPersistentDataContainer().get(key_fish_price, PersistentDataType.DOUBLE);
                    } else player.getInventory().addItem(item);
                }
                double n = Math.ceil(price * 10) / 10;
                vault.nologdeposit(player, price);
                player.sendMessage(prefix + " §e" + price + "$§bで売却しました");
            }
        }
    }

    @EventHandler
    public void clickSign(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType().name().contains("SIGN")) {
                if (event.getClickedBlock().getState() instanceof Sign sign) {
                    if (sign.getSide(Side.FRONT).getLine(1).equalsIgnoreCase("§f[§bMizuFishing§f]")) {
                        event.getPlayer().openInventory(createShopGui());
                    }
                }
            }
        }
    }

    @EventHandler
    public void placeSign(SignChangeEvent event) {

        if (event.getSide() == Side.FRONT) {
            if (event.getPlayer().hasPermission(permission)) {
                if (Objects.requireNonNull(event.getLine(0)).equalsIgnoreCase("[fishshop]")) {
                    event.setLine(0, "§e===========");
                    event.setLine(1, "§f[§bMizuFishing§f]");
                    event.setLine(2, "§bFish Selling");
                    event.setLine(3, "§e============");
                    Sign sign = (Sign) event.getBlock().getState();
                    sign.setWaxed(true);
                    sign.update();
                }
            }
        }
    }

    public Inventory createShopGui() {
        return Bukkit.createInventory(null, 45, guiPrefix);
    }
}
