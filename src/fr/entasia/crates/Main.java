package fr.entasia.crates;

import fr.entasia.crates.utils.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Main extends JavaPlugin {

	public static Main inst;
    public static ArrayList<CrateType> crateTypes = new ArrayList<>();
    public static HashMap<Block, CrateType> crateLocs = new HashMap<>();


	@Override
	public void onEnable() {
		try{
			saveDefaultConfig();
			inst = this;
			getLogger().info("Plugin de crates acivé ");
            ConfigurationSection cs = getConfig().getConfigurationSection("cratelocs");
			Set<String> list = cs.getKeys(false);
			if(list==null)getLogger().warning("Aucune crate dans la configuration !");
			else{
				World w;
				Location loc;
				ConfigurationSection cs2;
				for(String s : list){
					cs2 = getConfig().getConfigurationSection("cratelocs."+s);
					w = Bukkit.getWorld(cs2.getString("world"));
					loc = new Location(w, cs2.getInt("x")+0.5, cs2.getInt("y")+0.2, cs2.getInt("z")+0.5);
					if(w==null){
						getLogger().warning("Monde "+cs2.getString("world")+" invalide pour la crate aux coordonnées "+loc);
						continue;
					}
					String name = cs2.getString("name");
					for(CrateType ct : crateTypes){
						if(ct.name.equals(name)){
							break;
						}
					}
				}
			}

			getServer().getPluginManager().registerEvents(new Listeners(),this);

		}catch(Throwable e){
			e.printStackTrace();
			getLogger().severe("LE SERVEUR VA S'ETEINDRE !");
			getServer().shutdown();
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin de crates désactivé");
	}
}
