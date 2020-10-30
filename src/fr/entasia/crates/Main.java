package fr.entasia.crates;

import fr.entasia.apis.menus.MenuCreator;
import fr.entasia.crates.commands.CrateCmd;
import fr.entasia.crates.utils.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin {

	public static Main main;

	public static Random r = new Random();

	public static MenuCreator menu = new MenuCreator();


	@Override
	public void onEnable() {
		try{
			main = this;
			saveDefaultConfig();
			ConfigurationSection cs = getConfig().getConfigurationSection("crateslocs");
			if(cs==null) {
				getLogger().warning("Aucune crate dans la configuration !");
			} else{
				World w;
				Location loc;
				Block b;
				ConfigurationSection cs2;
				for(String s : cs.getKeys(false)){
					cs2 = getConfig().getConfigurationSection("crateslocs."+s);
					w = Bukkit.getWorld(cs2.getString("world"));
					loc = new Location(w, cs2.getInt("x")+0.5, cs2.getInt("y")+0.2, cs2.getInt("z")+0.5);
					if(w==null){
						getLogger().warning("Monde "+cs2.getString("world")+" invalide pour la crate aux coordonnées "+loc);
						continue;
					}
					String strType = cs2.getString("type");
					CrateType type = null;
					for(CrateType ct : CratesAPI.crateTypes){
						if(ct.name.equals(strType)){
							type = ct;
							break;
						}
					}
					b = loc.getBlock();
					if(type==null){
						getLogger().warning("Type "+strType+" invalide pour la crate aux coordonnées "+loc);
					}else if(b.getType()==type.block){
						CratesAPI.crateLocs.put(b, type);
					}else{
						getLogger().warning("Block invalide pour la crate aux coordonnées "+loc);
					}
				}
			}


			getCommand("crates").setExecutor(new CrateCmd());

			getServer().getPluginManager().registerEvents(new Listeners(),this);

			getLogger().info("Plugin de crates activé !");
		}catch(Throwable e){
			e.printStackTrace();
			getLogger().severe("Une erreur est survenue ! LE SERVEUR VA S'ETEINDRE");
			getServer().shutdown();
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("Plugin de crates désactivé");
	}
}
