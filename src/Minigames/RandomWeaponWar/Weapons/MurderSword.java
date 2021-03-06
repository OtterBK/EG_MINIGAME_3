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
import Utility.MyUtility;

public class MurderSword extends SpecialWeapon{

	public MurderSword(Minigame minigame) {
		super(minigame, Material.GOLD_SWORD, 1, (short)0, "살인자의 칼", 17, 18); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e살인자가 사용하던 칼이다.");
		loreList.add("§e적을 처치할 때 마다 최대 공격력이 2 올라간다.");
		loreList.add("§e최대 25까지 올라간다.");
		
		setLore(loreList);
	}

	@Override
	public void onInit() {
		setDamage(this.max_damage, 18);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		if(victim.getHealth() - e.getDamage() <= 0) {

			int maxDamage = this.max_damage+5;
			
			if(maxDamage > 25) {
				maxDamage = 25;
			}
			
			setDamage(this.min_damage, maxDamage);
			
			damager.sendMessage("§7--------- 피 흡수 완료 ------------");
			damager.sendMessage("§6검의 최대 데미지가 §a"+maxDamage+" §6로 설정됨");
			damager.sendMessage("§7--------------------------------");
			
			damager.getInventory().remove(this.getType());
			damager.getInventory().addItem(this);
			
			damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.0f, 2.0f);
		}
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
