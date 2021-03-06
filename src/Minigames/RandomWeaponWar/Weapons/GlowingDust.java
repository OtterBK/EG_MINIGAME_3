package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class GlowingDust extends SpecialWeapon{

	private PotionEffect glowPt = new PotionEffect(PotionEffectType.GLOWING, 300, 0);
	
	public GlowingDust(Minigame minigame) {
		super(minigame, Material.GLOWSTONE_DUST, 1, (short)0, "빛의 가루", 14, 16); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e밝은 빛을 띄는 가루");
		loreList.add("§e우클릭시 소량의 가루를 공중에 뿌린다.");
		loreList.add("§e공중에 뿌려진 가루는 모든 플레이어(자신제외)의");
		loreList.add("§e위치를 15초간 표시한다.");
		loreList.add("");
		loreList.add("§e쿨타임 : §b75초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 75);
		
		List<Player> enemyList = getEnemyList(p);
		
		TitleAPI.sendFullTitle(p, 0, 70, 0, "", "§e§l가루를 뿌렸습니다.");
		for(Player t : enemyList) {
			TitleAPI.sendFullTitle(t, 0, 70, 0, "", "§e§l빛의 가루를 뒤집어 썼습니다.");
			t.addPotionEffect(glowPt);
		}
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 2.0f);
		
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
