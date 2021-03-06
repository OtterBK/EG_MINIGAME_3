package Minigames.WarOfGod.Abilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.WarOfGod.WarOfGod;

public class Ability {

	public WarOfGod wog; //호출한 게임
	public String playerName; //플레이어이름
	public String abilityName; //능력명
	public int cooldown_primarySkill = 0; //주스킬 쿨타임 
	public int cooldown_secondarySkill = 0; //보조스클 쿨타임
	public int cooldown_passsive = 0; //패시브 보급 쿨타임
	public int cooldown_itemKit = 0; //아이템 보급 쿨타임
	public long nextTime_primarySkill = 0; //주스킬 남은 쿨타임
	public long nextTime_secondarySkill = 0; //보조스킬 남은 쿨타임
	public long nextTime_passive = 0; //패시브 남은 쿨타임
	public long nextTime_itemKit = 0; //아이템 보급 남은 쿨타임
	public int primaryStone = 0; //좌클 돌
	public int secondaryStone = 0; //우클 돌
	public MyCooldown cooldown;
	public Sidebar sidebar;
	
	
	public Ability(WarOfGod wog, Player p) {
		this.wog = wog;
		playerName = p.getName();
		cooldown_itemKit = 300;
		//this.abilityName = abilityName;
	}
	
	public void finalize() {
		//wog.server.egPM.printLog(abilityName+"어빌리티 객체 삭제됨"); 
	}
	
	public void helpMsg(Player p){
		
	}
	
	public void primarySkill() {
		
	}
	
	public void secondarySkill() {
		
	}
	
	public void setSidebar() {
		
	}
	
	public boolean exitsPlayer(Player p) {
		if(p == null || !p.isOnline()) return false;
		return true;
	}	
	
	public int getTimerId() {
		return this.cooldown.timerId;
	}
	
	public void setUI() {

	}
	
	public void hideUI() {
		Player p = Bukkit.getPlayer(playerName);
		if(exitsPlayer(p)) {
			sidebar.hideFrom(p);
		}
	}
	
	public void itemSupply(Player p) {
		if(cooldown.checkCooldown("itemKit")) {
			cooldown.setCooldown("itemKit");
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 2.0f);
			TitleAPI.sendFullTitle(p, 10,60, 20, "", "아이템을 재보급 받았습니다.");
			giveBasicItem(p);
		}
	}
	
	public void giveBasicItem(Player p) {
		p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET, 1));
		p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET, 1));
		p.getInventory().addItem(new ItemStack(Material.DIRT, 15));
		p.getInventory().addItem(new ItemStack(Material.SAPLING, 3));
		p.getInventory().addItem(new ItemStack(Material.SEEDS, 2));
		p.getInventory().addItem(new ItemStack(Material.INK_SACK ,10, (byte) 15));
	}
	
	///////////이벤트
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		
	}
	
	public void onEntityDamaged(EntityDamageEvent e) {
		
	}
	
	public void onClickInventory(InventoryClickEvent e) {
		
	}
	
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		
	}
	
	public void onHitted(EntityDamageByEntityEvent e) {
		
	}
	
	public void onBlockBreak(BlockBreakEvent e) {
		
	}
	
	public void onBlockPlaced(BlockPlaceEvent e) {
		
	}
	
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		for(String tName : (wog.getTeam(p.getName()).equalsIgnoreCase("BLUE") ?
				wog.blueTeam.teamList : wog.redTeam.teamList)) {
			if(wog.playerMap.get(tName).ability.abilityName.equalsIgnoreCase("아테나")) {
				Player t = Bukkit.getPlayer(tName);
				if(exitsPlayer(t)) {
					t.setLevel(t.getLevel()+1);
					TitleAPI.sendFullTitle(t, 10,60, 20, "", p.getName()+" 님의 사망으로 레벨상승");
					p.getWorld().playSound(t.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
				}
			}		
		}
	}
	
	public List<String> getMyTeam() {
		return (wog.getTeam(playerName).equalsIgnoreCase("BLUE") ?
				wog.blueTeam.teamList : wog.redTeam.teamList);
	}
	
	public List<String> getMyEnemy() {
		return (wog.getTeam(playerName).equalsIgnoreCase("BLUE") ?
				wog.redTeam.teamList : wog.blueTeam.teamList);
	}
	
	public void onKillPlayer(Player victim) {
		
	}
	
	public void onKilledByEnemy(Player killer) {
		
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		
	}
	
	public void onPlayerQuit(PlayerQuitEvent e) {
		
	}
	
	public void onMouseClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();
		if(p.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
			if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				primarySkill();
			} else if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				secondarySkill();
			}
		}
	}
	
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		
	}
	
	public void onInventoryClose(InventoryCloseEvent e) {
		
	}
	
	public void onPlayerMove(PlayerMoveEvent e) {
		
	}
	
	public void onPlayerItemPickUp(PlayerPickupItemEvent e) {
		
	}
	
	public void onItemDrop(PlayerDropItemEvent e) {
		
	}
	
	public void onChat(PlayerChatEvent e) {
		
	}
	
	public void onSneak(PlayerToggleSneakEvent e) {

	}
	
	public void onPlayerShotBow(EntityShootBowEvent e) {

	}
	
	public void onRegainHealth(EntityRegainHealthEvent e) {

	}
	
	////////////// 쿨타운
	
	public class MyCooldown {
		
		Ability ability;
		private int timerId;
		
		public MyCooldown(Ability ability) {
			this.ability = ability;
		}
		
		public boolean checkCooldown(String skillName) {
			
			boolean ret = false;
			int leftTime = 0;
			
			if(skillName.equalsIgnoreCase("primary")) {
				if(ability.nextTime_primarySkill <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_primarySkill- System.currentTimeMillis()) / 1000)+1;
				}
			} else if (skillName.equalsIgnoreCase("secondary")) {
				if(ability.nextTime_secondarySkill <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_secondarySkill - System.currentTimeMillis()) / 1000)+1;
				}
			} else if (skillName.equalsIgnoreCase("passive")) {
				if(ability.nextTime_passive <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_secondarySkill  - System.currentTimeMillis()) / 1000)+1;
				}
			}else if (skillName.equalsIgnoreCase("itemKit")) {
				if(ability.nextTime_itemKit <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_itemKit  - System.currentTimeMillis()) / 1000)+1;
				}
			}
			
			if(!ret) {
				Player p = Bukkit.getPlayer(ability.playerName);
				if(ability.exitsPlayer(p)) {
					ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+leftTime+"초 후 사용하실 수 있습니다.", 20);
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				}
			}
			return ret;
		}
		
		public int getLeftCooldown(String skillName) {
			float leftTime = 0;
			
			if(skillName.equalsIgnoreCase("primary")) {
				leftTime = (ability.nextTime_primarySkill - System.currentTimeMillis());
			} else if (skillName.equalsIgnoreCase("secondary")) {
				leftTime = (ability.nextTime_secondarySkill - System.currentTimeMillis());
			} else if (skillName.equalsIgnoreCase("passive")) {
				leftTime = (ability.nextTime_passive - System.currentTimeMillis());
			}else if (skillName.equalsIgnoreCase("itemKit")) {
				leftTime = (ability.nextTime_itemKit - System.currentTimeMillis());
			}
			if(leftTime < 0) return 0;
			
			return (int)(leftTime/1000)+1;
		}
		
		public void setCooldown(String skillName) {			
			if(skillName.equalsIgnoreCase("primary")) {
				nextTime_primarySkill = System.currentTimeMillis()+ability.cooldown_primarySkill*1000;
			} else if (skillName.equalsIgnoreCase("secondary")) {
				nextTime_secondarySkill = System.currentTimeMillis()+ability.cooldown_secondarySkill*1000;
			} else if (skillName.equalsIgnoreCase("itemKit")) {
				nextTime_itemKit = System.currentTimeMillis()+ability.cooldown_itemKit*1000;
			} else if (skillName.equalsIgnoreCase("passive")) {
				nextTime_passive = System.currentTimeMillis()+ability.cooldown_passsive*1000;
			} 
			
		}
		
		public void setCooldownSpec(String skillName, int leftTime) {
			if(skillName.equalsIgnoreCase("primary")) {
				nextTime_primarySkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("secondary")) {
				nextTime_secondarySkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("passive")) {
				nextTime_passive = System.currentTimeMillis() + (leftTime * 1000);
			} 
			
		}	
	}		
}
