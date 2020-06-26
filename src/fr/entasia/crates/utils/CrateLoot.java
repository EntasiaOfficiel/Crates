package fr.entasia.crates.utils;

import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.apis.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CrateLoot {

    public int chance;
    public static String name;
    public ItemStack item;


    public void win(Player p){
        ItemUtils.giveOrDrop(p,item);

    };
}
