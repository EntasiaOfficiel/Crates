package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import fr.entasia.errors.EntasiaException;
import org.bukkit.block.Block;

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
}
