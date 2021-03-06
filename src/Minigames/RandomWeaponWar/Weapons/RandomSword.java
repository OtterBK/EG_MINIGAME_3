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
		super(minigame, Material.IRON_SWORD, 1, (short)0, "õ�� ��", 10, 20); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� ���� �޶����� ��");
		loreList.add("��e��Ŭ���� ���� �ִ� ���ݷ��� �����Ѵ�.(�ִ� 20����)");
		loreList.add("��e��, �� ���� �ּ� ���ݷ��� 10���� �����̴�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b60��");
		
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
		
		p.sendMessage("��7--------- �� ���� �Ϸ� ------------");
		p.sendMessage("��6���� �ִ� �������� ��a"+maxDamage+" ��6�� ������");
		p.sendMessage("��7--------------------------------");
		
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
