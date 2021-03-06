package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class DeadRevive extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public DeadRevive(Minigame minigame) {
		super(minigame, Material.DIAMOND, 1, (short)0, "죽은자의 소생", 9, 9); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e죽은자를 소생시키는 아이템");
		loreList.add("§e우클릭시 현재 장소를 저장해둔다.");
		loreList.add("§e장소가 저장된 상태에서");
		loreList.add("§e이 무기를 §c손에 §e들고 체력이 5이하로 떨어질시");
		loreList.add("§e저장한 위치에서 부활하며 모든 체력을 회복한다.");
		loreList.add("§e부활한 뒤 이 아이템은 소멸한다.");
		loreList.add("");
		loreList.add("§e우클릭 쿨타임 : §b53초");
		loreList.add("§c왼손에 들어도 능력 유지됨");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
			if (!checkCooldown(p, CooldownType.Primary))
				return;

			setCooldown(p, CooldownType.Primary, 53);
			Location pl = p.getLocation();
			saved_Location.put(p.getName(), pl);
			p.sendMessage("§7--------- 위치 기록됨 ------------");
			p.sendMessage("§a" + pl.getBlockX() + ", " + pl.getBlockY() + ", " + pl.getBlockZ());
			p.sendMessage("§7--------------------------------");

			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.0f, 0.1f);
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		//Bukkit.getLogger().info("아이템 인식 "+e.getDamage());
		if(p.getHealth() - e.getDamage() <= 5) {//죽을 거 같으면
			Location l = saved_Location.get(p.getName());
			if(l != null) {//장소를 지정해둿다면
				e.setCancelled(true); //사망 방지
				p.setHealth(p.getMaxHealth());
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0f,2.0f);
				p.teleport(l, TeleportCause.PLUGIN);
				p.getInventory().remove(this.getType());
				if(p.getInventory().getItemInOffHand().getType() == this.getType()) {
					p.getInventory().setItemInOffHand(null);
				}
				
				p.getWorld().playSound(l, Sound.BLOCK_PORTAL_TRAVEL, 1.0f,2.0f);
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§c§l신의 가호가 당신을 소생시켰습니다.");
			}
		}
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
