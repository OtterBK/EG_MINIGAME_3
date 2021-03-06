package Minigames.HeroesWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import EGServer.EGServer;
import Minigames.Minigame;
import Utility.SkullCreator;

public class HRWBase extends Minigame{
	//팀
	public Team blueTeam;
	public Team redTeam;
	
	//플레이어들
	public HashMap<String, HRWPlayer> playerMap = new HashMap<String, HRWPlayer>(30);
	
	//아이템
	
	//public ItemStack stone_land;
	public ItemStack stone_forest;
	public ItemStack stone_miracle;
	public ItemStack stone_water;
	public ItemStack stone_fire;
	
	//public ItemStack ring_light;
	public ItemStack ring_speed;
	public ItemStack ring_gravity;
	public ItemStack ring_gale;
	public ItemStack ring_storm;
	
	//public ItemStack neck_black;
	public ItemStack neck_iron;
	public ItemStack neck_gold;
	public ItemStack neck_diamond;
	public ItemStack neck_emerald;
	
	public ItemStack tailsman_miracle;
	public ItemStack tailsman_eminent;
	public ItemStack tailsman_heal;
	//public ItemStack tailsman_meditation;
	public ItemStack tailsman_wisdom;
	
	/////// 각종 아이템
	public ItemStack item_hrwHelper;
	public ItemStack item_selectJob;
	public ItemStack item_returnBase;
	public ItemStack item_none_stone;
	public ItemStack item_none_ring;
	public ItemStack item_none_neck;
	public ItemStack item_none_tailsman;
	
	//점령지
	public CatchPoint point1;
	public CatchPoint point2;
	public CatchPoint point3;
	
	//설명 인벤	
	public Inventory inven_desc_tracer;
	public Inventory inven_desc_assasin;
	public Inventory inven_desc_hunter;
	public Inventory inven_desc_mercenary;
	public Inventory inven_desc_archer;
	public Inventory inven_desc_wizard;
	public Inventory inven_desc_hider;
	public Inventory inven_desc_virtuous;
	public Inventory inven_desc_marksman;
	public Inventory inven_desc_guardian;
	public Inventory inven_desc_monk;
	public Inventory inven_desc_knight;
	public Inventory inven_desc_fighter;
	public Inventory inven_desc_monarch;
	public Inventory inven_desc_predator;
	public Inventory inven_desc_priest;
	public Inventory inven_desc_alchemist;
	public Inventory inven_desc_blackMagician;
	public Inventory inven_desc_warrior;
	
	public String ms = "§7[§6전체§7] ";
	public String allms = "§7[ §6전체 §7] ";
	
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	public Inventory inven_hrwHelper;
	
	//능력 목록
	public List<Integer> abCodeList = new ArrayList<Integer>();
	
	public HRWBase(EGServer server) {
		super(server);

		abCodeList.add(3);
		abCodeList.add(4);
		abCodeList.add(5);
		abCodeList.add(6);
		abCodeList.add(7);
		abCodeList.add(12);
		abCodeList.add(13);
		abCodeList.add(14);
		abCodeList.add(15);
		//탱커
		abCodeList.add(21);
		abCodeList.add(22);
		abCodeList.add(23);
		abCodeList.add(24);
		abCodeList.add(25);
		abCodeList.add(30);
		abCodeList.add(31);
		//지원
		abCodeList.add(39);
		abCodeList.add(40);
		abCodeList.add(41);
			
		//팀
		blueTeam = new Team(this, "BLUE", Color.BLUE,"§7[ §b블루팀 §7] ", Color.fromRGB(0,0,255), abCodeList);
		redTeam = new Team(this, "RED", Color.RED,"§7[ §c레드팀 §7] ", Color.fromRGB(255,0,0), abCodeList);
		
		/////////////////// 블루팀 영웅선택
		blueTeam.jobList = Bukkit.createInventory(null, 54, "§f[ §b§l영웅선택 §f]");

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i < 54; i += 9) {
			blueTeam.jobList.setItem(i, item);
		}
		for (int i = 8; i < 54; i += 9) {
			blueTeam.jobList.setItem(i, item);
		}
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c공격군 §7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 강력한 공격스킬로 적을 섬멸합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(1, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b01cbfb414760efe504d4af739708a18b89b6145d7659bcf526f1e42d7bedb37");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c수비군 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 적을 방해하거나 공격을 막아 아군을 보호합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(19, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/41a6a411b802b22e5324795ce34e3cae4a83796a18d92332666cbf21488c0");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c지원군 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 치유 또는 강화스킬을 이용하여 아군을 돕습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(37, item);
		
		item = new ItemStack(Material.IRON_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a추적자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7기동성이 뛰어난 영웅으로 적의 지원군을");
		loreList.add("§7집중적으로 공격하기 좋습니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(3, item);
		
		item = new ItemStack(Material.DIAMOND_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a자객 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7준수한 기동성과 다수의 적에 대한 공격에");
		loreList.add("§7출중합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(4, item);
		
		item = new ItemStack(Material.GOLD_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a사냥꾼 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7매우 빠른 공격 스킬을 가지며");
		loreList.add("§7하나의 적을 집요하게 공격할 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(5, item);
		
		item = new ItemStack(Material.GOLD_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a용병 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7소유한 여러개의 기본능력을 지속적으로 변경하여 전투하는");
		loreList.add("§7영웅으로 스킬사용은 간단하지만 범용성은 뛰어납니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(6, item);
		
		item = new ItemStack(Material.BOW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a궁수 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7단일 대상에 대한 강력한 원거리공격이 특징인 영웅입니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(7, item);
		
		item = new ItemStack(Material.BLAZE_ROD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a마도사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7다수에게 원거리 마법 공격을 사용하는");
		loreList.add("§7화력에 특화된 영웅입니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a은둔자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7모습을 감춘 상태에서 적을 급습하는 영웅입니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(13, item);
		
		item = new ItemStack(Material.GOLD_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a선인 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7공중을 날아다니며 적을 공격합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(14, item);
		
		item = new ItemStack(Material.IRON_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a저격수 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7원거리에서 적에게 치명적인 공격을 가합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(15, item);
		
		item = new ItemStack(Material.SHIELD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b수호자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7방벽을 사용하여 주변 아군을 보호하며");
		loreList.add("§7강력한 CC스킬을 가지고 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(21, item);
		
		item = new ItemStack(Material.DIAMOND, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b수도승 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7아군에게 방벽을 부여하여 적의 공격을 방어합니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(22, item);
		
		item = new ItemStack(Material.BONE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b기사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적의 주의를 끌어 아군을 보호하고");
		loreList.add("§7견제합니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(23, item);
		
		item = new ItemStack(Material.IRON_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b격투가 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7끊임없는 공격으로 특정 대상의 공격을 방해합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(24, item);
		
		item = new ItemStack(Material.BLAZE_POWDER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b군주 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7아군이 받는 데미지를 대신 받거나");
		loreList.add("§7적의 이동을 저해합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(25, item);
		
		item = new ItemStack(Material.SHEARS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b포식자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7특정 대상을 아군 진영으로 끌고올 수 있으며");
		loreList.add("§7원하는 지역에 포탈을 열 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(30, item);
		
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b전사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적진에 돌격하여 적의 이동을");
		loreList.add("§7저해합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(31, item);
		
		item = new ItemStack(Material.SLIME_BALL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §e사제 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7아군의 체력을 회복시키거나 죽은 아군을");
		loreList.add("§7부활시킬 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(39, item);
		
		item = new ItemStack(Material.FLINT_AND_STEEL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §e연금 술사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적의 행동을 저지하고");
		loreList.add("§7아군을 강화하며 공격을 반사합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(40, item);
		
		item = new ItemStack(Material.COAL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §e흑마술사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적의 스킬 사용을 차단하고");
		loreList.add("§7아군을 강화합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(41, item);
		
		////////////////

		/////////////////// 레드팀 영웅선택
		redTeam.jobList = Bukkit.createInventory(null, 54, "§f[ §c§l영웅선택 §f]");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i < 54; i += 9) {
			redTeam.jobList.setItem(i, item);
		}
		for (int i = 8; i < 54; i += 9) {
			redTeam.jobList.setItem(i, item);
		}
	
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c공격군 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 강력한 공격스킬로 적을 섬멸합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(1, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b01cbfb414760efe504d4af739708a18b89b6145d7659bcf526f1e42d7bedb37");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c수비군 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 적을 방해하거나 공격을 막아 아군을 보호합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(19, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/41a6a411b802b22e5324795ce34e3cae4a83796a18d92332666cbf21488c0");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c지원군 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 치유 또는 강화스킬을 이용하여 아군을 돕습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(37, item);
		
		item = new ItemStack(Material.IRON_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a추적자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7기동성이 뛰어난 영웅으로 적의 지원군을");
		loreList.add("§7집중적으로 공격하기 좋습니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(3, item);
		
		item = new ItemStack(Material.DIAMOND_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a자객 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7준수한 기동성과 다수의 적에 대한 공격에");
		loreList.add("§7출중합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(4, item);
		
		item = new ItemStack(Material.GOLD_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a사냥꾼 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7매우 빠른 공격 스킬을 가지며");
		loreList.add("§7하나의 적을 집요하게 공격할 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(5, item);
		
		item = new ItemStack(Material.GOLD_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a용병 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7소유한 여러개의 기본능력을 지속적으로 변경하여 전투하는");
		loreList.add("§7영웅으로 스킬사용은 간단하지만 공격력은 뛰어납니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(6, item);
		
		item = new ItemStack(Material.BOW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a궁수 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7단일 대상에 대한 강력한 원거리공격이 특징인 영웅입니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(7, item);
		
		item = new ItemStack(Material.BLAZE_ROD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a마도사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7다수에게 원거리 마법 공격을 사용하는");
		loreList.add("§7화력에 특화된 영웅입니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a은둔자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7모습을 감춘 상태에서 적을 급습하는 영웅입니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(13, item);
		
		item = new ItemStack(Material.GOLD_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a선인 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7공중을 날아다니며 적을 공격합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(14, item);
		
		item = new ItemStack(Material.IRON_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §a저격수 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7원거리에서 적에게 치명적인 공격을 가합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(15, item);
		
		item = new ItemStack(Material.SHIELD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b수호자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7방벽을 사용하여 주변 아군을 보호하며");
		loreList.add("§7강력한 CC스킬을 가지고 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(21, item);
		
		item = new ItemStack(Material.DIAMOND, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b수도승 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7아군에게 방벽을 부여하여 적의 공격을 방어합니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(22, item);
		
		item = new ItemStack(Material.BONE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b기사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적을 주의를 끌어 아군을 보호하고");
		loreList.add("§7견제합니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(23, item);
		
		item = new ItemStack(Material.IRON_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b격투가 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7끊임없는 공격으로 특정 대상의 공격을 방해합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(24, item);
		
		item = new ItemStack(Material.BLAZE_POWDER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b군주 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7아군이 받는 데미지를 대신 받거나");
		loreList.add("§7적의 이동을 저해합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(25, item);
		
		item = new ItemStack(Material.SHEARS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b포식자 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7특정 대상을 아군 진영으로 끌고올 수 있으며");
		loreList.add("§7원하는 지역에 포탈을 열 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 하");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(30, item);
		
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b전사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적진에 돌격하여 적의 이동을");
		loreList.add("§7저해합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(31, item);
		
		item = new ItemStack(Material.SLIME_BALL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §e사제 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7아군의 체력을 회복시키거나 죽은 아군을");
		loreList.add("§7부활시킬 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(39, item);
		
		item = new ItemStack(Material.FLINT_AND_STEEL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §e연금 술사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적의 행동을 저지하고");
		loreList.add("§7아군을 강화하며 공격을 반사합니다.");
		loreList.add("");
		loreList.add("§7난이도: 중");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(40, item);
		
		item = new ItemStack(Material.COAL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §e흑마술사 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7적의 스킬 사용을 차단하고");
		loreList.add("§7아군을 강화합니다.");
		loreList.add("");
		loreList.add("§7난이도: 상");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(41, item);
		
		inven_desc_tracer = Bukkit.createInventory(null, 9, "§0§l영웅 - 추적자");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e축지§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7이동중인 방향으로 짧은 거리를 순식간에");
		loreList.add("§7이동합니다.");
		loreList.add("§7최대 5회까지 충전해둘 수 있습니다.");
		loreList.add("");
		loreList.add("§7충전 대기시간 : §a3초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e출환§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§76초전의 위치와 체력으로 되돌아갑니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a17초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(1 ,item);
		
		/*item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e출환§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§76초전의 위치와 체력으로 되돌아갑니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a6초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(2 ,item);*/
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e부착식 폭탄§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§71칸내 바라보는 적에게");
		loreList.add("§7폭탄을 부착 또는 설치합니다.");
		loreList.add("§7폭탄은 거리에 따라 최대 36의 피해를 줍니다.");
		loreList.add("§7폭발 범위에 자신도 있을 시 피해를 받습니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a50초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e과충전§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7축지의 재충전 시간이 3초에서");
		loreList.add("§72.4초로 줄어듭니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(8 ,item);
		
		inven_desc_assasin = Bukkit.createInventory(null, 9, "§0§l영웅 - 자객");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e생존 본능§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7빠르게 뒤로 점프합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a3초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e표창 투척§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7전방에 3개의 표창을 던집니다.");
		loreList.add("§7표창은 적에게 개당 8의 피해를 주고 8초간 독상태로 만듭니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a6초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e질풍§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7빠르게 전방으로 날아가며");
		loreList.add("§7경로상의 적에게 12의 피해를 줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a9초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e신검일체§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7사용 0.5초 후 6초간 강력한 무기를 소환합니다.");
		loreList.add("§7이동속도가 크게 증가하고 공격력이");
		loreList.add("§715가 되며 일자긋기의 재사용 대기시간이 초기화됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a88초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e이단 뛰기§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7점프키를 연속으로 2번 누를시");
		loreList.add("§72단 점프가 가능해지며 적을 처치할 시");
		loreList.add("§7일자긋기의 재사용 대기시간이 초기화됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a2초");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(8 ,item);
		
		
		inven_desc_hunter = Bukkit.createInventory(null, 9, "§0§l영웅 - 사냥꾼");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e고양감§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§73초간 이동속도가 75% 증가합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a10초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e상단 베기§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7위로 베어가르어 상대를 높이 띄우고");
		loreList.add("§713의 피해를 줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a5초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e연속 베기§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§72번 베어가릅니다.");
		loreList.add("§71회당 8의 피해를 줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e사냥§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7전방으로 밧줄을 던집니다.");
		loreList.add("§7적이 밧줄에 맞을 시 해당 적의 뒤로 이동하고");
		loreList.add("§725의 피해를 줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a39초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e삼단 베기§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7연속 베기 사용시 2회가 아닌");
		loreList.add("§73회 베어가릅니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(8 ,item);
		
		
		inven_desc_mercenary = Bukkit.createInventory(null, 9, "§0§l영웅 - 용병");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e독 검§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7타격 대상을 10초간 중독 상태로 만듭니다.");
		loreList.add("");
		loreList.add("§4기본 상시 발동 능력을 해당 능력으로 변경합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e백병전§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7타격 대상이 멀리 밀쳐집니다.");
		loreList.add("");
		loreList.add("§4기본 상시 발동 능력을 해당 능력으로 변경합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e전력 질주§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7이동 속도가 25% 증가합니다.");
		loreList.add("");
		loreList.add("§4기본 상시 발동 능력을 해당 능력으로 변경합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e회귀§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§730칸내에 바라보는 곳으로 순식간에 이동합니다.");
		loreList.add("§7이동시 원래 위치에 4초간 잔상을 남깁니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a35초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e회피§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§710% 확률로 적의 공격을 회피하고");
		loreList.add("§7해당 적의 뒤로 이동합니다.");
		loreList.add("§7회피시에 받는 피해가 절반이 됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(8 ,item);
		
		
		inven_desc_archer = Bukkit.createInventory(null, 9, "§0§l영웅 - 궁수");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e은화살§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7스킬 사용후 쏘는 화살 1개가 적을 타격할 시");
		loreList.add("§7해당 적을 1~3초간 구속합니다.");
		loreList.add("§7(활 시위를 당긴만큼 시간이 늘어남)");
		loreList.add("§7최대 지속시간은 6초입니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a9초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e속사§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e좌클릭 §f]");
		loreList.add("");
		loreList.add("§7스킬 사용후 쏘는 4개의 화살이 매우 빠르게");
		loreList.add("§7날라갑니다.");
		loreList.add("§7해당 화살이 주는 피해는 8로 고정됩니다.");
		loreList.add("§7최대 지속시간은 8초입니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a19초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e유성§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7바라보는 지점에 수많은 빛의 화살을 떨어뜨립니다.");
		loreList.add("§7화살은 블럭 또는 적을 맞출시 폭발하며 개당 27의");
		loreList.add("§7피해를  줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a77초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e강궁§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7은화살이 적을 맞출 시");
		loreList.add("§7구속이 아닌 기절시킵니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(8 ,item);
		
		
		
		inven_desc_wizard = Bukkit.createInventory(null, 9, "§0§l영웅 - 마도사");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e화염탄§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7전방으로 화염탄을 날립니다.");
		loreList.add("§7화염탄은 블럭 또는 적을 맞출 시 폭발하며");
		loreList.add("§7폭발지점 3칸내의 적에게 5의 피해를 주며 직격한 플레이어");
		loreList.add("§7에게는 9의 추가피해를 주고");
		loreList.add("§78초의 화상피해를 입힙니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a3초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e불덩이§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7바라보는 곳에 화염덩이를 던집니다.");
		loreList.add("§7화염덩이는 착탄시 불구덩이를 만들며");
		loreList.add("§7불구덩이 내의 적에게 초당 4의 피해와");
		loreList.add("§77초의 화상피해를 입힙니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a15초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e대분화§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7바라보는 곳에  매우 큰 폭발을 일으킵니다.");
		loreList.add("§7폭발 범위 내의 적은 중심지와의 거리에 따라");
		loreList.add("§7최대 50의 피해를 줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a91초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e진염§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7모든 화염피해가 4초 더 지속됩니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(8 ,item);
		
		
		inven_desc_hider = Bukkit.createInventory(null, 9, "§0§l영웅 - 은둔자");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e은둔§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7적에게 모습을 감춥니다.");
		loreList.add("§7은신 상태에서 적을 공격할 시");
		loreList.add("§76의 추가 피해를 줍니다.");
		loreList.add("§7공격 또는 피해를 받을 시 은신이 풀립니다.");
		loreList.add("§7은신 상태에서는 신전 점령, 토템파괴가 불가능합니다.");
		loreList.add("§7은신은 최대 25초간 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e그림자 이동§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7사용시 현재 위치를 저장해두고");
		loreList.add("§7재 사용시 저장해둔 위치로 이동합니다.");
		loreList.add("§7저장해둔 위치로 이동시 은신이 해제됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a9초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e방계강세§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7매우 빠르게 5번 베어가르어");
		loreList.add("§7바라보는 대상을 공격합니다.");
		loreList.add("§7각 공격은 4만큼 피해를 줍니다.");
		loreList.add("§7[5회 공격으로 취급됩니다.]");
		loreList.add("§7[사용시 은신이 해제됩니다.]");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a38초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e암약§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7은신 상태에서 적을 타격 시");
		loreList.add("§7적을 1초간 스킬사용 불가능 상태로 만듭니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(8 ,item);
		
		
		inven_desc_virtuous = Bukkit.createInventory(null, 9, "§0§l영웅 - 선인");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e급상승§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF§f]");
		loreList.add("");
		loreList.add("§7위로 빠르게 상승합니다.");
		loreList.add("§7지면을 밟은 상태에서만 사용 가능합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a10초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e장풍§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e좌클릭 §f]");
		loreList.add("");
		loreList.add("§7바라보는 방향으로 장풍을 날려");
		loreList.add("§7착탄 지점과의 거리에 따라");
		loreList.add("§7최대 9의 피해를 줍니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a2초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e상승 기류§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7에너지를 소모하여 공중에 살짝");
		loreList.add("§7부유합니다.");
		loreList.add("§7에너지는 최대 7개 소지 가능합니다.");
		loreList.add("§7에너지는 지면을 밟고 있을시에만 충전됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a16초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e폭풍§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7전방에 매우 많은 양의 장풍을 날립니다.");
		loreList.add("§7사용중에는 이동 및 스킬사용이 불가능합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a102초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(3 ,item);
		
		/*item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e낙법§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7낙사 피해를 받지 않습니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(8 ,item);*/
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e유산무수§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7장풍의 폭발 범위가 2칸 늘어나며");
		loreList.add("§7최대 에너지 소지량이 3개 늘어납니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(8 ,item);
		
		
		inven_desc_marksman = Bukkit.createInventory(null, 9, "§0§l영웅 - 사수");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e긴급 탈출§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§73칸내 바로보는 적을 밀치며");
		loreList.add("§7자신은 뒤로 물어납니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a10초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e조준경§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 + 좌클릭 §f]");
		loreList.add("");
		loreList.add("§7시야를 확대합니다.");
		loreList.add("§7다시한번 쓸 시 되돌립니다.");
		loreList.add("§7시야 확대중에는 이동이 불가능합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a0초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e사격§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7바라보는 곳으로 총알을 발사합니다.");
		loreList.add("§7총알은 맞은 적에게 18의 피해를 줍니다.");
		loreList.add("§7사용시 총알을 소모하며");
		loreList.add("§7총알은 최대 7개 소지 가능합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a1초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e장전§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 + F §f]");
		loreList.add("");
		loreList.add("§7총알을 장전합니다.");
		loreList.add("§7초당 1발을 장전하며 쉬프트를 뗄 시 취소합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a0초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e폭발탄§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7즉시 7발의 폭발탄을 장전합니다.");
		loreList.add("§7폭발탄은 총알의 착탄 지점에 폭발을 일으켜");
		loreList.add("§7착탄지점과의 거리에 따라 최대 13의 추가 피해를 입힙니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a60초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(4 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e재빠른 손재주§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7장전 속도가 0.5초 빨라집니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(8 ,item);
		
		
		inven_desc_guardian = Bukkit.createInventory(null, 9, "§0§l영웅 - 수호자");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e방벽 전개§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7방벽을 전개합니다.");
		loreList.add("§7전개시 10칸 내의 아군이 받는 피해를 방벽이 대신");
		loreList.add("§7받으며 방벽의 내구도는 레벨바로 알 수 있습니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a0초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e위압§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f+ §e쉬프트 §f]");
		loreList.add("");
		loreList.add("§7방벽내에 있는 적을 모두");
		loreList.add("§7방벽 밖으로 밀쳐냅니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a15초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e맹돌진§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7전방으로 돌진합니다.");
		loreList.add("§7돌진선상의 있는 적을 함께 끌고가며");
		loreList.add("§7벽에 부딫히거나 돌진이 끝나면 적에게 20의 피해를 주며");
		loreList.add("§72초간 기절시킵니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a11초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e대지진§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7전방 10칸 블럭에 지진을 일으키며");
		loreList.add("§7지진에 맞은 적은 8의 피해를 받고");
		loreList.add("§73초간 기절합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a95초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e방벽 강화§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7방벽의 최대 내구도가 50 증가합니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(8 ,item);
		
		
		inven_desc_monk = Bukkit.createInventory(null, 9, "§0§l영웅 - 수도승");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e공성 방벽§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7자신에게 방벽을 사용합니다.");
		loreList.add("§7방벽은 최대 25의  피해를 막아주며");
		loreList.add("§73초간 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a13초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e공투§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§745칸내 바라보는 아군에게 방벽을 사용합니다.");
		loreList.add("§7방벽은 최대 25의  피해를 막아주며");
		loreList.add("§73초간 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a11초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e격류§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7주변 30칸 내 모든 아군에게 방벽을 부여합니다.");
		loreList.add("§7방벽은 최대 15의  피해를 막아주며");
		loreList.add("§74초간 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a81초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e효율  개선§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7방벽이 막을 수 있는 최대 피해량이");
		loreList.add("§715 증가합니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(8 ,item);
		
		
		inven_desc_knight = Bukkit.createInventory(null, 9, "§0§l영웅 - 기사");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e저돌맹진§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7전방을 향해 빠르게 뜁니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a2초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e도발§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§712칸내의 적을 자신쪽으로 끌어당기고");
		loreList.add("§7이동속도를 15% 감소시킵니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e일기 가세§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§72칸내 적에게 5의 피해를 주고");
		loreList.add("§72초간 스킬 사용불가 상태로 만듭니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a14초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e돌파§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7스킬 사용후 0.6초 뒤");
		loreList.add("§7바라보는 방향 50칸 지점을 향해 빠르게 돌진합니다.");
		loreList.add("§7돌진 선상의 적은 10의  피해를 받고");
		loreList.add("§72초간 기절합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a89초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e기사도 정신§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7저돌맹진 사용시 좀 더 멀리 뛰며");
		loreList.add("§7도발과 일기가세의 범위가 2칸 늘어납니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(8 ,item);
		
		inven_desc_fighter = Bukkit.createInventory(null, 9, "§0§l영웅 - 격투가");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e순동§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7바라보는 적의 앞으로 순식간에 이동합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a6초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e강타§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§74칸내 바라보는 적에게 6의 피해를");
		loreList.add("§7주고 1초간 구속합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a4초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e정신 통일§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§75초간 이동불가 상태가되며");
		loreList.add("§740의 체력을 서서히 회복합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a19초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e일격 필살§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§72칸내 근접한 적에게 사용시");
		loreList.add("§7해당 적을 기절시키며 26의 피해를 줍니다.");
		loreList.add("§7시전중에는 무적상태가 됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a43초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e집중력 향상§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7정신 통일 사용시 4초만에");
		loreList.add("§7체력을 40 회복하게 됩니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(8 ,item);
		
		inven_desc_monarch = Bukkit.createInventory(null, 9, "§0§l영웅 - 군주");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e쇠사슬§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7쇠사슬을 던져 맞은 적을 1초간 기절시키고");
		loreList.add("§7자신의 앞으로 끌고옵니다.");
		loreList.add("§7최대 사정거리는 25칸입니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e공간 유폭§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7집약된 바람을 날려 착탄 지점을 기점으로");
		loreList.add("§7적을 날려버립니다.");
		loreList.add("§7직격으로 맞은 적은 하늘로 띄워집니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a11초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e안전 지대§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§74초간 8칸 내의 아군이 받는 피해를");
		loreList.add("§7절반으로 하고 자신이 대신 받습니다.");
		loreList.add("§7해당 스킬 사용중에는 체력이 1이하로 내려가지");
		loreList.add("§7않습니다. (중독, 화상 피해 제외)");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a16초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e공간 융합§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7바라보는 지점의 공간을 융합합니다.");
		loreList.add("§7융합된 공간은 8칸 내 적들을 모두 끌어당기며");
		loreList.add("§74초간 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a91초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e단련된 몸§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7안전지대가 감소시키는 피해량이 절반에서");
		loreList.add("§71/3 으로 증가합니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(8 ,item);
		
		inven_desc_predator = Bukkit.createInventory(null, 9, "§0§l영웅 - 포식자");		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e철벽§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§75초간 모든 CC가  면역됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a12초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e포획§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§75칸내 바라보는 적을 2초간 포획합니다.");
		loreList.add("§7적을 포획했을 시 자신은 이동속도가 25% 증가하며");
		loreList.add("§7포획한 적을 끌고갈 수 있습니다.");
		loreList.add("§7포획된 적은 3초간  기절하고 6초간");
		loreList.add("§7이동속도가 30% 감소합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a9초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e차원의 틈§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§750칸내 바라보는 지점에 포탈을 생성합니다.");
		loreList.add("§7아군은 해당 포탈을 사용하여 지정한 지점으로");
		loreList.add("§7텔레포트 할 수 있습니다.");
		loreList.add("§7포탈은 95초간 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a100초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e영혼 포식§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7적을 처치할 시 전체체력의 절반만큼 회복합니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(8 ,item);
		
		inven_desc_priest = Bukkit.createInventory(null, 9, "§0§l영웅 - 사제");		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e치유구슬§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7치유구슬을 날립니다.");
		loreList.add("§7치유구슬에 맞은 아군은 즉시");
		loreList.add("§715 체력을 회복합니다.");
		loreList.add("§7히트 판정이 좋습니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a4초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e치유 파동§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§77칸내 아군의 체력을 즉시");
		loreList.add("§718만큼 회복시킵니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a6초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e부활의 기도§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§7리스폰 대기중인 아군을");
		loreList.add("§7마지막으로 죽은 장소에서 부활시킵니다.");
		loreList.add("§7리스폰 대기중인 플레이어의 수는");
		loreList.add("§7우측 사이드바에 표시됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a98초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e치유 증폭§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7모든 치유량이 5 증가합니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(8 ,item);
		
		inven_desc_alchemist = Bukkit.createInventory(null, 9, "§0§l영웅 - 연금술사");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e강격처분§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7공기포를 날립니다. 공기포에 맞은 적은");
		loreList.add("§7뒤로 밀려나며 4의 피해를 받고 2초간 이동속도가");
		loreList.add("§740% 감소합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e사기 고양§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§712칸내의 아군을 바라보고 사용시");
		loreList.add("§7해당 아군은 7초간 이동속도가 30% 증가하고");
		loreList.add("§7공격력이 2 증가합니다.");
		loreList.add("§7또한 체력을 40만큼 서서히 회복시킵니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a11초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e연결 장치§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§710칸내의 적을 바라보고 사용시");
		loreList.add("§72.5초에 걸쳐 5회간 적을 약간끌어옵니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a15초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e성사의 신식§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§74초간 받는 모든 피해를 절반으로하여");
		loreList.add("§7공격자에게 반사합니다.");
		loreList.add("§7독과 화염은 제외입니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a45초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e기관 장치§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7사기고양과 연결장치의 시전 범위가");
		loreList.add("§7범위가 5칸 늘어나며 초당 1의 체력을");
		loreList.add("§7회복한다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(8 ,item);
		
		inven_desc_blackMagician = Bukkit.createInventory(null, 9, "§0§l영웅 - 흑마술사");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e검은 선풍§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7검은 바람을 날립니다. 바람에 맞은 적은");
		loreList.add("§72초간 쇠약상태에 빠집니다.");
		loreList.add("§7또한 바람의 착탄 지점에 바람을 퍼뜨려");
		loreList.add("§7착탄 지점 근처의 적은 1초간 쇠약상태가 됩니다.");
		loreList.add("§7바람은 최대 30칸까지 날라갑니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e암흑구§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7암흑 덩어리를 날립니다.");
		loreList.add("§7암흑구는 착탄시 반지름 4칸의 원을");
		loreList.add("§7만들며 이 안에 있는 적은");
		loreList.add("§7쇠약상태가 지속됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a13초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e저주§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7저주의 언어를 날립니다.");
		loreList.add("§7저주에 맞은 적은 1초간 구속되며");
		loreList.add("§74초간 이동속도가 60% 감소합니다.");
		loreList.add("§7또한 적으로부터 20의 체력을 흡수합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a10초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e극도§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§77칸내 아군 및 적군을 대상으로 의식을 진행합니다.");
		loreList.add("§7의식 범위내에 있는 아군은");
		loreList.add("§7의식 범위내에 있는 적군의 수 * 10 만큼 체력을 회복하고");
		loreList.add("§75초간 의식 범위내에 있는 적군의 수만큼 공격력이 증가합니다.");
		loreList.add("§7의식 범위내에 적군이 없을시 의식은 실패합니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a75초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e흑벽§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7자신의 근처 2.5칸내 적은");
		loreList.add("§7쇠약상태가 지속됩니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(8 ,item);		
		
		
		inven_desc_warrior = Bukkit.createInventory(null, 9, "§0§l영웅 - 전사");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e전방 돌입§f(§6Prim§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7바라보는 방향으로 돌입합니다.");
		loreList.add("§7돌입한 뒤 4칸내에 있는 적에게");
		loreList.add("§76만큼 피해를 입히고");
		loreList.add("§7높이 띄웁니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a12초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(0 ,item);

		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e참격§f(§6Secd§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §e우클릭 §f]");
		loreList.add("");
		loreList.add("§7전방 짧은 거리에 참격을 날립니다.");
		loreList.add("§7참격은 거리내 적에게 모두 영향을 미치며");
		loreList.add("§7참격에 맞은 적은 7만큼 피해를 받고");
		loreList.add("§72초간 구속상태가 됩니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a8초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e방어벽 생성§f(§6Tert§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §eF §f]");
		loreList.add("");
		loreList.add("§7방어벽을 생성합니다.");
		loreList.add("§7기본 방어벽은 10이며 4칸내 적 1명당");
		loreList.add("§710만큼 추가 방어벽을 얻습니다.");
		loreList.add("§7방어벽이 남아있는 상태에서는");
		loreList.add("§7직접 받는 피해를 방어벽이 대신 받습니다.");
		loreList.add("§7방어벽은 10초간 유지되며");
		loreList.add("§7경험치바로 남은 방어벽량을 확인할 수 있습니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a24초");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e일섬 무도§f(§6Ulmt§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e쉬프트 §f+ §eQ §f]");
		loreList.add("");
		loreList.add("§75칸내 적을 2초간 쇠약상태로 만들고");
		loreList.add("§7자신의 현재 체력의 1/3 만큼의 피해를 입히며");
		loreList.add("§7높이 띄웁니다.");
		loreList.add("");
		loreList.add("§7재사용 대기시간 : §a79초");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §e강타§f(§6Pasv§f) §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§f[ §e상시 발동 §f]");
		loreList.add("");
		loreList.add("§7전방 돌입을 맞은 적은");
		loreList.add("§72초간 쇠약상태가 됩니다.");
		loreList.add("");
		loreList.add("§c구매시 사용가능");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(8 ,item);
		
		
		//////아이템
		
		item_returnBase = new ItemStack(Material.PAPER, 1);
		meta = item_returnBase.getItemMeta();
		meta.setDisplayName("§f[ §6귀환 §f]");
		loreList = new ArrayList<String>(3);
		loreList.add("");
		loreList.add("§7- 우클릭시 기지로 귀환합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_returnBase.setItemMeta(meta);
		
		item_none_stone = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_stone.getItemMeta();
		meta.setDisplayName("§f[ §c보석 없음 §f]");
		item_none_stone.setItemMeta(meta);
		
		item_none_ring = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_ring.getItemMeta();
		meta.setDisplayName("§f[ §c반지없음 §f]");
		item_none_ring.setItemMeta(meta);
		
		item_none_neck = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_neck.getItemMeta();
		meta.setDisplayName("§f[ §c목걸이 없음 §f]");
		item_none_neck.setItemMeta(meta);
		
		item_none_tailsman = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_tailsman.getItemMeta();
		meta.setDisplayName("§f[ §c부적 없음 §f]");
		item_none_tailsman.setItemMeta(meta);
		
		stone_forest = new ItemStack(Material.INK_SACK, 1, (short)1);
		meta = stone_forest.getItemMeta();
		meta.setDisplayName("§f[ §6숲의 보석 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a체력 +8%");
		loreList.add("§7- §a이동속도 +4%");
		loreList.add("");
		meta.setLore(loreList);
		stone_forest.setItemMeta(meta);
		
		stone_miracle = new ItemStack(Material.INK_SACK, 1, (short)2);
		meta = stone_miracle.getItemMeta();
		meta.setDisplayName("§f[ §6영험의 보석 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a체력 +15%");
		loreList.add("");
		meta.setLore(loreList);
		stone_miracle.setItemMeta(meta);
		
		stone_water = new ItemStack(Material.INK_SACK, 1, (short)3);
		meta = stone_water.getItemMeta();
		meta.setDisplayName("§f[ §6물의 보석 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a체력 +25%");
		loreList.add("§7- §4주는피해 -1");
		loreList.add("");
		meta.setLore(loreList);
		stone_water.setItemMeta(meta);
		
		stone_fire = new ItemStack(Material.INK_SACK, 1, (short)4);
		meta = stone_fire.getItemMeta();
		meta.setDisplayName("§f[ §6불의 보석 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a체력 +30%");
		loreList.add("§7- §4이동속도 -10%");
		loreList.add("");
		meta.setLore(loreList);
		stone_fire.setItemMeta(meta);
		
		ring_speed = new ItemStack(Material.INK_SACK, 1, (short)5);
		meta = ring_speed.getItemMeta();
		meta.setDisplayName("§f[ §6신속의 반지 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a이동속도 +4%");
		loreList.add("§7- §a체력 +6%");
		loreList.add("");
		meta.setLore(loreList);
		ring_speed.setItemMeta(meta);
		
		ring_gravity = new ItemStack(Material.INK_SACK, 1, (short)6);
		meta = ring_gravity.getItemMeta();
		meta.setDisplayName("§f[ §6중력의 반지 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a이동속도 +8%");
		loreList.add("");
		meta.setLore(loreList);
		ring_gravity.setItemMeta(meta);
		
		ring_gale = new ItemStack(Material.INK_SACK, 1, (short)7);
		meta = ring_gale.getItemMeta();
		meta.setDisplayName("§f[ §6질풍의 반지 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a이동속도 +10%");
		loreList.add("§7- §4주는피해 -1");
		loreList.add("");
		meta.setLore(loreList);
		ring_gale.setItemMeta(meta);
		
		ring_storm = new ItemStack(Material.INK_SACK, 1, (short)8);
		meta = ring_storm.getItemMeta();
		meta.setDisplayName("§f[ §6폭풍의 반지 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a이동속도 +15%");
		loreList.add("§7- §4체력 -25%");
		loreList.add("");
		meta.setLore(loreList);
		ring_storm.setItemMeta(meta);
		
		neck_iron = new ItemStack(Material.INK_SACK, 1, (short)9);
		meta = neck_iron.getItemMeta();
		meta.setDisplayName("§f[ §6철 목걸이 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a주는피해 +1");
		loreList.add("§7- §a체력 +8%");
		loreList.add("");
		meta.setLore(loreList);
		neck_iron.setItemMeta(meta);
		
		neck_gold = new ItemStack(Material.INK_SACK, 1, (short)10);
		meta = neck_gold.getItemMeta();
		meta.setDisplayName("§f[ §6금 목걸이 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a주는피해 +1");
		loreList.add("§7- §a이동속도 +8%");
		loreList.add("");
		meta.setLore(loreList);
		neck_gold.setItemMeta(meta);
		
		neck_diamond = new ItemStack(Material.INK_SACK, 1, (short)11);
		meta = neck_diamond.getItemMeta();
		meta.setDisplayName("§f[ §6다이아몬드 목걸이 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a주는피해 +2");
		loreList.add("");
		meta.setLore(loreList);
		neck_diamond.setItemMeta(meta);
		
		neck_emerald = new ItemStack(Material.INK_SACK, 1, (short)12);
		meta = neck_emerald.getItemMeta();
		meta.setDisplayName("§f[ §6에메랄드 목걸이 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §a주는피해 +3");
		loreList.add("§7- §4체력 -25%");
		loreList.add("");
		meta.setLore(loreList);
		neck_emerald.setItemMeta(meta);
		
		tailsman_miracle = new ItemStack(Material.GOLD_NUGGET, 1);
		meta = tailsman_miracle.getItemMeta();
		meta.setDisplayName("§f[ §6기적의 부적 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §7체력이 0이하로 떨어질 시");
		loreList.add("§7  §750퍼센트 확률로 사망 저지 및 ");
		loreList.add("§7  §78만큼 체력 회복");
		loreList.add("§7  §7재사용 대기시간 : 120초");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_miracle.setItemMeta(meta);
		
		tailsman_eminent = new ItemStack(Material.INK_SACK, 1, (short)14);
		meta = tailsman_eminent.getItemMeta();
		meta.setDisplayName("§f[ §6유수의 부적 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §7모든 스킬의 피해량 20% 증가");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_eminent.setItemMeta(meta);
		
		tailsman_heal = new ItemStack(Material.INK_SACK, 1);
		meta = tailsman_heal.getItemMeta();
		meta.setDisplayName("§f[ §6치유의 부적 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §7적을 처치할 시 체력 20회복");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_heal.setItemMeta(meta);
		
		tailsman_wisdom = new ItemStack(Material.CLAY_BRICK, 1);
		meta = tailsman_wisdom.getItemMeta();
		meta.setDisplayName("§f[ §6명상의 부적 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- §7궁극기 재사용 대기시간 15% 감소");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_wisdom.setItemMeta(meta);
	}

	public Team getTeamObject(Player p) {
		if(redTeam.teamList.contains(p.getName()))
			return redTeam;
		else return blueTeam;
	}
	
	public String getTeam(Player p) {

		if(redTeam.teamList.contains(p.getName())){
			return "RED";	
		} else if(blueTeam.teamList.contains(p.getName())){
			return "BLUE";
		} else {
			return "없음";
		}		
	}
	
	public String getTeam(String pName) {
		if(playerMap.containsKey(pName)){
			return playerMap.get(pName).team.teamName;
		}
		
		return "없음";
	}
	
	public List<String> getEnemyList(String pName) {
		if(getTeam(pName).equalsIgnoreCase("BLUE")) {
			return redTeam.teamList;
		} else if(getTeam(pName).equalsIgnoreCase("RED")) {
			return blueTeam.teamList;
		}
		return ingamePlayer;
	}
	
	
	public void spawn(Player p) {
		if(!ingame) return;
		p.setGameMode(GameMode.SURVIVAL);
		if(playerMap.containsKey(p.getName())) {
			playerMap.get(p.getName()).team.spawn(p);
		} else {
			gameQuitPlayer(p, true, false);
			server.spawn(p);
		}
	}
	
	
	public String getAbiilityNameFromCode(int abCode) {
		String abName = "";
		switch(abCode) {
		case 3: abName = "추적자"; break;
		case 4: abName = "자객"; break;
		case 5: abName = "사냥꾼"; break;
		case 6: abName = "용병"; break;
		case 7: abName = "궁수"; break;
		case 12: abName = "마도사"; break;
		case 13: abName = "은둔자"; break;
		case 14: abName = "선인"; break;
		case 15: abName = "저격수"; break;
		
		case 21: abName = "수호자"; break;
		case 22: abName = "수도승"; break;
		case 23: abName = "기사"; break;
		case 24: abName = "격투가"; break;
		case 25: abName = "군주"; break;
		case 30: abName = "포식자"; break;
		case 31: abName = "전사"; break;
		
		case 39: abName = "사제"; break;
		case 40: abName = "연금술사"; break;
		case 41: abName = "흑마술사"; break;
		default: abName = "???"; break;
		}
		return abName;
	}
	
	
	public void hrwHelper(Player p, int slot) {
		switch (slot) {
		case 2:
			if(!playerMap.containsKey(p.getName())) {
				Team team = getTeamObject(p);
				if(team.selectAbilityMap.containsKey(p.getName())){
					int jobCode = team.selectAbilityMap.get(p.getName());
					if(jobCode == 3) {
						p.openInventory(inven_desc_tracer);
					} else if(jobCode == 4) {
						p.openInventory(inven_desc_assasin);
					} else if(jobCode == 5) {
						p.openInventory(inven_desc_hunter);
					} else if(jobCode == 6) {
						p.openInventory(inven_desc_mercenary);
					} else if(jobCode == 7) {
						p.openInventory(inven_desc_archer);
					} else if(jobCode == 12) {
						p.openInventory(inven_desc_wizard);
					} else if(jobCode == 13) {
						p.openInventory(inven_desc_hider);
					} else if(jobCode == 14) {
						p.openInventory(inven_desc_virtuous);
					} else if(jobCode == 15) {
						p.openInventory(inven_desc_marksman);
					} else if(jobCode == 21) {
						p.openInventory(inven_desc_guardian);
					} else if(jobCode == 22) {
						p.openInventory(inven_desc_monk);
					} else if(jobCode == 23) {
						p.openInventory(inven_desc_knight);
					} else if(jobCode == 24) {
						p.openInventory(inven_desc_fighter);
					} else if(jobCode == 25) {
						p.openInventory(inven_desc_monarch);
					} else if(jobCode == 30) {
						p.openInventory(inven_desc_predator);
					} else if(jobCode == 31) {
						p.openInventory(inven_desc_warrior);
					} else if(jobCode == 39) {
						p.openInventory(inven_desc_priest);
					} else if(jobCode == 40) {
						p.openInventory(inven_desc_alchemist);
					} else if(jobCode == 41) {
						p.openInventory(inven_desc_blackMagician);
					} else {
						p.sendMessage(ms+"영웅 코드를 확인할 수 없습니다. 관리자에게 문의해주세요.");
					}
				}else {
					p.sendMessage(ms+"아직 영웅을 선택하지 않으셨습니다.");
					p.closeInventory();
				}
			}else {
				playerMap.get(p.getName()).ability.invenHelper(p);
			}
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			return;
			
		case 4:
			Team team = getTeamObject(p);
			team.openStatInventory(p);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			return;
			
		case 6:
			p.openInventory(inven_gameHelper);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			return;

		default:
			return;
		}
	}
}
