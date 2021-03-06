package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class WitherKnife extends SpecialWeapon{
	
	
	private PotionEffect witherEffect = new PotionEffect(PotionEffectType.WITHER, 120, 1);
	
	public WitherKnife(Minigame minigame) {
		super(minigame, Material.STONE_HOE, 1, (short)0, "위더 표창", 10, 17); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e위더의 독을 바른 수리검이다.");
		loreList.add("§e우클릭시 바라보는 방향으로");
		loreList.add("§e3개의 표창을 날린다.");
		loreList.add("§e수리검에 맞은 적은 표창 1개당 5 피해를 받고");
		loreList.add("§e6초간 위더 상태에 빠진다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b9초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 9);
				
		p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.2f, 2.0f);
		
		List<Player> enemyList = getEnemyList(p);
		
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 5f;
		vp.c_distance = 18;
		vp.c_upOffset = -0.5f;
		vp.c_time = 40;
		vp.c_removeOnHit = true;
		vp.c_pVerticalOffset = 0.5f;
		vp.c_pHorizenOffset = 0.5f;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		vp.setOnHit(new Runnable() {
			public void run() {
				Player t = vp.hitPlayer.get(0);					
				t.damage(5);
				t.addPotionEffect(witherEffect);
			}
		});
		
		vp.launchProjectile();
		
		VirtualProjectile vp2 = new VirtualProjectile(minigame.server, p, enemyList);
		vp2.c_speed = 6f;
		vp2.c_distance = 15;
		vp2.c_upOffset = -0.5f;
		vp2.c_time = 40;
		vp2.c_removeOnHit = true;
		Location l = p.getLocation();
		l.setYaw(l.getYaw()+15);
		vp2.vector = l.getDirection();
		vp.c_pVerticalOffset = 0.5f;
		vp.c_pHorizenOffset = 0.5f;
		
		
		vp2.setOnDuring(new Runnable() {
			public void run() {
				vp2.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp2.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		vp2.setOnHit(new Runnable() {
			public void run() {
				Player t = vp2.hitPlayer.get(0);					
				t.damage(5);
				t.addPotionEffect(witherEffect);
			}
		});
		
		vp2.launchProjectile();
		
		VirtualProjectile vp3 = new VirtualProjectile(minigame.server, p, enemyList);
		vp3.c_speed = 5f;
		vp3.c_distance = 18;
		vp3.c_upOffset = -0.5f;
		vp3.c_time = 40;
		vp3.c_removeOnHit = true;
		l = p.getLocation();
		l.setYaw(l.getYaw()-15);
		vp3.vector = l.getDirection();
		vp.c_pVerticalOffset = 0.5f;
		vp.c_pHorizenOffset = 0.5f;
		
		vp3.setOnDuring(new Runnable() {
			public void run() {
				vp3.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp3.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		vp3.setOnHit(new Runnable() {
			public void run() {
				Player t = vp3.hitPlayer.get(0);					
				t.damage(5);
				t.addPotionEffect(witherEffect);
			}
		});
		
		vp3.launchProjectile();
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
	
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
