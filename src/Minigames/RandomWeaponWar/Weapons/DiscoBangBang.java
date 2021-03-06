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
		super(minigame, Material.RECORD_10, 1, (short)0, "���� ����", 11, 11); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e������ Ʋ �� �ִ� ���");
		loreList.add("��e��Ŭ���� ������ ư��.");
		loreList.add("��e������ Ʋ�����ִ� ������");
		loreList.add("��7��� �÷��̾ �ϴ÷� ���� ����.");
		loreList.add("");
		loreList.add("��7��Ÿ��: 77��");
		
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
		
		TitleAPI.sendFullTitle(p, 0, 30, 0, "��a��l~~��", "��e��l������ Ʋ�����ϴ�.");
		List<Player> enemyList = getEnemyList(p);
		
		EGScheduler sch = new EGScheduler(minigame);
		sch.schTime = 25;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					sch.schTime--;
					if(sch.schTime % 5 == 0) {
						for(Player t : enemyList) {
							TitleAPI.sendFullTitle(t, 0, 30, 0, "��a��l~~��", "��e��l������ ����ɴϴ�.");
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
