package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public class Listeners implements Listener {


	@EventHandler
	public void onCrateOpen(PlayerInteractEvent e){
		if(e.getHand()!= EquipmentSlot.HAND)return;
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		for(Map.Entry<Block, CrateType> entry : CratesAPI.crateLocs.entrySet()){
			if(entry.getKey().equals(b)){
				e.setCancelled(true);
				CrateType crateType = entry.getValue();
					if(b.hasMetadata("opening")){
						p.sendMessage("§cQuelqu'un est déjà en train d'ouvrir cette crate !");
					}else{
						if(crateType.removeKey(p))crateType.open(b,p);
						else p.sendMessage("§cTu n'as pas la clé pour ouvrir cette crate !");
					}
				return;
			}
		}
	}


	@EventHandler
	public void onArmorStandInteract(PlayerArmorStandManipulateEvent e){
		ArmorStand asd = e.getRightClicked();
		if(asd.getName().equals("AMPointer") || asd.getName().equals("AMPrize")){
			e.setCancelled(true);
		}
	}
}
