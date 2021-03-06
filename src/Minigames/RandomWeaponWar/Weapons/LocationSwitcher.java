package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class LocationSwitcher extends SpecialWeapon{
	
	
	private PotionEffect poisonEffect = new PotionEffect(PotionEffectType.POISON, 140, 1);
	
	public LocationSwitcher(Minigame minigame) {
		super(minigame, Material.IRON_SPADE, 1, (short)0, "��ġ ��ȯ��", 9, 13); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��eƯ�� ������ ��ġ�� �ٲܼ� �ִ� ����.");
		loreList.add("��e��Ŭ���� �ٶ󺸴� �������� �����̸� ������.");
		loreList.add("��e�� �����̿� ���� ���� �ڽŰ� ��ġ��");
		loreList.add("��e���� �ٲ��.");
		loreList.add("��e�����̰� ������ ���������� ���⸦ ����־�� ����ȴ�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b5��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 5);
				
		Location pel = p.getEyeLocation();
		
		Snowball snowball = p.launchProjectile(Snowball.class);
		snowball.setVelocity(pel.getDirection().multiply(2));
		snowball.setShooter(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0f, 0.5f);
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
		if(e.getDamager() instanceof Snowball) {	
			Location victimLoc = victim.getLocation().clone();
			victim.teleport(damager.getLocation(), TeleportCause.PLUGIN);
			damager.teleport(victimLoc);
			damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_SNOWMAN_AMBIENT, 1.0f, 0.5f);
			victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_SNOWMAN_AMBIENT, 1.0f, 0.5f);
		}		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
