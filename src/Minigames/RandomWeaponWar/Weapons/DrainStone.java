package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import de.slikey.effectlib.effect.ShieldEffect;

public class DrainStone extends SpecialWeapon{
	
	boolean draining = false;
	
	public DrainStone(Minigame minigame) {
		super(minigame, Material.CHORUS_FRUIT_POPPED, 1, (short)0, "변환의 돌", 10, 10); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e사상을 변환하는 돌");
		loreList.add("§e우클릭 시 3초간 직접 받는 모든 피해를");
		loreList.add("§e자신의 체력으로 변환한다.");
		loreList.add("§e효과 사용중에 무기를 들고있어야 적용된다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b29초");
		loreList.add("§c왼손에 들어도 능력 유지됨");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 29);
			
		draining = true;
		
		ShieldEffect particle = new ShieldEffect(effectManager);
		particle.setLocation(p.getLocation().add(0,0.5f,0));
		particle.particle = Particle.REDSTONE;
		particle.color = Color.fromRGB(255, 00, 255);
		particle.period = 20;
		particle.iterations = 3;
		particle.particleCount = 20;
		particle.radius = 1.5d;
		particle.start();
						
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 0.1F);
		
		EGScheduler sch = new EGScheduler(minigame);
		sch.schTime = 3;
		sch.schId =	Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				if(!minigame.ingamePlayer.contains(p.getName())) sch.cancelTask(true);
				if(sch.schTime > 0) {
					sch.schTime--;
					TitleAPI.sendFullTitle(p, 0, 30, 0, "§c§l"+sch.schTime, "§e§l변환의 돌 효과 종료까지");
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 0.5f);
				}else {
					sch.cancelTask(true);
					TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§e§l변환의 돌 효과 종료");
					draining = false;
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0F, 2.0F);
				}
				
			}
		}, 0l, 20l);
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(draining) {
			e.setCancelled(true);
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
		if(draining) {
			e.setCancelled(true);
			
			double newHp = victim.getHealth() + e.getDamage();
			if(newHp > victim.getMaxHealth()) newHp = victim.getMaxHealth();
			
			victim.setHealth(newHp);
			
			damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1.0F, 0.1F);
			victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
		}
	}
	
}
