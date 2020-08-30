package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import fr.entasia.errors.EntasiaException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

public class CratesAPI {

	public static ArrayList<CrateType> crateTypes = new ArrayList<>();
	public static HashMap<Block, CrateType> crateLocs = new HashMap<>();

	public static void registerCrateType(CrateType ct){
		if(Main.main ==null){
			ct.name = ct.name.toLowerCase();
			CratesAPI.crateTypes.add(ct);

		}else throw new EntasiaException("Config already loaded ! Please add loadbefore: [Crates] in plugin.yml");
	}


	public static void deleteCrate(Player p,Block b){
		ConfigurationSection cs = Main.main.getConfig().getConfigurationSection("crateslocs");
		if(!crateLocs.containsKey(b)) p.sendMessage("§7Ce bloc n'est pas une crate");
		else{
			CratesAPI.crateLocs.remove(b);
			Location loc=b.getLocation();
			World w1 = loc.getWorld();
			double x1 = loc.getX();
			double y1 = loc.getY();
			double z1= loc.getZ();
			for(String s : cs.getKeys(false)){
				String path = "crateslocs."+s+".";
				double x = Main.main.getConfig().getInt(path+"x");
				double y = Main.main.getConfig().getInt(path+"y");
				double z = Main.main.getConfig().getInt(path+"z");
				String w = Main.main.getConfig().getString(path+"world");
				if(x==x1 && y==y1 && z==z1 && w.equalsIgnoreCase(w1.getName())){

					cs.set(s,null);
					Main.main.saveConfig();
					p.sendMessage("§7Vous avez delete la crate");
				}
			}
		}
	}

	public static void createCrate(Player p, Block b, CrateType ct){
		ConfigurationSection cs = Main.main.getConfig().getConfigurationSection("crateslocs");
		if(crateLocs.containsKey(b)) p.sendMessage("§7Ce bloc est déjà une crate");
		else{
			CratesAPI.crateLocs.put(b, ct);
			Location loc=b.getLocation();
			World w = loc.getWorld();
			double x = loc.getX();
			double y = loc.getY();
			double z= loc.getZ();
			int crateNumber = 1;
			if(cs==null){
				Main.main.getConfig().createSection("crateslocs");
			} else{
				crateNumber += cs.getKeys(false).size();
			}
			String path = "crateslocs."+crateNumber+".";
			Main.main.getConfig().set(path+"x",x);
			Main.main.getConfig().set(path+"y",y);
			Main.main.getConfig().set(path+"z",z);
			Main.main.getConfig().set(path+"world",w.getName());
			Main.main.getConfig().set(path+"type",ct.name);
			Main.main.saveConfig();
			p.sendMessage("§aTu as bien posé une crate §2"+ct.name+"§a !");

		}
	}

	@Nullable
	public static CrateType getCrateType(String name){
		for(CrateType ct : crateTypes){
			if(ct.name.equals(name))return ct;
		}
		return null;
	}

}
