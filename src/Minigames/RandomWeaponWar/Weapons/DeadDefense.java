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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class DeadDefense extends SpecialWeapon{
	
	private PotionEffect absPt = new PotionEffect(PotionEffectType.ABSORPTION, 240, 5);
	
	public DeadDefense(Minigame minigame) {
		super(minigame, Material.GOLD_INGOT, 1, (short)0, "불사의 황금궤", 12, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e죽음을 막아주는 황금궤이다.");
		loreList.add("§e들고있는 상태에서 체력이 20%이하로 내려갈 시");
		loreList.add("§e그 공격을 회피하고");
		loreList.add("§e즉시 12초간 24만큼의 방어구를 얻는다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b23초");
		loreList.add("§c왼손에 들어도 능력 유지됨");
	
		setLore(loreList);
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
		Player p = (Player) e.getEntity();
		
		
		double baseHp = p.getMaxHealth()/5;
		if(p.getHealth() - e.getDamage() <= baseHp) {//20%이하시
			if(!checkCooldown(p, CooldownType.Primary)) return;
			
			setCooldown(p, CooldownType.Primary, 23);
			p.addPotionEffect(absPt);
			TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§c§l황금궤가 기적을 일으켰습니다.");

			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1.0f, 2.0f);
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
