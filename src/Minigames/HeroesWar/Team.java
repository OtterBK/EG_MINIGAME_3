package Minigames.HeroesWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import Minigames.HeroesWar.Abilities.Ability;
import Minigames.HeroesWar.Abilities.Alchemist;
import Minigames.HeroesWar.Abilities.Guardian;
import Minigames.HeroesWar.Abilities.Monarch;
import Minigames.HeroesWar.Abilities.Monk;
import Minigames.HeroesWar.Abilities.Warrior;

public class Team{
	
	public List<String> teamList = new ArrayList<String>(20);
	public Location loc_jobSelect;
	public Location loc_spawn;
	public Location loc_totem;
	public Location loc_base1;
	public Location loc_base2;
	public int leftCarrot = 40;
	public int maxLeftSoul = 900;
	public int leftSoul = 1200;
	public ItemStack item_Helmet;
	public ItemStack item_Plate;
	public ItemStack item_Pants;
	public ItemStack item_Shoes;
	public String ms;
	public Inventory jobList;
	public String teamName;
	public Color teamColor;
	public int carrotCnt = 0;
	public boolean buffering = false;
	public HRWBase hrw;
	
	//능력설정용
	public HashMap<String, Integer> selectAbilityMap = new HashMap<String, Integer>(40);
	public List<Integer> leftAbCode = new ArrayList<Integer>();
	
	//딜량 감소 영웅들
	public List<Ability> reduceAbilityList = new ArrayList<Ability>();
	
	//리스폰 대기중 목록
	public List<String> respawning = new ArrayList<String>();
	
	//포탈
	public Location predator_portal1 = null;
	public Material predator_portal1_backupType = null;
	public Location predator_portal2 = null;
	
	//스탯용
	public Inventory statList;
	public ItemStack pane_item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
	public HashMap<Integer, HRWPlayer> slotNumMap = new HashMap<Integer, HRWPlayer>();
	
	public List<Integer> abCodeList = new ArrayList<Integer>();
	
	public Team(HRWBase hrw, String teamName, Color armorColor, String ms, Color rgbTeamColor, List<Integer> codeList) {
		
		this.teamName = teamName;
		teamColor = rgbTeamColor;
		
		item_Helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta leathermeta = (LeatherArmorMeta) item_Helmet.getItemMeta();
		leathermeta.setColor(armorColor);
		//leathermeta.addEnchant(Enchantment.DURABILITY, 100, true);
		leathermeta.setUnbreakable(true);
		item_Helmet.setItemMeta(leathermeta);
		
		item_Plate = new ItemStack(Material.LEATHER_CHESTPLATE);
		leathermeta = (LeatherArmorMeta) item_Plate.getItemMeta();
		leathermeta.setColor(armorColor);
		//leathermeta.addEnchant(Enchantment.DURABILITY, 100, true);
		leathermeta.setUnbreakable(true);
		item_Plate.setItemMeta(leathermeta);
		
		item_Pants = new ItemStack(Material.LEATHER_LEGGINGS);
		leathermeta = (LeatherArmorMeta) item_Pants.getItemMeta();
		leathermeta.setColor(armorColor);
		//leathermeta.addEnchant(Enchantment.DURABILITY, 100, true);
		leathermeta.setUnbreakable(true);
		item_Pants.setItemMeta(leathermeta);
		
		item_Shoes = new ItemStack(Material.LEATHER_BOOTS);
		leathermeta = (LeatherArmorMeta) item_Shoes.getItemMeta();
		leathermeta.setColor(armorColor);
		//leathermeta.addEnchant(Enchantment.DURABILITY, 100, true);
		leathermeta.setUnbreakable(true);
		item_Shoes.setItemMeta(leathermeta);
		
		this.abCodeList = codeList;
		
		this.ms = ms;
		
		this.hrw = hrw;
		
		this.statList = Bukkit.createInventory(null, 54, "§0§l"+teamName+" 플레이어 목록");
	}
	
	public void sortBaseLoc() {
		if(loc_base1 == null || loc_base2 == null) return;
		
		if(loc_base1.getX() > loc_base2.getX()) {
			double tmp = loc_base2.getX();
			loc_base2.setX(loc_base1.getX());
			loc_base1.setX(tmp);
		}
		if(loc_base1.getY() > loc_base2.getY()) {
			double tmp = loc_base2.getY();
			loc_base2.setY(loc_base1.getY());
			loc_base1.setY(tmp);
		}
		if(loc_base1.getZ() > loc_base2.getZ()) {
			double tmp = loc_base2.getZ();
			loc_base2.setZ(loc_base1.getZ());
			loc_base1.setZ(tmp);
		}
	}
	
	public void InitAbCode() {
		leftAbCode.clear();
		leftAbCode.addAll(abCodeList);
	}
	
	public void add(Player p) {
		//Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join "+teamName+" "+p.getName());
		setArmor(p);
		teamList.add(p.getName());
	}
	
	public void setArmor(Player p) {
		p.getInventory().setHelmet(item_Helmet);
		p.getInventory().setChestplate(item_Plate);
		p.getInventory().setLeggings(item_Pants);
		p.getInventory().setBoots(item_Shoes);
	}
	
	public void setCarrot() {
		this.leftCarrot = teamList.size() * 12;
	}
	
	public void sortTanker() { //수도승, 군주, 수호자 순으로 정렬
		List<Ability> tmpList = new ArrayList(reduceAbilityList);
		reduceAbilityList.clear();
		
		for(Ability ability : tmpList) {
			if(ability instanceof Monk) {
				reduceAbilityList.add(ability);
			}
		}
		
		for(Ability ability : tmpList) {
			if(ability instanceof Monarch) {
				reduceAbilityList.add(ability);
			}
		}
		
		for(Ability ability : tmpList) {
			if(ability instanceof Warrior) {
				reduceAbilityList.add(ability);
			}
		}
		
		for(Ability ability : tmpList) {
			if(ability instanceof Alchemist) {
				reduceAbilityList.add(ability);
			}
		}
		
		for(Ability ability : tmpList) {
			if(ability instanceof Guardian) {
				reduceAbilityList.add(ability);
			}
		}
	}
	
	public void spawn(Player p) {
		p.teleport(loc_spawn, TeleportCause.PLUGIN);
	}
	
	public void putOnStatItem() {
		statList.clear();
		
		for(int i = 0; i <= 9; i++) { //장식 설정
			statList.setItem(i, pane_item);
		}
		for(int i = 44; i <= 53; i++) {
			statList.setItem(i, pane_item);
		}
		for(int i = 0; i <= 53; i+=9) {
			statList.setItem(i, pane_item);
		}
		for(int i = 8; i <= 53; i+=9) {
			statList.setItem(i, pane_item);
		}
		
		int slotNum = 10; 
		for(String tName : teamList) {
			HRWPlayer hrwP = hrw.playerMap.get(tName);
			if(hrwP == null) continue;
			statList.setItem(slotNum, hrwP.ability.getstatItem());
			slotNumMap.put(slotNum, hrwP);
			if(slotNum == 16 || slotNum == 25 || slotNum == 34) slotNum += 1;
			slotNum += 2;
		}
	}
	
	public void updateStatInventory() {
		for(int slotNum : slotNumMap.keySet()) {
			HRWPlayer hrwP = slotNumMap.get(slotNum);
			if(hrwP == null) continue;
			
			ItemStack item = statList.getItem(slotNum);
			if(item == null) continue;
			
			ItemMeta meta = item.getItemMeta();
			hrwP.ability.updateStatItem();
			List<String> loreList = hrwP.ability.statLoreList;
			
			if(loreList == null) continue;
			meta.setLore(loreList);
			item.setItemMeta(meta);
			
			if(hrw.ingamePlayer.contains(hrwP.ability.playerName)) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}else {
				item.removeEnchantment(Enchantment.DURABILITY);
			}
		}
	}
	
	public void openStatInventory(Player p) {
		if(hrw.countItem(statList, pane_item.getType()) == 0) {
			p.sendMessage(ms+"아직 전투가 시작되지 않았습니다.");
		}else {
			updateStatInventory();
			p.openInventory(statList);
		}
	}
	
}
