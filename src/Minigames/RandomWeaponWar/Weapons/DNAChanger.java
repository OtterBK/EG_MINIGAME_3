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

public class DNAChanger extends SpecialWeapon{
	
	private PotionEffect regenPt = new PotionEffect(PotionEffectType.REGENERATION, 200, 2);
	private PotionEffect ironPt = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0);
	
	public DNAChanger(Minigame minigame) {
		super(minigame, Material.STRING, 1, (short)0, "���� ���۱�", 13, 15); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e������ ������ ������ �� �ִ� ����̴�.");
		loreList.add("��e��Ŭ���� ��ü ������ Ȱ��ȭ�Ͽ�");
		loreList.add("��e10�ʰ� ȸ���ӵ��� ��������� ��½�Ų��.");
		loreList.add("��e����Ʈ ��Ŭ���� ������ ���Ľ���");
		loreList.add("��e10�ʰ� �޴� �������� �氨��Ų��.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b32��");
		loreList.add("��e����Ʈ ��Ŭ�� ��Ÿ�� : ��b22��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(p.isSneaking()) {
			if(!checkCooldown(p, CooldownType.Secondary)) return;
			
			setCooldown(p, CooldownType.Secondary, 22);
			
			p.addPotionEffect(ironPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1.0f, 0.5f);
			TitleAPI.sendFullTitle(p, 0, 30, 0, "��c��l���� ����","");
		}else {
			if(!checkCooldown(p, CooldownType.Primary)) return;
			
			setCooldown(p, CooldownType.Primary, 32);
			
			p.addPotionEffect(regenPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
			TitleAPI.sendFullTitle(p, 0, 30, 0, "��c��l���� Ȱ��ȭ","");
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

