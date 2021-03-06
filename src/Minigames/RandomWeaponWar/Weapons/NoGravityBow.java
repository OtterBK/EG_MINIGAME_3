package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;

public class NoGravityBow extends SpecialWeapon{
	
	
	ItemStack arrow = new ItemStack(Material.ARROW, 1);
	
	public NoGravityBow(Minigame minigame) {
		super(minigame, Material.BOW, 1, (short)0, "매직 보우", 2, 20); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e마법 처리가 된 활");
		loreList.add("§e이 활로 쏘는 화살은 중력의 영향을 받지 않는다.");
		loreList.add("§e또한 화살을 적에게 맞췄을 시");
		loreList.add("§e33% 확률로 10초간 화상상태를 입힌다.");
		loreList.add("§e근접 데미지는 1이다.");
		loreList.add("§e쉬프트+좌클릭으로 화살을 얻을 수 있다.");
		loreList.add("");
		loreList.add("§e쉬프트+좌클릭 쿨타임 : 0.5초");
	
		setLore(loreList);
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
		if(p.isSneaking()) {
			if(!checkCooldown(p, CooldownType.Primary)) return;
			
			setCooldown(p, CooldownType.Primary, 0.5);
			
			p.getInventory().addItem(arrow);
			
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.0f, 2.0f);
		}
		
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
		if(e.getDamager() instanceof Arrow) {
			e.setDamage(e.getDamage()*2);
			if(MyUtility.getRandom(0, 2) == 0) {
				victim.setFireTicks(200);
			}
		}else {
			e.setDamage(1);
		}
		 
	}
	
	@Override
	public int getCalcDamage() {
		return 1;
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPlayerShotBow(EntityShootBowEvent e) {
		Arrow a = (Arrow) e.getProjectile();
		a.setGravity(false);
	}
	
}
