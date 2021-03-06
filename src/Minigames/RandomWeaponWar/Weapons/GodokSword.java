package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class GodokSword extends SpecialWeapon{

	private PotionEffect slowPt = new PotionEffect(PotionEffectType.SLOW, 200, 0);
	private PotionEffect SpeedPt = new PotionEffect(PotionEffectType.SPEED, 200, 2);
	private PotionEffect ResiPt = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0);
	private PotionEffect RegenPt = new PotionEffect(PotionEffectType.REGENERATION, 160, 0);
	
	public GodokSword(Minigame minigame) {
		super(minigame, Material.STONE_SWORD, 1, (short)0, "고독의 검", 14, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e저주 받아 방치되었던  검이다.");
		loreList.add("§e피해를 받았을 때 체력이 33% 이상일 시");
		loreList.add("§e10초간 구속 1버프를 받는다.");
		loreList.add("§e반대로 체력이 33% 미만일 시");
		loreList.add("§e10초간 이속3, 저항1 버프를 받는다.");
		loreList.add("§e피해를 받을 때 마다 이 효과는 갱신된다.");
		loreList.add("§e체력이 50% 미만인 상태에서 적을 공격할 시");
		loreList.add("§e8초간 재생버프를 받는다.");
		loreList.add("§c왼손에 들어도 능력 유지됨");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {

	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		double baseHealth = p.getMaxHealth() / 3;
		if(p.getHealth() - e.getDamage() <= baseHealth) {//피해 받으면 33% 이하일때
			p.addPotionEffect(SpeedPt);
			p.addPotionEffect(ResiPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1.0f,2.0f);
		}else {
			p.addPotionEffect(slowPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1.0f,0.1f);
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		if(damager.getHealth() < damager.getMaxHealth()/2) {
			damager.addPotionEffect(RegenPt);
		}
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
