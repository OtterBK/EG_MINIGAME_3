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

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class OdinRod extends SpecialWeapon{
	
	public OdinRod(Minigame minigame) {
		super(minigame, Material.REDSTONE_TORCH_ON, 1, (short)0, "오딘의 지팡이", 13, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e마법의 신 오딘의 지팡이다.");
		loreList.add("§e우클릭시 주변 적에게 자신의 현재 체력의 절반만큼");
		loreList.add("§e피해를 주고 위로 띄운다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b24초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 24);
		
		List<Player> tmpList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_END_GATEWAY_SPAWN, 1.0f, 0.75f);
		TitleAPI.sendFullTitle(p, 10,60, 20, "", "마법을 발동합니다");
		
		for(Player t : tmpList) {
			if(t.getLocation().distance(p.getLocation()) <= 6) {
				t.damage((int)p.getHealth()/2);
				t.setVelocity(new Vector(0,1.0f,0));
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 1.2f);
				p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getLocation(), 30, 0.15F, 0.15f, 0.15f, 0.07f);
				TitleAPI.sendFullTitle(t, 10,60, 20, "", "오딘의 마법이 발동됐습니다.");
			}
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
