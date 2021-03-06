package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
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
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.EarthEffect;

public class EscapeWeapon extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public EscapeWeapon(Minigame minigame) {
		super(minigame, Material.BONE, 1, (short)0, "긴급 탈출 장치", 14, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e사용자를 빠르게 이탈하게 해주는 장치");
		loreList.add("§e우클릭시 뒤로 물러난다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b2초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 2);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0f, 4.0f);
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, 0.5F, 0.5f, 0.5f, 0.1f);
		
		Location l = p.getLocation();
		l.setPitch(0);
		Vector v = l.getDirection();
		v.multiply(-2);
		v.setY(0);
		p.setVelocity(v);
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
