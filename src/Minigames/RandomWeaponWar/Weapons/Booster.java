package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class Booster extends SpecialWeapon{

	private boolean noFallDamage = false;
	
	public Booster(Minigame minigame) {
		super(minigame, Material.FIREWORK_CHARGE, 1, (short)0, "부스터 엔진", 13, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e강력한 추진력을 가진 엔진");
		loreList.add("§e우클릭시 바라보는 방향으로 멀리");
		loreList.add("§e날아갈 수 있다.");
		loreList.add("§e착지시 이 무기를 들고 있을 경우");
		loreList.add("§e부스터로인한 낙사데미지는 받지 않는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b15초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 15);
			
		noFallDamage = true;
		p.setVelocity(p.getEyeLocation().getDirection().multiply(4.5f));
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 0.5f);
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 5, 0.1F, 0.1f, 0.1f, 0.1f);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
			public void run() {
				noFallDamage = false;
			}
		},200l);
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL) {
			if(noFallDamage) {
				noFallDamage = false;
				e.setCancelled(true);	
			}
		}
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
