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

public class RandomBuffer extends SpecialWeapon{
	
	public RandomBuffer(Minigame minigame) {
		super(minigame, Material.CLAY_BALL, 1, (short)0, "�̽ʻ��ü �ֻ���", 11, 16); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�Ⱬ�ϰ� ���� �̽ʻ��ü �ֻ�����.");
		loreList.add("��e��Ŭ���� �������� ���� �Ǵ� ������� ��´�.");
		loreList.add("��e����/������� �����ð�(�ִ�10��), ȿ�� ������ �����̴�.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b3��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 3);
		
		TitleAPI.sendFullTitle(p, 10, 60, 10, "", "��c��l�ֻ����� �������ϴ�.");
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_SHOOT, 1.0f, 0.5f);
		
		int time = 20*MyUtility.getRandom(3, 10);
		int lv = MyUtility.getRandom(0, 4);	
		PotionEffectType type;
		
		int rn = MyUtility.getRandom(1, 15);
		switch(rn) {
		
		case 1: type = PotionEffectType.SPEED; break;
		case 2: type = PotionEffectType.SLOW; break;
		case 3: type = PotionEffectType.HEAL; break;
		case 4: type = PotionEffectType.REGENERATION; break;
		case 5: type = PotionEffectType.BLINDNESS; break;
		case 6: type = PotionEffectType.JUMP; break;
		case 7: type = PotionEffectType.POISON; break;
		case 8: type = PotionEffectType.CONFUSION; break;
		case 9: type = PotionEffectType.DAMAGE_RESISTANCE; break;
		case 10: type = PotionEffectType.FIRE_RESISTANCE; break;
		case 11: type = PotionEffectType.GLOWING; break;
		case 12: type = PotionEffectType.INVISIBILITY; break;
		case 13: type = PotionEffectType.WATER_BREATHING; break;
		case 14: type = PotionEffectType.HEALTH_BOOST; break;
		case 15: type = PotionEffectType.WITHER; break;
		
		default: type = PotionEffectType.SPEED;
		}
		
		PotionEffect pt = new PotionEffect(type, time, lv);
		p.addPotionEffect(pt);
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

