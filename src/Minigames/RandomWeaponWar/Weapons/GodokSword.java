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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class GodokSword extends SpecialWeapon{

	private PotionEffect slowPt = new PotionEffect(PotionEffectType.SLOW, 200, 0);
	private PotionEffect SpeedPt = new PotionEffect(PotionEffectType.SPEED, 200, 2);
	private PotionEffect ResiPt = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0);
	private PotionEffect RegenPt = new PotionEffect(PotionEffectType.REGENERATION, 160, 0);
	
	public GodokSword(Minigame minigame) {
		super(minigame, Material.STONE_SWORD, 1, (short)0, "���� ��", 14, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� �޾� ��ġ�Ǿ���  ���̴�.");
		loreList.add("��e���ظ� �޾��� �� ü���� 33% �̻��� ��");
		loreList.add("��e10�ʰ� ���� 1������ �޴´�.");
		loreList.add("��e�ݴ�� ü���� 33% �̸��� ��");
		loreList.add("��e10�ʰ� �̼�3, ����1 ������ �޴´�.");
		loreList.add("��e���ظ� ���� �� ���� �� ȿ���� ���ŵȴ�.");
		loreList.add("��eü���� 50% �̸��� ���¿��� ���� ������ ��");
		loreList.add("��e8�ʰ� ��������� �޴´�.");
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
		double baseHealth = p.getMaxHealth() / 3;
		if(p.getHealth() - e.getDamage() <= baseHealth) {//���� ������ 33% �����϶�
			p.addPotionEffect(SpeedPt);
			p.addPotionEffect(ResiPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1.0f,2.0f);
		}else {
			p.addPotionEffect(slowPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1.0f,0.1f);
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		if(damager.getHealth() < damager.getMaxHealth()/2) {
			damager.addPotionEffect(RegenPt);
		}
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
