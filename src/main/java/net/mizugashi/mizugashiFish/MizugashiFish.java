package net.mizugashi.mizugashiFish;

import net.mizugashi.mizugashiFish.data.FishData;
import net.mizugashi.mizugashiFish.data.FishSetting;
import net.mizugashi.mizugashiFish.data.RodData;
import net.mizugashi.mizugashiFish.data.RodSetting;
import net.mizugashi.mizugashiFish.events.Fishing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class MizugashiFish extends JavaPlugin {

    public static JavaPlugin plugin;
    public static VaultManager vault;
    public static String prefix = "[§bMizuFishing§r]";
    public static List<FishData> common_list = new ArrayList<>();
    public static List<FishData> uncommon_list = new ArrayList<>();
    public static List<FishData> rare_list = new ArrayList<>();
    public static List<FishData> super_list = new ArrayList<>();
    public static List<FishData> legend_list = new ArrayList<>();
    public static HashMap<String, RodData> rod_map= new HashMap<>();
    public static int common_chance;
    public static int uncommon_chance;
    public static int rare_chance;
    public static int super_chance;
    public static int legend_chance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        vault = new VaultManager(plugin);
        Objects.requireNonNull(getCommand("fish")).setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new Fishing(), plugin);
        plugin.getDataFolder().mkdir();

        File file = new File(plugin.getDataFolder().getPath() + "/fish.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String rarity : config.getKeys(false)) {
            for (String fish_name : Objects.requireNonNull(config.getConfigurationSection(rarity)).getKeys(false)) {
                switch (rarity) {
                    case "common" ->  common_list.add(FishSetting.getFishData(rarity, fish_name));
                    case "uncommon" ->  uncommon_list.add(FishSetting.getFishData(rarity, fish_name));
                    case "rare" ->  rare_list.add(FishSetting.getFishData(rarity, fish_name));
                    case "super_rare" ->  super_list.add(FishSetting.getFishData(rarity, fish_name));
                    case "legend" ->  legend_list.add(FishSetting.getFishData(rarity, fish_name));
                }
            }
        }
        File rod_file = new File(plugin.getDataFolder().getPath() + "/rod.yml");
        FileConfiguration rod_config = YamlConfiguration.loadConfiguration(rod_file);
        for (String rod_name : rod_config.getKeys(false)) {
            rod_map.put(rod_name, RodSetting.getRodData(rod_name));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
