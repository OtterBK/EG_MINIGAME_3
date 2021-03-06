package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class Booster extends SpecialWeapon{

	private boolean noFallDamage = false;
	
	public Booster(Minigame minigame) {
		super(minigame, Material.FIREWORK_CHARGE, 1, (short)0, "�ν��� ����", 13, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e������ �������� ���� ����");
		loreList.add("��e��Ŭ���� �ٶ󺸴� �������� �ָ�");
		loreList.add("��e���ư� �� �ִ�.");
		loreList.add("��e������ �� ���⸦ ��� ���� ���");
		loreList.add("��e�ν��ͷ����� ���絥������ ���� �ʴ´�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b15��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 15);
			
		noFallDamage = true;
		p.setVelocity(p.getEyeLocation().getDirection().multiply(4.5f));
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 0.5f);
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 5, 0.1F, 0.1f, 0.1f, 0.1f);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
			public void run() {
				noFallDamage = false;
			}
		},200l);
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL) {
			if(noFallDamage) {
				noFallDamage = false;
				e.setCancelled(true);	
			}
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
