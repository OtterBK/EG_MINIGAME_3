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

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class ThunderAxe extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public ThunderAxe(Minigame minigame) {
		super(minigame, Material.GOLD_AXE, 1, (short)0, "토르의 망치", 13, 16); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e번개의 신 토르의 망치이다.");
		loreList.add("§e우클릭시 바라보는 곳에 번개를 떨군다.");
		loreList.add("§e쉬프트 + 우클릭시 주위 5칸 적에게 번개를 떨군다.");
		loreList.add("§7번개는 20의 피해를 준다.");
		loreList.add("");
		loreList.add("§e우클릭 쿨타임 : §b16초");
		loreList.add("§e쉬프트 + 우클릭 쿨타임 : §b27초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
		
		if(!p.isSneaking()) {
			if(!checkCooldown(p, CooldownType.Primary)) return;
			
			setCooldown(p, CooldownType.Primary, 16);
			
			Block b = p.getTargetBlock(null, 50);
			if(b.getType() == Material.AIR) {
				TitleAPI.sendFullTitle(p, 10,40, 20, "", "바라보는 곳에 블럭이 없거나 너무 멉니다.");
			} else {
				Location l = b.getLocation();
				MyUtility.sendLightning(p, l);
				for (Player t : getEnemyList(p)) {
					MyUtility.sendLightning(t, l);
					if (t.getLocation().distance(l) < 2.5f) {
						t.damage(20);
					}
					//Bukkit.broadcastMessage(t.getLocation()+" / "+l+" / "+t.getLocation().distance(l));
				}
				TitleAPI.sendFullTitle(p, 10, 60, 20, "", "번개가 내리칩니다.");
			}		
		}else {
			if(!checkCooldown(p, CooldownType.Secondary)) return;
			
			setCooldown(p, CooldownType.Secondary, 27);
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1.0f, 0.5f);
			
			for (Player t : getEnemyList(p)) {
					if(p.getLocation().distance(t.getLocation()) <= 5) {
						for(String tmpName : minigame.ingamePlayer) {
							Player tmp = Bukkit.getPlayer(tmpName);
							if(existPlayer(tmp)) MyUtility.sendLightning(tmp, t.getLocation());
						}	
						t.damage(20);
					}					
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
