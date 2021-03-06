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

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import de.slikey.effectlib.effect.ShieldEffect;

public class BorrowHealth extends SpecialWeapon{
	
	public BorrowHealth(Minigame minigame) {
		super(minigame, Material.BREWING_STAND_ITEM, 1, (short)0, "미래 조정기", 15, 17); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e미래의 현상을 가져올 수 있는 무기");
		loreList.add("§e우클릭 시 체력을 50만큼 회복한다.");
		loreList.add("§e10초후  체력이 50만큼 깍인다.");
		loreList.add("§e단, 이로인해 즉사하지는 않는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b51초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 51);
			
		int amt = 50;
		double newHp = p.getHealth() + amt;
		if(newHp > p.getMaxHealth()) newHp = p.getMaxHealth();
		p.setHealth(newHp);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
			public void run() {
				p.damage(0.01);
				int amt = 50;
				double newHp = p.getHealth() - amt;
				if(newHp < 1) newHp = 1;
				p.setHealth(newHp);
			}
		}, 200l);
		
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

	}
	
}
