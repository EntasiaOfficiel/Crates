package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import fr.entasia.errors.EntasiaException;

public class CratesAPI {
	public static void registerCrate(CrateType ct){
		if(Main.inst==null){
			Main.crateTypes.add(ct);
		}else throw new EntasiaException("Config already loaded ! Please add loadbefore: [Crates] in plugin.yml");
	}
}
