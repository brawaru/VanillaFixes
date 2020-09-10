package com.github.brawaru.vanillafixes.fixes.dirtypath;

import com.github.brawaru.vanillafixes.VanillaFixes;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class DirtyPath implements Listener {
    public DirtyPath(VanillaFixes plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Lists all shovels
     */
    private Material[] shovels = {
            Material.WOODEN_SHOVEL,
            Material.STONE_SHOVEL,
            Material.GOLDEN_SHOVEL,
            Material.IRON_SHOVEL,
            Material.DIAMOND_SHOVEL,
            Material.NETHERITE_SHOVEL
    };

    /**
     * Returns whether the material is a shovel item.
     *
     * @param material Material to check
     * @return True if shovel, otherwise false
     */
    private boolean isShovel(Material material) {
        for (Material shovel : shovels) {
            if (material.equals(shovel)) return true;
        }

        return false;
    }

    private final Random rand = new Random();

    /**
     * Attempts to damage tool respecting it's durability enchantment.
     *
     * @param tool Tool to damage
     * @param damage Damage applied to tool
     */
    @SuppressWarnings("SameParameterValue")
    private void damageTool(Damageable tool, int damage) {
        ItemMeta meta = (ItemMeta) tool;

        int durabilityLevel = meta.getEnchantLevel(Enchantment.DURABILITY);
        int breakingChance = 100 / (durabilityLevel + 1);

        for (int i = 1; i < damage; i++) {
            boolean takesDamage = (rand.nextInt(100) + 1) > breakingChance;

            if (!takesDamage) continue;

            tool.setDamage(tool.getDamage() - 1);
        }
    }

    /**
     * Returns pseudorandom float between provided minimum and maximum.
     *
     * @param min Minimum returning value
     * @param max Maximum returning value
     * @return Pseudorandom float value
     */
    @SuppressWarnings("SameParameterValue")
    private float nextRandomFloat(float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }

//    /**
//     * Returns pseudorandom float between zero and provided maximum.
//     *
//     * @param max Maximum returning value
//     * @return Pseudorandom float value
//     */
//    private float nextRandomFloat(float max) {
//        return nextRandomFloat(0, max);
//    }

    /**
     * Event that occurs whenever player interacts with the block.
     *
     * @param event Player interaction event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock().equals(Event.Result.DENY)) return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        ItemStack itemInHand = event.getItem();

        if (itemInHand == null) return;

        Material itemInHandMaterial = itemInHand.getType();

        if (!isShovel(itemInHandMaterial)) return;

        Block interactedBlock = event.getClickedBlock();

        if (interactedBlock == null || !interactedBlock.getType().equals(Material.DIRT)) return;

        Block upRelative = interactedBlock.getRelative(BlockFace.UP);

        if (!upRelative.getType().isAir()) return;

        Damageable tool = (Damageable) itemInHand.getItemMeta();

        damageTool(tool, 2);

        interactedBlock.setType(Material.GRASS_PATH);

        event.getPlayer().playSound(
                interactedBlock.getLocation(),
                Sound.ITEM_SHOVEL_FLATTEN,
                SoundCategory.BLOCKS,
                1.0F,
                nextRandomFloat(0.8F, 1.0F)
        );

        event.getPlayer().swingMainHand();
    }
}
