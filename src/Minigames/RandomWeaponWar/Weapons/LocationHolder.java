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
import org.bukkit.util.Vector;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.EarthEffect;

public class LocationHolder extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public LocationHolder(Minigame minigame) {
		super(minigame, Material.WOOD_HOE, 1, (short)0, "���� ���� ��ġ", 14, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� ������ ������ ������ų�� �ִ� ��ġ");
		loreList.add("��e��Ŭ���� �ٶ󺸴� ������ ��ġ ���� ��ġ�� ������");
		loreList.add("��75�ʰ� �� ��ġ�� 8ĭ�� ���� �������.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b29��");
	
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
		
		setCooldown(p, CooldownType.Primary, 29);
		
		List<Player> enemyList = getEnemyList(p.getName());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1.0f, 3.0f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 3f;
		vp.c_distance = 20;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.TOWN_AURA, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				Location l = vp.projectileLoc;			
				Effect effect = new EarthEffect(effectManager);
				effect.setLocation(l);
				effect.period = 36;
				effect.iterations = 2;
				effect.start();
				
				EGScheduler sch = new EGScheduler(minigame);
				sch.schTime = 10;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
					public void run() {
						if(sch.schTime-- > 0) {
							l.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 2.5f);
							//l.getWorld().spawnParticle(Particle.SMOKE_LARGE, l, 20, 0.25F, 0.25f, 0.25f, 0.25f);
							for(Player t : getEnemyList(p.getName())) {
								if(t.getLocation().distance(l) < 8) {					
									Vector v = l.toVector().subtract(t.getLocation().toVector());
									v.normalize();
									v.multiply(1f);
									t.setVelocity(v);
								}
							}
						} else {
							sch.cancelTask(true);
						}							
					}
				}, 0l, 8l);
			}
		});
		
		vp.setOnHit(new Runnable() {
			public void run() {
				Location l = vp.projectileLoc;			
				Effect effect = new EarthEffect(effectManager);
				effect.setLocation(l);
				effect.period = 36;
				effect.iterations = 2;
				effect.start();
				
				EGScheduler sch = new EGScheduler(minigame);
				sch.schTime = 10;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
					public void run() {
						if(sch.schTime-- > 0) {
							l.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 2.5f);
							//l.getWorld().spawnParticle(Particle.CRIT, l, 60, 0.25F, 0.25f, 0.25f, 0.25f);
							for(Player t : getEnemyList(p.getName())) {
								if(t.getLocation().distance(l) < 8) {
									Vector v = l.toVector().subtract(t.getLocation().toVector());
									v.normalize();
									v.multiply(1f);
									t.setVelocity(v);
								}
							}
						} else {
							sch.cancelTask(true);
						}							
					}
				}, 0l, 8l);
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
