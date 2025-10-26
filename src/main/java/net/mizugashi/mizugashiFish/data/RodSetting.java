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

        data.display_name = config.getString(rod_name + ".Displayname");

        data.chance = config.getIntegerList(rod_name + ".Chance");

        for (int i : data.chance) {
            data.all_chance += i;
        }

        return data;
    }

}
