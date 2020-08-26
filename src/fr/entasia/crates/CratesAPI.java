package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import fr.entasia.errors.EntasiaException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;

public class CratesAPI {

	public static ArrayList<CrateType> crateTypes = new ArrayList<>();
	public static HashMap<Block, CrateType> crateLocs = new HashMap<>();

	public static void registerCrate(CrateType ct){
		if(Main.main ==null){
			CratesAPI.crateTypes.add(ct);

		}else throw new EntasiaException("Config already loaded ! Please add loadbefore: [Crates] in plugin.yml");
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
