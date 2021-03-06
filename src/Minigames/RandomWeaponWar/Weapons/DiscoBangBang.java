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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.EarthEffect;

public class DiscoBangBang extends SpecialWeapon{
	
	public DiscoBangBang(Minigame minigame) {
		super(minigame, Material.RECORD_10, 1, (short)0, "디스코 팡팡", 11, 11); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e음악을 틀 수 있는 기계");
		loreList.add("§e우클릭시 음악을 튼다.");
		loreList.add("§e음악이 틀어져있는 동안은");
		loreList.add("§7모든 플레이어를 하늘로 조금 띄운다.");
		loreList.add("");
		loreList.add("§7쿨타임: 77초");
		
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {	
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 77);
		
		TitleAPI.sendFullTitle(p, 0, 30, 0, "§a§l~~♩", "§e§l음악을 틀었습니다.");
		List<Player> enemyList = getEnemyList(p);
		
		EGScheduler sch = new EGScheduler(minigame);
		sch.schTime = 25;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					sch.schTime--;
					if(sch.schTime % 5 == 0) {
						for(Player t : enemyList) {
							TitleAPI.sendFullTitle(t, 0, 30, 0, "§a§l~~♩", "§e§l음악이 들려옵니다.");
							t.setVelocity(new Vector(0, 1f, 0));
						}
					}
					switch (MyUtility.getRandom(0, 6)) {
					case 0:
						minigame.sendSound(Sound.BLOCK_NOTE_PLING, 1.5f, 1.0f);
						break;
					case 1:
						minigame.sendSound(Sound.BLOCK_NOTE_PLING, 1.5f, 1.0f);
						break;
					case 2:
						minigame.sendSound(Sound.BLOCK_NOTE_SNARE, 1.5f, 1.0f);
						break;
					case 3:
						minigame.sendSound(Sound.BLOCK_NOTE_BASEDRUM, 1.5f, 1.0f);
						break;
					case 4:
						minigame.sendSound(Sound.BLOCK_NOTE_CHIME, 1.5f, 1.0f);
						break;
					case 5:
						minigame.sendSound(Sound.BLOCK_NOTE_XYLOPHONE, 1.5f, 1.0f);
						break;
					case 6:
						minigame.sendSound(Sound.BLOCK_NOTE_BELL, 1.5f, 1.0f);
						break;
					}
				}else {
					sch.cancelTask(true);
				}			
			}
		}, 0l, 4l);	
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
