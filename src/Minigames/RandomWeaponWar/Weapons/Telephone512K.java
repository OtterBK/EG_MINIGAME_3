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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;

public class Telephone512K extends SpecialWeapon{
	
	public Telephone512K(Minigame minigame) {
		super(minigame, Material.CLAY_BRICK, 1, (short)0, "512K", 10, 16); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e군용 전화기인 512K이다.");
		loreList.add("§e우클릭시 자신의 현재 위치로부터");
		loreList.add("§e바라보는 방향에 폭격을 요청한다.");
		loreList.add("§e폭격은 4초후 떨어지며 5번의 폭격을 가한다.");
		loreList.add("§e각 폭격은 폭심지와의 거리에 따라");
		loreList.add("§e50의 피해를 준다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b23초");
	
		setLore(loreList);
	}
	
	@Override
	public void onInit() {
		cooldown.clear();
	}
	
	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 23);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1.0f, 0.1f);
		
		List<Location> targetLoc = new ArrayList<Location>(5);
		
		Vector v = p.getLocation().getDirection().setY(0).normalize();
		v.multiply(5); //5칸앞
		
		Location l = p.getLocation().clone();
		targetLoc.add(l.clone()); //1번째 타격
		targetLoc.add(l.clone().add(v)); //2번째 타격
		targetLoc.add(l.clone().add(v.clone().multiply(2))); //3번째 타격
		targetLoc.add(l.clone().add(v.clone().multiply(3))); //4번째 타격
		targetLoc.add(l.clone().add(v.clone().multiply(4))); //5번째 타격
		
		List<Player> enemyList = getEnemyList(p);
		
		EGScheduler sch = new EGScheduler(minigame);
		sch.schTime = 12;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				if(sch.schTime-- > 0) {
					targetLoc.get(0).getWorld().playSound(targetLoc.get(0), Sound.ENTITY_MINECART_RIDING, 5.0f, 0.75f);
				}else {
					sch.cancelTask(true);
				}
			}
		}, 20l, 10l);
		
		sch.schTime2 = 0;
		Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
			public void run() {
				for(sch.schTime2 = 0; sch.schTime2 < targetLoc.size(); sch.schTime2++) {
					final int index = sch.schTime2;
					Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
						public void run() {
							//p.sendMessage(""+index);
							VirtualProjectile vp = new VirtualProjectile(minigame.server, p, enemyList);
							vp.c_speed = 2.0f;
							vp.c_distance = 50;
							vp.c_time = 100;
							vp.vector = new Vector(0, -1, 0);
							vp.c_startLoc = targetLoc.get(index).add(0,40,0);
							
							vp.setOnDuring(new Runnable() {
								public void run() {
									vp.projectileLoc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
									vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_FIREWORK_LAUNCH, 1.0F, 1.0F);
								}
							});
							
							vp.setOnNonHit(new Runnable() {
								public void run() {
									vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vp.projectileLoc, 3, 0.2F, 0.2f, 0.2f, 0.2f);
									vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 3f, 2.0f);
									p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
									for(Player t : enemyList) {
										int dis = (int)t.getLocation().distance(vp.projectileLoc);
										if(dis <= 5) {
											t.damage(50 - (dis*5));
										}
									}
								}
							});	
							
							vp.setOnHit(vp.onNonHit);
							
							vp.launchProjectile();
						}
					}, (index*10));
				}
			}
		}, 60l);
	
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

