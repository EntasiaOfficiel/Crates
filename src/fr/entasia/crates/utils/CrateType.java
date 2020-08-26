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

	public Material block;
	public static class Prize{

		public ArmorStand am;
		public CrateLoot loot;
		public double angle;
//		public int p;

		public Prize(Entity ent){
			this.am = (ArmorStand) ent;
		}

	}


	public ArrayList<CrateLoot> loots = new ArrayList<>();
	public String name;
	public ItemStack key;

	public boolean canOpen(Player p){
		ItemStack item = p.getInventory().getItemInMainHand();
		return item.isSimilar(key);
	}

	public void open(Block block, Player player){
		block.setMetadata("opening", new FixedMetadataValue(Main.main, true));
		DirectionalContainer chest = (DirectionalContainer) block.getState().getData();
		Location centerLoc = block.getLocation().add(0.5, 0, 0.5);
		Location pointerLoc = centerLoc.clone();
		ArmorStand pointer;

		double t1;
		switch(chest.getFacing()){
			case NORTH:{
				pointerLoc.add(0,0,-2.5);
				pointerLoc.setYaw(-90);
				t1 = Math.toRadians(270);
				break;
			}
			case EAST:{
				pointerLoc.add(2.5,0,0);
				pointerLoc.setYaw(0);
				t1=Math.toRadians(0);
				break;
			}
			case SOUTH:{
				pointerLoc.add(0,0,2.5);
				pointerLoc.setYaw(90);
				t1 = Math.toRadians(90);
				break;
			}
			case WEST:{
				pointerLoc.add(-2.5,0,0);
				pointerLoc.setYaw(180);
				t1 = Math.toRadians(180);
				break;
			}
			default:{
				return;
			}
		}

		ArmorStand asd = (ArmorStand) pointerLoc.getWorld().spawnEntity(pointerLoc, EntityType.ARMOR_STAND);
		asd.setCustomName("AMPointer");
		asd.setHeadPose(translate(0, 0, 0));
		asd.setArms(true);
		asd.setSmall(true);
		asd.setBasePlate(false);
		asd.setGravity(false);

		asd.setRightArmPose(translate(-10,90,90));
		asd.setItemInHand(new ItemStack(Material.STICK));
		asd.setInvulnerable(true);
		asd.setCanPickupItems(false);
		pointer = asd;
		asd.setVisible(false);
		asd.setInvulnerable(true);

		final Prize[] armorStands = new Prize[loots.size()];
		int j = Main.r.nextInt(loots.size());

		double slice = Math.PI*2/loots.size();

		for(int i=0;i<loots.size();i++){
			Prize p = new Prize(centerLoc.getWorld().spawnEntity(pointerLoc, EntityType.ARMOR_STAND));
			p.am.setCustomName("AMPrize");
			p.loot = loots.get(j);
			p.angle=t1+slice*i;
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


		Prize closestPrize = null;
		Randomiser r = new Randomiser();
		for(Prize p : armorStands){
			if(r.isInNext(p.loot.chance)){
				closestPrize = p;
				break;
			}
		}

		ArmorStand finalPointer = pointer;
		Prize finalClosestPrize = closestPrize;
		new BukkitRunnable() {
			final double max = Main.r.nextInt(20*2) + 20*6;
			int time = 0;
			int period = 1;
			boolean end = false;
			boolean wait = false;



			double closestIndex = 0;

			@Override
			public void run() {

				time++;
				if(time%period!=0)return; // ralentissement de l'animation


				if(wait){
					if(time>40){
						cancel();
						for(Prize prize : armorStands){
							prize.am.remove();
						}
						finalPointer.remove();
						block.setMetadata("opening", new FixedMetadataValue(Main.main, false));
						return;
					}
					return;


				}else if(end){ //derniers déplacements
					double tFinal = finalClosestPrize.angle - t1;
					if(tFinal%(Math.PI*2)<=1){

						player.sendMessage("§7Tu as gagné " + finalClosestPrize.loot.name);

						wait=true;
						finalClosestPrize.loot.win(player);
						time=0;

						return;
					}
				}else if(time>max){ // animation de base terminée
					System.out.println("fin");
					System.out.println(period);
					period = 6;
					double tFinal = finalClosestPrize.angle - t1;
					closestIndex= tFinal%(Math.PI*2);
					if(finalClosestPrize ==null){
						player.sendMessage("§cErreur , veuillez contacter un membre du staff !");
						cancel();
						return;
					}
					end = true;

				}else if(time%30==0&&period<4){
					System.out.println("update");
					period++; //ralentissement de l'animation
				}

				Location loc;
				for(Prize p : armorStands){
					loc = centerLoc.clone();

					double x = 2*Math.cos(p.angle);
					double z = 2*Math.sin(p.angle);
					loc.add(x, 0, z);
					p.am.teleport(loc);

					p.angle+=Math.PI*2/PLACES;

				}


			}
		}.runTaskTimer(Main.main, 0, 1);









	}

	public static EulerAngle translate(double a, double b, double c){
		return new EulerAngle(Math.toRadians(a), Math.toRadians(b), Math.toRadians(c));
	}

}
