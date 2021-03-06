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

public class OnePunch extends SpecialWeapon{
	
	private PotionEffect shortBlind = new PotionEffect(PotionEffectType.BLINDNESS, 80, 0);
	private PotionEffect longBlind = new PotionEffect(PotionEffectType.BLINDNESS, 160, 0);
	
	public OnePunch(Minigame minigame) {
		super(minigame, Material.FLINT, 1, (short)0, "핵펀치 글러브", 9, 10); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e아주 단단한 글러브다.");
		loreList.add("§e해당 무기로 타격된 적은 체력의 50%를 잃는다.");
		loreList.add("§e33초마다 사용된다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b33초");
	
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
		
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		if(!checkCooldown(damager, CooldownType.Primary)) return;
		
		setCooldown(damager, CooldownType.Primary, 33);
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 0.1f);
		victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
		e.setDamage((victim.getHealth()/2));
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		
	}
	
}
