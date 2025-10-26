package net.mizugashi.mizugashiFish.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.mizugashi.mizugashiFish.MizugashiFish.permission;
import static net.mizugashi.mizugashiFish.MizugashiFish.rod_map;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("rank");
            list.add("shop");
            if (sender.hasPermission(permission)) {
                list.add("list");
                list.add("get");
                list.add("reload");
                list.add("contest");
            }
            return strings(args[0], list);
        }
        if (args.length == 2) {
            if (sender.hasPermission(permission)) {
                if (args[0].equalsIgnoreCase("get")) {
                    List<String> keyList = new ArrayList<>(rod_map.keySet());
                    return strings(args[1], keyList);
                }
                if (args[0].equalsIgnoreCase("contest")) {
                    return strings(args[1], List.of("start", "stop"));
                }
            }
        }
        return null;
    }

    private List<String> strings(String s,List<String> args){
        List<String> list = new ArrayList<>();
        for(String s1:args){
            if(s1.startsWith(s))
                list.add(s1);
        }
        return list;
    }

}
