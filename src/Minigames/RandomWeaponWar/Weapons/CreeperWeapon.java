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

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class CreeperWeapon extends SpecialWeapon{
	
	public CreeperWeapon(Minigame minigame) {
		super(minigame, Material.TNT, 1, (short)0, "����", 10, 15); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� ���� ȭ���� �� �����.");
		loreList.add("��e��Ŭ���� �����Ѵ�.");
		loreList.add("��e������ 7ĭ�� ������ ��� �÷��̾�(�ڽ�����)����");
		loreList.add("��e��ü ü���� 50%��ŭ ���ظ� �ش�.");
		loreList.add("��e��, �̷����� ��������� �ʴ´�.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b36��");
	
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

		setCooldown(p, CooldownType.Primary, 36);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 0.5f);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
		p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, p.getLocation(), 10, 5F, 5f, 5f, 0.5f);
		
		for(String tName : minigame.ingamePlayer) {
			Player t = Bukkit.getPlayer(tName);
			if(!existPlayer(t)) return;
			double dis = t.getLocation().distance(p.getLocation());
			if(dis <= 7) {
				double dmg = t.getMaxHealth()/2;
				if(t.getHealth() - dmg < 1) {
					dmg = t.getHealth() - 1;
				}
				t.damage(dmg);
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
