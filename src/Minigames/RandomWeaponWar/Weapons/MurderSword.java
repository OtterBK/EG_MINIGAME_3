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
		super(minigame, Material.GOLD_SWORD, 1, (short)0, "�������� Į", 17, 18); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�����ڰ� ����ϴ� Į�̴�.");
		loreList.add("��e���� óġ�� �� ���� �ִ� ���ݷ��� 2 �ö󰣴�.");
		loreList.add("��e�ִ� 25���� �ö󰣴�.");
		
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
			
			damager.sendMessage("��7--------- �� ��� �Ϸ� ------------");
			damager.sendMessage("��6���� �ִ� �������� ��a"+maxDamage+" ��6�� ������");
			damager.sendMessage("��7--------------------------------");
			
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
