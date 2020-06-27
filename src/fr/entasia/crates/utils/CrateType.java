package fr.entasia.crates.utils;

import fr.entasia.crates.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Vector;

public class CrateType {

    public static int PLACES = 32;
    public static double THEPI = Math.PI/PLACES*2;


    public static class Prize{

        public ArmorStand am;
        public CrateLoot loot;
        public int place;

        public Prize(Entity ent){
            this.am = (ArmorStand) ent;
        }

        public void move(){
            place++;
            if(place==PLACES)place = 0;
        }

    }


    public ArrayList<CrateLoot>  loots = new ArrayList<>();
    public String name;
    public ItemStack key;
    public ArmorStand end;

    public boolean canOpen(Player p){
        ItemStack item = p.getInventory().getItemInMainHand();
        return item.getType().equals(key.getType()) && item.getItemMeta().getDisplayName().equals(key.getItemMeta().getDisplayName());
    }

    public void open(Block block){
        DirectionalContainer chest = (DirectionalContainer) block.getState().getData();
        Location center = block.getLocation();
        Location end = center;
        switch(chest.getFacing()){
            case NORTH:{
                end.add(0,0,-2);
                end.setYaw((float) Math.toRadians(0));
                break;
            }
            case EAST:{
                end.add(2,0,0);
                end.setYaw((float) Math.toRadians(90));
                break;
            }
            case SOUTH:{
                end.add(0,0,2);
                end.setYaw((float) Math.toRadians(180));
                break;
            }
            case WEST:{
                end.add(-2,0,0);
                end.setYaw((float) Math.toRadians(-90));
                break;
            }
            default:{
                return;
            }
        }
        ArmorStand asd = (ArmorStand) center.getWorld().spawnEntity(end, EntityType.ARMOR_STAND);
        asd.setArms(true);
        asd.setSmall(true);
        asd.setBasePlate(false);
        asd.setGravity(false);

        asd.setRightArmPose(translate(-10,90,90));
        asd.setItemInHand(new ItemStack(Material.STICK));
        asd.setInvulnerable(true);
        asd.setCanPickupItems(false);
        this.end = asd;

        ArrayList<Prize> armorStands = new ArrayList<>();
        int i = Main.r.nextInt(loots.size());
        for(Location l : getCirclePoints(loots.size(), 2, center)){
            Prize p = new Prize(l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND));
            p.loot = loots.get(i);
            p.place = i;

            armorStands.add(p);
            i++;
            if(i==loots.size())i=0;
        }



        new BukkitRunnable() {
            final int max = Main.r.nextInt(40)+300; // 80-100
            int time = 0;
            int period = 1;
            double t = 0;
            public Prize near;
            @Override
            public void run() {
                time++;
                if(time%period!=0)return; //ralentissement de l'animation
                
                if(time>max){ // animation terminée
                    if(near==null){
                        int a = -1;
                        for(Prize p : armorStands){
                            if(p.place==0){
                                near = p;
                                break;
                            }
                            if(p.place>a){
                                a = p.place;
                                near = p;
                            }
                        }
                    }

                    if(near.place==0){
                        cancel();
//                        near.loot //TODO DONNER LE LOOT AU JOUEUR
                        return;
                    }

                }else{
                    if(time%5==0)period++; //ralentissement de l'animation





                    Location loc;
                    for(Prize p : armorStands){
                        loc = block.getLocation();
                        double x = 2*Math.cos(t);
                        double z = 2*Math.sin(t);
                        loc.add(x, 0, z);
                        p.am.teleport(loc);
                        p.move();
                    }
                    t+=THEPI;
                }

            }
        }.runTaskTimer(Main.inst, 0, 1);







        for(Prize p : armorStands){
            p.am.remove();
        }
        armorStands.clear();
        this.end.remove();

    }

    public static EulerAngle translate(double a, double b, double c){
        return new EulerAngle(Math.toRadians(a), Math.toRadians(b), Math.toRadians(c));
    }

    public static ArrayList<Location> getCirclePoints(int points, double radius, Location center){
        double slice = 2 * Math.PI / points;
        ArrayList<Location> locs = new ArrayList<>();
        for (int i = 0; i < points; i++)
        {
            double angle = slice * i;
            double newX = (center.getX() + radius * Math.cos(angle));
            double newZ = (center.getZ() + radius * Math.sin(angle));
            locs.add(new Location(center.getWorld(),newX,center.getY()+2,newZ));


        }
        return locs;
    }

}
