package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;

public class ForceTaker extends SpecialWeapon{
	
	public ForceTaker(Minigame minigame) {
		super(minigame, Material.DOUBLE_PLANT, 1, (short)4, "��Ż ���", 1, 1); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e������ �ڱ⼺�� ������ �ִ� ����̴�.");
		loreList.add("��e��Ŭ���� 7ĭ�� �ٶ󺸰� �ִ� ����");
		loreList.add("��e����ִ� �������� ��Ż�Ѵ�.");
		loreList.add("��7�ɷ� ����Ŀ� �� �������� �������.");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
	
		List<Player> hit = minigame.getCorsurPlayer(p, getEnemyList(p), 7, false);
		if(hit != null) {
			
			p.getInventory().remove(this.getType());
			Player target = hit.get(0);
			
			ItemStack item = target.getInventory().getItemInMainHand();
			p.getInventory().addItem(item);
			target.getInventory().remove(item.getType());
			
			TitleAPI.sendFullTitle(target, 0, 60, 0, "", "��e��l���⸦ ��Ż ���߽��ϴ�.");
			TitleAPI.sendFullTitle(p, 0, 60, 0, "", "��e��l���⸦ ��Ż�߽��ϴ�.");
			
			target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.1f, 0.1f);
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.1f, 0.1f);
		}else {
			ActionBarAPI.sendActionBar(p, "��2��l����� �������� �ʽ��ϴ�.", 40);
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
