package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.VortexEffect;

public class DragonRelic extends SpecialWeapon{
	
	public DragonRelic(Minigame minigame) {
		super(minigame, Material.SKULL_ITEM, 1, (short)5, "드래곤 랠릭", 15, 20); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e고대 용의 유물이다.");
		loreList.add("§e우클릭시 전방을 향해 용의 포효를 날린다.");
		loreList.add("§e최대 50칸까지 영향을 미친다.");
		loreList.add("§e사용후에는 2초간 반동을 받는다.");
		loreList.add("§e쉬프트 우클릭시 용의 위압을 가해");
		loreList.add("§e주변의 적을 날려버린다.");
		loreList.add("§e쉬프트 좌클릭시 불을 날려 맞은 적을");
		loreList.add("§e12초간 불태운다.");
		loreList.add("");
		loreList.add("§e우클릭 쿨타임 : §b30초");
		loreList.add("§e쉬프트 우클릭 쿨타임 : §b10초");
		loreList.add("§e쉬프트 좌클릭 쿨타임 : §b12초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		
		if(!p.isSneaking()) {
			
			if(!checkCooldown(p, CooldownType.Primary)) return;
			
			setCooldown(p, CooldownType.Primary, 30);
		
			Location l = p.getEyeLocation();
			//l.setPitch(0);
			Location l2 = p.getLocation();
			//l2.setPitch(0);
			//p.teleport(l2);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 254));
			//p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 248));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 255));
			
			Vector v = l.getDirection();
			v.setY(0);
			v.normalize();
			
			Location l_start = l.clone().add(v);
			v.multiply(0.7);
			Location l_end = l.clone().add(v);
			
			EGScheduler sch = new EGScheduler(minigame);
			sch.schTime = 3;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {	
				public void run() {
					if(sch.schTime-- > 0) {
						VortexEffect ef = new VortexEffect(effectManager);
						ef.setLocation(l_start);
						ef.particle = Particle.REDSTONE;
						ef.color = Color.fromRGB(51,0,51);
						ef.grow = 0.4f;
						ef.iterations = 7;
						ef.setTargetLocation(l_end);
						ef.start();	
						
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 2.5f);
					} else {
						sch.cancelTask(true);
						
						List<Player> enemyList = getEnemyList(p.getName());
						VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
						vp.c_speed = 7f;
						vp.c_distance = 50;
						vp.c_time = 100;
						vp.c_removeOnHit = false;
						vp.c_removeOnBlock = true;
						vp.c_pHorizenOffset = 1.0f;
						vp.c_pVerticalOffset = 1.0f;
						
						vp.setOnDuring(new Runnable() {
							public void run() {
								if(!p.isDead()) {
									vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vp.projectileLoc, 3, 0.1F, 0.1f, 0.1f, 0.1f);
									vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 2.0f);
									for(Player t : enemyList) {
										if(vp.projectileLoc.distance(t.getLocation())<= 5) {
											t.damage(20);
										}
									}								
								}			
							}
						});
						
						vp.setOnHit(new Runnable() {
							public void run() {
								
							}
						});
						
						
						vp.setOnNonHit(vp.onHit);
											
						vp.launchProjectile();
					}
				}
			}, 0l, 4l);	
		}else {
			if(!checkCooldown(p, CooldownType.Secondary)) return;
			
			setCooldown(p, CooldownType.Secondary, 10);
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT, 1.0f, 0.3f);
			p.getWorld().spawnParticle(Particle.CRIT_MAGIC, p.getLocation(), 4, 1.5F, 1.5f, 1.5f, 0.1f);
			
			for(Player t : getEnemyList(p)) {
				double dis = t.getLocation().distance(p.getLocation());
				if(dis <= 7){
					Vector v = t.getLocation().toVector().subtract(p.getLocation().toVector());
					v.normalize();
					try {
						v.checkFinite();
					}catch (Exception e) {
						continue;
					}
					v.multiply(7.5-dis);
					t.setVelocity(v);
					t.getWorld().playSound(t.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 1.0f, 2.0f);
					t.getWorld().spawnParticle(Particle.SWEEP_ATTACK, t.getLocation(), 1, 0.01f, 0.01f, 0.01f, 1f);
				}
			}	
		}
	}

	@Override
	public void onLeftClick(Player p) {
		if(p.isSneaking()) {
			if(!checkCooldown(p, CooldownType.Teritary)) return;
			
			setCooldown(p, CooldownType.Teritary, 12);
			
			List<Player> enemyList = getEnemyList(p);
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_SHOOT, 1.0f, 1.5f);
			VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
			vp.c_speed = 1.5f;
			vp.c_distance = 50;
			vp.c_time = 100;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, vp.projectileLoc, 5, 0.1F, 0.1f, 0.1f, 0.1f);
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_ENDERDRAGON_HURT, 0.5f, 2.0f);
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 0.25f, 2.0f);
					Player hit = vp.hitPlayer.get(0);
					hit.setFireTicks(240);
					
				}
			});
			
			
			vp.launchProjectile();
		}
		
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
