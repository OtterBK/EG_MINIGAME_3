package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.ShieldEffect;

public class SnipeRifle extends SpecialWeapon{
	
	public SnipeRifle(Minigame minigame) {
		super(minigame, Material.IRON_HOE, 1, (short)0, "저격총", 5, 5); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e평범한 저격총이다.");
		loreList.add("§e우클릭시 바라보는 곳으로 총알을 발사한다.");
		loreList.add("§e총알에 맞은 적은 20의 피해를 입는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b2초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 2);
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 4.0f, 0.25f);
			p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 10, 0.5F, 0.5f, 0.5f, 0.1f);
			
			Location l = p.getLocation();
			l.setPitch(0);
			Vector v = l.getDirection();
			v.multiply(-0.5);
			v.setY(0);
			p.setVelocity(v);
			List<Player> enemyList = getEnemyList(p.getName());
			VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
			vp.c_speed = 50f;
			vp.c_distance = 100;
			vp.c_time = 100;
			vp.c_pHorizenOffset = 0.4f;
			vp.c_pVerticalOffset = 0.3f;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
	
			vp.setOnHit(new Runnable() {
				public void run() {
					
					Player hit = vp.hitPlayer.get(0);
				
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.2f, 1.5f);
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.2f, 0.5f);
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 1.5f);
						hit.getWorld().playSound(hit.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 1.5f);
						hit.damage(20);
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.2f, 1.5f);
						vp.projectileLoc.getWorld().spawnParticle(Particle.REDSTONE, vp.projectileLoc, 4, 0F, 0f, 0f, 0.35f);
					
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
		
	}
	
}
