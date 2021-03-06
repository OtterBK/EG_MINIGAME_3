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

public class RandomSword extends SpecialWeapon{

	public RandomSword(Minigame minigame) {
		super(minigame, Material.IRON_SWORD, 1, (short)0, "천운 검", 10, 20); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e행운도에 따라 달라지는 검");
		loreList.add("§e우클릭시 검의 최대 공격력을 설정한다.(최대 20까지)");
		loreList.add("§e단, 이 검의 최소 공격력은 10으로 고정이다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b60초");
		
		setLore(loreList);
	}

	@Override
	public void onInit() {
		setDamage(this.max_damage, 10);
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 60);
		
		int maxDamage = MyUtility.getRandom(10, 20);
		
		setDamage(this.min_damage, maxDamage);
		
		p.sendMessage("§7--------- 검 변형 완료 ------------");
		p.sendMessage("§6검의 최대 데미지가 §a"+maxDamage+" §6로 설정됨");
		p.sendMessage("§7--------------------------------");
		
		p.getInventory().remove(this.getType());
		p.getInventory().addItem(this);
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 2.0f);
		
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
