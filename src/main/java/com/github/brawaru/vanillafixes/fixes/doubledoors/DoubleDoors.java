package com.github.brawaru.vanillafixes.fixes.doubledoors;

import com.github.brawaru.vanillafixes.VanillaFixes;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.github.brawaru.vanillafixes.fixes.doubledoors.DoubleDoorsCommon.isDoor;

public class DoubleDoors implements Listener  {
    private final VanillaFixes plugin;

    public DoubleDoors(VanillaFixes plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;
    }

    /**
     * Event that happens whenever player interacts with something.
     *
     * When it happens, we check if it was interaction with the door and if so,
     * run fixing task, that is going to synchronize two doors if there are two
     * doors and they are linked together.
     *
     * @param event Player interaction event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onInteract(PlayerInteractEvent event) {
        // If any of previous handlers canceled the event, do not do anything.
        if (event.useInteractedBlock().equals(Event.Result.DENY)) return;

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (event.getPlayer().isSneaking()) return;

        Block clickedBlock = event.getClickedBlock();

        // Iron doors never can be opened by hand, so we should skip them for this event
        if (!isDoor(clickedBlock) || clickedBlock.getType().equals(Material.IRON_DOOR)) return;

        new DoubleDoorsFixTask(clickedBlock).runTaskLater(plugin, 0);
    }
}
