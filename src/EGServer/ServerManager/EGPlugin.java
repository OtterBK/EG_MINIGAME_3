package EGServer.ServerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.mojang.authlib.GameProfile;

import EGServer.EGServer;
import Utility.SkullCreator;
import Utility.Vector3D;

public class EGPlugin {
	
	public EGServer server;

	public EGPlugin(EGServer server) {
		this.server = server;
	}

	public void unloadPlugin() {

	}

	
	//////////// EG서버용 각종 유틸리티 ///////////////
	public String getFromFile(String specPath, String fileName, String param) {
		File file = new File(server.getDataFolder().getPath() + specPath, fileName);
		if (!file.exists() || file.isDirectory())
			return null;
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			return fileConfig.getString(param);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public void dirSetting(String folderName) {
		String path = server.getDataFolder().getAbsolutePath()+"\\"+folderName;
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public boolean isInArea(Location pl, Location l1, Location l2) {
		double minX = l1.getX() < l2.getX() ? l1.getX() : l2.getX();
		double maxX = l1.getX() > l2.getX() ? l1.getX() : l2.getX();
		double minY = l1.getY() < l2.getY() ? l1.getY() : l2.getY();
		double maxY = l1.getY() > l2.getY() ? l1.getY() : l2.getY();
		double minZ = l1.getZ() < l2.getZ() ? l1.getZ() : l2.getZ();
		double maxZ = l1.getZ() > l2.getZ() ? l1.getZ() : l2.getZ();
		
		if(minX <= pl.getX() &&
				maxX >= pl.getX() &&
				minY <= pl.getY() &&
				maxY >= pl.getY() &&
				minZ <= pl.getZ() &&
				maxZ >= pl.getZ()) {
			return true;
		} else {
			return false;	
		}
	}
	
	public boolean hasItem(Player p, ItemStack item, int amt) {
		int tamt = amt;
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (tamt > 0) {
				ItemStack pitem = p.getInventory().getItem(i);
				if (pitem != null && pitem == item) {
					tamt -= pitem.getAmount();
					if (tamt <= 0) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public boolean hasItem(Player p, Material itemMt, int amt) {
		int tamt = amt;
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (tamt > 0) {
				ItemStack pitem = p.getInventory().getItem(i);
				if (pitem != null && pitem.getType() == itemMt) {
					tamt -= pitem.getAmount();
					if (tamt <= 0) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public int countItem(Inventory inven, Material itemMt) {
		int amt = 0;
		for (int i = 0; i < inven.getSize(); i++) {
			ItemStack pitem = inven.getItem(i);
			if (pitem != null && pitem.getType() == itemMt) {
				amt += pitem.getAmount();
			}
		}

		return amt;
	}

	public boolean takeItem(Player p, ItemStack item, int amt) {
		int tamt = amt;
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (tamt > 0) {
				ItemStack pitem = p.getInventory().getItem(i);
				if (pitem != null && pitem == item) {
					tamt -= pitem.getAmount();
					if (tamt <= 0) {
						removeItem(p, item, amt);
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public boolean takeItem(Player p, Material itemMt, int amt) { //삭제할 개수가 부족하면 false 반환하고 삭제안함
		int tamt = amt;
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (tamt > 0) {
				ItemStack pitem = p.getInventory().getItem(i);
				if (pitem != null && pitem.getType() == itemMt) {
					tamt -= pitem.getAmount();
					if (tamt <= 0) {
						removeItem(p, itemMt, amt);
						return true;
					}
				}
			}
		}

		return false;
	}

	public void removeItem(Player p, ItemStack item, int amt) { //삭제할 개수가 부족하면 아이템 전체  삭제로 대체
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (amt > 0) {
				ItemStack pitem = p.getInventory().getItem(i);
				if (pitem != null && pitem == item) {
					if (pitem.getAmount() >= amt) {
						int itemamt = pitem.getAmount() - amt;
						pitem.setAmount(itemamt);
						p.getInventory().setItem(i, amt > 0 ? pitem : null);
						p.updateInventory();
						return;
					} else {
						amt -= pitem.getAmount();
						p.getInventory().setItem(i, null);
						p.updateInventory();
					}
				}
			} else {
				return;
			}
		}
	}
	
	public void removeItem(Player p, Material itemMt, int amt) {
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (amt > 0) {
				ItemStack pitem = p.getInventory().getItem(i);
				if (pitem != null && pitem.getType() == itemMt) {
					if (pitem.getAmount() >= amt) {
						int itemamt = pitem.getAmount() - amt;
						pitem.setAmount(itemamt);
						p.getInventory().setItem(i, amt > 0 ? pitem : null);
						p.updateInventory();
						return;
					} else {
						amt -= pitem.getAmount();
						p.getInventory().setItem(i, null);
						p.updateInventory();
					}
				}
			} else {
				return;
			}
		}
	}
	
	public ItemStack makeItem(Material mt, String name, int min, int max) {
		ItemStack tmpitem = new ItemStack(mt, 1);
		ItemMeta meta = tmpitem.getItemMeta();
		meta.setDisplayName("§c" + name);
		List<String> lorelist = new ArrayList<String>();
		String damage = "§7공격력: ";
		if (max == min) {
			damage += String.valueOf(min);
		} else {
			damage += min + "-";
			damage += max;
		}
		lorelist.add(damage);
		meta.setLore(lorelist);
		tmpitem.setItemMeta(meta);
		return tmpitem;
	}

	public ItemStack makeGun(Material mt, String name, int min, int max) {
		ItemStack tmpitem = new ItemStack(mt, 1);
		ItemMeta meta = tmpitem.getItemMeta();
		meta.setDisplayName("§c" + name);
		List<String> lorelist = new ArrayList<String>();
		String damage = "§7화력: ";
		if (max == min) {
			damage += String.valueOf(min);
		} else {
			damage += min + "-";
			damage += max;
		}
		lorelist.add(damage);
		meta.setLore(lorelist);
		tmpitem.setItemMeta(meta);
		return tmpitem;
	}

	public int getItemDamage(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore())
			return 1;
		List<String> lorelist = item.getItemMeta().getLore();
		if (lorelist.size() <= 0)
			return 1;
		String damagestr = lorelist.get(0);
		if (!damagestr.contains("공격력"))
			return 1;
		// §7공격력: 7~8
		if (!damagestr.contains("-"))
			return Integer.valueOf(damagestr.substring(7, 8));
		int min = Integer.valueOf(damagestr.substring(7, 8));
		int max = Integer.valueOf(damagestr.substring(9, 10));
		return (int) (Math.random() * (max - min + 1) + min);
	}
	
	public String getHeldMainItemName(Player p){
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return "meta없음";
		return item.getItemMeta().getDisplayName();
	}
	
	public String getHeldOffItemName(Player p){
		ItemStack item = p.getInventory().getItemInOffHand();
		if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return "meta없음";
		return item.getItemMeta().getDisplayName();
	}
	
	public ItemStack getPotionItemStack(PotionType type, boolean throwable, boolean extend, boolean upgraded, String displayName){
        ItemStack potion = new ItemStack(throwable ? Material.SPLASH_POTION : Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setBasePotionData(new PotionData(type, extend, upgraded));
        potion.setItemMeta(meta);
        return potion;
    }
	
	
	public ItemStack getHead(Player p) {
		return new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		//return SkullCreator.itemFromUuid(p.getUniqueId());
	}
	
	/*public ItemStack getHead(String name) {
		if(name == null) return null;
		Player p = Bukkit.getPlayer(name);
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		UUID id = p.getUniqueId();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + p.getName());
		skull.setItemMeta(meta);
		
		return skull;
	} 처리시간이 긴 것 같아서 안넣음*/ 
	
	public ItemStack getHead(String name) {	
		//Player p = Bukkit.getPlayer(name);
		//if(!existPlayer(p)) return null;
		return new ItemStack(Material.SKULL_ITEM, 1, (byte) 4);
	}
	
	
	public boolean existPlayer(Player p) {
		if(p == null || !p.isOnline()) return false;
		return true;
	}
	
	///사이드바지우기
	public void removeSidebar(Player p) {
		SidebarString tmpLine = new SidebarString("");
		Sidebar sidebar = new Sidebar("EG서버", server, 600, tmpLine);
		sidebar.hideFrom(p);
	}
	
    public List<Player> getCorsurPlayer(Player p, List<Player> targetList, int range, boolean multi) {
        final Player observer = p;
        
        Location observerPos1 = observer.getEyeLocation();
        Vector3D observerDir1 = new Vector3D(observerPos1.getDirection());
        Vector3D observerStart1 = new Vector3D(observerPos1);
        
        Location loc = p.getLocation();
        
        for(int i = 1; i <= 10; i++){
            Vector3D observerEnd1 = observerStart1.add(observerDir1.multiply(i));
            loc = new Location(p.getWorld(), observerEnd1.x, observerEnd1.y, observerEnd1.z);
        	if(loc.getBlock().getType() != Material.AIR){
        		if(i == 1){
        			range = i;
        		}
    			break;
        	}
        }
        
        Location observerPos = observer.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());

        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(range));

        List<Player> hitList = new ArrayList<Player>();
        Player hit = null;  

        // Get nearby entities
        for (Player target : targetList) 
        {
        // Bounding box of the given player
        	Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.8, 0, -0.8);
            Vector3D maximum = targetPos.add(0.8, 2, 0.8);

            if (target != observer && hasIntersection(observerStart, observerEnd, minimum, maximum)) 
            {
            	hitList.add(target);
            	if (hit == null || hit.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) 
            	{    
            		hit = target;
            	}
            }
        }
                
        // Hit the closest player
        if(multi) {
        	return hitList;
        } else {
        	if(hit != null) {
        		hitList.clear();
        		hitList.add(hit);
        		return hitList;
        	}
    
        }
        return null;

        
        
    }
    
    public void egCancelMovement(PlayerMoveEvent e) {
    	if(e.getFrom().getY() - e.getTo().getY() <= 0)
			e.setTo(e.getFrom());	
    }
    
    // Source:
   // [url]http://www.gamedev.net/topic/338987-aabb---line-segment-intersection-test/[/url]
   private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
       final double epsilon = 0.0001f;
        
       Vector3D d = p2.subtract(p1).multiply(0.5);
       Vector3D e = max.subtract(min).multiply(0.5);
       Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
       Vector3D ad = d.abs();
        
       if (Math.abs(c.x) > e.x + ad.x)
           return false;
       if (Math.abs(c.y) > e.y + ad.y)
           return false;
       if (Math.abs(c.z) > e.z + ad.z)
           return false;
        
       if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
           return false;
       if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
           return false;
       if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
           return false;
        
       return true;
   }

}
