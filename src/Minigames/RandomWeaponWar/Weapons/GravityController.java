package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;

public class GravityController extends SpecialWeapon{
	
	public GravityController(Minigame minigame) {
		super(minigame, Material.GOLD_SPADE, 1, (short)0, "���߷� ������", 11, 17); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�Ͻ������� ����� �޴� �߷��� �ݴ�� ����� �������.");
		loreList.add("��e��Ŭ���� ���� �ӵ��� �������� ������.");
		loreList.add("��e�� �������� ���� ���� ���� ���� �������.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b18��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 18);
	
		List<Player> enemyList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
		vp.c_speed = 2.5f;
		vp.c_distance = 50;
		vp.c_time = 100;
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.REDSTONE, vp.projectileLoc, 2, 0F, 0f, 0f, 0f);
			}
		});

		
		vp.setOnHit(new Runnable() {
			public void run() {

				Player hit = vp.hitPlayer.get(0);
				
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.2f, 2.0f);
				
				hit.setVelocity(new Vector(0, 4.5f, 0));		
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
