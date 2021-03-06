package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;

public class GravityController extends SpecialWeapon{
	
	public GravityController(Minigame minigame) {
		super(minigame, Material.GOLD_SPADE, 1, (short)0, "반중력 조절기", 11, 17); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e일시적으로 대상이 받는 중력을 반대로 만드는 조절기다.");
		loreList.add("§e우클릭시 빠른 속도로 레이저를 날린다.");
		loreList.add("§e이 레이저에 맞은 적은 위로 높이 띄어진다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b18초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 18);
	
		List<Player> enemyList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 2.5f;
		vp.c_distance = 50;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.REDSTONE, vp.projectileLoc, 2, 0F, 0f, 0f, 0f);
			}
		});

		
		vp.setOnHit(new Runnable() {
			public void run() {

				Player hit = vp.hitPlayer.get(0);
				
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.2f, 2.0f);
				
				hit.setVelocity(new Vector(0, 4.5f, 0));		
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
