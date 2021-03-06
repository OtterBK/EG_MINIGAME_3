package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import de.slikey.effectlib.effect.ShieldEffect;

public class BorrowHealth extends SpecialWeapon{
	
	public BorrowHealth(Minigame minigame) {
		super(minigame, Material.BREWING_STAND_ITEM, 1, (short)0, "�̷� ������", 15, 17); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�̷��� ������ ������ �� �ִ� ����");
		loreList.add("��e��Ŭ�� �� ü���� 50��ŭ ȸ���Ѵ�.");
		loreList.add("��e10����  ü���� 50��ŭ ���δ�.");
		loreList.add("��e��, �̷����� ��������� �ʴ´�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b51��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 51);
			
		int amt = 50;
		double newHp = p.getHealth() + amt;
		if(newHp > p.getMaxHealth()) newHp = p.getMaxHealth();
		p.setHealth(newHp);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
			public void run() {
				p.damage(0.01);
				int amt = 50;
				double newHp = p.getHealth() - amt;
				if(newHp < 1) newHp = 1;
				p.setHealth(newHp);
			}
		}, 200l);
		
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

	}
	
}
