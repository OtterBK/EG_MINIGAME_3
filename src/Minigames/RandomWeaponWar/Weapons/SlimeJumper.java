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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class SlimeJumper extends SpecialWeapon{
	
	private PotionEffect jumpPt = new PotionEffect(PotionEffectType.JUMP, 20, 1);
	
	public SlimeJumper(Minigame minigame) {
		super(minigame, Material.SLIME_BALL, 1, (short)0, "말랑말랑한 구체", 10, 13); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e말랑말랑한 구체이다.");
		loreList.add("§e들고있는 동안은 점프강화 2버프를 받는다.");
		loreList.add("§c왼손에 들어도 능력 유지됨");
	
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
							if(t.hasPotionEffect(PotionEffectType.JUMP)) t.removePotionEffect(PotionEffectType.JUMP);
							t.addPotionEffect(jumpPt);
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

