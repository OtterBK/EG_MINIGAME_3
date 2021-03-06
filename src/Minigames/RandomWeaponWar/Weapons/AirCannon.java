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
		super(minigame, Material.GOLD_HOE, 1, (short)0, "������", 12, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���⸦ �����Ͽ� �߻��ϴ� ���� �����̴�.");
		loreList.add("��e��Ŭ���� ����� ���⸦ ������.");
		loreList.add("��e����� ����� ��ź�� �����ϸ� 5�� �������� �Բ�");
		loreList.add("��e���� ��ź ������������ ����������.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b11��");
	
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
