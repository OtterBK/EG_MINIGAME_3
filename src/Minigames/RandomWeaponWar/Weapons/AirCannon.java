package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.HeroesWar.Team;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;

public class AirCannon extends SpecialWeapon{
	
	public AirCannon(Minigame minigame) {
		super(minigame, Material.GOLD_HOE, 1, (short)0, "공기포", 12, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e공기를 압축하여 발사하는 소형 대포이다.");
		loreList.add("§e우클릭시 압축된 공기를 날린다.");
		loreList.add("§e압축된 공기는 착탄시 폭발하며 5의 데미지와 함께");
		loreList.add("§e적을 착탄 지점기준으로 날려버린다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b11초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 11);
		
		p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_SHOOT, 1.2f, 2.5f);
		
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, getEnemyList(p.getName()));
		vp.c_speed = 4f;
		vp.c_distance = 40f;
		vp.c_time = 300;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.END_ROD, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.2f, 2.5f);	
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				Location l = vp.projectileLoc;
				for(Player t : getEnemyList(p.getName())) {
					double dis = l.distance(t.getLocation());
					if(dis < 5) {
						Vector v = t.getLocation().toVector().subtract(l.toVector());	
						v.normalize();
						v.multiply(3-dis/2);
						if(v.getY() > 1.5) v.setY(1.5);
						t.setVelocity(v);
					}
				}
			}
		});
		
		vp.setOnHit(new Runnable() {
			public void run() {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.2f, 2.5f);	
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				Location l = vp.projectileLoc;
				for(Player t : getEnemyList(p.getName())) {
					double dis = l.distance(t.getLocation());
					if(dis < 5) {
						Vector v = l.toVector().subtract(t.getLocation().toVector());	
						v.normalize();
						v.multiply(6-dis);
						if(v.getY() > 1.5) v.setY(1.5);
						t.setVelocity(v);
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
