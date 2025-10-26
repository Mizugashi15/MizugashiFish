package net.mizugashi.mizugashiFish.events;

import net.mizugashi.mizugashiFish.data.FishData;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.stream.Collectors;

import static net.mizugashi.mizugashiFish.MizugashiFish.*;
import static net.mizugashi.mizugashiFish.utils.Contest.timerBar;

public class Fishing implements Listener {

    @EventHandler
    void onFishing (PlayerFishEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("FishWorld")) {
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                EquipmentSlot slot = event.getHand();
                ItemStack fishingRod = event.getPlayer().getInventory().getItem(slot);
                if (fishingRod != null && fishingRod.getType().name().contains("FISHING_ROD")) {
                    if (fishingRod.hasItemMeta()) {
                        ItemMeta meta = fishingRod.getItemMeta();
                        if (meta.getPersistentDataContainer().has(key_rod_name)) {
                            String rodname = meta.getPersistentDataContainer().get(key_rod_name, PersistentDataType.STRING);
                            Entity caughtEntity = event.getCaught();
                            if (caughtEntity instanceof Item item) {
                                Random random = new Random();
                                ItemStack fishItem = null;
                                ItemMeta fishMeta = null;
                                double random_chance = random.nextDouble(rod_map.get(rodname).all_chance);
                                double chance = 0;
                                for (int i = 0 ; i <= rod_map.get(rodname).chance.size() ; i++) {
                                    chance += rod_map.get(rodname).chance.get(i);
                                    if (random_chance <= chance) {
                                        fishItem = getFish(i, event.getPlayer().getName());
                                        fishMeta = fishItem.getItemMeta();
//                                        event.getPlayer().getInventory().addItem(fishItem);
                                        break;
                                    }
                                }
                                if (Boolean.TRUE.equals(fishMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "announce"), PersistentDataType.BOOLEAN))) {
                                    Bukkit.broadcastMessage(prefix + " §b" + event.getPlayer().getName() + "が§f\"" + fishMeta.getDisplayName() + "§f\"§bを釣り上げた！");
                                }
                                if (Boolean.TRUE.equals(fishMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "firework"), PersistentDataType.BOOLEAN))) {
                                    Firework firework = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), Firework.class);
                                    FireworkMeta fireworkMeta = firework.getFireworkMeta();
                                    fireworkMeta.setPower(1);
                                    fireworkMeta.addEffect(FireworkEffect.builder().trail(true).withColor(Color.WHITE).withColor(Color.AQUA).with(FireworkEffect.Type.BURST).build());
                                    firework.setFireworkMeta(fireworkMeta);
                                }
                                item.setItemStack(fishItem);
                                BukkitScheduler scheduler = Bukkit.getScheduler();
                                scheduler.runTaskLater(plugin, item::remove, 20);
                                if (contest) {
                                    String[] s = fishMeta.getPersistentDataContainer().get(new NamespacedKey(plugin, "fisher"), PersistentDataType.STRING).split(":");
                                    double newScore = fishMeta.getPersistentDataContainer().get(key_fish, PersistentDataType.DOUBLE);
                                    double bestScore = best_length.getOrDefault(event.getPlayer(), 0.0);
                                    if (newScore > bestScore) {
                                        event.getPlayer().sendMessage(prefix + " §e" +  newScore +"cm §bベスト記録を更新しました！");
                                        best_length.put(event.getPlayer(), newScore);
                                        best_fish.put(s[0], s[1]);
                                        ranking.put(s[0], newScore);
                                        getSortedRanking(event.getPlayer(), s[1]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static ItemStack getFish(int rarity, String player_name) {
        ItemStack fish = null;
        ItemMeta meta;
        List<String> lore = new ArrayList<>();
        List<FishData> dataList = new ArrayList<>();
        Random random = new Random();
        double random_chance = 0;
        switch (rarity) {
            case 0 -> {
                dataList = common_list;
                random_chance = random.nextDouble(common_chance);
                lore.add("§f§lCommon");
            }
            case 1 -> {
                dataList = uncommon_list;
                random_chance = random.nextDouble(uncommon_chance);
                lore.add("§a§lUncommon");
            }
            case 2 -> {
                dataList = rare_list;
                random_chance = random.nextDouble(rare_chance);
                lore.add("§b§lRare");
            }
            case 3 -> {
                dataList = super_list;
                random_chance = random.nextDouble(super_chance);
                lore.add("§5§lSuperRare");
            }
            case 4 -> {
                dataList = legend_list;
                random_chance = random.nextDouble(legend_chance);
                lore.add("§c§lLegend");
            }
        }
        for (FishData data : dataList) {
            if (random_chance <= data.chance) {
                fish = new ItemStack(Material.valueOf(data.type));
                meta = fish.getItemMeta();
                double lengthWidth = data.length_max - data.length_min;
                double caughtLength = Math.ceil(random.nextDouble(lengthWidth) * 10)/10;
                double length = caughtLength + data.length_min;
                double price = (Math.ceil(((data.price_max - data.price_min) / lengthWidth) * 10 ) / 10) * caughtLength + data.price_min;
                meta.setDisplayName(data.display_name.replace("&", "§") + " " + length + "cm");
                if (data.lore != null) {
                    for (String s : data.lore) {
                        lore.add(0, s);
                    }
                    meta.setLore(lore);
                }
                meta.setCustomModelData(data.custom_model_data);
                meta.getPersistentDataContainer().set(key_fish, PersistentDataType.DOUBLE, length);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "fisher"), PersistentDataType.STRING, player_name + ":" + data.display_name.replace("&", "§") + " " + length + "cm");
                meta.getPersistentDataContainer().set(key_fish_price, PersistentDataType.DOUBLE, price);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "announce"), PersistentDataType.BOOLEAN, data.announce);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "firework"), PersistentDataType.BOOLEAN, data.firework);
                fish.setItemMeta(meta);
                break;
            }
        }
        return fish;
    }

    public void getSortedRanking(Player player, String fish) {

        List<String> s = new ArrayList<>();
        List<String> s1 = new ArrayList<>();
        int i = 1;
        for (Map.Entry<String, Double> entry : ranking.entrySet()) {
            s.add(entry.getKey());
            if (i == 3) {
                break;
            }
            i++;
        }
        HashMap<String, Double> map = ranking.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));
        i = 1;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            s1.add(entry.getKey());
            if (i == 3) {
                break;
            }
            i++;
        }
        if (!s.equals(s1)) {
            for (int j = 0 ; j <= 3 ; j++) {
                if (!s.get(j).equalsIgnoreCase(s1.get(j))) {
                    for (Map.Entry<String, Double> entry : map.entrySet()) {
                        Bukkit.getPlayer(entry.getKey()).sendMessage(prefix + " §e" + player.getName() + "§fが" + fish + "§fを釣り上げて§e" + j + "位§fになった！");
                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void playerJoinAddBossBar(PlayerJoinEvent event) {
        if (contest) timerBar.addPlayer(event.getPlayer());
    }

}
