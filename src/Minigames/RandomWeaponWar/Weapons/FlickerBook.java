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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class FlickerBook extends SpecialWeapon{

	public FlickerBook(Minigame minigame) {
		super(minigame, Material.PAPER, 1, (short)0, "점멸 주문서", 13, 17); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e점멸을 사용하는 법이 적힌 주문서");
		loreList.add("§e우클릭시 바라보는 방향으로 짧게 텔레포트한다.");
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
			
		Location pel = p.getEyeLocation();
		Location tl = pel.clone().add(pel.getDirection().multiply(5));
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1.0f, 2.0f);
		p.teleport(tl, TeleportCause.PLUGIN);
		p.getWorld().playSound(tl, Sound.ENTITY_SHULKER_TELEPORT, 1.0f, 2.0f);
		
		tl.getWorld().spawnParticle(Particle.SMOKE_LARGE, tl, 3, 0.1F, 0.1f, 0.1f, 0.1f);
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
