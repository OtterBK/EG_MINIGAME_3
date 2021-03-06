package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.EarthEffect;

public class DeathNote extends SpecialWeapon{
	
	public DeathNote(Minigame minigame) {
		super(minigame, Material.BOOK_AND_QUILL, 1, (short)0, "������Ʈ", 1, 1); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e����� �������� ������ ������Ʈ");
		loreList.add("��e��Ŭ���� ��� �÷��̾�(�ڽ�����)����");
		loreList.add("��7��ü ü���� 65%��ŭ ���ظ� ������.");
		loreList.add("��7��, �̷����� ��������� �ʴ´�.");
		loreList.add("");
		loreList.add("��7���� �� �������� �������.");
		
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {	
		p.getInventory().remove(this.getType());
		TitleAPI.sendFullTitle(p, 0, 60, 0, "��4��l������Ʈ", "��e��l����� �̸��� �������ϴ�.");
		for(Player t : getEnemyList(p)) {
			TitleAPI.sendFullTitle(t, 0, 60, 0, "��4��l������Ʈ", "��e��l�����°� �Ǿ����ϴ�.");
			double amt = (t.getMaxHealth()/100)*65;
			if(t.getHealth() - amt < 10) {
				amt = t.getHealth() - 10;
			}
			t.damage(amt);
			t.getWorld().playSound(t.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 0.1f);
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
