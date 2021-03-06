package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class BurnOut extends SpecialWeapon{
	
	public BurnOut(Minigame minigame) {
		super(minigame, Material.LEATHER, 1, (short)0, "�� �ƿ�", 12, 12); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��eȭ���� ���� �������� ���� �����̴�.");
		loreList.add("��e����ִ� ������ ȭ�� �������� ���� �ʴ´�.");
		loreList.add("��e��Ŭ���� 6ĭ�� ���� 8�ʰ� ���¿��.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b15��");
		loreList.add("��c�޼տ� �� �ɷ� ������");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if (!checkCooldown(p, CooldownType.Primary))
			return;

		setCooldown(p, CooldownType.Primary, 15);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_BURN, 1.0f, 0.5f);
		p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 15, 5F, 5f, 5f, 0.5f);
		
		for(Player t : getEnemyList(p)) {
			double dis = t.getLocation().distance(p.getLocation());
			if(dis <= 6) {
				t.setFireTicks(160);
			}
		}
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FIRE ||
			e.getCause() == DamageCause.FIRE_TICK) {
			e.setCancelled(true);
		}	
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
