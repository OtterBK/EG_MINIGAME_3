package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class PoisonKnife extends SpecialWeapon{
	
	
	private PotionEffect poisonEffect = new PotionEffect(PotionEffectType.POISON, 140, 1);
	
	public PoisonKnife(Minigame minigame) {
		super(minigame, Material.PRISMARINE_SHARD, 1, (short)0, "���� ������", 10, 12); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� �ٸ� �������̴�.");
		loreList.add("��e��Ŭ���� �ٶ󺸴� ��������");
		loreList.add("��e�������� ������.");
		loreList.add("��e�����˿� ���� ���� 7�ʰ� �ߵ� ���¿� ������.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b8��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 8);
				
		Location pel = p.getEyeLocation();
		
		Arrow bulletArrow = p.launchProjectile(Arrow.class);
        bulletArrow.setVelocity(pel.getDirection().multiply(3));
        bulletArrow.setShooter(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 1.0f, 2.0f);
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
		if(e.getDamager() instanceof Arrow) {
			victim.addPotionEffect(poisonEffect);	
		}		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
