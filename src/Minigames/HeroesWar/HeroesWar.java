package Minigames.HeroesWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.MovehereCommand;
import com.mysql.jdbc.Buffer;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.HeroesWar.Abilities.Ability;
import Minigames.HeroesWar.Abilities.Alchemist;
import Minigames.HeroesWar.Abilities.Archer;
import Minigames.HeroesWar.Abilities.Assassin;
import Minigames.HeroesWar.Abilities.BlackMagician;
import Minigames.HeroesWar.Abilities.Fighter;
import Minigames.HeroesWar.Abilities.Guardian;
import Minigames.HeroesWar.Abilities.Hider;
import Minigames.HeroesWar.Abilities.Hunter;
import Minigames.HeroesWar.Abilities.Knight;
import Minigames.HeroesWar.Abilities.Marksman;
import Minigames.HeroesWar.Abilities.Mercenary;
import Minigames.HeroesWar.Abilities.Monarch;
import Minigames.HeroesWar.Abilities.Monk;
import Minigames.HeroesWar.Abilities.Predator;
import Minigames.HeroesWar.Abilities.Priest;
import Minigames.HeroesWar.Abilities.Tracer;
import Minigames.HeroesWar.Abilities.Virtuous;
import Minigames.HeroesWar.Abilities.Warrior;
import Minigames.HeroesWar.Abilities.Wizard;
import Utility.MyUtility;
import Utility.SkullCreator;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.BigBangEffect;
import de.slikey.effectlib.effect.BleedEffect;
import de.slikey.effectlib.effect.DnaEffect;
import de.slikey.effectlib.effect.EarthEffect;
import de.slikey.effectlib.effect.ExplodeEffect;
import de.slikey.effectlib.effect.FlameEffect;
import de.slikey.effectlib.effect.FountainEffect;
import de.slikey.effectlib.effect.GridEffect;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.JumpEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.LoveEffect;
import de.slikey.effectlib.effect.MusicEffect;
import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.effect.SkyRocketEffect;
import de.slikey.effectlib.effect.SmokeEffect;
import de.slikey.effectlib.effect.StarEffect;
import de.slikey.effectlib.effect.TextEffect;
import de.slikey.effectlib.effect.TraceEffect;
import de.slikey.effectlib.effect.VortexEffect;
import de.slikey.effectlib.effect.WarpEffect;
import me.confuser.barapi.BarAPI;

public class HeroesWar extends HRWBase{

	// 이벤트용
	public EventHandlerHRW event;

	// 로케이션

	
	///점령지 로케이션
	public int pointSetting = 0; //1 = point1블럭, 2= point1양털1지점 3 = point1양털2지점, 4= point1블럭
	public int baseSetting = 0;
	public Location tmpWoolPos1;
	public Location loc_buffer;
	
	// 게임 플레이어 리스트
	private List<String> teamChatList = new ArrayList<String>(40);
	public HashMap<String, HRWPlayer> reConnectMap = new HashMap<String, HRWPlayer>(30);
	private HashMap<String, EGScheduler> removeReconnectTimer = new HashMap<String, EGScheduler>(30);
	
	//////////// 스킬 관련
	public boolean skillBlock;

	//////// 게임 관련
	public int gameStep = 0; //0 시작대기  1 팀설정 2 영웅선택 3게임진행 4 게임종료
	private String locPath;
	public Material coreBlock = Material.PRISMARINE;
	public Material pointBlock = Material.EMERALD_BLOCK;
	public Location loc_ending;
	public boolean giveBonuse = false;
	
	
	public HashMap<String, ItemStack> tmpItemMap = new HashMap<String, ItemStack>(10);
	public HashMap<String, Integer> tmpItemPriceMap = new HashMap<String, Integer>(10);
	public HashMap<String, Integer> tmpItemSlotMap = new HashMap<String, Integer>(10);
	
	public Inventory inven_skillShop;
	public Inventory inven_potionShop;
	public Inventory inven_itemShop;
	public Inventory inven_itemCheck;
	
	//능력 목록
	
	public PotionEffect baseBoost = new PotionEffect(PotionEffectType.REGENERATION, 100, 2);
	
	///스케쥴
	public EGScheduler mainSch;	
	
	public HeroesWar(EGServer server, String gameName, String displayGameName, String cmdMain) {
		super(server);
		
		ItemStack item;
		ItemMeta meta;
		List<String> loreList;
		mainSch = new EGScheduler(this);
		//////////////////// 필수 설정값
	
		ms = "§7[ §e! §7] §f: §c히어로즈워 §f>> "; // 기본 메세지
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = gameName.equalsIgnoreCase("HeroesWar2") ? 18 : 10;
		maxPlayer = gameName.equalsIgnoreCase("HeroesWar2") ? 28 : 16;
		startCountTime = 70;
		this.cmdMain = cmdMain;
		dirSetting(gameName);
		
		point1 = new CatchPoint(this ,1, "§e§l엔더");
		point2 = new CatchPoint(this, 2, "§f§l중앙");
		point3 = new CatchPoint(this, 3, "§c§l네더");

		
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		
		item_hrwHelper = new ItemStack(Material.BOOK, 1);
		meta = item_hrwHelper.getItemMeta();
		meta.setDisplayName("§f[ §b게임 메뉴 §f]");
		loreList = new ArrayList<String>(1);
		loreList.add("§7- 우클릭시 히어로즈워의 게임 메뉴를 엽니다.");
		meta.setLore(loreList);
		item_hrwHelper.setItemMeta(meta);
		
		item_selectJob = new ItemStack(Material.PAPER, 1);
		meta = item_selectJob.getItemMeta();
		meta.setDisplayName("§f[ §c영웅 선택 §f]");
		loreList = new ArrayList<String>(1);
		loreList.add("§7- 우클릭시 영웅을 선택합니다.");
		meta.setLore(loreList);
		item_selectJob.setItemMeta(meta);
		
	
		
		/////////////////// 게임 도움말 인벤토리
		inven_hrwHelper = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"히어로즈워");

		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §e스킬 확인 §7-");
		item.setItemMeta(meta);
		inven_hrwHelper.setItem(2, item);		
		meta.setDisplayName("§7- §c아군 영웅 확인 §7-");
		item.setItemMeta(meta);
		inven_hrwHelper.setItem(4, item);
		meta.setDisplayName("§7- §b게임 도우미§7-");
		item.setItemMeta(meta);
		inven_hrwHelper.setItem(6, item);
		////////////////
		
		/////////////////// 게임 도우미
		inven_gameHelper = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 도우미");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			inven_gameHelper.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			inven_gameHelper.setItem(i, item);
		}
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c승리조건");
		loreList = new ArrayList<String>();
		loreList.add("§7- 승리조건을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c공물이란?");
		item.setItemMeta(meta);
		inven_gameHelper.setItem(13, item);
		item = new ItemStack(Material.BOOKSHELF, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임규칙");
		loreList = new ArrayList<String>();
		loreList.add("§7- 게임을 플레이하며");
		loreList.add("§7  지켜야하는 규칙을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(15, item);
		////////////////
	
		
		//////////////// 상점관련
		
		inven_skillShop = Bukkit.createInventory(null, 9, "§0§l스킬 상점");
	
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e87031c4726ddedd65b6a11d3147e6724defbb290da29cbb79da2490546cbf");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c패시브 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 패시브 스킬을 배웁니다. ");
		loreList.add("§7  가격: §6당근 18개. ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_skillShop.setItem(2, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e87031c4726ddedd65b6a11d3147e6724defbb290da29cbb79da2490546cbf");
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c궁극기 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 궁극기 스킬을 배웁니다. ");
		loreList.add("§7  가격: §6당근 35개. ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_skillShop.setItem(6, item);
		
		inven_potionShop = Bukkit.createInventory(null, 9, "§0§l포션 상점");
		
		item = new ItemStack(Material.POTION, 1, (short) 8197);
		//item = new ItemStack(Material.GHAST_TEAR, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c회복약(약) §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 체력을 12회복합니다. ");
		loreList.add("§7  가격: §6당근 1개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_potionShop.setItem(2 ,item);
		
		item = new ItemStack(Material.POTION, 1, (short) 8229);
		//item = new ItemStack(Material.MAGMA_CREAM, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c회복약(강) §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 체력을 25회복합니다. ");
		loreList.add("§7  가격: §6당근 2개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_potionShop.setItem(6 ,item);
		
		inven_itemShop = Bukkit.createInventory(null, 36, "§0§l아이템 상점");
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c보석 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  가격: §6당근 15개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(0 ,item);
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c반지 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  가격: §6당근 15개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(9 ,item);
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c목걸이 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  가격: §6당근 15개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(18 ,item);
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c부적 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  가격: §6당근 20개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(27 ,item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c판매 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  판매 가격: §6당근  7개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(8 ,item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c판매 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  판매 가격: §6당근  7개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(17 ,item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c판매 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  판매 가격: §6당근  7개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(26 ,item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c판매 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7  판매 가격: §6당근  10개 ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(35 ,item);
		
		inven_itemShop.setItem(3 ,stone_forest);
		inven_itemShop.setItem(4 ,stone_miracle);
		inven_itemShop.setItem(5 ,stone_water);
		inven_itemShop.setItem(6 ,stone_fire);
		
		inven_itemShop.setItem(12 ,ring_speed);
		inven_itemShop.setItem(13 ,ring_gravity);
		inven_itemShop.setItem(14 ,ring_gale);
		inven_itemShop.setItem(15 ,ring_storm);
		
		inven_itemShop.setItem(21 ,neck_iron);
		inven_itemShop.setItem(22 ,neck_gold);
		inven_itemShop.setItem(23 ,neck_diamond);
		inven_itemShop.setItem(24 ,neck_emerald);
		
		inven_itemShop.setItem(30 ,tailsman_miracle);
		inven_itemShop.setItem(31 ,tailsman_eminent);
		inven_itemShop.setItem(32 ,tailsman_heal);
		inven_itemShop.setItem(33 ,tailsman_wisdom);
		
		inven_itemCheck = Bukkit.createInventory(null, 9, "§0§l판매 확인");
		
		item = new ItemStack(Material.WOOL, 1,(short)1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §a판매 후 구매 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 동일한 아이템 종류를 구매가격의");
		loreList.add("§7  반값으로 판매 후 구매");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemCheck.setItem(2 ,item);
		
		item = new ItemStack(Material.WOOL, 1,(short)4);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c구매 취소 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 구매 취소");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemCheck.setItem(6 ,item);
		
		/////////////
		

		
		///////////
		

		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		
		//////////////////////////
		event = new EventHandlerHRW(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}

	@Override
	public void joinGame(Player p) {
		if(server.specList.containsKey(p.getName())) {
			server.spawn(p);
		}
		if(!doneSetting) {
			p.sendMessage(server.ms_alert+"해당 미니게임은 아직 관리자 기본 설정이 완료되지 않았습니다.");
		} else if(joinBlock) {
			p.sendMessage(server.ms_alert+"해당 미니게임은 관리자에 의해 현재 입장 불가 상태입니다.");
		} else if (ingamePlayer.contains(p.getName())) {
				p.sendMessage(server.ms_alert + "이미 이 게임에 참가중이십니다.");
		} else if (!server.noGameName.contains(server.playerList.get(p.getName()))) { //플레이중인 행동이 게임아님 목록에 없을경우
			p.sendMessage(server.ms_alert + "이미 다른 미니게임에 참가중 입니다.");
		} else if (ingame) {
			if(gameStep == 2 || gameStep == 3) {
				if(ingamePlayer.size() < maxPlayer) {
					if (reConnectMap.containsKey(p.getName())) {
						MyUtility.allClear(p);
						ingamePlayer.add(p.getName());
						HRWPlayer hrwp = reConnectMap.get(p.getName());
						playerMap.put(p.getName(), hrwp);
						hrwp.team.add(p);
						sendMessage(
								server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 재접속 하였습니다. ");
						hrwp.ability.reConnect();
						server.playerList.put(p.getName(), disPlayGameName);
						server.spawnList.remove(p.getName());
						server.waitingPlayer.remove(p.getName());
						EGScheduler sch = removeReconnectTimer.get(p.getName());
						if (sch != null) { // 재접속 2분 타이머 해제
							sch.cancelTask(true);
							removeReconnectTimer.remove(p.getName());
						}
					} else {
						if (reConnectMap.size() < maxPlayer) {
							breakInto(p);
							if (removeReconnectTimer.containsKey(p.getName())) {
								removeReconnectTimer.remove(p.getName());
								p.sendMessage(ms + "퇴장후 2분안에 재접속하지 않아 진행중이었던 당신의 게임정보가 삭제됐습니다.");
							}
						} else {
							p.sendMessage(ms + "아직 재접속 대기중인 플레이어가 " + (reConnectMap.size() - ingamePlayer.size())
									+ "명 있습니다.");
						}
					}		
				}else {
					p.sendMessage(server.ms_alert + "이미 최대인원입니다."); 
				}
			}else {
				p.sendMessage(server.ms_alert+"현재 난입이 불가능합니다. 잠시후 시도하세요.");
			}
		} else if (ingamePlayer.size() >= maxPlayer) {
			p.sendMessage(server.ms_alert + "이미 최대인원인 "+maxPlayer +"명의 플레이어가 참가중입니다.");
		} else {
			PlayerData pData = server.egDM.getPlayerData(p.getName());
			if(pData != null) pData.setGame(this);
			server.waitingPlayer.put(p.getName(),disPlayGameName);
			MyUtility.allClear(p);
			p.setFoodLevel(20);
			p.closeInventory();
			ingamePlayer.add(p.getName());
			server.spawnList.remove(p.getName());
			server.playerList.put(p.getName(), disPlayGameName);
			p.getInventory().setItem(8, helpItem);
			p.getInventory().setHeldItemSlot(8);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 참여했습니다. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			if(lobbyStart) {
				if(startSch.schTime > 1)
					BarAPI.setMessage(p, ChatColor.GREEN+"게임 시작까지", startSch.schTime);
			} else {
				if ((ingamePlayer.size() >= minPlayer)) {
					startCount();
				} 
			}
		}
	}
	
	@Override
	public void startGame() {
		if (ingamePlayer.size() < minPlayer-2) {
			sendMessage(ms + "게임 진행에 필요한 최소인원을 충족하지 못했습니다.\n" + ms + "게임을 종료합니다.");
			endGame(false);
			return;
			// forceEnd();
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED + "히어로즈워" + ChatColor.GRAY + "가 시작 되었습니다!");
		initGame();
		ingame = true;
		gameStep = 1;
		
		//sendMessage(ms+"게임이 시작 됐습니다.");
		sendTitle(ChatColor.RED+"게임 시작",  ChatColor.BLUE+""+disPlayGameName, 80);
		redTeam.InitAbCode(); //남은 영웅 코드 초기화
		blueTeam.InitAbCode(); ///남은 영웅 코드 초기화
		//// 팀설정 ////
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendMessage(ms + "팀 설정중입니다.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				setTeam();
			}
		}, 80l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 2;
				sendMessage(ms + "곧 전투가 시작됩니다. 종이를 우클릭하여 영웅을 선택해주세요.");
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(p == null || !p.isOnline()) continue;
					/*if(getTeam(p).equalsIgnoreCase("BLUE")) {
						p.teleport(blueTeam.loc_jobSelect);
					} else if(getTeam(p).equalsIgnoreCase("RED")) {
						p.teleport(redTeam.loc_jobSelect);
					}*/
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"§l영웅 선택", ChatColor.RED+"§l영웅을 선택해주세요");
					BarAPI.setMessage(p, ChatColor.GRAY+"전투 시작까지", 50);
					p.getInventory().setItem(0, item_selectJob);
					p.getInventory().setItem(8, item_hrwHelper);
					p.getInventory().setHeldItemSlot(0);		
					MyUtility.attackDelay(p, false);
					p.teleport(getTeamObject(p).loc_spawn, TeleportCause.PLUGIN);
					p.closeInventory();
					PlayerInventory pInv = p.getInventory();
					pInv.setItem(80, null);
					pInv.setItem(81, null);
					pInv.setItem(82, null);
					pInv.setItem(83	, null);
				}
				sendSound(Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
				basicSetting();
			}
		}, 200l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				try {
					for(int i = 0; i < ingamePlayer.size(); i++) {
						Player p = Bukkit.getPlayer(ingamePlayer.get(i));
						if(existPlayer(p)) {
							removeItem(p, Material.PAPER, 64);
							p.setGameMode(GameMode.SURVIVAL);
							p.teleport(getTeamObject(p).loc_spawn, TeleportCause.PLUGIN);
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 110, 249));
							p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 110, 249));
						}
					}	
				} catch(Exception e) {
					
				}		
			}
		}, 1220l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("§e§l5", "§c전투 시작까지", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1240l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("§e§l4", "§c전투 시작까지", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
				redTeam.loc_totem.getBlock().setType(Material.PRISMARINE);
				redTeam.loc_totem.getBlock().setData((byte)2);
				blueTeam.loc_totem.getBlock().setType(Material.PRISMARINE);
				blueTeam.loc_totem.getBlock().setData((byte)2);
			}
		}, 1260l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("§e§l3", "§c전투 시작까지", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
				
				if(gameName.equalsIgnoreCase("HeroesWar2")) {
					loc_buffer.getBlock().setType(Material.AIR);
					placeBuffer(loc_buffer, 300); //2채널만 영혼비석 사용
				}
			}
		}, 1280l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("§e§l2", "§c전투 시작까지", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1300l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("§e§l1", "§c전투 시작까지", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1320l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {	
				sendTitle("§c§l전투 시작", "", 60);
				mainSchedule();
				sendSound(Sound.ENTITY_ENDERDRAGON_AMBIENT, 1.5f, 1.5f);
				sendMessage(ms + "전투를 시작합니다! 당근을 모아 팀의 전투력을 강화하고\n"
						+ms+"점령지를 점령하여 전투에서 승리하세요!");
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.setWalkSpeed(0.25f);
					}								
				}	
				divideSetAbility();
			}
		}, 1340l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				blueTeam.putOnStatItem();
				redTeam.putOnStatItem();	
			}
		},1400l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : playerMap.keySet()) {
					Player t = Bukkit.getPlayer(pName);
					if(existPlayer(t)) {
						sendTitle("§c§l스킬 확인법!", "§e§l인벤토리의 §a§l책 우클릭 -> 스킬 설명§e§l 클릭", 120);
					}
					applyHealthBar();				
				}		
			}
		}, 1440l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : playerMap.keySet()) {
					Player t = Bukkit.getPlayer(pName);
					if(existPlayer(t)) {
						sendTitle("§c§l", "§a§l책 우클릭 -> 아군 확인§e§l에서 아군 확인 가능", 120);
					}
				}		
			}
		}, 1520l);
	}
	
	public void breakInto(Player p) {
		server.waitingPlayer.put(p.getName(),disPlayGameName);
		MyUtility.allClear(p);
		p.setFoodLevel(20);
		p.closeInventory();
		ingamePlayer.add(p.getName());
		server.spawnList.remove(p.getName());
		server.playerList.put(p.getName(), disPlayGameName);
		p.getInventory().setItem(8, helpItem);
		p.getInventory().setHeldItemSlot(8);
		sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 난입했습니다.");
		TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
		sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
		
		int redTeamSize = 0;
		int blueTeamSize = 0;
		
		if(reConnectMap.size() >= 1) {
			for(HRWPlayer hrwp : reConnectMap.values()) {
				if(hrwp.team == null) continue;
				if(hrwp.team == redTeam){
					redTeamSize++;
				}else {
					blueTeamSize++;
				}
			}
		}else {
			redTeamSize = redTeam.teamList.size();
			blueTeamSize = blueTeam.teamList.size();
		}
		
		
		if(redTeamSize < blueTeamSize) {
			setRedTeam(p);
		}else {
			setBlueTeam(p);
		}
		
		if(gameStep == 2) {
			p.sendMessage(ms + "중도참여 하셨습니다. 종이를 우클릭하여 영웅을 선택해주세요.\n"+ms+"곧 게임이 시작됩니다.");
			TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"§l영웅 선택", ChatColor.RED+"§l영웅을 선택해주세요");
			p.getInventory().setItem(0, item_selectJob);
			p.getInventory().setItem(8, item_hrwHelper);
			p.getInventory().setHeldItemSlot(0);		
			MyUtility.attackDelay(p, false);
			p.teleport(getTeamObject(p).loc_jobSelect, TeleportCause.PLUGIN);
			p.closeInventory();
			PlayerInventory pInv = p.getInventory();
			pInv.setItem(80, null);
			pInv.setItem(81, null);
			pInv.setItem(82, null);
			pInv.setItem(83	, null);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
		}else if(gameStep == 3){
			p.sendMessage(ms + "중도참여 하셨습니다. 종이를 우클릭하여 영웅을 선택해주세요.");
			TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"§l영웅 선택", ChatColor.RED+"§l영웅을 선택해주세요");
			p.getInventory().setItem(0, item_selectJob);
			p.getInventory().setItem(8, item_hrwHelper);
			p.getInventory().setHeldItemSlot(0);		
			MyUtility.attackDelay(p, false);
			p.teleport(getTeamObject(p).loc_jobSelect, TeleportCause.PLUGIN);
			p.closeInventory();
			PlayerInventory pInv = p.getInventory();
			pInv.setItem(80, null);
			pInv.setItem(81, null);
			pInv.setItem(82, null);
			pInv.setItem(83	, null);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
		}
	}
	
	@Override
	public void gameQuitPlayer(Player p, boolean annouce, boolean isDead) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			blueTeam.teamList.remove(p.getName());
			redTeam.teamList.remove(p.getName());
			server.playerList.put(p.getName(), "로비");
			if (ingame && gameStep == 3) {
				if(playerMap.containsKey(p.getName())) {
					Ability ab = playerMap.get(p.getName()).ability;
					ab.gameQuit();
					ab.respawnSch.cancelTask(true);
					ab.team.respawning.remove(p.getName());
				}		
				if(blueTeam.teamList.size() <= 0) {
					server.egCM.broadCast(server.ms_alert+"히어로즈워 게임의 블루팀에 플레이어가 존재하지 않아. 종료되었습니다.");
					endGame(false);
				} else if(redTeam.teamList.size() <= 0) {
					server.egCM.broadCast(server.ms_alert+"히어로즈워 게임의 레드팀에 플레이어가 존재하지 않아. 종료되었습니다.");
					endGame(false);
				} 
			} else {
				ingamePlayer.remove(p.getName());
				if(annouce) {
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장 했습니다. "
							+ ChatColor.RESET + "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
					sendSound(Sound.BLOCK_CLOTH_BREAK,1.5f, 1.5f);
				}		
				if(ingamePlayer.size() <= 0) {
					if(lobbyStart) {
						lobbyStart = false;
						Bukkit.getScheduler().cancelTask(startSch.schId);
					}				
				}
			}
			playerMap.remove(p.getName());
			if(reConnectMap.containsKey(p.getName())) {
				EGScheduler sch = new EGScheduler(this);
				sch.schId = Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						if(!ingamePlayer.contains(p.getName())) {
							HRWPlayer hrwp = reConnectMap.get(p.getName());
							if(hrwp != null) {
								reConnectMap.remove(p.getName());
								Team team = hrwp.team;
								if(team.selectAbilityMap.containsKey(p.getName())) {
									int tmpCode = team.selectAbilityMap.get(p.getName());
									team.leftAbCode.add(tmpCode);
									team.selectAbilityMap.remove(p.getName());
									team.jobList.getItem(tmpCode).removeEnchantment(Enchantment.DURABILITY);
								} 
								hrwp.team.reduceAbilityList.remove(hrwp.ability);
							}		
							sch.cancelTask(true);	
						}
					}
				}, 1200l);
				removeReconnectTimer.put(p.getName(), sch);
			}else {
				Team team = getTeamObject(p);
				if(team.selectAbilityMap.containsKey(p.getName())) {
					int tmpCode = team.selectAbilityMap.get(p.getName());
					team.leftAbCode.add(tmpCode);
					team.selectAbilityMap.remove(p.getName());
					team.jobList.getItem(tmpCode).removeEnchantment(Enchantment.DURABILITY);
				} 
			}		
		}
	}
	
	//////////////////

	public void initGame() {
		point1.paintWoolBlocks((byte)0);
		point2.paintWoolBlocks((byte)0);
		point3.paintWoolBlocks((byte)0);
		
		point1.catchTeam = "중립";	
		point2.catchTeam = "중립";
		point3.catchTeam = "중립";
		
		point1.catchingPlayer = "";
		point2.catchingPlayer = "";
		point3.catchingPlayer = "";
		
		redTeam.leftSoul = redTeam.maxLeftSoul;
		blueTeam.leftSoul = blueTeam.maxLeftSoul;
		if(gameName.equalsIgnoreCase("HeroesWar2")) {
			redTeam.leftSoul += 100;
			blueTeam.leftSoul += 100;
		}
		playerMap.clear();
		teamChatList.clear();
		gameStep = 0;
		blueTeam.teamList.clear();
		redTeam.teamList.clear();	
		blueTeam.selectAbilityMap.clear();
		redTeam.selectAbilityMap.clear();
		blueTeam.carrotCnt = 0;
		redTeam.carrotCnt = 0;
		reConnectMap.clear();
		giveBonuse = false;
		redTeam.statList.clear();
		blueTeam.statList.clear();
		redTeam.respawning.clear();
		blueTeam.respawning.clear();
		redTeam.reduceAbilityList.clear();
		blueTeam.reduceAbilityList.clear();
		
		clearClickMap();
		
		redTeam.slotNumMap.clear();
		blueTeam.slotNumMap.clear();
		
		for(ItemStack item : redTeam.jobList) {
			if(item != null) {
				if(item.getType() != Material.AIR) {
					item.removeEnchantment(Enchantment.DURABILITY)	;
				}
			}
		}
		for(ItemStack item : blueTeam.jobList) {
			if(item != null) {
				if(item.getType() != Material.AIR) {
					item.removeEnchantment(Enchantment.DURABILITY)	;
				}
			}
		}
	}

	public int getBreakIntoSize() {
		if(gameStep == 3) {
			return maxPlayer - reConnectMap.size();	
		}else {
			return maxPlayer - ingamePlayer.size();
		}
		
	}
	
	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		blueTeam.loc_jobSelect = loadLocation(gameName, "blueJobSelect");
		redTeam.loc_jobSelect = loadLocation(gameName, "redJobSelect");
		blueTeam.loc_spawn = loadLocation(gameName, "blueSpawn");
		redTeam.loc_spawn = loadLocation(gameName, "redSpawn");
		blueTeam.loc_totem = loadLocation(gameName, "blueTotem");
		redTeam.loc_totem = loadLocation(gameName, "redTotem");
		blueTeam.loc_base1 = loadLocation(gameName, "blueBase1");
		blueTeam.loc_base2 = loadLocation(gameName, "blueBase2");
		redTeam.loc_base1 = loadLocation(gameName, "redBase1");
		redTeam.loc_base2 = loadLocation(gameName, "redBase2");
		loc_buffer = loadLocation(gameName, "buffer");
		if (loc_Join == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (loc_buffer == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 버퍼블럭이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.loc_jobSelect == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 영웅 선택지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (redTeam.loc_jobSelect == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 영웅 선택지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.loc_spawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 스폰지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (redTeam.loc_spawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 스폰지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.loc_base1 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 기지1번지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.loc_base1 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 기지1번지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (redTeam.loc_base1 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 기지1번지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (redTeam.loc_base2 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 기지2번지점이 설정되지 않았습니다.");
			ret = false;
		}
		blueTeam.sortBaseLoc();
		redTeam.sortBaseLoc();
		if(!point1.loadData(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml")) {
			server.egPM.printLog("[" + disPlayGameName + "] 1번 점령지가 설정되지 않았습니다.");
			ret = false;
		}
		if(!point2.loadData(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml")) {
			server.egPM.printLog("[" + disPlayGameName + "] 2번 점령지가 설정되지 않았습니다.");
			ret = false;
		}		
		if(!point3.loadData(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml")) {
			server.egPM.printLog("[" + disPlayGameName + "] 3번 점령지가 설정되지 않았습니다.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
			
		}
		
		return ret;
	}
	
	/////////// 인벤토리 클릭 ////////////////
	
	public void gameHelper(Player p, int slot) {
		switch (slot) {
		case 11:
			p.sendMessage("§7게임 이름 §f: §c히어로즈워");
			p.sendMessage("§f각각의 영웅을 선택하여 승리조건을 달성하는");
			p.sendMessage("§f팀, 협동, 경쟁게임입니다.");
			p.sendMessage("§f게임은 레드팀, 블루팀 두 진영으로 나뉘어 진행되며");
			p.sendMessage("§f결과적으로 상대방의 영혼의 개수를 0으로 만들면 승리합니다.");
			p.sendMessage("§f상대의 영혼을 없애기 위해서는 맵에 존재하는");
			p.sendMessage("§f3개의 신전을 점령해야합니다.");
			p.closeInventory();
			return;
			
		case 13:
			p.sendMessage("§f팀이 포탑을 점령하기 위해서는 제단에 공물을 바쳐야합니다.");
			p.sendMessage("§f공물은 황금당근을 뜻하며 황금 당근은 팀이 적을 처치하거나");
			p.sendMessage("§f팀이 적팀의 토템을 부쉈을 때 얻을 수 있습니다.");
			p.sendMessage("§f또한 공물은 스킬 구매, 아이템, 포션등 상점을");
			p.sendMessage("§f이용할 시 화폐로서 사용됩니다.");
			p.sendMessage("§c※ '/tc' 명령어 입력시 팀 채팅으로 전환합니다.");
			p.closeInventory();
			return;
			
		case 15:
			p.sendMessage("§f고의적인 아군방해는 금지입니다.");
			p.closeInventory();
			return;

		default:
			return;
		}
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
					}else {
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

	/////////////////////////////////
	
	public void mainSchedule() {
		
		mainSch.schTime = 0;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!ingame) mainSch.cancelTask(true);
				
				List<String> keySet = new ArrayList<String>(playerMap.keySet());
				
				if(mainSch.schTime < 0) {
					mainSch.schTime = 2;
					int blueOccupy = 0;
					int redOccupy = 0;
					
					if(point1.catchTeam.equalsIgnoreCase("BLUE")) {
						blueOccupy += 1;
					} else if(point1.catchTeam.equalsIgnoreCase("RED")) {
						redOccupy += 1;
					}
					
					if(point2.catchTeam.equalsIgnoreCase("BLUE")) {
						blueOccupy += 1;
					} else if(point2.catchTeam.equalsIgnoreCase("RED")) {
						redOccupy += 1;
					}
					
					if(point3.catchTeam.equalsIgnoreCase("BLUE")) {
						blueOccupy += 1;
					} else if(point3.catchTeam.equalsIgnoreCase("RED")) {
						redOccupy += 1;
					}
					
					blueTeam.leftSoul -= redOccupy;
					redTeam.leftSoul -= blueOccupy;
					
					if(!giveBonuse) {
						if(blueTeam.leftSoul - redTeam.leftSoul > 300) {
							giveBonuse = true;
							giveCarrot(redTeam.teamList, "§b§l적과의 영혼차가 300개 이상으므로 당근 + 35", 35);
							for(String tName : redTeam.teamList) {
								Player t = Bukkit.getPlayer(tName);
								if(existPlayer(t)) TitleAPI.sendFullTitle(t, 10, 70, 10, "§b§l지원금 지급됨", "§e§l적과의 영혼차가 300개 이상으므로 당근 + 35");
							}
						}else if(redTeam.leftSoul - blueTeam.leftSoul > 300) {
							giveBonuse = true;
							giveCarrot(blueTeam.teamList, "§b§l적과의 영혼차가 300개 이상으므로 당근 + 35", 35);
							for(String tName : blueTeam.teamList) {
								Player t = Bukkit.getPlayer(tName);
								if(existPlayer(t)) TitleAPI.sendFullTitle(t, 10, 70, 10, "§b§l지원금 지급됨", "§e§l적과의 영혼차가 300개 이상으므로 당근 + 35");
							}
						}
					}				
					
					if(blueTeam.leftSoul <= 0) {						
						mainSch.cancelTask(true);
						redTeamWin();					
					} else if(redTeam.leftSoul <= 0) {
						mainSch.cancelTask(true);
						blueTeamWin();
					}
					
					for(String pName : keySet) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p))
							setBar(p);
					}
				} else {
					mainSch.schTime -= 1;
				}
				for(String pName : keySet) {
					Ability ab = playerMap.get(pName).ability;
					ab.setUI();
					if(ab.percentMode) {
						if(!ab.blockPerUlti) ab.per_Ultimate_Now += 1;	
					}
				}			
			}
		}, 0l, 5l);
		
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length > 1) {
			if (cmd[1].equalsIgnoreCase("set")) {
				if (cmd.length > 2) {
					if (cmd[2].equalsIgnoreCase("loc")) {
						if (cmd.length > 3) {
							if (cmd[3].equalsIgnoreCase("join")) {
								saveLocation(gameName, "JoinPos", p.getLocation());
								loadGameData();
								p.sendMessage("[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
							}  else if (cmd[3].equalsIgnoreCase("bluejob")) {
								saveLocation(gameName, "blueJobSelect", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 영웅선택지점 설정완료");
							} else if (cmd[3].equalsIgnoreCase("redjob")) {
								saveLocation(gameName, "redJobSelect", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 영웅선택지점 설정완료");
							} else if (cmd[3].equalsIgnoreCase("blueSpawn")) {
								saveLocation(gameName, "blueSpawn", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 스폰지점 설정완료");
							} else if (cmd[3].equalsIgnoreCase("redSpawn")) {
								saveLocation(gameName, "redSpawn", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 스폰지점 설정완료");
							} else if (cmd[3].equalsIgnoreCase("blueTotem")) {
								saveLocation(gameName, "blueTotem", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 토템 설정완료");
							} else if (cmd[3].equalsIgnoreCase("redTotem")) {
								saveLocation(gameName, "redTotem", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 토템 설정완료");
							} else if (cmd[3].equalsIgnoreCase("buffer")) {
								saveLocation(gameName, "buffer", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "버퍼블럭 설정완료");
							} else if(cmd[3].equalsIgnoreCase("point1")) {
								pointSetting = 1;
								p.sendMessage("[" + disPlayGameName + "] " + "점령지로 사용할 블럭 선택해주세요.");
							} else if(cmd[3].equalsIgnoreCase("point2")) {
								pointSetting = 4;
								p.sendMessage("[" + disPlayGameName + "] " + "점령지로 사용할 블럭 선택해주세요.");
							} else if(cmd[3].equalsIgnoreCase("point3")) {
								pointSetting = 7;
								p.sendMessage("[" + disPlayGameName + "] " + "점령지로 사용할 블럭 선택해주세요.");
							} else if(cmd[3].equalsIgnoreCase("bluebase")) {
								baseSetting = 1;
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 기지 1번지점을 선택해주세요.");
							} else if(cmd[3].equalsIgnoreCase("redbase")) {
								baseSetting = 3;
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 기지 1번지점을 선택해주세요.");
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"은 올바르지 않은 인수");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"인수를 입력해주세요.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc join - 게임 시작 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluejob - 블루팀 영웅선택지점");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redjob - 레드팀 영웅선택 지점");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluespawn - 블루팀 스폰");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redspawn - 레드팀 스폰");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluetotem - 블루팀 토템");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redtotem - 레드팀 토템");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluebase - 블루팀 기지");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redbase - 레드팀 기지");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc buffer - 버프 블럭");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc- 게임 지점 설정");
				}
			} else if (cmd[1].equalsIgnoreCase("join")) {
				if(cmd.length > 2) joinGame(Bukkit.getPlayer(cmd[2]));
				else joinGame(p);
			} else if (cmd[1].equalsIgnoreCase("forceend")) {
				endGame(true);
			}else if (cmd[1].equalsIgnoreCase("quit")) {
				gameQuitPlayer(p, true, false);
			} else if (cmd[1].equalsIgnoreCase("block")) {
				if(joinBlock) joinBlock = false;
				else joinBlock = true;
			} else if (cmd[1].equalsIgnoreCase("start")) {
				startCount();
			} else if (cmd[1].equalsIgnoreCase("debug0")) {
				point2.paintWoolBlocks(Byte.valueOf(cmd[2]));
			}else if (cmd[1].equalsIgnoreCase("debug1")) {
				offerToAltar(p, 1000);
			} else if (cmd[1].equalsIgnoreCase("debug2")) {
				playerMap.remove(p.getName());
				blueTeam.teamList.remove(p.getName());
				ingamePlayer.remove(p.getName());
				setBlueTeam(p);
				ingame = true;
				gameStep = 3;
				ingamePlayer.add(p.getName());
				server.playerList.put(p.getName(), "테스트");
				server.spawnList.remove(p.getName());
				p.sendMessage("설정완료");
				if(cmd[2].equalsIgnoreCase("1")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "추적자", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("2")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "자객", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("3")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "사냥꾼", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("4")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "용병", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("5")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "궁수", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("6")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "마도사", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("7")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "은둔자", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("8")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "선인", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("9")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "사수", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("10")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "수호자", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("11")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "수도승", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("12")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "기사", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("13")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "격투가", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("14")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "군주", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("15")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "포식자", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("16")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "사제", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("17")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "연금술사", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("18")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new BlackMagician(this, p, "흑마술사", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("19")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Warrior(this, p, "전사", blueTeam), blueTeam));
				} 
				playerMap.get(p.getName()).ability.ultimate = true;
				playerMap.get(p.getName()).ability.passive = true;
			} else if (cmd[1].equalsIgnoreCase("debug3")) {
				playerMap.remove(p.getName());
				redTeam.teamList.remove(p.getName());
				ingamePlayer.remove(p.getName());
				setRedTeam(p);
				ingame = true;
				gameStep = 3;
				ingamePlayer.add(p.getName());
				server.playerList.put(p.getName(), "테스트");
				server.spawnList.remove(p.getName());
				p.sendMessage("설정완료");
				if(cmd[2].equalsIgnoreCase("1")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "추적자", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("2")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "자객", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("3")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "사냥꾼", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("4")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "용병", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("5")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "궁수", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("6")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "마도사", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("7")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "은둔자", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("8")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "선인", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("9")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "사수", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("10")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "수호자", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("11")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "수도승", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("12")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "기사", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("13")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "격투가", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("14")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "군주", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("15")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "포식자", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("16")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "사제", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("17")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "연금술사", redTeam), redTeam));
				} 
				playerMap.get(p.getName()).ability.ultimate = true;
				playerMap.get(p.getName()).ability.passive = true;
			}else if (cmd[1].equalsIgnoreCase("debug4")) {
				ingame = true;
				mainSchedule();
			}else if (cmd[1].equalsIgnoreCase("debug5")) {
				p.sendMessage(""+loc_buffer);
				placeBuffer(loc_buffer, 1);
			}else if (cmd[1].equalsIgnoreCase("debug6")) {
				applyHealthBar();
			}else if(cmd[1].equalsIgnoreCase("debug7")) {
				 p.sendMessage(playerMap.get(p.getName()).ability.checkWeapon(p.getInventory().getContents())+"");
			}else if(cmd[1].equalsIgnoreCase("debug8")) {
				playerMap.get(p.getName()).ability.updateStatItem();
				p.sendMessage(playerMap.get(p.getName()).ability.statLoreList.size()+"");
				 p.sendMessage(playerMap.get(p.getName()).ability.statLoreList+"");
				 p.getInventory().addItem(playerMap.get(p.getName()).ability.statItem);
			}else if(cmd[1].equalsIgnoreCase("debug9")) {
				Ability ab =playerMap.get(p.getName()).ability;
				ab.percentMode = true;
			}else if(cmd[1].equalsIgnoreCase("debug10")) {
				Runtime runtime = Runtime.getRuntime();
				System.gc();
				long before = ((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
				p.sendMessage("현재 메모리 : §b"+before+" MB\n");
				for(int i = 0; i < Integer.valueOf(cmd[2]); i++) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "추적자", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "자객", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "사냥꾼", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "용병", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "궁수", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "마도사", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "은둔자", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "선인", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "사수", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "수호자", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "수도승", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "기사", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "격투가", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "군주", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "포식자", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "사제", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "연금술사", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new BlackMagician(this, p, "흑마술사", blueTeam), blueTeam));
					playerMap.put(p.getName(), new HRWPlayer(p, new Warrior(this, p, "전사", blueTeam), blueTeam));
				}
				mainSchedule();
				redTeam.putOnStatItem();
				blueTeam.putOnStatItem();
				applyHealthBar();
				long after = ((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
				p.sendMessage("데이터 생성후 메모리 : §b"+after+" MB\n");
				initGame();
				System.gc();
				long last = ((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
				p.sendMessage("GC후 메모리 : §b"+last+" MB\n");
			}
		}  else {
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" join - 게임 참가");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" quit - 게임 퇴장");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set - 게임 설정");
		}
	}
		
		  public void writePentagon(Location loc, Location loc2, Location loc3, Location loc4, Location loc5)
		  {
		    EffectManager eManager = new EffectManager(server);

		    LineEffect effect = new LineEffect(eManager);
		    LineEffect effect1 = new LineEffect(eManager);
		    LineEffect effect2 = new LineEffect(eManager);
		    LineEffect effect3 = new LineEffect(eManager);
		    LineEffect effect4 = new LineEffect(eManager);
		    LineEffect effect5 = new LineEffect(eManager);
		    LineEffect effect6 = new LineEffect(eManager);
		    LineEffect effect7 = new LineEffect(eManager);
		    LineEffect effect8 = new LineEffect(eManager);
		    LineEffect effect9 = new LineEffect(eManager);

		    effect.setLocation(loc);
		    effect.setTargetLocation(loc2);
		    effect1.setLocation(loc);
		    effect1.setTargetLocation(loc3);
		    effect2.setLocation(loc);
		    effect2.setTargetLocation(loc4);
		    effect3.setLocation(loc);
		    effect3.setTargetLocation(loc5);
		    effect4.setLocation(loc2);
		    effect4.setTargetLocation(loc3);
		    effect5.setLocation(loc2);
		    effect5.setTargetLocation(loc4);
		    effect6.setLocation(loc2);
		    effect6.setTargetLocation(loc5);
		    effect7.setLocation(loc3);
		    effect7.setTargetLocation(loc4);
		    effect8.setLocation(loc3);
		    effect8.setTargetLocation(loc5);
		    effect9.setLocation(loc4);
		    effect9.setTargetLocation(loc5);

		    effect.particle = Particle.REDSTONE;
		    effect1.particle = Particle.REDSTONE;
		    effect2.particle = Particle.REDSTONE;
		    effect3.particle = Particle.REDSTONE;
		    effect4.particle = Particle.REDSTONE;
		    effect5.particle = Particle.REDSTONE;
		    effect6.particle = Particle.REDSTONE;
		    effect7.particle = Particle.REDSTONE;
		    effect8.particle = Particle.REDSTONE;
		    effect9.particle = Particle.REDSTONE;

		    effect.start();
		    effect1.start();
		    effect2.start();
		    effect3.start();
		    effect4.start();
		    effect5.start();
		    effect6.start();
		    effect7.start();
		    effect8.start();
		    effect9.start();
		  }
	
	public void basicSetting() {
		blueTeam.setCarrot();
		redTeam.setCarrot();
	}
	
	public void updateScoreboard() {
		
	}
	
	public void giveCarrot(List<String> list, String reason, int amt) {
		for(String pName : list) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT,amt));
				if(reason != null) 
					ActionBarAPI.sendActionBar(p, "§c§l"+reason, 80);				
			}
		}
	}

	public void setTeam() {
		List<String> tmpList = new ArrayList<String>(ingamePlayer);
		MyUtility.mixList(tmpList);
		for(int i = 0; i < tmpList.size(); i++){
			Player p = Bukkit.getPlayer(ingamePlayer.get(i));
			if(existPlayer(p)) {
				MyUtility.allClear(p);
				if(blueTeam.teamList.size() <= redTeam.teamList.size()){
					setBlueTeam(p);
				} else {
					setRedTeam(p);
				}
			}
		}
	}
	
	public void setBlueTeam(Player p) {
		blueTeam.add(p);

		//backuptlist.put(p.getName(), "블루");
	}
	
	public void setRedTeam(Player p) {
		redTeam.add(p);
		//backuptlist.put(p.getName(), "블루");
	}	
	
	public void setBar(Player p) {
		String team = getTeam(p);
		int bluePercent = (int)(((float) blueTeam.leftSoul/blueTeam.maxLeftSoul)*100) < 2 ? (int)(((float) blueTeam.leftSoul/blueTeam.maxLeftSoul)*100) : 2;
		int redPercent = (int)(((float) redTeam.leftSoul/redTeam.maxLeftSoul)*100) < 2 ? (int)(((float) redTeam.leftSoul/redTeam.maxLeftSoul)*100) : 2;
		try{BarAPI.setMessage(p, ChatColor.BLUE+"블루팀 영혼 §7- §6"+blueTeam.leftSoul+" §f| "+ChatColor.RED+"레드팀 영혼 §7- §6 "+redTeam.leftSoul,
				(team.equalsIgnoreCase("BLUE") ? bluePercent : redPercent));
		}catch(Exception e) {
			
		}
	}
	
	////////////인벤토리 클릭 관련
	
	
	
	public void abilitySelect(Player p, int slotNum) {
		if(abCodeList.contains(slotNum)) {
			p.closeInventory();
			selectAb(p, slotNum);
		}
	}
	
	public void selectAb(Player p, int slotNum) {
		Team team = getTeamObject(p);
		if(team.leftAbCode.contains(slotNum)) {
			if(team.selectAbilityMap.containsKey(p.getName())) {
				int tmpCode = team.selectAbilityMap.get(p.getName());
				team.leftAbCode.add(tmpCode);
				team.selectAbilityMap.remove(p.getName());
				team.jobList.getItem(tmpCode).removeEnchantment(Enchantment.DURABILITY);
			} 
			team.leftAbCode.remove(Integer.valueOf(slotNum));
			team.selectAbilityMap.put(p.getName(), slotNum);
			TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l"+getAbiilityNameFromCode(slotNum) + " 선택 완료");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0F, 2.0F);
			team.jobList.getItem(slotNum).addUnsafeEnchantment(Enchantment.DURABILITY, 1);	
			if(gameStep == 3) {
				setAbility(p);
				p.teleport(getTeamObject(p).loc_spawn, TeleportCause.PLUGIN);
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						Ability ab = playerMap.get(p.getName()).ability;
						ab.applyItems();
						Objective tmpO = ab.sidebar.getTheScoreboard().getObjective("health");
						if(tmpO != null) {
							tmpO.unregister();
						}
						Objective o = ab.sidebar.getTheScoreboard().registerNewObjective("health", "health");
						o.setDisplayName("§cHP");
						o.setDisplaySlot(DisplaySlot.BELOW_NAME);
						team.putOnStatItem();
					}
				}, 20);
			}
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 2.0F);
			TitleAPI.sendFullTitle(p, 10, 60, 10, "", ChatColor.RED+"이미 선택된 영웅입니다.");
		}
	}
	
	///////////상점 관련/////
	
	public void skillShopClick(Player p, int slotNum) {
		if(!playerMap.containsKey(p.getName())) return;
		if(slotNum == 2) {
			Ability ab = playerMap.get(p.getName()).ability;
			if(ab.passive) {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l이미 패시브를 배웠습니다.");
				return;
			}
			if(takeItem(p, Material.GOLDEN_CARROT, 18)) {
				ab.passive = true;
				if(ab instanceof Assassin) p.setAllowFlight(true);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l패시브를 배웠습니다.");
				p.closeInventory();
			} else {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다..");
			}
			
		} else if(slotNum == 6) {
			Ability ab = playerMap.get(p.getName()).ability;
			if(ab.ultimate) {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l이미 궁극기를 배웠습니다.");
				return;
			}
			if(takeItem(p, Material.GOLDEN_CARROT, 35)) {
				ab.ultimate = true;
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l궁극기를 배웠습니다.");
				p.closeInventory();		
			} else {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
			}	
		}
	}
	
	public void potionShopClick(Player p, int slotNum) {
		if(!playerMap.containsKey(p.getName())) return;
		if(slotNum == 2) {
			if(takeItem(p, Material.GOLDEN_CARROT, 1)) {
				ItemStack item = new ItemStack(Material.POTION, 1, (short)8197);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§7- §c회복약(약) §7-");
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			} else {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
			}
			
		} else if(slotNum == 6) {
			if(takeItem(p, Material.GOLDEN_CARROT, 2)) {
				ItemStack item = new ItemStack(Material.POTION, 1, (short)8229);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§7- §c회복약(강) §7-");
				item.setItemMeta(meta);
				p.getInventory().addItem(item);
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			} else {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
			}	
		}
	}
	
	public void itemShopClick(Player p, int slotNum) {
		if(!playerMap.containsKey(p.getName())) return;
		HRWPlayer hrwp = playerMap.get(p.getName());
		if(slotNum == 3 || slotNum == 4 || slotNum == 5 || slotNum == 6) {
			if(p.getInventory().getItem(4) != null) {
				if(p.getInventory().getItem(4).getType() != Material.AIR && p.getInventory().getItem(4).getType() != Material.STAINED_GLASS_PANE) {
					ItemStack item = inven_itemShop.getItem(slotNum);
					tmpItemMap.put(p.getName(), item);
					tmpItemPriceMap.put(p.getName(), 15);
					tmpItemSlotMap.put(p.getName(), 4);
					p.openInventory(inven_itemCheck);
				} else {
					if(takeItem(p, Material.GOLDEN_CARROT, 15)) {
						ItemStack item = inven_itemShop.getItem(slotNum);
						p.getInventory().setItem(4,item);
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
						hrwp.item_stone = item;
						hrwp.ability.applyItems();
					} else {
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
					}
				}
			}			
		} else if(slotNum == 12 || slotNum == 13 || slotNum == 14 || slotNum == 15) {
			if(p.getInventory().getItem(5) != null) {
				if(p.getInventory().getItem(5).getType() != Material.AIR && p.getInventory().getItem(5).getType() != Material.STAINED_GLASS_PANE) {
					ItemStack item = inven_itemShop.getItem(slotNum);
					tmpItemMap.put(p.getName(), item);
					tmpItemPriceMap.put(p.getName(), 15);
					tmpItemSlotMap.put(p.getName(), 5);
					p.openInventory(inven_itemCheck);
				} else {
					if(takeItem(p, Material.GOLDEN_CARROT, 15)) {
						ItemStack item = inven_itemShop.getItem(slotNum);
						p.getInventory().setItem(5,item);
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
						hrwp.item_ring = item;
						hrwp.ability.applyItems();
					} else {
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
					}
				}
			}		
		} else if(slotNum == 21 || slotNum == 22 || slotNum == 23 || slotNum == 24) {
			if(p.getInventory().getItem(6) != null){
				if(p.getInventory().getItem(6).getType() != Material.AIR && p.getInventory().getItem(6).getType() != Material.STAINED_GLASS_PANE) {
					ItemStack item = inven_itemShop.getItem(slotNum);
					tmpItemMap.put(p.getName(), item);
					tmpItemPriceMap.put(p.getName(), 15);
					tmpItemSlotMap.put(p.getName(), 6);
					p.openInventory(inven_itemCheck);
				} else {
					if(takeItem(p, Material.GOLDEN_CARROT, 15)) {
						ItemStack item = inven_itemShop.getItem(slotNum);
						p.getInventory().setItem(6,item);
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
						hrwp.item_neck = item;
						hrwp.ability.applyItems();
					} else {
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
					}
				}
			}		
		} else if(slotNum == 30 || slotNum == 31 || slotNum == 32 || slotNum == 33) {
			ItemStack itemTmp = p.getInventory().getItem(7);
			if(itemTmp != null) {
				if(itemTmp.getType() != Material.AIR && p.getInventory().getItem(7).getType() != Material.STAINED_GLASS_PANE) {
					ItemStack item = inven_itemShop.getItem(slotNum);
					tmpItemMap.put(p.getName(), item);
					tmpItemPriceMap.put(p.getName(), 20);
					tmpItemSlotMap.put(p.getName(), 7);
					p.openInventory(inven_itemCheck);
				} else {
					if(takeItem(p, Material.GOLDEN_CARROT, 20)) {
						ItemStack item = inven_itemShop.getItem(slotNum);
						p.getInventory().setItem(7,item);
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
						hrwp.item_tailsman = item;
						hrwp.ability.applyItems();
					} else {
						p.closeInventory();
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
					}
				}
			}	
		} else if(slotNum == 8) {
			ItemStack itemTmp = p.getInventory().getItem(4);
			if(itemTmp != null) {
				if(itemTmp.getType() != Material.AIR && itemTmp.getType() != Material.STAINED_GLASS_PANE) {
					p.getInventory().setItem(4, item_none_stone);
					p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 7));
					TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l판매 완료");
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					hrwp.item_stone = null;
					hrwp.ability.applyItems();
				}
			}		
		}  else if(slotNum == 17) {
			ItemStack itemTmp = p.getInventory().getItem(5);
			if(itemTmp != null) {
				if(itemTmp.getType() != Material.AIR && itemTmp.getType() != Material.STAINED_GLASS_PANE) {
					p.getInventory().setItem(5, item_none_ring);
					p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 7));
					TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l판매 완료");
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					hrwp.item_ring = null;
					hrwp.ability.applyItems();
				}
			}		
		}  else if(slotNum == 26) {
			ItemStack itemTmp = p.getInventory().getItem(6);
			if(itemTmp != null) {
				if(itemTmp.getType() != Material.AIR && itemTmp.getType() != Material.STAINED_GLASS_PANE) {
					p.getInventory().setItem(6,item_none_neck);
					p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 7));
					TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l판매 완료");
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					hrwp.item_neck = null;
					hrwp.ability.applyItems();
				}
			}		
		}  else if(slotNum == 35) {
			ItemStack itemTmp = p.getInventory().getItem(7);
			if(itemTmp != null) {
				if(itemTmp.getType() != Material.AIR && itemTmp.getType() != Material.STAINED_GLASS_PANE) {
					p.getInventory().setItem(7, item_none_tailsman);
					p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, 10));
					TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l판매 완료");
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					hrwp.item_tailsman = null;
					hrwp.ability.applyItems();
				}
			}		
		}
	}
	
	public void itemCheckClick(Player p, int slotNum) {
		if(!playerMap.containsKey(p.getName())) return;
		if(slotNum == 2) {
			if(!tmpItemMap.containsKey(p.getName()) || !tmpItemPriceMap.containsKey(p.getName())
					|| !tmpItemSlotMap.containsKey(p.getName())) {
				p.sendMessage(ms+"구매 오류 발생 다시 시도해보세요.");
				return;
			}
			ItemStack item = tmpItemMap.get(p.getName());
			int price = tmpItemPriceMap.get(p.getName());
			int slot = tmpItemSlotMap.get(p.getName());
			if(takeItem(p, Material.GOLDEN_CARROT, price - (int)(price/2))) {
				tmpItemMap.remove(p.getName());
				tmpItemPriceMap.remove(p.getName());
				tmpItemSlotMap.remove(p.getName());
				p.getInventory().setItem(slot,item);
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§a§l구매 완료");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				playerMap.get(p.getName()).ability.applyItems();
			} else {
				p.closeInventory();
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "§6§l당근이 부족합니다.");
			}
		} else if(slotNum == 6) {
			p.closeInventory();	
		}
	}
	
	/////////
	
	public void divideSetAbility() {
		List<String> tmpList = new ArrayList<String>(ingamePlayer);
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = tmpList.size();
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					sch.schTime--;
					Player p = Bukkit.getPlayer(tmpList.get(sch.schTime));
					if(existPlayer(p))
						setAbility(p);
				} else {
					sch.cancelTask(true);
				}
			}
		}, 1l, 1l);
	}
	
	public void setAbility(Player p) {
		Team team = getTeamObject(p);
		if(!team.selectAbilityMap.containsKey(p.getName())) {
			int rnCode = team.leftAbCode.get(MyUtility.getRandom(0, team.leftAbCode.size()-1));
			team.selectAbilityMap.put(p.getName(), rnCode);
			team.leftAbCode.remove(Integer.valueOf(rnCode));
			team.jobList.getItem(rnCode).addUnsafeEnchantment(Enchantment.DURABILITY, 1);	
		} 
		switch(team.selectAbilityMap.get(p.getName())) {
		case 3: playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "추적자", team), team)); break;
		case 4: playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "자객", team), team)); break;
		case 5: playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "사냥꾼", team), team)); break;
		case 6: playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "용병", team), team)); break;
		case 7: playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "궁수", team), team)); break;
		case 12: playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "마도사", team), team)); break;
		case 13: playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "은둔자", team), team)); break;
		case 14: playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "선인", team), team)); break;
		case 15: playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "저격수", team), team)); break;
		
		case 21: playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "수호자", team), team)); break;
		case 22: playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "수도승", team), team)); break;
		case 23: playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "기사", team), team)); break;
		case 24: playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "격투가", team), team)); break;
		case 25: playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "군주", team), team)); break;
		case 30: playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "포식자", team), team)); break;
		case 31: playerMap.put(p.getName(), new HRWPlayer(p, new Warrior(this, p, "전사", team), team)); break;
		
		case 39: playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "사제", team), team)); break;
		case 40: playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "연금술사", team), team)); break;
		case 41: playerMap.put(p.getName(), new HRWPlayer(p, new BlackMagician(this, p, "흑마술사", team), team)); break;
		
		default: p.sendMessage(ms+"존재하지 않는 영웅코드("+team.selectAbilityMap.get(p.getName())+"입니다. - 관리자에게 문의해주세요.");
		}
		HRWPlayer hrwp = playerMap.get(p.getName());
		hrwp.ability.hrwData = hrwp;
		reConnectMap.put(p.getName(), hrwp);
		int carrot = (int)hrwp.team.carrotCnt / 2;
		p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, carrot));
	}
	
	public void applyHealthBar() {
		for(String name : playerMap.keySet()){
			Ability ab = playerMap.get(name).ability;
			Objective tmpO = ab.sidebar.getTheScoreboard().getObjective("health");
			if(tmpO != null) {
				tmpO.unregister();
			}
			Objective o = ab.sidebar.getTheScoreboard().registerNewObjective("health", "health");
			o.setDisplayName("§cHP");
			o.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		gameStep = 3;
		/*Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String name : playerMap.keySet()){
					Player t = Bukkit.getPlayer(name);
					if(existPlayer(t)) {
						//t.damage(0.01);
					}
				}
			}
		}, 20l);*/
	}
	
	public void sendStackMessage(String pName, int stack) {
		if(stack == 2) {
			sendMessage(ms+"§b"+pName+" -> §e2명 연속처치!");
		} else if(stack == 3) {
			sendMessage(ms+"§b"+pName+" -> §e3명 연속처치!");
		} else if(stack == 4) {
			sendMessage(ms+"§b"+pName+" -> §e폭주! 4명 연속처치!");
		} else if(stack >= 5) {
			sendMessage(ms+"§b"+pName+" -> §e스택 킬러! "+stack+"명 연속처치!");
		}
	}
	
	public void placeTotem(Location l, int delay) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				l.getBlock().setType(Material.PRISMARINE);
				l.getBlock().setData((byte)2);
			}
		}, 20 * delay);
	}
	
	public void placeBuffer(Location l, int delay) { //영혼비석 안씀
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(l.getBlock().getType() == Material.DIAMOND_BLOCK) return;
				l.getBlock().setType(Material.DIAMOND_BLOCK);
				for(String tName : playerMap.keySet()) {
					Player t = Bukkit.getPlayer(tName);
					if(existPlayer(t)) {
						t.playSound(t.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 0.25f);
						t.sendMessage(ms+"영혼 비석 재생됨");
						TitleAPI.sendFullTitle(t, 10, 100, 10, "§d§l영혼 비석 재생완료", "§8§l파괴시 5분간 팀원 공격력 +2");
					}
				}
			}
		}, 20 * delay);
	}

	public void offerToAltar(Player p, int amt) {
		if(getTeam(p).equalsIgnoreCase("BLUE")) {
			if(blueTeam.leftCarrot == 0) {
				p.sendMessage(ms+"이미 신전이 활성화 되었습니다. 당근은 반환됩니다.");
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, amt));
				return;
			}
			blueTeam.leftCarrot -= amt;
			if(blueTeam.leftCarrot <= 0) {
				blueTeam.leftCarrot = 0;
				TitleAPI.sendFullTitle(p, 10, 60, 10, ChatColor.RED+"신전이 활성화 되었습니다.", p.getName()+" 님께서 제단에 당근 "+ amt+"개를 바쳤습니다.");
				sendMessage(ms+"블루팀은 이제 신전을 점령할 수 있습니다!");
			} else {
				for(String pName : blueTeam.teamList) {
					Player t = Bukkit.getPlayer(pName);
					if(existPlayer(t))
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", p.getName()+" 님께서 제단에 당근 "+ amt+"개를 바쳤습니다.");
				}
			}

		} else if(getTeam(p).equalsIgnoreCase("RED")) {
			if(redTeam.leftCarrot == 0) {
				p.sendMessage(ms+"이미 신전이 활성화 되었습니다. 당근은 반환됩니다.");
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, amt));
				return;
			}
			redTeam.leftCarrot -= amt;
			if(redTeam.leftCarrot <= 0) {
				redTeam.leftCarrot = 0;
				TitleAPI.sendFullTitle(p, 10, 60, 10, ChatColor.RED+"제단이 활성화 되었습니다.", p.getName()+" 님께서 제단에 당근 "+ amt+"개를 바쳤습니다.");
				sendMessage(ms+"레드팀은 이제 신전을 점령할 수 있습니다!");
			} else {
				for(String pName : redTeam.teamList) {
					Player t = Bukkit.getPlayer(pName);
					if(existPlayer(t))
						TitleAPI.sendFullTitle(p, 10, 60, 10, "", p.getName()+" 님께서 제단에 당근 "+ amt+"개를 바쳤습니다.");
				}
			}
			//sendTeamChat(redTeam, redMS+p.getName()+" 님께서 제단에 당근 "+ amt+"개를 바쳤습니다.");
		}
		updateScoreboard();
	}
	
	public void occupyPoint(Player p, Location l) {
		String team = getTeam(p);
		//server.egPM.printLog(""+l.getBlockX()+"/"+l.getBlockY()+"/"+l.getBlockZ());
		boolean isNeute = false;
		if(team.equalsIgnoreCase("BLUE")) {
			if(blueTeam.leftCarrot > 0) {
				isNeute = true;
			}
		} else if(team.equalsIgnoreCase("RED")) {
			if(redTeam.leftCarrot > 0) {
				isNeute = true;
			}
		}
		if(MyUtility.compareLocation(point1.pointLoc, l)){
			point1.occupied(p, team, isNeute);
		} else if(MyUtility.compareLocation(point2.pointLoc, l)){
			point2.occupied(p, team, isNeute);
		} else if(MyUtility.compareLocation(point3.pointLoc, l)){
			point3.occupied(p, team, isNeute);
		}
	}
	
	public void performence2() {
		sendSound(Sound.BLOCK_NOTE_PLING);
		for(String pName : playerMap.keySet()) {	
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				HRWPlayer hrwp = playerMap.get(pName);
				hrwp.ability.updateStatItem();
				for(String msg : hrwp.ability.statLoreList) {
					p.sendMessage(msg);
				}
			}
		}
	}
	
	public void performence() {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					for(String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							Location tmpL = p.getLocation();
							Firework fw = (Firework) tmpL.getWorld().spawnEntity(tmpL, EntityType.FIREWORK);
							FireworkMeta fwm = fw.getFireworkMeta();

							fwm.setPower(1);
							fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

							fw.setFireworkMeta(fwm);
							fw.detonate();
							
					        Firework fw2 = (Firework) tmpL.getWorld().spawnEntity(tmpL, EntityType.FIREWORK);
					        fw2.setFireworkMeta(fwm);
						}
					}
				}else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 20l);
	}
	
	public void blueTeamWin() {
		
		gameStep = 4;
		sendMessage(ms + ChatColor.GRAY + "\n레드팀의 영혼이 전부 소진됐습니다! 블루팀이 승리했습니다!");
		performence();
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				MyUtility.allClear(p);
		}
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						playerMap.get(pName).ability.respawnSch.cancelTask(true);
						if(existPlayer(p)) {
							if(getTeam(p).equalsIgnoreCase("BLUE")) {
								//server.egGM.giveGold(p.getName(), 200);
								//p.sendMessage(ms + "승리 보상으로 200골드를 받으셨습니다.");
							} else {
								//server.egGM.giveGold(p.getName(), 150);
								//p.sendMessage(ms + "게임 참가 보상으로 보상으로 150골드를 받으셨습니다.");
							}
						}
					}
				}
			}, 100L);
		} catch (Exception e) {
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				performence2();
			}
		}, 160L);
		
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					divideSpawn();
					endGame(false);
					server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "블루팀의 승리로 "+disPlayGameName+"가 종료 되었습니다.");
				}
			}, 300L);
		} catch (Exception e) {
			endGame(true);
		}	
	}
	
	public void redTeamWin() {
		gameStep = 4;
		sendMessage(ms + ChatColor.GRAY + "\n블루팀의 영혼이 전부 소진됐습니다! 레드팀이 승리했습니다!");
		performence();
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				MyUtility.allClear(p);
		}
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						playerMap.get(pName).ability.respawnSch.cancelTask(true);
						if(existPlayer(p)) {
							if(getTeam(p).equalsIgnoreCase("RED")) {
								//server.egGM.giveGold(p.getName(), 200);
								//p.sendMessage(ms + "승리 보상으로 200골드를 받으셨습니다.");
							} else {
								//server.egGM.giveGold(p.getName(), 150);
								//p.sendMessage(ms + "게임 참가 보상으로 보상으로 150골드를 받으셨습니다.");
							}
						}
					}
				}
			}, 100L);
		} catch (Exception e) {
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				performence2();
			}
		}, 160L);
		
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					divideSpawn();
					endGame(false);
					server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "레드팀의 승리로 "+disPlayGameName+"가 종료 되었습니다.");
				}
			}, 300L);
		} catch (Exception e) {
			endGame(true);
		}
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + disPlayGameName+ " 게임이 강제 종료 되었습니다.");
		}
		
		if(redTeam.predator_portal1 != null && redTeam.predator_portal1_backupType != null) {
			redTeam.predator_portal1.getBlock().setType(redTeam.predator_portal1_backupType);
			redTeam.predator_portal1 = null;
		}
		if(blueTeam.predator_portal1 != null && blueTeam.predator_portal1_backupType != null) {
			blueTeam.predator_portal1.getBlock().setType(blueTeam.predator_portal1_backupType);
			redTeam.predator_portal2 = null;
		}
		for (int i = 0; i < ingamePlayer.size(); i++) {
			String pName = ingamePlayer.get(i);
			Player p = Bukkit.getPlayer(pName);
			if(playerMap.containsKey(p.getName())) {
				playerMap.get(pName).ability.respawnSch.cancelTask(true);
			}	
		}
		divideSpawn();
		try {
			int schFreeCnt = 0;
			for(EGScheduler sch : schList) {
				if(sch.cancelTask(false))
					schFreeCnt += 1;
			}
			schList.clear();
			Bukkit.getLogger().info(disPlayGameName+"해제된 스케쥴 수 : "+schFreeCnt);
		}catch(Exception e) {
			
		}
		
		try {
			int schFreeCnt = 0;
			for(EGScheduler sch : removeReconnectTimer.values()) {
				if(sch.cancelTask(false))
					schFreeCnt += 1;
			}
			removeReconnectTimer.clear();
			Bukkit.getLogger().info(disPlayGameName+"해제된 리컨넥트 스케쥴 수 : "+schFreeCnt);
		}catch(Exception e) {
			
		}
		
		for (Entity entity : Bukkit.getWorld("world").getEntities()) {
			if (entity instanceof Item || entity instanceof Arrow) {
				if (entity.getLocation().distance(loc_Join) < 300)
					entity.remove();
			}
		}
		
		ingame = false;
		ending = false;
		initGame();
		ingamePlayer.clear();
	}

	//////////////// 이벤트
	public class EventHandlerHRW extends EGEventHandler {

		private HeroesWar game;

		public EventHandlerHRW(EGServer server, HeroesWar hrw) {
			super(server);
			this.game = hrw;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase(cmdMain)) {
				e.setCancelled(true);
				if (p.isOp()) {
					game.onCommand(p, cmd);
				}
			} 
			if(ingamePlayer.contains(p.getName())) { //게임 참여해야지만 가능한 명령어
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerCommand(e);
				
				if(cmd[0].equalsIgnoreCase("/tc")) {
					String team = getTeam(p);
					if(team.equalsIgnoreCase("BLUE") || team.equalsIgnoreCase("RED")) {
						if(teamChatList.contains(p.getName())) {
							teamChatList.remove(p.getName());
							p.sendMessage(ms+"전체 채팅으로 전환합니다.");
							e.setCancelled(true);
						} else {
							teamChatList.add(p.getName());
							p.sendMessage(ms+"팀 채팅으로 전환합니다.");
							e.setCancelled(true);
						}
					}
				} else if(server.cmdSpawn.contains(cmd[0])) {
					gameQuitPlayer(p, true, false);
				}
			}
		}

		@EventHandler
		public void onEntityDamaged(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();
				if (ingamePlayer.contains(p.getName())) {
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				            Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				            	public void run() {
				            		p.teleport(loc_Join, TeleportCause.PLUGIN);
				            	}
				            }, 2l);
				        }
				        e.setCancelled(true);
					} else if(gameStep == 1) {
						if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포		           
							 p.teleport(loc_Join, TeleportCause.PLUGIN);
							 Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					            	public void run() {
					            		p.teleport(loc_Join, TeleportCause.PLUGIN);
					            	}
					            }, 2l);
				        }
						e.setCancelled(true);
					} else if(gameStep == 2) {
				        if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포		           
				        	//p.teleport((getTeam(p).equalsIgnoreCase("BLUE") ? blueTeam.loc_jobSelect : redTeam.loc_jobSelect));
				        	 p.teleport(loc_Join, TeleportCause.PLUGIN);
				        	 Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					            	public void run() {
					            		p.teleport(loc_Join, TeleportCause.PLUGIN);
					            	}
					            }, 2l);
				        }
				        e.setCancelled(true);
					} else if(gameStep == 3) {
						if (cause.equals(DamageCause.VOID)) { //게임중 허공 뎀100		           
				        	 e.setDamage(100);
				        }
						if(cause.equals(DamageCause.FALL)) {
					        	e.setCancelled(true);
					    } else {
					    	if(e.getDamage() != 0.01) {
								if(playerMap.get(p.getName()) != null) {
									Team team = playerMap.get(p.getName()).team;
									Location pl = p.getLocation();
									Location l1 = team.loc_base1;
									Location l2 = team.loc_base2;	
									//p.sendMessage(""+pl);
									//p.sendMessage(""+l1);
									//p.sendMessage(""+l2);
									if(l1.getX() < pl.getX() &&
											l2.getX() > pl.getX() &&
											l1.getY() < pl.getY() &&
											l2.getY() > pl.getY() &&
											l1.getZ() < pl.getZ() &&
											l2.getZ() > pl.getZ()) {
										ActionBarAPI.sendActionBar(p, "§c§l기지안에서는 데미지를 입지 않습니다.", 60);
										e.setCancelled(true);
									} else {
										playerMap.get(p.getName()).ability.onEntityDamaged(e);	
									}
								}															        
							}	
					    }							
					} else if(gameStep == 4) {
				        if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				            Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				            	public void run() {
				            		p.teleport(loc_Join, TeleportCause.PLUGIN);
				            	}
				            }, 2l);
				        }
				        e.setCancelled(true);
					}
					
				}
			}
		}

		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§l"+inventoryGameName+" 도우미")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				p.closeInventory();
				gameHelper(p, e.getSlot());
			}
			
			if (!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onClickInventory(e);
			
			if(e.getCurrentItem() != null && !(e.getCurrentItem().getType() == Material.GOLDEN_CARROT 
					|| e.getCurrentItem().getType() == Material.GHAST_TEAR
					|| e.getCurrentItem().getType() == Material.MAGMA_CREAM
					|| e.getCurrentItem().getType() == Material.AIR
					|| e.getCurrentItem().getType() == Material.POTION
					|| e.getCurrentItem().getType() == Material.GLASS_BOTTLE)){
				//p.sendMessage(ms+"이 아이템은 옮기실 수 없습니다.");
				e.setCancelled(true);
				p.updateInventory();
			}
			
			if (e.getInventory().getTitle().contains("영웅선택")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				abilitySelect(p, e.getSlot());
			} else if(e.getInventory().getTitle().contains("스킬 상점")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				skillShopClick(p, e.getSlot());
			}else if(e.getInventory().getTitle().contains("포션 상점")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				potionShopClick(p, e.getSlot());
			}else if(e.getInventory().getTitle().contains("아이템 상점")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				itemShopClick(p, e.getSlot());
			}else if(e.getInventory().getTitle().contains("판매 확인")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				itemCheckClick(p, e.getSlot());
			}else if(e.getInventory().getTitle().contains("히어로즈워")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				hrwHelper(p, e.getSlot());
			}
		}

		@EventHandler
		public void onHitPlayer(EntityDamageByEntityEvent e) {
			

			if(e.getEntity() instanceof Player && ingame) { //게임시작했을때만
				Player player = (Player) e.getEntity();
				Player damager = null;

				if (!ingamePlayer.contains(player.getName())) return; //피해자가 겜에 참가중이지않으면 규칙 무시

				boolean isDirectAttach = true;
				Arrow arrow = null;
				
				if (e.getDamager() instanceof Snowball) { //화살과 총알에대한 공격자 설정
					Snowball snowball = (Snowball) e.getDamager();
					if (snowball.getShooter() instanceof Player) {
						damager = (Player) snowball.getShooter();
						isDirectAttach = false;
					}
				}
				if (e.getDamager() instanceof Arrow) {
					arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() instanceof Player) {
						damager = (Player) arrow.getShooter();
						isDirectAttach = false;
					}
					e.getDamager().remove();	
				}
				if (e.getDamager() instanceof Player)
					damager = (Player) e.getDamager();
				
				if (damager == null) //공격자 없으면 리턴
					return;
				
				if (!ingamePlayer.contains(damager.getName())) { //공격자가 겜에 참가중이지않으면 규칙 무시
					return;
				}
				
				
				if(playerMap.get(damager.getName()) != null) {
					if(playerMap.containsKey(player.getName())) {
						Team team = playerMap.get(player.getName()).team;
						Location pl = player.getLocation();
						Location l1 = team.loc_base1;
						Location l2 = team.loc_base2;
						if(l1.getX() < pl.getX() &&
								l2.getX() > pl.getX() &&
								l1.getY() < pl.getY() &&
								l2.getY() > pl.getY() &&
								l1.getZ() < pl.getZ() &&
								l2.getZ() > pl.getZ()) {
							ActionBarAPI.sendActionBar(player, "§c§l기지안에서는 데미지를 입지 않습니다.", 60);
							ActionBarAPI.sendActionBar(damager, "§c§l상대 기지안에서는 공격이 불가능합니다.", 60);
							e.setCancelled(true);
						} else {
							if(playerMap.containsKey(player.getName()) && playerMap.containsKey(damager.getName())) {
								if(!playerMap.get(player.getName()).team.equals(playerMap.get(damager.getName()).team)) {
									if(!isDirectAttach || checkVictimDelay(damager.getName(), player)) {
										playerMap.get(damager.getName()).ability.onHitPlayer(e);
									//	Bukkit.broadcastMessage("이벤트 실행");
									} else {
										e.setCancelled(true);
										//Bukkit.broadcastMessage("이벤트 캔슬");
									}
								} else {
									e.setCancelled(true);
								}
							}			
						}
					}			
				}
					
				
				ItemStack weapon = damager.getInventory().getItemInMainHand();
				
				if(weapon != null && (
						weapon.getType().toString().contains("AXE") || 
						weapon.getType().toString().contains("HOE") ||
						weapon.getType().toString().contains("SWORD") ||
						weapon.getType().toString().contains("SPADE") ||
						weapon.getType().toString().contains("BOW"))){
					short dura = weapon.getType().getMaxDurability();
					weapon.setDurability((short) (dura - dura));
				}

				if(arrow != null) arrow.remove();

			}
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			if(!ingame || gameStep != 3) {
				e.setCancelled(true);
				return;
			}
			boolean block_noCatching = false;
			if(playerMap.get(p.getName()) != null) {
				Ability ab = playerMap.get(p.getName()).ability;
				ab.onBlockBreak(e);
				block_noCatching = ab.block_noCatching;
			}

			Location bl = e.getBlock().getLocation();
			
			if(e.getBlock().getType() != coreBlock && e.getBlock().getType() != Material.DIAMOND_BLOCK) {
				e.setCancelled(true);
			} else if(e.getBlock().getType() == Material.DIAMOND_BLOCK){
				if(block_noCatching) {
					e.setCancelled(true);
					p.sendMessage(ms+"은신 상태에서는 영혼 비석 파괴가 불가능합니다.");
				} else {
					if(MyUtility.compareLocation(bl, loc_buffer)) {
						placeBuffer(loc_buffer, 300);
						if(getTeam(p).equalsIgnoreCase("BLUE")) {
							for(String pName : redTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if(existPlayer(t)) {
									TitleAPI.sendFullTitle(t, 10, 70, 10, "","§b"+p.getName()+" §e님께서 영혼비석을 파괴하였습니다. ");
								}
							}
							for(String pName :blueTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if(existPlayer(t)) {
									if(playerMap.containsKey(t.getName())) {
										TitleAPI.sendFullTitle(t, 10, 70, 10, "","§b"+p.getName()+" §e님께서 영혼비석을 파괴하였습니다. ");
										playerMap.get(t.getName()).ability.customDamageUp(t, t, 2, 300, true);
										t.sendMessage(ms+"팀이 영혼비석 파괴 -> 5분간 주는피해 +2");
									}								
								}
									
							}	
						} else if(getTeam(p).equalsIgnoreCase("RED")) {
							for(String pName : redTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if(existPlayer(t)) {
									TitleAPI.sendFullTitle(t, 10, 70, 10, "","§c"+p.getName()+" §e님께서 영혼비석을 파괴하였습니다. ");
									playerMap.get(t.getName()).ability.customDamageUp(t, t, 1, 300, true);
									t.sendMessage(ms+"팀이 영혼비석 파괴 -> 3분간 주는피해 +1, 이동속도 +10%");
								}		
							}
							for(String pName :blueTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if(existPlayer(t)) {
									TitleAPI.sendFullTitle(t, 10, 70, 10, "","§c"+p.getName()+" §e님께서 영혼비석을 파괴하였습니다. ");
								}
							}	
						}
					}	
				}		
			} else if(e.getBlock().getType() == coreBlock){
				if(block_noCatching) {
					e.setCancelled(true);
					p.sendMessage(ms+"은신 상태에서는 토템 파괴가 불가능합니다.");
				} else {
					if (getTeam(p).equalsIgnoreCase("BLUE")) {
						if (MyUtility.compareLocation(bl, redTeam.loc_totem)) {
							placeTotem(bl, 10);
							p.sendMessage(ms + "10초 후 토템이 재생됩니다.");
							giveCarrot(blueTeam.teamList, p.getName() + " 님이 적팀의 토템을 파괴하여 당근+6", 6);
							blueTeam.carrotCnt += 6;
							for (String pName : redTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if (existPlayer(t))
									TitleAPI.sendFullTitle(t, 10, 70, 10, "", "§c§l레드팀§7§l의 토템이 파괴됐습니다.");
							}
							for (String pName : blueTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if (existPlayer(t)) {
									TitleAPI.sendFullTitle(t, 10, 70, 10, "", "§c§l레드팀§7§l의 토템이 파괴됐습니다.");
								}
							}
						} else {
							p.sendMessage(ms + "팀의 토템을 부수지 마세요!");
							e.setCancelled(true);
						}
					} else if (getTeam(p).equalsIgnoreCase("RED")) {
						if (MyUtility.compareLocation(bl, blueTeam.loc_totem)) {
							giveCarrot(redTeam.teamList, p.getName() + " 님이 적팀의 토템을 파괴하여 당근+6", 6);
							redTeam.carrotCnt += 6;
							placeTotem(bl, 10);
							p.sendMessage(ms + "10초 후 토템이 재생됩니다.");
							for (String pName : blueTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if (existPlayer(t))
									TitleAPI.sendFullTitle(t, 10, 70, 10, "",  "§b§l블루팀§7§l의 토템이 파괴됐습니다");
							}
							for (String pName : redTeam.teamList) {
								Player t = Bukkit.getPlayer(pName);
								if (existPlayer(t))
									TitleAPI.sendFullTitle(t, 10, 70, 10, "",  "§b§l블루팀§7§l의 토템이 파괴됐습니다");
							}
						} else {
							p.sendMessage(ms + "팀의 토템을 부수지 마세요!");
							e.setCancelled(true);
						}
					}
				}
			}
		}
		
		@EventHandler
		public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onPlayerItemConsume(e);
			//e.setCancelled(true);
		}
		
		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;

			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onBlockPlaced(e);
			
			e.setCancelled(true);
		}
		
		@EventHandler
		public void onRegainHealth(EntityRegainHealthEvent e) {
			if(!(e.getEntity() instanceof Player)) return;
			Player p = (Player) e.getEntity();
			
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onRegainHealth(e);
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onItemDrop(e);
			Material m = e.getItemDrop().getItemStack().getType();
			if(m != Material.GOLDEN_CARROT && m != Material.POTION) {
				if(m == Material.GLASS_BOTTLE) {
					e.getItemDrop().remove();
				}else {
					e.setCancelled(true);	
				}
			}
		}

		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e) {
			if (((e.getEntity() instanceof Player)) && ingame) {
				Player p = e.getEntity();
				if(!ingamePlayer.contains(p.getName())) return;
								
				HRWPlayer hrwp = playerMap.get(p.getName());
				String k = hrwp.lastDamager;
//				Bukkit.broadcastMessage(k+","+hrwp.ability.abilityName);
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerDeath(e);
				
				if(k != null) {
					if(playerMap.get(k) != null)
						playerMap.get(k).ability.onPlayerKill(p);
				}

				e.getDrops().clear();
				e.setDroppedExp(0);
				
				if(ingame) {				
					if(hrwp.alreadyDead) return;
					hrwp.alreadyDead = true;
					Ability ab = hrwp.ability;
					ab.lastDeathLoc = p.getLocation().add(0,0.5,0);		
					if(hrwp.carrotCnt == 0) {
						hrwp.carrotCnt = countItem(p.getInventory(), Material.GOLDEN_CARROT);
					}
					if (ab.backUpInvenMap == null) {
						if (hasItem(p, Material.BOOK, 1)) {
							ab.backUpInvenMap = p.getInventory().getContents();
							ab.backUpArmorMap = p.getInventory().getArmorContents();
						}
					}
					p.setHealth(0);
					hrwp.death += 1;
					if (getTeam(p).equalsIgnoreCase("BLUE")) {
						if (k == null || k == p.getName()) {
							giveCarrot(redTeam.teamList, "적팀" + p.getName() + " 님이 자살하여 당근+1", 1);
							redTeam.carrotCnt += 1;
						}	else {
							HRWPlayer killerHrwP = playerMap.get(k);
							if(killerHrwP == null) return;
							killerHrwP.kill += 1;
							if(System.currentTimeMillis() - killerHrwP.lastKillTime <= 5000) {
								killerHrwP.nowStackKill += 1;
								if(killerHrwP.nowStackKill > killerHrwP.maxStackKill) {
									killerHrwP.maxStackKill = killerHrwP.nowStackKill;
								}
								sendStackMessage(k, killerHrwP.nowStackKill);								
							}else {
								killerHrwP.nowStackKill = 1;
							}
							killerHrwP.lastKillTime = System.currentTimeMillis();
							//Bukkit.getLogger().info(System.currentTimeMillis()+" / "+killerHrwP.lastKillTime);
							//Bukkit.getLogger().info(killerHrwP.nowStackKill+"");
							giveCarrot(redTeam.teamList, k + " 님이 적을 처치하여 당근+1", 1);
							redTeam.carrotCnt += 1;
						}						
					} else if (getTeam(p).equalsIgnoreCase("RED")) {
						if (k == null || k == p.getName()) {
							giveCarrot(blueTeam.teamList, "적팀" + p.getName() + " 님이 자살하여 당근+1", 1);
							blueTeam.carrotCnt += 1;
						}
						else {
							HRWPlayer killerHrwP = playerMap.get(k);
							if(killerHrwP != null) {
								killerHrwP.kill += 1;
								if(System.currentTimeMillis() - killerHrwP.lastKillTime <= 5000) {
									killerHrwP.nowStackKill += 1;
									if(killerHrwP.nowStackKill > killerHrwP.maxStackKill) {
										killerHrwP.maxStackKill = killerHrwP.nowStackKill;
									}else {
										killerHrwP.nowStackKill = 1;
									}
									sendStackMessage(k, killerHrwP.nowStackKill);
								}else {
									killerHrwP.nowStackKill = 1;
								}
								killerHrwP.lastKillTime = System.currentTimeMillis();
								//Bukkit.getLogger().info(System.currentTimeMillis()+" / "+killerHrwP.lastKillTime);
								//Bukkit.getLogger().info(killerHrwP.nowStackKill+"");
							}							
							giveCarrot(blueTeam.teamList, k + " 님이 적을 처치하여 당근+1", 1);
							blueTeam.carrotCnt += 1;
						}
					}		
					
					k = null;
				} else {
					gameQuitPlayer(p, true, false);
				}
			}
		}
		
		@EventHandler
		public void onPlayerShotBow(EntityShootBowEvent e) {
			if (((e.getEntity() instanceof Player)) && ingame) {
				Player p = (Player) e.getEntity();
				
				if(!ingamePlayer.contains(p.getName())) return;
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerShotBow(e);

			}
		}
		
		@EventHandler
		public void onPlayerRespawn(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null) {
				HRWPlayer hrwp = playerMap.get(p.getName());
				e.setRespawnLocation(hrwp.ability.team.loc_spawn);
				hrwp.alreadyDead = false;
				hrwp.ability.onPlayerRespawn(e);
			}
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onPlayerQuit(e);
			gameQuitPlayer(p, true, false);
		}

		@EventHandler
		public void onMouseClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();
			//////////설정용
			if(pointSetting != 0 && action == Action.LEFT_CLICK_BLOCK) {
				if(pointSetting == 1) {
					point1.savePointLoc(locPath, "location.yml", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point1의 점령 블럭 설정완료, 양털 블럭의 1번 지점을 설정해주세요.");
					pointSetting += 1;
				} else if(pointSetting == 4) {
					point2.savePointLoc(locPath, "location.yml", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point2의 점령 블럭 설정완료, 양털 블럭의 1번 지점을 설정해주세요.");
					pointSetting += 1;
				} else if(pointSetting == 7) {
					point3.savePointLoc(locPath, "location.yml", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point3의 점령 블럭 설정완료, 양털 블럭의 1번 지점을 설정해주세요.");
					pointSetting += 1;
				} else if(pointSetting == 2) {
					tmpWoolPos1 = e.getClickedBlock().getLocation();
					p.sendMessage(ms+"point1의 양털 블럭 1지점 설정완료, 양털 블럭의 2번 지점을 설정해주세요.");
					pointSetting += 1;
				} else if(pointSetting == 3) {
					point1.saveWoolLoc(locPath, "location.yml", tmpWoolPos1, e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point1의 양털 블럭 2지점 설정완료, 설정종료.");
					pointSetting = 0;
				} else if(pointSetting == 5) {
					tmpWoolPos1 = e.getClickedBlock().getLocation();
					p.sendMessage(ms+"point2의 양털 블럭 1지점 설정완료, 양털 블럭의 2번 지점을 설정해주세요.");
					pointSetting += 1;
				} else if(pointSetting == 6) {
					point2.saveWoolLoc(locPath, "location.yml", tmpWoolPos1, e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point2의 양털 블럭 2지점 설정완료, 설정종료.");
					pointSetting = 0;
				} else if(pointSetting == 8) {
					tmpWoolPos1 = e.getClickedBlock().getLocation();
					p.sendMessage(ms+"point3의 양털 블럭 1지점 설정완료, 양털 블럭의 2번 지점을 설정해주세요.");
					pointSetting += 1;
				} else if(pointSetting == 9) {
					point3.saveWoolLoc(locPath, "location.yml", tmpWoolPos1, e.getClickedBlock().getLocation());
					//server.egPM.printLog(""+tmpWoolPos1.getBlockX()+"/"+tmpWoolPos1.getBlockY()+"/"+tmpWoolPos1.getBlockZ());
					//server.egPM.printLog(""+e.getClickedBlock().getLocation().getBlockX()+"/"+e.getClickedBlock().getLocation().getBlockY()+"/"+e.getClickedBlock().getLocation().getBlockZ());
					p.sendMessage(ms+"point3의 양털 블럭 2지점 설정완료, 설정종료.");
					pointSetting = 0;
				} 
				e.setCancelled(true);
			}///////////////
			
			if(baseSetting != 0 && action == Action.LEFT_CLICK_BLOCK) {
				if(baseSetting == 1) {
					saveLocation(gameName, "blueBase1", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"블루팀 기지 1번지점 설정완료, 2번 지점을 설정해주세요.");
					baseSetting += 1;
				} else if(baseSetting == 2) {
					saveLocation(gameName, "blueBase2", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"블루팀 기지 지점 설정완료");
					baseSetting = 0;
				} else if(baseSetting == 3) {
					saveLocation(gameName, "redBase1", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"레드팀 기지 1번지점 설정완료, 2번 지점을 설정해주세요.");
					baseSetting += 1;
				} else if(baseSetting == 4) {
					saveLocation(gameName, "redBase2", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"레드팀 기지 지점 설정완료");
					baseSetting = 0;
				} 
				e.setCancelled(true);
			}///////////////
			if (!ingamePlayer.contains(p.getName())) return; //우클릭만 허용
			
			if(playerMap.get(p.getName()) != null) {
				Ability ab = playerMap.get(p.getName()).ability;
				ab.onMouseClick(e);
				/*ItemStack item = p.getInventory().getItemInMainHand();
				if(item.getType() == Material.GHAST_TEAR) {
					removeItem(p, Material.GHAST_TEAR, 1);
					ab.healPotion1();
				} else if(item.getType() == Material.MAGMA_CREAM) {
					removeItem(p, Material.MAGMA_CREAM, 1);
					ab.healPotion2();
				}*/
			}
				
			
			if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
					
			if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c영웅 선택 §f]")) {
				if (getTeam(p).equalsIgnoreCase("BLUE")) {
					p.openInventory(blueTeam.jobList);
				} else {
					p.openInventory(redTeam.jobList);
				}
			}
			
			if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §b게임 메뉴 §f]")) {
				p.openInventory(inven_hrwHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6귀환 §f]")) {
				if(playerMap.containsKey(p.getName()))
					playerMap.get(p.getName()).ability.returning();
			}

			Block b = e.getClickedBlock();
			if (e.getClickedBlock() != null) {
				if(b.getType() == Material.CHEST) {
					e.setCancelled(true);
					String team = getTeam(p);
					if((team.equalsIgnoreCase("BLUE") && blueTeam.leftCarrot == 0) ||
							(team.equalsIgnoreCase("RED") && redTeam.leftCarrot == 0)){
						p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.0f, 1.5f);
						p.sendMessage(ms+"이미 신전이 활성화 되어있습니다.");
					} else {
						p.playSound(p.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1.0f, 1.0f);
						Inventory altarInven = Bukkit.createInventory(null, 9, "§c§l제단");
						p.sendMessage(ms+"제단에 바칠 당근을 넣어주세요.");
						p.openInventory(altarInven);
					}
				}
			}
		}
		
		@EventHandler
		public void onInteractEntity(PlayerInteractEntityEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return; //우클릭만 허용
			if(e.getRightClicked() instanceof Player){
				Player t = (Player) e.getRightClicked();
				if(t.getName().contains("스킬 상점")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					p.openInventory(inven_skillShop);
				} else if(t.getName().contains("포션 상점")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					p.openInventory(inven_potionShop);
				} else if(t.getName().contains("아이템 상점")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					p.openInventory(inven_itemShop);
				} else if(t.getName().contains("§f[ §c신관 §f]") && getTeam(p).equalsIgnoreCase("RED")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					MyUtility.healUp(p);
					TitleAPI.sendTitle(p, 10, 70, 10, "§e§l치유됨");
				}  else if(t.getName().contains("§f[ §b신관 §f]") && getTeam(p).equalsIgnoreCase("BLUE")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					MyUtility.healUp(p);
					TitleAPI.sendTitle(p, 10, 70, 10, "§e§l치유됨");
				}
			}
		}
		
		@EventHandler
		public void onFoodLevelChange(FoodLevelChangeEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(ingamePlayer.contains(p.getName())) e.setFoodLevel(20);
			}
		}
		
		@EventHandler
		public void onInventoryClose(InventoryCloseEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onInventoryClose(e);
				
				Inventory inven = e.getInventory();
				if(inven.getTitle().equalsIgnoreCase("§c§l제단")) {
					int cnt = countItem(inven, Material.GOLDEN_CARROT);
					if(cnt != 0) offerToAltar(p, cnt);
				} else if(inven.getTitle().contains("판매 확인")) {
					tmpItemMap.remove(p.getName());
					tmpItemPriceMap.remove(p.getName());
					tmpItemSlotMap.remove(p.getName());
				}
			}
		}
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName()) && ingame) {
				///위치이동 아니면 캔슬
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				
				boolean block_noCatching = false;
				
				if(playerMap.get(p.getName()) != null) {
					Ability ab = playerMap.get(p.getName()).ability;
					ab.onPlayerMove(e);
					block_noCatching = ab.block_noCatching;
				}
				
				Location gotoLoc = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
				gotoLoc.setY(gotoLoc.getBlockY()-1);
				if(gotoLoc.getBlock().getType() == pointBlock && gameStep == 3) {
					if(block_noCatching) p.sendMessage(ms+"은신 상태에서는 점령이 불가능합니다.");
					else occupyPoint(p, gotoLoc);
				} else if(gotoLoc.getBlock().getType() == Material.SPONGE) {
					if(gameName.equalsIgnoreCase("HeroesWar2")) {
						p.setVelocity(p.getLocation().getDirection().multiply(9.7D).setY(6));
					} else {
						p.setVelocity(p.getLocation().getDirection().multiply(3D).setY(2));
					}
				    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 0.8f);
				}
				if(getTeam(p).equalsIgnoreCase("BLUE")) {
					if(gotoLoc.distance(redTeam.loc_spawn) <= 32) {
						p.damage(10);
						p.sendMessage(ms+"적팀 기지안에서의 이동은 피해를 받습니다.");
					}else if(gotoLoc.distance(blueTeam.loc_spawn) <= 32) {
						p.addPotionEffect(game.baseBoost);
					}
				} else if(getTeam(p).equalsIgnoreCase("RED")) {
					if(gotoLoc.distance(blueTeam.loc_spawn) <= 32) {
						p.damage(10);
						p.sendMessage(ms+"적팀 기지안에서의 이동은 피해를 받습니다.");
					}else if(gotoLoc.distance(redTeam.loc_spawn) <= 32) {
						p.addPotionEffect(game.baseBoost);
					}
				}
					
				if(gotoLoc.getBlock().getRelative(0, 1, 0).getType() == Material.STATIONARY_WATER){
					if(playerMap.containsKey(p.getName())) {
						playerMap.get(p.getName()).ability.healSpring();
					}
				} 
			}
		}
			
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onChat(e);
				String msg = e.getMessage();
				if(teamChatList.contains(p.getName())) {
					String team = getTeam(p);
					String str = (team.equalsIgnoreCase("BLUE") ? blueTeam.ms : redTeam.ms)+p.getName()+" >> §b"+msg;
					HRWPlayer hrwP = playerMap.get(p.getName());
					if(hrwP != null) {
						str += " §f[ §a"+hrwP.ability.abilityName+" §f]";
					}
					server.egCM.sendMessagesToStringList((team.equalsIgnoreCase("BLUE") ? blueTeam.teamList : redTeam.teamList), p, str, false);
				} else {
					String str = ms+p.getName()+" >> §6"+msg;
					server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);
				}
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onSneak(PlayerToggleSneakEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onSneak(e);
			}
		}
		
		@EventHandler
		public void onPlayerToggleFlight(PlayerToggleFlightEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerToggleFlight(e);
			}
		}
		
		@EventHandler
		public void onPlayerCraft(CraftItemEvent e) {
			if(e.getWhoClicked() instanceof Player) {
				Player p = (Player) e.getWhoClicked();	
				if (!ingamePlayer.contains(p.getName())) { // 겜에 참가중이지않으면 규칙 무시
					return;
				}
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onChangeHand(PlayerSwapHandItemsEvent e) {
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(e.getOffHandItem() == null) {
					
				}else if(e.getOffHandItem().getType() == Material.AIR){
					
				}else {
					e.setCancelled(true);
					if (playerMap.get(p.getName()) != null)
						playerMap.get(p.getName()).ability.onChangeHand(e);
				}
			}
		}
		
		@EventHandler
		public void onArrowHit(PlayerToggleSneakEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onSneak(e);
			}
		}
	}
}
