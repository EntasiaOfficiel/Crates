package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
public class Listeners implements Listener {


    @EventHandler

    public void onCrateOpen(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        for(Block block : Main.crateLocs.keySet()){
            if(b.equals(block)){
                // c'est une crate

            }
        }
    }
}
