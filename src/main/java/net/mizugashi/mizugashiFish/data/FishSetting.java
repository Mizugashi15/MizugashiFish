package net.mizugashi.mizugashiFish.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static net.mizugashi.mizugashiFish.MizugashiFish.*;

public class FishSetting {

    public static FishData getFishData(String rarity, String fish_name) {

        FishData data = new FishData();
        File fishFile = new File(plugin.getDataFolder().getPath() + "/fish.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(fishFile);
        ConfigurationSection section = config.getConfigurationSection(rarity);

        data.name = fish_name;

        data.type = section.getString(fish_name + ".Type");

        data.display_name = section.getString(fish_name + ".Displayname");

        data.lore = section.getStringList(fish_name + ".Lore");

        data.custom_model_data = section.getInt(fish_name + ".CustomModelData");

        data.length_min = section.getInt(fish_name + ".Length.min");
        data.length_max = section.getInt(fish_name + ".Length.max");

        data.price_min = section.getInt(fish_name + ".Price.min");
        data.price_max = section.getInt(fish_name + ".Price.max");

        data.announce = section.getBoolean(fish_name + ".Announce");

        data.firework = section.getBoolean(fish_name + ".Firework");

        data.chance = section.getInt(fish_name + ".Chance");

        switch (rarity) {
            case "common" -> common_chance += section.getInt(fish_name + ".Chance");
            case "uncommon" -> uncommon_chance += section.getInt(fish_name + ".Chance");
            case "rare" -> rare_chance += section.getInt(fish_name + ".Chance");
            case "super_rare" -> super_chance += section.getInt(fish_name + ".Chance");
            case "legend" -> legend_chance += section.getInt(fish_name + ".Chance");
        }

        return data;
    }
}
