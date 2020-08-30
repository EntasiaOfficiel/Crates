package fr.entasia.crates.utils;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.crates.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;

public class CrateType {

	private static final int PLACES = 32;

	public Material block = Material.CHEST;

	static class Prize{

		public ArmorStand am;
		public CrateLoot loot;
		public double angle;

		public Prize(Entity ent){
			this.am = (ArmorStand) ent;
		}

	}


	protected ArrayList<CrateLoot> loots = new ArrayList<>();
	public int maxPercent;
	public String name;
	public ItemStack key;

	public void addLoot(CrateLoot loot){
		loots.add(loot);
		maxPercent+=loot.chance;
	}

	public boolean removeKey(Player p){
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item.isSimilar(key)){
			item.subtract();
			return true;
		}else return false;
	}

	public void open(Block block, Player player){

		block.setMetadata("opening", new FixedMetadataValue(Main.main, true));
		String name;
		for(ArmorStand as : block.getLocation().getNearbyEntitiesByType(ArmorStand.class, 3)){
			name = as.getCustomName();
			if("AMPointer".equals(name)||"AMPrize".equals(name)){
				as.remove();
			}
		}

		DirectionalContainer chest = (DirectionalContainer) block.getState().getData();
		Location centerLoc = block.getLocation().add(0.5, 0, 0.5);
		Location pointerLoc = centerLoc.clone();

		double baseAngle;
		switch(chest.getFacing()){
			case NORTH:{
				pointerLoc.add(0,0,-2.5);
				pointerLoc.setYaw(-90);
				baseAngle = Math.toRadians(270);
				break;
			}
			case EAST:{
				pointerLoc.add(2.5,0,0);
				pointerLoc.setYaw(0);
				baseAngle=Math.toRadians(0);
				break;
			}
			case SOUTH:{
				pointerLoc.add(0,0,2.5);
				pointerLoc.setYaw(90);
				baseAngle = Math.toRadians(90);
				break;
			}
			case WEST:{
				pointerLoc.add(-2.5,0,0);
				pointerLoc.setYaw(180);
				baseAngle = Math.toRadians(180);
				break;
			}
			default:{
				return;
			}
		}

		ArmorStand pointer = (ArmorStand) pointerLoc.getWorld().spawnEntity(pointerLoc, EntityType.ARMOR_STAND);
		pointer.setCustomName("AMPointer");
		pointer.setHeadPose(translate(0, 0, 0));
		pointer.setArms(true);
		pointer.setSmall(true);
		pointer.setBasePlate(false);
		pointer.setGravity(false);

		pointer.setRightArmPose(translate(-10,90,90));
		pointer.setItemInHand(new ItemStack(Material.STICK));
		pointer.setInvulnerable(true);
		pointer.setCanPickupItems(false);

		pointer.setVisible(false);
		pointer.setInvulnerable(true);

		final Prize[] armorStands = new Prize[loots.size()];
		int j = Main.r.nextInt(loots.size());

		double slice = Math.PI*2/loots.size();

		for(int i=0;i<loots.size();i++){
			Prize p = new Prize(centerLoc.getWorld().spawnEntity(pointerLoc, EntityType.ARMOR_STAND));
			p.am.setCustomName("AMPrize");
			p.loot = loots.get(j);
			p.angle=baseAngle+slice*i;
			p.am.setItemInHand(p.loot.item);
			p.am.setArms(true);
			p.am.setInvulnerable(true);
			p.am.setVisible(false);
			p.am.setCanPickupItems(false);
			p.am.setGravity(false);
			p.am.setRightArmPose(translate(0,270,320));
			armorStands[i] = p;
			j++;
			if(j==loots.size())j=0;
		}


		Prize temp = null;
		Randomiser r = new Randomiser(maxPercent);
		for(Prize p : armorStands){
			if(r.isInNext(p.loot.chance)){
				temp = p;
				break;
			}
		}
		if(temp ==null){
			player.sendMessage("§cUne erreur s'est produite ! Contacte un membre du Staff !");
			return;
		}

		Prize closestPrize = temp;
		new BukkitRunnable() {
			final double max = Main.r.nextInt(20*2) + 20*6;
			int time = 0;
			int period = 1;
			byte mode = 0;
			/*
			0 - normal
			1 - end
			2 - wait
			 */

			@Override
			public void run() {

				time++;
				if(time%period!=0)return; // ralentissement de l'animation


				if(mode==2){
					if(time>40){
						cancel();
						for(Prize prize : armorStands){
							prize.am.remove();
						}
						pointer.remove();
						block.removeMetadata("opening", Main.main);
					}
				}else {
					if (mode == 1) { //derniers déplacements
						double tFinal = closestPrize.angle - baseAngle;
						if (tFinal % (Math.PI * 2) <= 1) {
							period = 5;

							player.sendMessage("§aFin du tirage !");

							mode = 2;
							closestPrize.loot.win(player);
							time = 0;
						}
					} else {
						if (time > max) { // animation de base terminée
							mode = 1;
						} else if (time % 45 == 0 && period < 4) {
							period++; //ralentissement de l'animation
						}
					}

					Location loc;
					for (Prize p : armorStands) {
						loc = centerLoc.clone();

						double x = 2 * Math.cos(p.angle);
						double z = 2 * Math.sin(p.angle);
						loc.add(x, 0, z);
						p.am.teleport(loc);

						p.angle += Math.PI * 2 / PLACES;


					}
				}
			}
		}.runTaskTimer(Main.main, 0, 1);







	}

	public static EulerAngle translate(double a, double b, double c){
		return new EulerAngle(Math.toRadians(a), Math.toRadians(b), Math.toRadians(c));
	}

}
