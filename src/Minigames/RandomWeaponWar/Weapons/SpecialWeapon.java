package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import Utility.MyUtility;
import de.slikey.effectlib.EffectManager;

public abstract class SpecialWeapon extends ItemStack{

	protected EffectManager effectManager;
	
	protected Minigame minigame;
	private String weaponName;
	protected int min_damage;
	protected int max_damage;
	protected List<String> lore;
	
	protected HashMap<String, CustomCooldown> cooldown = new HashMap<String, CustomCooldown>();
	
	public SpecialWeapon(Minigame minigame, Material meterial, int cnt, short srt, String weaponName, int minDamage, int maxDamage) {
		super(meterial, cnt, srt);
		
		this.minigame = minigame;
		this.effectManager = minigame.server.egEM;
		this.weaponName = weaponName;
		
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName("§f[ §c"+this.weaponName+" §f]");
		meta.setUnbreakable(true);
		this.setItemMeta(meta);
		
		setDamage(minDamage, maxDamage);
	}
	
	public abstract void onInit();
	
	protected abstract void onRightClick(Player p);
	
	protected abstract void onLeftClick(Player p);
	
	protected void setDamage(int minDamage, int maxDamage) {		
		
		this.min_damage = minDamage;
		this.max_damage = maxDamage;
		
		setLore(lore);	
	}
	
	protected void setLore(List<String> loreList) {
		
		this.lore = loreList;
		
		List<String> tmpLore = new ArrayList<String>();
		if(lore == null) {
			tmpLore = new ArrayList<String>();
			tmpLore.add("");
			tmpLore.add("§6공격력 : §a"+this.min_damage+" - "+this.max_damage);
			tmpLore.add("");
		}else {
			tmpLore.add("");
			tmpLore.add("§6공격력 : §a"+this.min_damage+" - "+this.max_damage);
			tmpLore.add("");
			for(String str : lore) {
				tmpLore.add(str);
			}
			tmpLore.add("");
		}
		ItemMeta meta = this.getItemMeta();
		meta.setLore(tmpLore);
		this.setItemMeta(meta);

	}
	
	protected boolean existPlayer(Player t) {
		if(t != null) {
			if(minigame.ingamePlayer.contains(t.getName())){
				return true;
			}
		}
		return false;
	}
	
	protected boolean checkCooldown(Player p, CooldownType type) {
		CustomCooldown ccd = cooldown.get(p.getName());
		if(ccd == null) {
			ccd = new CustomCooldown(p.getName());
			cooldown.put(p.getName(), ccd);
			
			return true;
		}else {
			long leftCooldown = ccd.getLeftCooldown(type);
			if(leftCooldown <= 0) {
				
				return true;
			}else {
				int leftCooldown_Sec = (int)leftCooldown/1000;
				ActionBarAPI.sendActionBar(p, "§f§l"+leftCooldown_Sec+"초 후 사용 가능합니다.", 60);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 0.25F);
				
				return false;
			}
		}
	}
	
	protected void setCooldown(Player p, CooldownType type, double cooldownTime) {
		CustomCooldown ccd = cooldown.get(p.getName());
		if(ccd == null) {
			ccd = new CustomCooldown(p.getName());
			ccd.setCooldown(type, cooldownTime);
			cooldown.put(p.getName(), ccd);
		}else {
			ccd.setCooldown(type, cooldownTime);
		}
	}
	
	public String getDisplayName() {
		return this.getItemMeta().getDisplayName();
	}
	
	public String getWeaponName() {
		return this.weaponName;
	}
	
	public int getCalcDamage() {
		return MyUtility.getRandom(this.min_damage, this.max_damage);
	}
	
	public List<Player> getEnemyList(Player p){
		return getEnemyList(p.getName());
	}
	
	public List<Player> getEnemyList(String pName){
		List<Player> enemyList = new ArrayList<Player>(minigame.ingamePlayer.size());
		for(String tName : minigame.ingamePlayer) {
			if(tName.equalsIgnoreCase(pName)) continue;
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				enemyList.add(t);
			}
		}
		return enemyList;
	}
	
	public boolean compareItemName(Player t) {
		ItemStack mainItem = t.getInventory().getItemInMainHand();
		ItemStack subItem = t.getInventory().getItemInOffHand();
		boolean result = false;
		if(mainItem != null) {
			ItemMeta meta = mainItem.getItemMeta();
			if(meta != null) {
				String disPlayName = meta.getDisplayName();
				if(disPlayName != null) {
					if(disPlayName.equalsIgnoreCase(this.getDisplayName())) {
						result = true;
					}
				}
			}
		}
		if(subItem != null && !result) {
			ItemMeta meta = subItem.getItemMeta();
			if(meta != null) {
				String disPlayName = meta.getDisplayName();
				if(disPlayName != null) {
					if(disPlayName.equalsIgnoreCase(this.getDisplayName())) {
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	//이벤트
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			onRightClick(e.getPlayer());
		}else if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			onLeftClick(e.getPlayer());
		}
	}	
	
	public abstract void onPlayerMove(PlayerMoveEvent e);
	
	public abstract void onEntityDamaged(EntityDamageEvent e);
	
	public abstract void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim);
	
	public abstract void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim);
	
	public void onRegainHealth(EntityRegainHealthEvent e) {
		
	}
	
	public void onPlayerShotBow(EntityShootBowEvent e) {
		
	}
	
}
