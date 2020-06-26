package fr.entasia.crates.utils;

import fr.entasia.crates.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
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

    public ArrayList<CrateLoot>  loots = new ArrayList<>();
    public String name;
    public ItemStack key;
    public ArrayList<Location> locations = new ArrayList<>();
    public ArrayList<ArmorStand> armorStands = new ArrayList<>();
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
            case NORTH:
                end.add(0,0,-2);
                end.setYaw((float) Math.toRadians(0));
            case EAST:
                end.add(2,0,0);
                end.setYaw((float) Math.toRadians(90));
            case SOUTH:
                end.add(0,0,2);
                end.setYaw((float) Math.toRadians(180));
            case WEST:
                end.add(-2,0,0);
                end.setYaw((float) Math.toRadians(-90));
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
        locations.addAll(getCirclePoints(loots.size(), 2, center));
        final int[] time = {0};
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                time[0]++;
                if(time[0]>=100){
                    cancel();
                }
                if(time[0] % 10==0){
                    for(int i=0; i<loots.size();i++){
                        int number = time[0]/10;


                    }
                }
            }
        }.runTask(Main.inst);





        for(ArmorStand ast : armorStands){
            ast.remove();
        }
        armorStands.clear();
        this.end.remove();
        locations.clear();

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
