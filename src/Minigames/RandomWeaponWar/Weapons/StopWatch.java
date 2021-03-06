package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class StopWatch extends SpecialWeapon{
	
	private PotionEffect slowPt = new PotionEffect(PotionEffectType.SLOW, 100, 249);
	//private PotionEffect jumpPt = new PotionEffect(PotionEffectType.JUMP, 100, 249);
	
	public StopWatch(Minigame minigame) {
		super(minigame, Material.WATCH, 1, (short)0, "스톱 워치", 11, 11); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e모든 플레이어의 시간을 멈추게 할 수 있는 시계이다.");
		loreList.add("§e우클릭시 모든 플레이어에게 5초간");
		loreList.add("§e구속250 디버프를 주고 점프를 할 수 없게한다.");
		loreList.add("");
		loreList.add("§e우클릭 쿨타임 : §b70초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 70);
		
		List<Player> enemyList = getEnemyList(p);
		TitleAPI.sendFullTitle(p, 0, 60, 0, "§6§l시간 정지","§e§l움직임이 느려집니다.");
		
		for(Player t : enemyList) {
			
			TitleAPI.sendFullTitle(t, 0, 60, 0, "§6§l시간 정지","§e§l움직임이 느려집니다.");
			t.getWorld().playSound(t.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1.0f, 2.5f);
			if(t.getName().equalsIgnoreCase(p.getName())) continue;
			t.addPotionEffect(slowPt);
			//t.addPotionEffect(jumpPt);
		}
		
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

