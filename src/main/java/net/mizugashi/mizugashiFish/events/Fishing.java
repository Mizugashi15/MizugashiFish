package net.mizugashi.mizugashiFish.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import static net.mizugashi.mizugashiFish.MizugashiFish.key_rod_name;
import static net.mizugashi.mizugashiFish.MizugashiFish.plugin;

public class Fishing implements Listener {

    @EventHandler
    void onFishing (PlayerFishEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("FishWorld")) {
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                EquipmentSlot slot = event.getHand();
                ItemStack fishingRod = event.getPlayer().getInventory().getItem(slot);
                if (fishingRod != null && fishingRod.getType().name().contains("FISHING_ROD")) {
                    if (fishingRod.hasItemMeta()) {
                        if (fishingRod.getItemMeta().getPersistentDataContainer().has(key_rod_name)) {
                            if (event.getCaught() instanceof Item item) {



                                item = spawnDummyItem(event.getHook().getLocation(), )
                            }
                        }
                    }
                }
            }
        }
    }

    public static Item spawnDummyItem(Location location, ItemStack itemStack) {
        Item dummy = location.getWorld().dropItem(location, itemStack);
        dummy.setPickupDelay(-32768);
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(plugin, dummy::remove, 10);
        return dummy;
    }

}
