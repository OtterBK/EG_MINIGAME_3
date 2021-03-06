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

public class Stimulant extends SpecialWeapon{
	
	public Stimulant(Minigame minigame) {
		super(minigame, Material.SUGAR, 1, (short)0, "������", 14, 15); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���ظ� �޾��� �� 5�ʰ� �������� ������ ��´�.");
		loreList.add("��eȿ���� ���� ������ �����̴�.");
		loreList.add("��c�޼տ� �� �ɷ� ������");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
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
		Player p = (Player) e.getEntity();
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
		
		int lv = MyUtility.getRandom(0, 4);	
		PotionEffectType type;
		
		int rn = MyUtility.getRandom(1, 7);
		switch(rn) {
		
		case 1: type = PotionEffectType.SPEED; break;
		case 2: type = PotionEffectType.REGENERATION; break;
		case 3: type = PotionEffectType.JUMP; break;
		case 4: type = PotionEffectType.DAMAGE_RESISTANCE; break;
		case 5: type = PotionEffectType.FIRE_RESISTANCE; break;
		case 6: type = PotionEffectType.WATER_BREATHING; break;
		case 7: type = PotionEffectType.HEALTH_BOOST; break;
		
		default: type = PotionEffectType.SPEED;
		}
		
		PotionEffect pt = new PotionEffect(type, 100, lv);
		p.addPotionEffect(pt);
		
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

