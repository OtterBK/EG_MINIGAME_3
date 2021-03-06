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

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import Utility.VirtualProjectile;

public class DNAChanger extends SpecialWeapon{
	
	private PotionEffect regenPt = new PotionEffect(PotionEffectType.REGENERATION, 200, 2);
	private PotionEffect ironPt = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 0);
	
	public DNAChanger(Minigame minigame) {
		super(minigame, Material.STRING, 1, (short)0, "세포 조작기", 13, 15); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e생물의 세포를 조정할 수 있는 기계이다.");
		loreList.add("§e우클릭시 생체 세포를 활성화하여");
		loreList.add("§e10초간 회복속도를 비약적으로 상승시킨다.");
		loreList.add("§e쉬프트 우클릭시 세포를 증식시켜");
		loreList.add("§e10초간 받는 데미지를 경감시킨다.");
		loreList.add("");
		loreList.add("§e우클릭 쿨타임 : §b32초");
		loreList.add("§e쉬프트 우클릭 쿨타임 : §b22초");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(p.isSneaking()) {
			if(!checkCooldown(p, CooldownType.Secondary)) return;
			
			setCooldown(p, CooldownType.Secondary, 22);
			
			p.addPotionEffect(ironPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1.0f, 0.5f);
			TitleAPI.sendFullTitle(p, 0, 30, 0, "§c§l세포 증식","");
		}else {
			if(!checkCooldown(p, CooldownType.Primary)) return;
			
			setCooldown(p, CooldownType.Primary, 32);
			
			p.addPotionEffect(regenPt);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
			TitleAPI.sendFullTitle(p, 0, 30, 0, "§c§l세포 활성화","");
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

