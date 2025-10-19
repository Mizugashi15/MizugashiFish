package net.mizugashi.mizugashiFish.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class Fishing implements Listener {

    @EventHandler
    void onFishing (PlayerFishEvent event) {

        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("FishWorld")) {
            if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {



            }
        }

    }

}
