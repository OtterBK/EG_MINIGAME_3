package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class MagnetWeapon extends SpecialWeapon{
	
	public MagnetWeapon(Minigame minigame) {
		super(minigame, Material.FLINT_AND_STEEL, 1, (short)0, "�ڼ�", 9, 16); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�� ������ �ڼ��̴�.");
		loreList.add("��e��Ŭ���� 15ĭ�� �ٶ󺸴� ����");
		loreList.add("��e�ణ ����´�");
		loreList.add("��e�� �ڼ����� Ÿ���� ����");
		loreList.add("��e�ָ� �з�����.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b7��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		
		if(!checkCooldown(p, CooldownType.Primary)) return;

		List<Player> hit = minigame.getCorsurPlayer(p, getEnemyList(p.getName()), 15, false);
		if(hit != null) {
			
			setCooldown(p, CooldownType.Primary, 11);
			
			Player target = hit.get(0);
			
			EGScheduler sch = new EGScheduler(minigame);
			sch.schTime = 7;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
				public void run() {
					if(sch.schTime > 0) {
						sch.schTime--;
						Vector v = p.getLocation().toVector().subtract(target.getLocation().toVector());
						v.normalize();
						v.multiply(1f);
						target.setVelocity(v);
						target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1.0f, 2.0f);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1.0f, 2.0f);
					}else {
						sch.cancelTask(true);
					}						
				}
			}, 0l, 10l);
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
