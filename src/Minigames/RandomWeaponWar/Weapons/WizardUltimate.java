package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.ShieldEffect;

public class WizardUltimate extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public WizardUltimate(Minigame minigame) {
		super(minigame, Material.COAL, 1, (short)0, "불의 돌", 10, 13); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e매우 많은 양의  불꽃이 압축되어있는 돌");
		loreList.add("§e우클릭시 바라보는 곳에 불의 세례를 내린다.");
		loreList.add("§7불의 세례를 맞은 적은 40의 피해를 받고");
		loreList.add("§e6초의 화상피해를 입힌다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b48초");
	
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
		
		setCooldown(p, CooldownType.Primary, 48);
		
		List<Player> enemyList = getEnemyList(p.getName());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_SHOOT, 1.0f, 0.5f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 1.35f;
		vp.c_distance = 60;
		vp.c_time = 200;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				Location l = MyUtility.getGroundLocation(vp.projectileLoc);
				CircleEffect ef1 = new CircleEffect(effectManager);
				ef1.particle = Particle.CRIT_MAGIC;
				ef1.angularVelocityX = 0;
				ef1.angularVelocityY = 0;
				ef1.angularVelocityZ = 0;
				ef1.particleSize = 1;
				ef1.wholeCircle = true;
				ef1.particleOffsetX = 0.1f;
				ef1.particleOffsetZ = 0.1f;
				ef1.particleOffsetY = 0.1f;
				ef1.particleCount = 2;
				ef1.radius = 6.5f;
				ef1.period = 20;
				ef1.iterations = 6;
				ef1.setLocation(l);
				ef1.start();						                
				
				
				for(Player t : enemyList) {
					if(t.getLocation().distance(l) <= 10) {
						TitleAPI.sendFullTitle(t, 0, 40, 0, ChatColor.RED+"주의!", "당신은 마도사의 궁극기 범위내에 있습니다!");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 1.5f);
					}
				}
				
				EGScheduler sch = new EGScheduler(minigame);
				//sch.schTime = 10;
				sch.schId = Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
					
					public void run() {
						
						List<Player> enemyList = getEnemyList(p.getName());
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT, 1.0f, 1.5f);
						VirtualProjectile vp1 = new VirtualProjectile(minigame.server, p, enemyList);
						vp1.c_speed = 1.3f;
						vp1.c_distance = 80;
						vp1.c_time = 400;
						//vp1.c_pHorizenOffset = 3f;
						vp1.c_startLoc = l.clone().add(0,15,0);
						vp1.vector = new Vector(0,-1,0);
						
						vp1.setOnDuring(new Runnable() {
							public void run() {
								Location l = MyUtility.getGroundLocation(vp1.projectileLoc);
								ShieldEffect ef2 = new ShieldEffect(effectManager);
								ef2.particle = Particle.FLAME;
								ef2.particleSize = 1;
								ef2.particleOffsetX = 0.1f;
								ef2.particleOffsetZ = 0.1f;
								ef2.particleOffsetY = 0.1f;
								ef2.particleCount = 2;
								ef2.radius = 3;
								ef2.iterations = 1;
								ef2.setLocation(vp1.projectileLoc);
								ef2.start();									
							}
						});
						
						vp1.setOnNonHit(new Runnable() {
							public void run() {
								vp1.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vp1.projectileLoc, 2, 1F, 1f, 1f, 0.1f);
								vp1.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 3.0f, 0.5f);
								for(Player t : enemyList) {
									int dis = (int)t.getLocation().distance(vp1.projectileLoc);
									if(dis <= 10) {
										t.damage(40);
										t.setFireTicks(120);
									}
								}
							}
						});

						vp1.setOnHit(vp1.onNonHit);
						vp1.launchProjectile();
						
						sch.cancelTask(true);
					}
					
				}, 25l);
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
