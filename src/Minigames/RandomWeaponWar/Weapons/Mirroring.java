package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class Mirroring extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public Mirroring(Minigame minigame) {
		super(minigame, Material.NETHER_BRICK_ITEM, 1, (short)0, "�̷���", 10, 13); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� �ڽ��� ��ȭ�ϴ� ����");
		loreList.add("��e�� ���⸦ ����ִ� �ڽ��� ���� ����");
		loreList.add("��eü���� 1�̵ȴ�.");
		loreList.add("��c�޼տ� �� �ɷ� ������");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
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
		if(victim.getHealth() - e.getDamage() <= 0) {		
			damager.damage(0.01);
			damager.setHealth(1);
			TitleAPI.sendFullTitle(damager, 0, 100, 0, "��4��l����", "��b��l�̷����� ���ָ� �޾ҽ��ϴ�.");
		}
		
	}
	
}
