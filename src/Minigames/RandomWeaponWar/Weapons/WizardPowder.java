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

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;

public class WizardPowder extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public WizardPowder(Minigame minigame) {
		super(minigame, Material.BLAZE_POWDER, 1, (short)0, "�������� �Ŀ�ġ", 8, 9); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�������� ������ ����ϴ� �Ŀ�ġ��.");
		loreList.add("��e���� ���������� ���� ���� ����ִ�.");
		loreList.add("��e��Ŭ���� ȭ�����̸� ������.");
		loreList.add("��7ȭ�����̴� ��ź�� �ұ����̸� �����");
		loreList.add("��7�ұ����� ���� ������ �ʴ� 8�� ���ؿ�");
		loreList.add("��76���� ȭ�����ظ� ������.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b10��");
	
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
		
		setCooldown(p, CooldownType.Primary, 10);
		
		List<Player> enemyList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 2.0f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 1f;
		vp.c_distance = 25;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				Location l = MyUtility.getGroundLocation(vp.projectileLoc).add(0,0.25,0);
				CircleEffect ef1 = new CircleEffect(effectManager);
				ef1.particle = Particle.FLAME;
				ef1.angularVelocityX = 0;
				ef1.angularVelocityY = 0;
				ef1.angularVelocityZ = 0;
				ef1.particleSize = 1;
				ef1.wholeCircle = true;
				ef1.particleOffsetX = 0.1f;
				ef1.particleOffsetZ = 0.1f;
				ef1.particleOffsetY = 0.1f;
				ef1.particleCount = 2;
				ef1.radius = 4;
				ef1.period = 26;
				ef1.iterations = 5;
				ef1.setLocation(l);
				ef1.start();
				
				EGScheduler sch = new EGScheduler(minigame);
				sch.schTime = 10;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
					
					public void run() {
						
						if(sch.schTime > 0) {
							sch.schTime -= 1;
							vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_FIRE_AMBIENT, 1.5f, 0.25f);
							for(Player t : enemyList) {
								int dis = (int)t.getLocation().distance(vp.projectileLoc);
								if(dis <= 3) {
									t.damage(4);
									t.setFireTicks(120);
								}
							}
						} else {
							sch.cancelTask(true);							
						}
					}
					
				}, 10l, 10l);
			}
		});

		vp.setOnHit(vp.onNonHit);
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
