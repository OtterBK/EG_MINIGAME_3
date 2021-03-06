package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;

public class ParkourBook extends SpecialWeapon{
	
	public ParkourBook(Minigame minigame) {
		super(minigame, Material.ENCHANTED_BOOK, 1, (short)0, "파쿠르 교본서", 1, 2); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e파쿠르 기술이 기록된 서적이다.");
		loreList.add("§e우클릭시 파쿠르를 사용할 수 있게 해준다.");
		loreList.add("");
		loreList.add("§e사용후 아이템은 사라진다.");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§c§l파쿠르를 배웠습니다.");
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0f, 0.1f);
		
		minigame.server.egParkour.join(p.getName());
		p.getInventory().remove(this.getType());
		p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD ,1));
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

