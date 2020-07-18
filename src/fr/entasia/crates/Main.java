package fr.entasia.crates;

import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.crates.commands.CrateCommand;
import fr.entasia.crates.utils.CrateLoot;
import fr.entasia.crates.utils.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Random;

public class Main extends JavaPlugin {

	public static Main main;

    public static Random r = new Random();


    static{
		CrateType crateVote = new CrateType();
		crateVote.block = Material.CHEST;
		CrateLoot loot1 =new CrateLoot(80, new ItemBuilder(Material.IRON_SWORD).build());
		loot1.name = "Loot 1";
		CrateLoot loot2 =new CrateLoot(5, new ItemBuilder(Material.WOOD_SWORD).build());
		loot2.name = "Loot 2";
		CrateLoot loot3 =new CrateLoot(5, new ItemBuilder(Material.GOLD_SWORD).build());
		loot3.name = "Loot 3";
		CrateLoot loot4 =new CrateLoot(5, new ItemBuilder(Material.IRON_SPADE).build());
		loot4.name = "Loot 4";
		CrateLoot loot5 =new CrateLoot(5, new ItemBuilder(Material.DIAMOND_SWORD).build());
		loot5.name = "Loot 5";
		crateVote.loots.add(loot1);
		crateVote.loots.add(loot2);
		crateVote.loots.add(loot3);
		crateVote.loots.add(loot4);
		crateVote.loots.add(loot5);
		crateVote.name = "vote";
		crateVote.key = new ItemBuilder(Material.TRIPWIRE_HOOK).build();
		CratesAPI.registerCrate(crateVote);



    }


	@Override
	public void onEnable() {
		getCommand("crates").setExecutor(new CrateCommand());

		getServer().getPluginManager().registerEvents(new Listeners(),this);
		try{
			saveDefaultConfig();
			main = this;
			getLogger().info("Plugin de crates acivé ");
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
