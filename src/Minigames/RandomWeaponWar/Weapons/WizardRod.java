package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;

public class WizardRod extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public WizardRod(Minigame minigame) {
		super(minigame, Material.BLAZE_ROD, 1, (short)0, "마도사의 지팡이", 8, 9); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e히어로즈워 마도사가 쓰다버린 지팡이다.");
		loreList.add("§e조금 약해졌지만 불의 힘이 담겨있다.");
		loreList.add("§e우클릭시 화염탄을 날린다.");
		loreList.add("§7화염탄은 블럭 또는 적을 맞출 시 폭발한다.");
		loreList.add("§7폭발지점 4칸내의 적은 8의 피해를 받고");
		loreList.add("§7직격한 플레이어는 20의 추가피해를 받으며");
		loreList.add("§75초간 화상피해를 입힌다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b5초");
	
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
		
		setCooldown(p, CooldownType.Primary, 5);
		
		List<Player> enemyList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 2.5f;
		vp.c_distance = 50;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.FLAME, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vp.projectileLoc, 1, 0.1F, 0.1f, 0.1f, 0.1f);
				vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
				for(Player t : enemyList) {
					int dis = (int)t.getLocation().distance(vp.projectileLoc);
					if(dis <= 3) {
						t.damage(5);
						t.setFireTicks(100);
					}
				}
			}
		});
		
		vp.setOnHit(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, vp.projectileLoc, 3, 0.1F, 0.1f, 0.1f, 0.1f);
				vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
				Player hit = vp.hitPlayer.get(0);
				hit.damage(20);
				for(Player t : enemyList) {
					int dis = (int)t.getLocation().distance(vp.projectileLoc);
					if(dis <= 4) {
						t.damage(8);
						t.setFireTicks(100);
					}
				}
			}
		});
		
		
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
