package net.mizugashi.mizugashiFish.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static net.mizugashi.mizugashiFish.MizugashiFish.plugin;

public class RodSetting {

    public static RodData getRodData(String rod_name) {

        RodData data = new RodData();
        File file = new File(plugin.getDataFolder().getPath() + "/rod.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        data.name = rod_name;

        data.display_name = config.getString("Displayname");

        data.lore = config.getStringList("Lore");

        data.all_chance += data.common = config.getInt("Common");
        data.all_chance += data.uncommon = config.getInt("Uncommon");
        data.all_chance += data.rare = config.getInt("Rare");
        data.all_chance += data.super_rare = config.getInt("SuperRare");
        data.all_chance += data.legend = config.getInt("Legend");

        return data;
    }

}
