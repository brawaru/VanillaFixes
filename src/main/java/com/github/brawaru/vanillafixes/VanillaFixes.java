package com.github.brawaru.vanillafixes;

import com.github.brawaru.vanillafixes.fixes.dirtypath.DirtyPath;
import com.github.brawaru.vanillafixes.fixes.doubledoors.DoubleDoors;
import org.bukkit.plugin.java.JavaPlugin;

public final class VanillaFixes extends JavaPlugin {

    @Override
    public void onEnable() {
        new DoubleDoors(this);
        new DirtyPath(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
