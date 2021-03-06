package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.Vector3D;
import de.slikey.effectlib.effect.LineEffect;

public class DragonSword extends SpecialWeapon{
	
	public DragonSword(Minigame minigame) {
		super(minigame, Material.WOOD_SWORD, 1, (short)0, "용광검", 16, 19); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e단군고기에 나오는 해모수가 찬 전설의 칼");
		loreList.add("§e우클릭시 바라보는 방향으로 빠르게 날라가며");
		loreList.add("§e지나온 거리의 적에게 20 피해를 준다.");
		loreList.add("§e하늘을 향해 사용할 경우에는 낙사데미지를 받는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b11초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 11);
			
		List<Player> tmpList = getEnemyList(p.getName());
		
		List<Player> hitList = minigame.getCorsurPlayer(p, tmpList, 10, true);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_HURT, 1.5F, 0.1f);
		p.setVelocity(p.getEyeLocation().getDirection().multiply(2.65f));
		for(Player t : hitList) {
			t.damage(20);
		}
		final Player observer = p;
		
        Location observerPos = observer.getEyeLocation();
        final Vector3D observerDir = new Vector3D(observerPos.getDirection());
        final Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(12));
        
        Location loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y-1, observerEnd.z).add(0,0.5f,0); 
		LineEffect le = new LineEffect(effectManager);
		le.particle = Particle.SPELL_INSTANT;
		le.particles = 15;
		le.period = 1;
		le.iterations = 15;
		le.particleOffsetX = 0.15f;
		le.particleOffsetY = 0f;
		le.particleOffsetZ = 0.15f;
		le.setLocation(p.getEyeLocation().add(0,-1,0));
		le.setTargetLocation(loc);
		le.start();
		
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
		// TODO Auto-generated method stub
		
	}
	
}
