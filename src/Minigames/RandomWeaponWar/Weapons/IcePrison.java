package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class IcePrison extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public IcePrison(Minigame minigame) {
		super(minigame, Material.ICE, 1, (short)0, "절대영도", 14, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e매우 차가운 얼음이다.");
		loreList.add("§e우클릭시 냉기를 날리며");
		loreList.add("§e냉기가 착탄한 지점 위로 8초간");
		loreList.add("§e3x3크기의 얼음 감옥을 만든다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b12초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 12);
		
		List<Player> enemyList = new ArrayList<Player>(minigame.ingamePlayer.size());
		for(String tName : minigame.ingamePlayer) {
			if(tName.equalsIgnoreCase(p.getName())) continue;
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				enemyList.add(t);
			}
		}
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BURN, 1.0f, 2.0f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 1f;
		vp.c_distance = 25;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.WATER_DROP, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				Location l = MyUtility.getGroundLocation(vp.projectileLoc).add(0,0.25,0);
				
				HashMap<Location, Material> targetMaterialMap = new HashMap<Location, Material>(25);
				HashMap<Location, Byte> targetDataMap = new HashMap<Location, Byte>(25);
				for(int y = -1; y <= 1; y++) {
					for (int x = -1; x <= 1; x++) {
						for (int z = -1; z <= 1; z++) {
							Location tl = l.clone().add(x, y, z);
							Block b = tl.getBlock();

							targetMaterialMap.put(tl, b.getType());
							targetDataMap.put(tl, b.getData());

							tl.getBlock().setType(Material.ICE);
						}
					}
				}
				
				
				
				p.playSound(p.getLocation(), Sound.BLOCK_SNOW_PLACE, 1.0f, 0.1f);
				vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_SNOW_PLACE, 1.0f, 0.1f);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
					public void run() {
						for(Location l : targetMaterialMap.keySet()) {
							Material mt = targetMaterialMap.get(l);
							byte data = targetDataMap.get(l);
							
							if(mt != null) {
								l.getBlock().setTypeIdAndData(mt.getId(), data, true);	
							}						
						}
						vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_GLASS_BREAK, 1.0f, 0.1f);
						p.playSound(vp.projectileLoc, Sound.BLOCK_GLASS_BREAK, 1.0f, 0.1f);
					}
				}, 160l);
			}
		});

		vp.setOnHit(vp.onNonHit);
		vp.launchProjectile();
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
