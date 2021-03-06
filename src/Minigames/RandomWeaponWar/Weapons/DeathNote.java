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
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.EarthEffect;

public class DeathNote extends SpecialWeapon{
	
	public DeathNote(Minigame minigame) {
		super(minigame, Material.BOOK_AND_QUILL, 1, (short)0, "데스노트", 1, 1); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e사람을 죽음으로 몰고가는 데스노트");
		loreList.add("§e우클릭시 모든 플레이어(자신제외)에게");
		loreList.add("§7전체 체력의 65%만큼 피해를 입힌다.");
		loreList.add("§7단, 이로인해 즉사하지는 않는다.");
		loreList.add("");
		loreList.add("§7사용시 이 아이템은 사라진다.");
		
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {	
		p.getInventory().remove(this.getType());
		TitleAPI.sendFullTitle(p, 0, 60, 0, "§4§l데스노트", "§e§l모두의 이름을 적었습니다.");
		for(Player t : getEnemyList(p)) {
			TitleAPI.sendFullTitle(t, 0, 60, 0, "§4§l데스노트", "§e§l빈사상태가 되었습니다.");
			double amt = (t.getMaxHealth()/100)*65;
			if(t.getHealth() - amt < 10) {
				amt = t.getHealth() - 10;
			}
			t.damage(amt);
			t.getWorld().playSound(t.getLocation(), Sound.ENTITY_GHAST_HURT, 1.0f, 0.1f);
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
