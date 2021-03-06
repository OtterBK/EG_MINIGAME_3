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

public class RollerSkate extends SpecialWeapon{
	
	private PotionEffect longSpeed = new PotionEffect(PotionEffectType.SPEED, 200, 3);
	private PotionEffect shortSpeed = new PotionEffect(PotionEffectType.SPEED, 100, 0);
	
	public RollerSkate(Minigame minigame) {
		super(minigame, Material.PRISMARINE_CRYSTALS, 1, (short)0, "롤러 스케이트", 10, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e싱싱 달릴수 있는 롤러 스케이트다.");
		loreList.add("§e우클릭시 10초간 이속증가 4버프를 받는다.");
		loreList.add("§e피해를 받을 시 5초간 이속증가 1버프를 받는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b20초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 20);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 1.0f, 2.0f);	
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 15, 0.25F, 0.25f, 0.25f, 0.4f);
		TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§c§l달려 달려~.");
		p.addPotionEffect(longSpeed);
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(p.hasPotionEffect(PotionEffectType.SPEED)) return;
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
		if(!p.hasPotionEffect(PotionEffectType.SPEED)) p.addPotionEffect(shortSpeed);
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
