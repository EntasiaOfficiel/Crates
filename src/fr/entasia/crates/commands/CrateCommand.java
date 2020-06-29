package fr.entasia.crates.commands;

import fr.entasia.crates.CratesAPI;
import fr.entasia.crates.utils.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Set;

public class CrateCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(!p.hasPermission("entasia.crate")) return true;
            if(args.length == 0) {
                p.sendMessage("§cMet un argument !");
                args(sender);
            }else{
                switch(args[0]){
                    case "add":
                    case "create":
                        if(args.length==1)p.sendMessage("§cMet un nom de crate à créer !");
                        else{
                            for(CrateType ct : CratesAPI.crateTypes){
                                if(ct.name.equals(args[1])){
                                    System.out.println(ct.name);
                                    System.out.println(args[1]);
                                    // TODO CREER LA CRATE
                                    Block block = p.getTargetBlock((Set<Material>) null, 20);
                                    if(ct.block.equals(block.getType())){
                                        CratesAPI.crateLocs.put(block, ct);

                                    }else{
                                        p.sendMessage("§cLe bloc n'est pas de bon type pour la crate");
                                    }

                                    return true;
                                }
                            }
                            p.sendMessage("§cAucune crate !");

                        }
                       break;
                    case "list":
                        // TODO LISTER LES CRATES
                        break;
                    default:
                        sender.sendMessage("§cArgument "+args[0]+" invalide !");
                        args(sender);
                }
            }
        }

        return false;
    }

    private static void args(CommandSender sender){
        sender.sendMessage("§cArguments disponibles :");
        sender.sendMessage("§c- create");
        sender.sendMessage("§c- list");
        sender.sendMessage("§c- delete");
    }
}
