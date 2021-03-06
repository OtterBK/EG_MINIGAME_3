package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class LocationSaver extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public LocationSaver(Minigame minigame) {
		super(minigame, Material.END_ROD, 1, (short)0, "위치 기록기", 12, 14); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e소유자의 위치를 기록하는 장치");
		loreList.add("§e우클릭시 현재 위치를 기록한다.");
		loreList.add("§e쉬프트+우클릭시 기록한 위치로 이동한다.");
		loreList.add("");
		loreList.add("§e우클릭 쿨타임 : §b12초");
		loreList.add("§e쉬프트+ 우클릭 쿨타임 : §b40초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(p.isSneaking()) {
			Location sl = saved_Location.get(p.getName());
			if(sl != null) {
				if (!checkCooldown(p, CooldownType.Secondary))
					return;

				setCooldown(p, CooldownType.Secondary, 40);
				p.teleport(sl, TeleportCause.PLUGIN);
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
				saved_Location.remove(p.getName());
				
			}else {
				p.sendMessage("§6먼저 위치를 기록해주세요.");
				p.playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.5f);
			}		
		}else {
			if (!checkCooldown(p, CooldownType.Primary))
				return;

			setCooldown(p, CooldownType.Primary, 12);
			Location pl = p.getLocation();
			saved_Location.put(p.getName(), pl);
			p.sendMessage("§7--------- 위치 기록됨 ------------");
			p.sendMessage("§a" + pl.getBlockX() + ", " + pl.getBlockY() + ", " + pl.getBlockZ());
			p.sendMessage("§7--------------------------------");

			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 2.0f);
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
