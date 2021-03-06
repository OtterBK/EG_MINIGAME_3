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
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class AiasShield extends SpecialWeapon{
	
	private float shield = 100;
	
	public AiasShield(Minigame minigame) {
		super(minigame, Material.SHIELD, 1, (short)0, "아이아스의 방패", 9, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e그리스의 영웅인 아이아스의 방패");
		loreList.add("§e방패를 들면 하단 레벨바에 방패의 내구도가");
		loreList.add("§e표시된다. 이 내구도가 다 없어질 때까지는");
		loreList.add("§e모든 피해를 방패가 대신 받는다.");
		loreList.add("§e내구도는 방패를 든 상태에서");
		loreList.add("§e우클릭을 하고 있지 않다면 충전된다.");	
		loreList.add("§c왼손에 들어도 능력 유지됨");
	
		setLore(loreList);
		
		timer();
	}

	private void timer() {
		EGScheduler sch = new EGScheduler(null);
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				for(String tName : minigame.ingamePlayer) {
					Player t = Bukkit.getPlayer(tName);
					if(existPlayer(t)) {
						if(compareItemName(t)) {
							t.setExp(shield/100);
							if(!t.isBlocking()) {
								shield += 5;
								if(shield > 100) shield = 100;
							}
						}
					}
				}
			}
		}, 0l, 10l);
	}
	
	@Override
	public void onInit() {
		cooldown.clear();
	}
	
	@Override
	public void onRightClick(Player p) {

	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(p.isBlocking()) {
			if(shield > 0) {
				shield -= e.getDamage();
				if(shield <= 0) {
					shield = 0;
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 0.5f);
				}else {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0f, 2.0f);
				}
				e.setDamage(0.01);
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

