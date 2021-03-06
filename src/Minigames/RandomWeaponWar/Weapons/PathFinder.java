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
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class PathFinder extends SpecialWeapon{

	public PathFinder(Minigame minigame) {
		super(minigame, Material.EMERALD, 1, (short)0, "위치 탐지기", 11, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e위치를 감지하는 레이더");
		loreList.add("§e우클릭시 모든 플레이어와의 거리와");
		loreList.add("§e위치 좌표를 알 수 있다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b10초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 10);
		
		List<String> findList = minigame.ingamePlayer;
		
		Location pl = p.getLocation();
		p.sendMessage("§7--------- 위치 현황 ------------");
		for(String tName : findList) {
			if(tName.equalsIgnoreCase(p.getName())) continue;
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				Location tl = t.getLocation();
				p.sendMessage("§6"+t.getName()+" : §a"+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+" §6| 거리 : §a"+(int)tl.distance(pl)+"m");
			}
		}
		p.sendMessage("§7--------------------------------");
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 2.0f);
		
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
