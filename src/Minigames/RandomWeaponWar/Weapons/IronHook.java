package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
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
import Minigames.HeroesWar.Team;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;

public class IronHook extends SpecialWeapon{
	
	public IronHook(Minigame minigame) {
		super(minigame, Material.TRIPWIRE_HOOK, 1, (short)15, "철제 갈고리", 12, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e철로 만들어진 갈고리다.");
		loreList.add("§e우클릭시 갈고리를 날린다.");
		loreList.add("§e갈고리에 맞은 적은 자신의 앞으로 끌려온다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b9초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 9);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.2f, 0.1f);
		
		Location l = p.getLocation();
		
		VirtualProjectile vp = new VirtualProjectile(minigame.server, p, getEnemyList(p.getName()));
		vp.c_speed = 2.2f;
		vp.c_distance = 25f;
		vp.c_time = 300;
		vp.c_pHorizenOffset = 1.2f;
		vp.c_pVerticalOffset = 1.2f;		
		
		vp.setOnDuring(new Runnable() {
			public void run() {
				vp.projectileLoc.getWorld().spawnParticle(Particle.SNOW_SHOVEL, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_PLACE, 1.2f, 2.0f);
			}
		});
		
		vp.setOnNonHit(new Runnable() {
			public void run() {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.2f, 2.0f);		
			}
		});
		
		vp.setOnHit(new Runnable() {
			public void run() {
				Player t = vp.hitPlayer.get(0);
				
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1.2f, 2.0f);
				
				EGScheduler sch = new EGScheduler(minigame);
				sch.schTime = (int) p.getLocation().distance(t.getLocation())/2;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
					public void run() {
						if(sch.schTime-- > 0) {
							p.teleport(l, TeleportCause.PLUGIN);
							Location tl = null;
							if(sch.schTime != 0) {
								tl = l.clone().add(l.getDirection().multiply(sch.schTime*2));
							} else {
								tl = l.clone().add(l.getDirection());
							}
							tl.setYaw(tl.getYaw()*-1);
							t.teleport(tl.add(0,0.2,0), TeleportCause.PLUGIN);
							LineEffect ef = new LineEffect(effectManager);
							ef.particle = Particle.SNOW_SHOVEL;
							ef.iterations = 1;
							ef.setLocation(l.clone().add(0,1.5,0));
							ef.setTargetLocation(tl.clone().add(0,1.5,0));
							ef.start();
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.2f, 2.0f);
						} else {
							sch.cancelTask(true);
						}
					}
				}, 6l, 1l);				    
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
