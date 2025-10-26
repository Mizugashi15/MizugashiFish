package net.mizugashi.mizugashiFish.utils;

import net.mizugashi.mizugashiFish.MizugashiFish;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static net.mizugashi.mizugashiFish.MizugashiFish.*;

public class Contest {

    static int remainingTime;
    public static BossBar timerBar;
    static BukkitRunnable timertask;

    //コンテスト開始
    public static void startContest(int titleTime) {

        contest = true;
        remainingTime = titleTime;

        String titleName = "§b釣りコンテスト開催中！ §f残り時間" + remainingTime + "秒";
        timerBar = Bukkit.createBossBar(titleName, BarColor.BLUE, BarStyle.SEGMENTED_10);
        timerBar.setVisible(true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            timerBar.addPlayer(player);
        }

        ranking.clear();
        best_length.clear();
        Bukkit.broadcastMessage(prefix + " §b釣りコンテスト開催！");

        timertask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingTime <= 0) {
                    int i = 1;
                    ItemStack prizeItem = plugin.getConfig().getItemStack("contest_item.prize");
                    ItemStack joinItem = plugin.getConfig().getItemStack("contest_item.join");
                    List<String> winners = new ArrayList<>();
                    for (Map.Entry<String, Double> entry : ranking.entrySet()) {
                        winners.add(entry.getKey());
                        if (i == 3) break;
                        i++;
                    }
                    i = 1;
                    for (Map.Entry<String, Double> entry : ranking.entrySet()) {
                        Player player = Bukkit.getPlayer(entry.getKey());
                        if (player == null) continue;
                        switch (i) {
                            case 1 -> {
                                player.getInventory().addItem(prizeItem);
                                player.getInventory().addItem(prizeItem);
                                player.getInventory().addItem(prizeItem);
                            }
                            case 2 -> {
                                player.getInventory().addItem(prizeItem);
                                player.getInventory().addItem(prizeItem);
                            }
                            case 3 -> player.getInventory().addItem(prizeItem);
                        }
                        player.getInventory().addItem(joinItem);
                        player.sendMessage(prefix + " §bコンテストが終了しました");
                        player.sendMessage("§e==========最終結果==========");
                        player.sendMessage(" §e1位 " + winners.get(0) + " " + best_fish.get(winners.get(0)));
                        if (winners.size() > 1) player.sendMessage(" §f2位 " + winners.get(1) + " " + best_fish.get(winners.get(1)));
                        if (winners.size() > 2) player.sendMessage(" §63位 " + winners.get(2) + " " + best_fish.get(winners.get(2)));
                        player.sendMessage("§e==========================");
                        i++;
                    }

                    timerBar.setTitle("§fコンテスト終了！");
                    timerBar.setProgress(0.0);
                    MizugashiFish.contest = false;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timerBar.removeAll();
                        }
                    }.runTaskLater(plugin, 100);
                    this.cancel();
                    return;
                }
                String updateTitle = "§b釣りコンテスト開催中！ §f残り時間" + remainingTime + "秒";
                timerBar.setTitle(updateTitle);

                double progress = (double) remainingTime / titleTime;
                timerBar.setProgress(Math.max(0.0, progress));

                remainingTime--;
            }
        };
        timertask.runTaskTimer(plugin, 1, 20);
    }

    //コンテスト強制終了
    public static void stopContest() {
        contest = false;
        timertask.cancel();
        timertask = null;

        if (timerBar != null) {
            timerBar.removeAll();
            timerBar = null;
        }
        remainingTime = 0;
    }

    //定期開催
    public static void scheduleDailyContest(int hour, int minute, int second) {
        Runnable task = () -> {
            startContest(1800);
        };

        Calendar now = Calendar.getInstance();

        Calendar nextExecution = Calendar.getInstance();
        nextExecution.set(Calendar.HOUR_OF_DAY, hour);
        nextExecution.set(Calendar.MINUTE, minute);
        nextExecution.set(Calendar.SECOND, second);
        nextExecution.set(Calendar.MILLISECOND, 0);

        if (nextExecution.before(now)) {
            nextExecution.add(Calendar.DATE, 1);
        }

        long delayMillis = nextExecution.getTimeInMillis() - now.getTimeInMillis();

        long delayTicks = delayMillis / 50L;

        Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
    }

}
