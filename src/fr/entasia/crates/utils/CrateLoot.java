package fr.entasia.crates.utils;

import fr.entasia.apis.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateLoot {

	public int chance;
	public ItemStack item;
	public String name;

	public CrateLoot(){
	}

	public CrateLoot(int chance, String name, ItemStack item){
		this.chance = chance;
		this.name = name;
		this.item = item;
	}

	public void win(Player p){
		ItemUtils.giveOrDrop(p, item);
	}
}
