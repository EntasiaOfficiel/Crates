package fr.entasia.crates;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Plugin de crates acivé ");
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin de crates désactivé");
    }
}
