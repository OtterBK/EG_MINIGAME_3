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
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class OdinRod extends SpecialWeapon{
	
	public OdinRod(Minigame minigame) {
		super(minigame, Material.REDSTONE_TORCH_ON, 1, (short)0, "������ ������", 13, 15); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e������ �� ������ �����̴�.");
		loreList.add("��e��Ŭ���� �ֺ� ������ �ڽ��� ���� ü���� ���ݸ�ŭ");
		loreList.add("��e���ظ� �ְ� ���� ����.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b24��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 24);
		
		List<Player> tmpList = getEnemyList(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_END_GATEWAY_SPAWN, 1.0f, 0.75f);
		TitleAPI.sendFullTitle(p, 10,60, 20, "", "������ �ߵ��մϴ�");
		
		for(Player t : tmpList) {
			if(t.getLocation().distance(p.getLocation()) <= 6) {
				t.damage((int)p.getHealth()/2);
				t.setVelocity(new Vector(0,1.0f,0));
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 1.2f);
				p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p.getLocation(), 30, 0.15F, 0.15f, 0.15f, 0.07f);
				TitleAPI.sendFullTitle(t, 10,60, 20, "", "������ ������ �ߵ��ƽ��ϴ�.");
			}
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
