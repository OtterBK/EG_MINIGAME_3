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

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;

public class WindShooter extends SpecialWeapon{
	
	public WindShooter(Minigame minigame) {
		super(minigame, Material.INK_SACK, 1, (short)15, "��ǳ ��ä", 14, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�ٶ��� ������ �ִ� ��ä�̴�.");
		loreList.add("��e��Ŭ���� ��ǳ�� ������.");
		loreList.add("��e��ǳ�� ���� ���� ũ�� ��������");
		loreList.add("��715�������� �޴´�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b12��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 12);
		
		Location tmp = p.getEyeLocation();
		tmp.setYaw(tmp.getYaw()-85);
		Vector direction1 = tmp.getDirection();
		direction1.setY(0);
		direction1.multiply(1.9f);
		tmp = p.getEyeLocation();
		tmp.setYaw(tmp.getYaw()+85);
		Vector direction2 = tmp.getDirection();
		direction2.setY(0);
		direction2.multiply(1.9f);
		
		Location pEye = p.getEyeLocation().clone();
		pEye.setPitch(0);
		Vector knockBack = pEye.getDirection();
		
		List<Player> enemyList = getEnemyList(p);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_JUMP, 1.0f, 0.1f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		
		vp.c_speed = 0.5f;
		vp.c_distance = 7;
		vp.c_time = 20;
		vp.c_pHorizenOffset = 4.0f;
		vp.c_removeOnHit = false;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				Location l1 = vp.projectileLoc.clone();
				l1.add(direction1);
				
				Location l2 = vp.projectileLoc.clone();
				l2.add(direction2);
				
				LineEffect ef = new LineEffect(effectManager);
				ef.particle = Particle.CLOUD;
				//ef.color = Color.fromRGB(153, 255, 204);
				ef.iterations = 1;
				ef.setLocation(l1);
				ef.setTargetLocation(l2);
				ef.start();
				
				//vp.projectileLoc.getWorld().spawnParticle(Particle.FLAME, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnHit(new Runnable() {
			public void run() {
				for(Player hit : vp.hitPlayer) {
					hit.getWorld().spawnParticle(Particle.FALLING_DUST, hit.getLocation(), 10, 0.1F, 0.1f, 0.1f, 0.1f);
					hit.getWorld().playSound(hit.getLocation(), Sound.ENTITY_HOSTILE_SWIM, 1.0f, 0.1f);
					hit.damage(15);
					hit.setVelocity(knockBack.multiply(4.5f));
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
