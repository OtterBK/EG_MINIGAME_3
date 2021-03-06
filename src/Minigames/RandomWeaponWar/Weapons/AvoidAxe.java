package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import de.slikey.effectlib.effect.ShieldEffect;

public class AvoidAxe extends SpecialWeapon{
	
	public AvoidAxe(Minigame minigame) {
		super(minigame, Material.STONE_AXE, 1, (short)0, "ȸ�� ����", 12, 12); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e������ ���ظ� ���� ��");
		loreList.add("��e25% Ȯ���� ���ظ� ȸ���ϰ�");
		loreList.add("��e���� �ڷ� �̵��Ѵ�.");
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
		if (MyUtility.getRandom(1, 4) <= 1) {
			Location tl = damager.getLocation().subtract(damager.getEyeLocation().getDirection());
			tl.setY(damager.getLocation().getY());
			victim.teleport(tl, TeleportCause.PLUGIN);
			victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.2f, 0.5f);
			ActionBarAPI.sendActionBar(victim, "��2��lȸ�� ����", 40);
			e.setCancelled(true);
		}
	}
	
}
