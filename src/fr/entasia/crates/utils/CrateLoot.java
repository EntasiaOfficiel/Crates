package fr.entasia.crates.utils;

import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.crates.CratesAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateLoot {

    public int chance;
    public ItemStack item;
    public String name;

    public CrateLoot(int chance, ItemStack item){
        this.chance = chance;
        this.item = item;
    }

    public void win(Player p){
        ItemUtils.giveOrDrop(p,item);

    };
}
