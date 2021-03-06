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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import de.slikey.effectlib.effect.ShieldEffect;

public class Blinder extends SpecialWeapon{
	
	private PotionEffect shortBlind = new PotionEffect(PotionEffectType.BLINDNESS, 80, 0);
	private PotionEffect longBlind = new PotionEffect(PotionEffectType.BLINDNESS, 160, 0);
	
	public Blinder(Minigame minigame) {
		super(minigame, Material.COAL, 1, (short)0, "말소성 덩어리", 11, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e매우 어두운색의 물체다.");
		loreList.add("§e해당 무기로 적을 타격시 4초간 실명상태에 빠뜨린다.");
		loreList.add("§e우클릭시 10칸내 적을");
		loreList.add("§e8초간 실명상태에 빠뜨린다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b25초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 25);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 2.0f);	
		p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 15, 0.25F, 0.25f, 0.25f, 0.4f);
		TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§c§l주위에 어두운 기운이 퍼졌습니다.");
		
		List<Player> enemyList = getEnemyList(p);
		
		for(Player t : enemyList) {
			if(existPlayer(t)) {
				if(t.getLocation().distance(p.getLocation()) <= 7) {
					t.addPotionEffect(longBlind);
					TitleAPI.sendFullTitle(t, 10, 60, 10, "", "§c§l주위에 어두운 기운이 퍼졌습니다.");
					t.getWorld().playSound(t.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 2.0f);
				}			
			}
		}
		
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
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 2.0f);
		victim.addPotionEffect(shortBlind);
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		
	}
	
}
