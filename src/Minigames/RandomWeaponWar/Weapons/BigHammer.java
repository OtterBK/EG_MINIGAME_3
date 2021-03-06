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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.HeroesWar.Team;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;

public class BigHammer extends SpecialWeapon{
	
	private PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW, 80, 9);
	
	public BigHammer(Minigame minigame) {
		super(minigame, Material.DIAMOND_AXE, 1, (short)0, "대망치", 15, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e매우 큰 망치이다.");
		loreList.add("§e우클릭시 지면에 망치를 찍어내려");
		loreList.add("§e전방의 지진을 일으킨다.");
		loreList.add("§e지진 범위내의 적은 8피해를 받고");
		loreList.add("§e4초간 구속 10 디버프를 받는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b26초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 26);
		
		Location tmp = p.getEyeLocation();
		tmp.setYaw(tmp.getYaw()-85);
		Vector direction1 = tmp.getDirection();
		direction1.setY(0);
		direction1.multiply(4);
		tmp = p.getEyeLocation();
		tmp.setYaw(tmp.getYaw()+85);
		Vector direction2 = tmp.getDirection();
		direction2.setY(0);
		direction2.multiply(4);
		
		List<Player> enemyList = getEnemyList(p.getName());
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.01f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		
		vp.c_speed = 1f;
		vp.c_distance = 10;
		vp.c_time = 20;
		vp.c_surface = true;
		vp.c_pHorizenOffset = 6.0f;
		vp.c_pVerticalOffset = 0.5f;
		vp.c_removeOnHit = false;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				Location l1 = vp.projectileLoc.clone();
				l1.add(direction1);
				
				Location l2 = vp.projectileLoc.clone();
				l2.add(direction2);
				
				LineEffect ef = new LineEffect(effectManager);
				ef.particle = Particle.CRIT;
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
					hit.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hit.getLocation(), 1, 0.1F, 0.1f, 0.1f, 0.1f);
					hit.getWorld().playSound(hit.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
					hit.damage(8);
					hit.addPotionEffect(slowEffect);
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
