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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.EarthEffect;

public class MoveToPlayerWeapon extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public MoveToPlayerWeapon(Minigame minigame) {
		super(minigame, Material.STONE_SPADE, 1, (short)0, "���� �ӽ�", 12, 15); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���İ��� ��ġ ��ǥ�� �̵���Ű�� �ӽ�");
		loreList.add("��e��Ŭ���� �ٶ󺸴� �÷��̾��");
		loreList.add("��7���İ��� �̵��Ѵ�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b8��");
	
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
		
		List<Player> hit = minigame.getCorsurPlayer(p, getEnemyList(p.getName()), 20, false);
		if(hit != null) {
			setCooldown(p, CooldownType.Primary, 8);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
			Player t = hit.get(0);
			Location l = t.getLocation().add(t.getLocation().getDirection().multiply(0.5f));
			l.setYaw(l.getYaw()*-1);
			l.setY(t.getLocation().getY());
			p.teleport(l, TeleportCause.PLUGIN);
		} else {
			ActionBarAPI.sendActionBar(p, "��2��l����� �������� �ʽ��ϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
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
