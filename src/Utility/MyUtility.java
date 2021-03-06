package Utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_12_R1.EntityShulker;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class MyUtility {

	private static AttributeModifier attackCooldownModi= new AttributeModifier("BetterPvP", 99999999.0D, AttributeModifier.Operation.ADD_NUMBER);
	private static int attackKey = 16;
	
	//포션 효과, 인벤토리 , 장비 초기화
	public static void allClear(Player player) {
		PlayerInventory pInv = player.getInventory();
		pInv.clear();
		pInv.setHelmet(null);
		pInv.setChestplate(null);
		pInv.setLeggings(null);
		pInv.setBoots(null);
		pInv.setItem(80, null);
		pInv.setItem(81, null);
		pInv.setItem(82, null);
		pInv.setItem(83	, null);
		player.setExp(0);
		player.setLevel(0);
		player.setFoodLevel(20);
		for(PotionEffect effect:player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
	}
	
	//블럭 빛나기...?
	public static EntityShulker sendGlowingBlock(Plugin server, Player p, Location loc, long lifetime){
		PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        //  Scoreboard nmsScoreBoard = ((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle();

          EntityShulker shulker = new EntityShulker(((CraftWorld) loc.getWorld()).getHandle());
          shulker.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
          shulker.setFlag(6, true); //Glow
          shulker.setFlag(5, true); //Invisibility

          PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(shulker);
          connection.sendPacket(spawnPacket);

          Bukkit.getScheduler().scheduleSyncDelayedTask(server, () -> {
              PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(shulker.getId());
              connection.sendPacket(destroyPacket);
          }, lifetime + (long) ((Math.random() + 1) * 100));
          
          return shulker;
    }
	
	public static EntityShulker sendGlowingBlock(Plugin server, Player p, Location loc){
		PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        //  Scoreboard nmsScoreBoard = ((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle();

          EntityShulker shulker = new EntityShulker(((CraftWorld) loc.getWorld()).getHandle());
          shulker.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
          shulker.setFlag(6, true); //Glow
          shulker.setFlag(5, true); //Invisibility

          PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(shulker);
          connection.sendPacket(spawnPacket);
          
          return shulker;
    }
	
	public static void removeGlowingBlock(Player p, EntityShulker shulker){
		PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

		PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(shulker.getId());
		connection.sendPacket(destroyPacket);
          
    }
	
	
	//번개 효과 보내기
	public static void sendLightning(Player p, Location l){
        Class<?> light = getNMSClass("EntityLightning");
        try {
            Constructor<?> constu =
                    light
                    .getConstructor(getNMSClass("World"),
                             double.class, double.class,
                             double.class, boolean.class, boolean.class);
            Object wh  = p.getWorld().getClass().getMethod("getHandle").invoke(p.getWorld());
            Object lighobj = constu.newInstance(wh, l.getX(), l.getY(), l.getZ(), false, false);
         
            Object obj =
                    getNMSClass("PacketPlayOutSpawnEntityWeather")
                    .getConstructor(getNMSClass("Entity")).newInstance(lighobj);
         
            sendPacket(p, obj);
            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 100, 1);
        } catch (NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
 
    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
 
     public static void sendPacket(Player player, Object packet) {
         try {
             Object handle = player.getClass().getMethod("getHandle").invoke(player);
             Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
             playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"))
             .invoke(playerConnection, packet);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
	
	//네임 태그 변경 - 안됨
	@Deprecated
	public static void changeName(String name, Player player) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", (Class<?>[]) null);
            Object entityPlayer = getHandle.invoke(player);
            Class<?> entityHuman = entityPlayer.getClass().getSuperclass();
            Field bH = entityHuman.getDeclaredField("bH");
            bH.setAccessible(true);
            bH.set(entityPlayer, new GameProfile(player.getUniqueId(), name));
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.hidePlayer(player);
                players.showPlayer(player);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
	
	public static void potionClear(Player player) {
		for(PotionEffect effect:player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
	}
	
	//min ~ max 중 값 1개 반환
	public static int getRandom(int min, int max) {
		return (int)(Math.random() * (max - min + 1) + min);
	}
	
	public static Color getRandomColor() {
		int rn = getRandom(0, 11);
		switch(rn) {
		case 0: return Color.AQUA;
		case 1: return Color.BLACK;
		case 2: return Color.BLUE;
		case 3: return Color.GREEN;
		case 4: return Color.GRAY;
		case 5: return Color.LIME;
		case 6: return Color.NAVY;
		case 7: return Color.ORANGE;
		case 8: return Color.PURPLE;
		case 9: return Color.RED;
		case 10: return Color.WHITE;
		case 11: return Color.YELLOW;
		default: return Color.AQUA;
		}
	}
	
	//풀피로 만들어주기
	public static void healUp(Player p) {
		p.setHealth((p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
	}
	
	//해시맵을 받아 그 해시맵을 무작위 순서로 석어줌
	public static void mixMap(HashMap map) {
		if (map.size() > 1) {
			List keylist = new ArrayList();
			List valuelist = new ArrayList();
			keylist.addAll(map.keySet());
			valuelist.addAll(map.values());
			map.clear();
			while (keylist.size() > 0) {
				int ri = MyUtility.getRandom(0, keylist.size()-1);
				map.put(keylist.get(ri), valuelist.get(ri));
				keylist.remove(ri);
				valuelist.remove(ri);
			}
		}
	}
	
	//해시맵을 받아 그 해시맵을 무작위 순서로 석어줌
	public static void mixList(List list) {
		if (list.size() > 1) {
			List tmpList = new ArrayList(list);
			list.clear();
			while (tmpList.size() > 0) {
				int ri = MyUtility.getRandom(0, tmpList.size()-1);
				list.add(tmpList.get(ri));
				tmpList.remove(ri);
			}
		}
	}
	
	
	//소수점 2자리 반올림
	public static float getTwoRound(float num) {
		num *= 100;
		float ret = (int) num;
		
		return (ret/100);
	}
	
	//플레이어 이름들로 플레이어 객체 반환
	public static List<Player> stringListToPlayer(List<String> strList){
		
		List<Player> pList = new ArrayList<Player>(strList.size());	
		
		for(String s : strList) {
			
			if(s == null) continue;
			Player p = Bukkit.getPlayer(s);
			if(p!= null && p.isOnline()) {
				pList.add(p);
			}
				
		}
		
		return pList;
		
	}

	//해쉬맵을 value 기준으로  정렬
	public static List sortByValue(final Map map, boolean ascending) {
		List<String> list = new ArrayList();
		list.addAll(map.keySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Object v1 = map.get(o1);
				Object v2 = map.get(o2);
				return ((Comparable) v2).compareTo(v1);
			}
		});
		
		if(ascending) Collections.reverse(list); // 주석시 오름차순

		return list;

	}
	
	//로케이션의 땅블럭 위치
	public static Location getGroundLocation(Location l) {
		Location tmpL = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
		while(tmpL.getBlock().getType() == Material.AIR && tmpL.getY() > 0) {
			tmpL.setY(tmpL.getY()-1);
		}
		return tmpL.add(0,1.5f,0);
	}
	
	//로케이션의 가장가까운위블럭 위치
	public static Location getUpLocation(Location l) {
		int i = 1;
		Location tmpL = new Location(l.getWorld(), l.getX(), l.getY() + i, l.getZ());
		while(tmpL.getBlock().getType() == Material.AIR) {
			if(tmpL.getY() >= 255) {
				return null;
			} else {
				i += 1;
				tmpL.setY(tmpL.getY()+i);
			}
		}
		return tmpL.add(0,0f,0);
	}
	
	//로케이션의 가장 높은 블럭 위치
	public static Location getTopLocation(Location l, int limitY) {
		Location top = l;
		Location tmpL = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
		
		for(int i = tmpL.getBlockY(); i <= limitY; i++) {
			
			tmpL.setY(tmpL.getBlockY()+1);
			if(tmpL.getBlock().getType() != Material.AIR) {
				top = tmpL.clone();
			}
			
		}
		return top;
	}
		
	
	//어택 딜레이 활성/비활성
	public static void attackDelay(Player p, boolean enable) {
		try {
			if(enable) {
				Collection<AttributeModifier> c = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers();
				//p.sendMessage(c.toString());
				for(AttributeModifier a : c) {
					p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(a);
				}			
			} else {
				p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(attackCooldownModi);
			}
		} catch(IllegalArgumentException e) {
			
		}
	}
	
	public static float getNormalizeSpeed(float spd) {
		spd *= 10000000;
		spd = Math.round(spd);
		spd /= 10000000;
		return spd;
	}
	
	public static void setMaxHealth(Player p, double amt) { //최대 체력 설정
		try {
			p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(amt);
		} catch (IllegalArgumentException e) {
			
		}	
	}
	
	public static boolean compareLocation(Location loc1, Location loc2) {
		if(loc1.getBlockX() == loc2.getBlockX() &&
			loc1.getBlockY() == loc2.getBlockY() &&
			loc1.getBlockZ() == loc2.getBlockZ()) {
			return true;
		}
		return false;
	}
	
	///로케이션의 정수값으로만 
	public static Location getIntLocation(Player player) {
		return new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
	}



}
