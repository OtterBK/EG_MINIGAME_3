package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class ShootFireball extends SpecialWeapon{
	
	public ShootFireball(Minigame minigame) {
		super(minigame, Material.MAGMA_CREAM, 1, (short)0, "화염구", 14, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e화염구를 생성 가능한 무기.");
		loreList.add("§e우클릭시 바라보는 방향으로");
		loreList.add("§e화염구를 날린다.");
		loreList.add("§e화염구에 맞은 적은  8 피해를 받고 5초간 불탄다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b4초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 4);
				
		Location pel = p.getEyeLocation();
		
		Fireball fireball = p.launchProjectile(Fireball.class);
		fireball.setVelocity(pel.getDirection().multiply(2));
		fireball.setShooter(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 2.0f);
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
		if(e.getDamager() instanceof Fireball) {
			victim.setFireTicks(100);
			victim.damage(5);
		}		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
