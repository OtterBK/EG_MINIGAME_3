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
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class AiasShield extends SpecialWeapon{
	
	private float shield = 100;
	
	public AiasShield(Minigame minigame) {
		super(minigame, Material.SHIELD, 1, (short)0, "���̾ƽ��� ����", 9, 15); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�׸����� ������ ���̾ƽ��� ����");
		loreList.add("��e���и� ��� �ϴ� �����ٿ� ������ ��������");
		loreList.add("��eǥ�õȴ�. �� �������� �� ������ ��������");
		loreList.add("��e��� ���ظ� ���а� ��� �޴´�.");
		loreList.add("��e�������� ���и� �� ���¿���");
		loreList.add("��e��Ŭ���� �ϰ� ���� �ʴٸ� �����ȴ�.");	
		loreList.add("��c�޼տ� �� �ɷ� ������");
	
		setLore(loreList);
		
		timer();
	}

	private void timer() {
		EGScheduler sch = new EGScheduler(null);
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				for(String tName : minigame.ingamePlayer) {
					Player t = Bukkit.getPlayer(tName);
					if(existPlayer(t)) {
						if(compareItemName(t)) {
							t.setExp(shield/100);
							if(!t.isBlocking()) {
								shield += 5;
								if(shield > 100) shield = 100;
							}
						}
					}
				}
			}
		}, 0l, 10l);
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
		if(p.isBlocking()) {
			if(shield > 0) {
				shield -= e.getDamage();
				if(shield <= 0) {
					shield = 0;
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 0.5f);
				}else {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.0f, 2.0f);
				}
				e.setDamage(0.01);
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

