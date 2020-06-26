package fr.entasia.crates;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.crates.utils.CrateLoot;
import fr.entasia.crates.utils.CrateType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class Listeners implements Listener {


    @EventHandler

    public void onCrateOpen(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        for(Map.Entry<Block, CrateType> entry : Main.crateLocs.entrySet()){
            if(entry.getKey().equals(b)){
                CrateType crateType = entry.getValue();
                if(!crateType.canOpen(p)) return;
                Randomiser r = new Randomiser(100);
                for(CrateLoot loot : crateType.loots){
                    if(r.isInNext(loot.chance)){
                        loot.win(p);
                    }
                }

            }
        }
    }
}
