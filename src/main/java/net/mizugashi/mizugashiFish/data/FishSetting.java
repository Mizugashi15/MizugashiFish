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

        data.type = section.getString("Type");

        data.display_name = section.getString("Displayname");

        data.custom_model_data = section.getInt("CustomModelData");

        data.lore = section.getStringList("Lore");

        data.length_min = section.getInt("Length.min");
        data.length_max = section.getInt("Length.max");

        data.price_min = section.getInt("Price.min");
        data.price_max = section.getInt("Price.max");

        data.announce = section.getBoolean("Announce");

        data.firework = section.getBoolean("Firework");

        data.chance = section.getInt("Chance");

        switch (rarity) {
            case "common" -> common_chance += section.getInt("Chance");
            case "uncommon" -> uncommon_chance += section.getInt("Chance");
            case "rare" -> rare_chance += section.getInt("Chance");
            case "super_rare" -> super_chance += section.getInt("Chance");
            case "legend" -> legend_chance += section.getInt("Chance");
        }

        return data;
    }
}
