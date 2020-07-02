package fr.entasia.crates;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.crates.utils.CrateLoot;
import fr.entasia.crates.utils.CrateType;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.MetadataValue;

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
                if(!crateType.canOpen(p)) return;
                boolean opening = false;
                if(b.getMetadata("opening") !=null){
                    for (MetadataValue value : b.getMetadata("opening")) {
                        opening = value.asBoolean();
                    }
                }

                if(opening){
                    p.sendMessage("§7Quelqu'un est déjà en train d'ouvrir cette crate, attends ton tour ");
                    return;
                }
                crateType.open(b,p);
                if(p.getInventory().getItemInMainHand().getAmount() ==1) p.getInventory().setItemInMainHand(null);
                else p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);



            }
        }
    }


    @EventHandler
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent e){
        ArmorStand asd = e.getRightClicked();
        if(asd.getName().equalsIgnoreCase("AMPointer") || asd.getName().equalsIgnoreCase("AMPrize")){
            e.setCancelled(true);
        }
    }
}
