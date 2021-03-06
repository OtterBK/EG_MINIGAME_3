package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class RandomMedic extends SpecialWeapon{
	
	public RandomMedic(Minigame minigame) {
		super(minigame, Material.SADDLE, 1, (short)0, "���� ����", 11, 11); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�������� ȸ�� �������� ����ִ� ���ڴ�.");
		loreList.add("��e��Ŭ���� 10 ~ 50��ŭ�� ü���� ��� ȸ���Ѵ�.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b23��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 23);
		
		int amt = MyUtility.getRandom(10, 50);
		double newHp = p.getHealth() + amt;
		if(newHp > p.getMaxHealth()) newHp = p.getMaxHealth();
		p.setHealth(newHp);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
		
		TitleAPI.sendFullTitle(p, 10, 60, 10, "", "��b��l"+amt+"��ŭ�� ü���� ȸ���߽��ϴ�.");
		
	
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

