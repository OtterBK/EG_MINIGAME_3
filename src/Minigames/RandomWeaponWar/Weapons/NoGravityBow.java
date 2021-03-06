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
		super(minigame, Material.BOW, 1, (short)0, "���� ����", 2, 20); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� ó���� �� Ȱ");
		loreList.add("��e�� Ȱ�� ��� ȭ���� �߷��� ������ ���� �ʴ´�.");
		loreList.add("��e���� ȭ���� ������ ������ ��");
		loreList.add("��e33% Ȯ���� 10�ʰ� ȭ����¸� ������.");
		loreList.add("��e���� �������� 1�̴�.");
		loreList.add("��e����Ʈ+��Ŭ������ ȭ���� ���� �� �ִ�.");
		loreList.add("");
		loreList.add("��e����Ʈ+��Ŭ�� ��Ÿ�� : 0.5��");
	
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
