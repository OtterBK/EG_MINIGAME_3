package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class Levitator extends SpecialWeapon{

	private boolean noFallDamage = false;
	
	public Levitator(Minigame minigame) {
		super(minigame, Material.QUARTZ, 1, (short)0, "무중력 장치", 12, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e중력을 제어할 수 있는 장치");
		loreList.add("§e우클릭시 5초간 비행할 수 있다.");
		loreList.add("§e비행중 장치가 꺼지면 낙사데미지를 받는다.");
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
		
		boolean usingParkour = minigame.server.egParkour.isUsing(p.getName());
		if(usingParkour) {
			minigame.server.egParkour.quit(p.getName());
		}
		
		p.setAllowFlight(true);
		p.setFlying(true);
		p.setFlySpeed(0.05f);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 0.5f);
		EGScheduler sch = new EGScheduler(minigame);
		sch.schTime = 6;
		sch.schId =	Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				if(!minigame.ingamePlayer.contains(p.getName())) sch.cancelTask(true);
				if(sch.schTime > 1) {
					sch.schTime--;
					TitleAPI.sendFullTitle(p, 0, 30, 0, "§c§l"+sch.schTime, "§e§l장치가 꺼지기까지");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PARROT_FLY, 1.0f, 0.5f);
				}else {
					sch.cancelTask(true);
					TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§e§l장치 베터리 없음");
					p.setAllowFlight(false);
					p.setFlying(false);
					
					if(usingParkour) {
						minigame.server.egParkour.join(p.getName());
					}
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
