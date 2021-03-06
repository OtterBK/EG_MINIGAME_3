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

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;

public class ForceTaker extends SpecialWeapon{
	
	public ForceTaker(Minigame minigame) {
		super(minigame, Material.DOUBLE_PLANT, 1, (short)4, "강탈 장미", 1, 1); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e강력한 자기성을 가지고 있는 장미이다.");
		loreList.add("§e우클릭시 7칸내 바라보고 있는 적이");
		loreList.add("§e들고있는 아이템을 강탈한다.");
		loreList.add("§7능력 사용후에 이 아이템은 사라진다.");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
	
		List<Player> hit = minigame.getCorsurPlayer(p, getEnemyList(p), 7, false);
		if(hit != null) {
			
			p.getInventory().remove(this.getType());
			Player target = hit.get(0);
			
			ItemStack item = target.getInventory().getItemInMainHand();
			p.getInventory().addItem(item);
			target.getInventory().remove(item.getType());
			
			TitleAPI.sendFullTitle(target, 0, 60, 0, "", "§e§l무기를 강탈 당했습니다.");
			TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§e§l무기를 강탈했습니다.");
			
			target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.1f, 0.1f);
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.1f, 0.1f);
		}else {
			ActionBarAPI.sendActionBar(p, "§2§l대상이 존재하지 않습니다.", 40);
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
