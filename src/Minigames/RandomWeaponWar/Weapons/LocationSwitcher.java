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
		super(minigame, Material.IRON_SPADE, 1, (short)0, "위치 변환기", 9, 13); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e특정 대상과의 위치를 바꿀수 있는 기계다.");
		loreList.add("§e우클릭시 바라보는 방향으로 눈덩이를 던진다.");
		loreList.add("§e이 눈덩이에 맞은 적은 자신과 위치가");
		loreList.add("§e서로 바뀐다.");
		loreList.add("§e눈덩이가 적에게 맞을때까지 무기를 들고있어야 적용된다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b5초");
	
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
