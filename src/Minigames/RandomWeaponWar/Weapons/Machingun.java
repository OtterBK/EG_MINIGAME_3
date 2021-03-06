package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class Machingun extends SpecialWeapon{
	
	public Machingun(Minigame minigame) {
		super(minigame, Material.IRON_INGOT, 1, (short)0, "속사기", 12, 12); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e화살을 매우 빠른속도로 연사할 수 있다.");
		loreList.add("§e다만 사정거리는 짧고");
		loreList.add("§e데미지 또한 강하지는 않다.");
		loreList.add("§e우클릭시 사용이 가능하다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b0.35초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 0.35);
				
		Location pel = p.getEyeLocation();
		
		Arrow bulletArrow = p.launchProjectile(Arrow.class);
        bulletArrow.setVelocity(pel.getDirection().multiply(1));
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 1.0f, 2.0f);
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
		// TODO Auto-generated method stub
		
	}
	
}
