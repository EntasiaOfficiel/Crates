package fr.entasia.crates.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CrateLoot {

    public static int chance;
    public static String name;

    public abstract void win(Player p);
}
