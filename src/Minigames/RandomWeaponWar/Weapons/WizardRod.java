package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;

public class WizardRod extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public WizardRod(Minigame minigame) {
		super(minigame, Material.BLAZE_ROD, 1, (short)0, "�������� ������", 8, 9); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�������� �����簡 ���ٹ��� �����̴�.");
		loreList.add("��e���� ���������� ���� ���� ����ִ�.");
		loreList.add("��e��Ŭ���� ȭ��ź�� ������.");
		loreList.add("��7ȭ��ź�� �� �Ǵ� ���� ���� �� �����Ѵ�.");
		loreList.add("��7�������� 4ĭ���� ���� 8�� ���ظ� �ް�");
		loreList.add("��7������ �÷��̾�� 20�� �߰����ظ� ������");
		loreList.add("��75�ʰ� ȭ�����ظ� ������.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b5��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 5);
		
		List<Player> enemyList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 2.5f;
		vp.c_distance = 50;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.FLAME, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vp.projectileLoc, 1, 0.1F, 0.1f, 0.1f, 0.1f);
				vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
				for(Player t : enemyList) {
					int dis = (int)t.getLocation().distance(vp.projectileLoc);
					if(dis <= 3) {
						t.damage(5);
						t.setFireTicks(100);
					}
				}
			}
		});
		
		vp.setOnHit(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, vp.projectileLoc, 3, 0.1F, 0.1f, 0.1f, 0.1f);
				vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
				Player hit = vp.hitPlayer.get(0);
				hit.damage(20);
				for(Player t : enemyList) {
					int dis = (int)t.getLocation().distance(vp.projectileLoc);
					if(dis <= 4) {
						t.damage(8);
						t.setFireTicks(100);
					}
				}
			}
		});
		
		
		vp.launchProjectile();
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
