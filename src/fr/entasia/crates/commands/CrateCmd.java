package fr.entasia.crates.commands;

import fr.entasia.crates.CratesAPI;
import fr.entasia.crates.utils.CrateType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CrateCmd implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return true;
		Player p = (Player) sender;
		if (!p.hasPermission("admin.crates")) return true;
		if (args.length == 0) {
			p.sendMessage("§cMet un argument !");
			args(sender);
			return true;
		}
		switch (args[0]) {
			case "create": {
				if (args.length == 1) p.sendMessage("§cMet un nom de crate à créer !");
				else {
					CrateType ct = CratesAPI.getCrateType(args[1]);
					if (ct == null) {
						p.sendMessage("§cCette crate n'existe pas !");
					} else {
						Block block = p.getTargetBlock(null, 20);
						if(block==null){
							p.sendMessage("§cRegarde un block !");
						}else{
							if (ct.block.equals(block.getType())) {
								CratesAPI.createCrate(p, block, ct);
							} else {
								p.sendMessage("§cLe bloc n'est pas de bon type pour la crate");
							}
						}
					}
				}
				break;
			}
			case "delete": {
				Block block = p.getTargetBlock(null, 20);
				if(block==null){
					p.sendMessage("§cRegarde un block !");
				}else{
					CratesAPI.deleteCrate(p,block);
				}
				break;
			}
			case "key": {
				if (args.length == 1) p.sendMessage("§cMet un nom de crate à créer !");
				else {
					CrateType ct = CratesAPI.getCrateType(args[1]);
					if (ct == null) {
						p.sendMessage("§cCette crate n'existe pas !");
					} else {
						p.getInventory().addItem(ct.key.clone());
					}
				}
				break;
			}
			case "types":{
				p.sendMessage("§7Liste des types de crate :");
				for (CrateType type : CratesAPI.crateTypes) {
					p.sendMessage("§7- "+type.name);
				}
				break;
			}
			case "list": {
				p.sendMessage("§7Liste des crates :");
				for (Map.Entry<Block, CrateType> entry : CratesAPI.crateLocs.entrySet()) {
					Location loc = entry.getKey().getLocation();
					String coords = loc.getBlockX() + "§8; §7" + loc.getBlockY() + "§8; §7" + loc.getBlockZ();
					p.sendMessage("§7-" + entry.getValue().name + " §8:§7 " + coords+" §8(§7"+loc.getWorld().getName()+"§8)");
				}
				break;
			}
			default:{
				sender.sendMessage("§cArgument " + args[0] + " invalide !");
				args(sender);
			}
		}
		return true;
	}

	private static void args(CommandSender sender){
		sender.sendMessage("§cArguments disponibles :");
		sender.sendMessage("§c- create");
		sender.sendMessage("§c- key");
		sender.sendMessage("§c- types");
		sender.sendMessage("§c- list");
		sender.sendMessage("§c- delete");
	}
}
