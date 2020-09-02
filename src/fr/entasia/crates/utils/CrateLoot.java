package fr.entasia.crates.utils;

import fr.entasia.apis.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrateLoot {

	protected int chance;
	public ItemStack item;
	protected String name;

	public CrateLoot(int chance, String name, ItemStack item){
		this.chance = chance;
		this.name = name;
		this.item = item;
	}

	public void win(Player p){
		p.sendMessage("§aTu as gagné "+name+" §a!");
		ItemUtils.giveOrDrop(p, item);
	}
}
