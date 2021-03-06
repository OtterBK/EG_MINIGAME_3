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

public class ThrowBomb extends SpecialWeapon{
	
	public ThrowBomb(Minigame minigame) {
		super(minigame, Material.DIAMOND_HOE, 1, (short)0, "폭약탄", 13, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e직선으로 나가는 폭탄이다.");
		loreList.add("§e우클릭시 바라보는 방향으로 폭탄을 날린다.");
		loreList.add("§e폭탄은 적 또는 벽에 맞을 시 폭발하며");
		loreList.add("§e폭심지 기준 5칸내 적에게 최대 18피해를 준다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b3초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 3);
		
		List<Player> enemyList = getEnemyList(p.getName());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0f, 0.35f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 1.7f;
		vp.c_distance = 40;
		vp.c_time = 100;
		vp.c_removeOnHit = false;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.CLOUD, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0.1F, 0.1f, 0.1f, 0.1f);
				vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 3.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 3.0f);
				for(Player t : enemyList) {
					int dis = (int)t.getLocation().distance(vp.projectileLoc);
			
					if(dis <= 5) {
						t.damage(18-(dis*3));
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
