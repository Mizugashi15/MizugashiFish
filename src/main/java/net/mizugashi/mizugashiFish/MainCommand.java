package net.mizugashi.mizugashiFish;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.mizugashi.mizugashiFish.data.FishSetting;
import net.mizugashi.mizugashiFish.data.RodSetting;
import net.mizugashi.mizugashiFish.events.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

import static net.mizugashi.mizugashiFish.MizugashiFish.*;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage("§7========" + prefix + "§7========");
                    player.sendMessage("§f/fish §elist");
                    player.sendMessage("§7➡ 魚のリストを表示します");
                    player.sendMessage("§f/fish §eget [釣り竿名]");
                    player.sendMessage("§7➡ 釣り竿を取得します");
                    player.sendMessage("§f/fish §eshop");
                    player.sendMessage("§7➡ 魚の販売画面を表示します");
                    player.sendMessage("§f/fish §ereload");
                    player.sendMessage("§7➡ データファイルを再読み込みします");
                    player.sendMessage("§f/fish §econtest start [秒数]");
                    player.sendMessage("§7➡ 釣りコンテストの開始 秒数指定");
                    player.sendMessage("§f/fish §econtest stop");
                    player.sendMessage("§7➡ 現在開催されているコンテストの強制終了");
                    return true;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    TextComponent component = new TextComponent("§fCommon§eの魚を表示");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fish list common"));
                    player.spigot().sendMessage(component);
                    component = new TextComponent("§aUncommon§eの魚を表示");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fish list uncommon"));
                    player.spigot().sendMessage(component);
                    component = new TextComponent("§bRare§eの魚を表示");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fish list rare"));
                    player.spigot().sendMessage(component);
                    component = new TextComponent("§5SuperRare§eの魚を表示");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fish list superrare"));
                    player.spigot().sendMessage(component);
                    component = new TextComponent("§cLegend§eの魚を表示");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fish list legend"));
                    player.spigot().sendMessage(component);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    common_list.clear();
                    uncommon_list.clear();
                    rare_list.clear();
                    super_list.clear();
                    legend_list.clear();
                    rod_map.clear();
                    common_chance = 0;
                    uncommon_chance = 0;
                    rare_chance = 0;
                    super_chance = 0;
                    legend_chance = 0;

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
                    player.sendMessage(prefix + " §aリロードしました");
                    return true;
                }
                if (args[0].equalsIgnoreCase("shop")) {
                    player.openInventory(new Shop().createShopGui());
                    return true;
                }
            }

        } else {
            sender.sendMessage(prefix + " §cプレイヤーのみ入力できます！");
            return false;
        }
        return false;
    }
}
