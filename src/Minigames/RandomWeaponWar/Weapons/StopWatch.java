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

public class StopWatch extends SpecialWeapon{
	
	private PotionEffect slowPt = new PotionEffect(PotionEffectType.SLOW, 100, 249);
	//private PotionEffect jumpPt = new PotionEffect(PotionEffectType.JUMP, 100, 249);
	
	public StopWatch(Minigame minigame) {
		super(minigame, Material.WATCH, 1, (short)0, "���� ��ġ", 11, 11); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e��� �÷��̾��� �ð��� ���߰� �� �� �ִ� �ð��̴�.");
		loreList.add("��e��Ŭ���� ��� �÷��̾�� 5�ʰ�");
		loreList.add("��e����250 ������� �ְ� ������ �� �� �����Ѵ�.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b70��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 70);
		
		List<Player> enemyList = getEnemyList(p);
		TitleAPI.sendFullTitle(p, 0, 60, 0, "��6��l�ð� ����","��e��l�������� �������ϴ�.");
		
		for(Player t : enemyList) {
			
			TitleAPI.sendFullTitle(t, 0, 60, 0, "��6��l�ð� ����","��e��l�������� �������ϴ�.");
			t.getWorld().playSound(t.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1.0f, 2.5f);
			if(t.getName().equalsIgnoreCase(p.getName())) continue;
			t.addPotionEffect(slowPt);
			//t.addPotionEffect(jumpPt);
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

