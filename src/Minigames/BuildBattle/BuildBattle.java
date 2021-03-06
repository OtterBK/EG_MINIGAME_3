package Minigames.BuildBattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;
import Utility.RepairMap;
import me.confuser.barapi.BarAPI;

public class BuildBattle extends Minigame{
	// 이벤트용
	public EventHandlerBBT event;
	
	public String ms = "§7[§6전체§7] ";

	///////////// private
	// 게임 플레이어 리스트
	//private HashMap<String, BdbPlayer> playerMap = new HashMap<String, BdbPlayer>();
	private String cmdMain;
	private ItemStack item_score1;
	private ItemStack item_score2;
	private ItemStack item_score3;
	private ItemStack item_score4;
	private ItemStack item_score5;
	//////// 게임 관련
	
	public int gameStep = 0;
	public EGScheduler mainSch;
	public List<Location> loc_Start = new ArrayList<Location>(12);
	public String topic = "";
	public List<String> topicList = new ArrayList<String>();
	public String screenedpName = "";
	public HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
	public List<String> reConnectList = new ArrayList<String>();
	public HashMap<String, Integer> giverMap = new HashMap<String, Integer>();
	public List<Material> banBlock = new ArrayList<Material>();
	public List<Material> banItem = new ArrayList<Material>();
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//복구용 맵
	public List<RepairMap> mapList = new ArrayList<RepairMap>();
	public String locPath;
	private int lastGamePlayerCnt = 0;
	
	//////// 사이드바
	private Sidebar bdbSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public BuildBattle(EGServer server, String cmdMain) {

		//////////////////// 필수 설정값
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "§7[ §e! §7] §f: §c빌드 콘테스트 §f>> "; // 기본 메세지
		gameName = "BuildBattle";
		disPlayGameName = ChatColor.RED+"빌드 콘테스트";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 4;
		maxPlayer = 12;
		startCountTime = 100;
		//복구용 맵 생성
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		for(int i = 0; i < 12; i++) {
			mapList.add(new RepairMap(server, "area"+i));	
		}	
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드	
		
		/////////////////////// 자동 설정(아이템등등)
		dirSetting("BuildBattle");
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		bdbSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
		/////////////////// 게임 도우미
		inven_gameHelper = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 도우미");

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			inven_gameHelper.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			inven_gameHelper.setItem(i, item);
		}
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c승리조건 §7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7주제에 맞는 건축물을 만든 후");
		loreList.add("§7심사 시간에 가장 많은 점수를 얻으면 승리합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7게임이 시작되면 주제가 주어집니다.(예: 헬리콥터)");
		loreList.add("§75분간 해당 주제에 맞는 건축물을 만듭니다.");
		loreList.add("§7건축 시간이 지나면 참여한 플레이어들이 직접 심사를 진행합니다.");
		loreList.add("§7심사를 통해 가장 많은 점수를 얻으면 승리합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item_score1 = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item_score1.getItemMeta();
		meta.setDisplayName("§7[ §c1점 §7]");
		loreList.add("");
		loreList.add("§7우클릭시 해당 점수를 줍니다.");
		meta.setLore(loreList);
		item_score1.setItemMeta(meta);
		
		item_score2 = new ItemStack(Material.WOOL, 1, (byte)1);
		meta = item_score2.getItemMeta();
		meta.setDisplayName("§7[ §c2점 §7]");
		loreList.add("");
		loreList.add("§7우클릭시 해당 점수를 줍니다.");
		meta.setLore(loreList);
		item_score2.setItemMeta(meta);
		
		item_score3 = new ItemStack(Material.WOOL, 1, (byte)4);
		meta = item_score3.getItemMeta();
		meta.setDisplayName("§7[ §c3점 §7]");
		loreList.add("");
		loreList.add("§7우클릭시 해당 점수를 줍니다.");
		meta.setLore(loreList);
		item_score3.setItemMeta(meta);
		
		item_score4 = new ItemStack(Material.WOOL, 1, (byte)5);
		meta = item_score4.getItemMeta();
		meta.setDisplayName("§7[ §c4점 §7]");
		loreList.add("");
		loreList.add("§7우클릭시 해당 점수를 줍니다.");
		meta.setLore(loreList);
		item_score4.setItemMeta(meta);
		
		item_score5 = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item_score5.getItemMeta();
		meta.setDisplayName("§7[ §c5점 §7]");
		loreList.add("");
		loreList.add("§7우클릭시 해당 점수를 줍니다.");
		meta.setLore(loreList);
		item_score5.setItemMeta(meta);
		
		event = new EventHandlerBBT(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
		
		setTopic();
		setBanBlock();
		setBanItem();
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
			if(reConnectList.contains(p.getName())) {
				ingamePlayer.add(p.getName());
				moveToMyArea(p);
				sendMessage(ms+p.getName()+" 님이 재접속 하셨습니다.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 0.1f);
				if(gameStep == 3) p.setGameMode(GameMode.ADVENTURE);
				else p.setGameMode(GameMode.CREATIVE);
				bdbSidebar.showTo(p);
				server.playerList.put(p.getName(), disPlayGameName);
				server.spawnList.remove(p.getName());
				server.waitingPlayer.remove(p.getName());
			} else {
				if(canSpectate && loc_spectate != null) {
					MyUtility.allClear(p);
					p.sendMessage(server.ms_alert + "이미 게임이 시작되었습니다. 관전장소로 이동되며 관전 전용 채팅을 사용합니다.");	
					p.teleport(loc_spectate, TeleportCause.PLUGIN);
					server.specList.put(p.getName(), this);
					p.setGameMode(GameMode.SPECTATOR);
					p.setFlying(true);
				} else {
					p.sendMessage(server.ms_alert + "이미 게임이 시작되었습니다."); 
				}
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
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 참여했습니다. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			p.sendMessage(ms+"맵 복구가 필요한 게임입니다. 시작 대기시간이 100초입니다.");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
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
	public void startCount() {
		if(ingame) return;
		lobbyStart = true;
		startSch.cancelTask(true);
		startSch.schTime = startCountTime;
		if(lastGamePlayerCnt == 0) lastGamePlayerCnt = maxPlayer;
		for(int i = 0; i < lastGamePlayerCnt; i++) {
			RepairMap map = mapList.get(i);
			map.Repair(startCountTime-10);
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " 게임이 " + ChatColor.GRAY + "곧 시작됩니다.");
		sendBossbar(ChatColor.GREEN+"게임 시작까지", startSch.schTime);
		startSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if (startSch.schTime > 0) {
					if (startSch.schTime == 30 || startSch.schTime == 10) {
						sendMessage(server.ms_alert + ChatColor.GRAY + "게임이 "
								+ ChatColor.AQUA + startSch.schTime + ChatColor.GRAY + "초 뒤 시작됩니다.");
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
					}
					startSch.schTime -= 1;
				} else {
					ingame = true;
					lobbyStart = false;
					startSch.cancelTask(true);
					playerSet();
					startGame();
				}
			}
		}, 0L, 20L);
	}

	@Override
	public void startGame() {
		if(ingamePlayer.size() <= 3) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "빌드 콘테스트 " + ChatColor.GRAY + "이 시작 되었습니다!");
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// 오프닝	
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		reConnectList.addAll(ingamePlayer);
		lastGamePlayerCnt = ingamePlayer.size();
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime < ingamePlayer.size()) {
					String pName = ingamePlayer.get(sch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						moveToMyArea(p);
					}
					sch.schTime++;
				}else {
					sch.cancelTask(true);
				}
			}
		}, 20l, 5l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				updateSidebar();
				for (String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)){
						p.setGameMode(GameMode.ADVENTURE);
						// 풀피로 만듬
						MyUtility.healUp(p);
						MyUtility.allClear(p);         
						//p.teleport(loc_Join);
						TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);
						bdbSidebar.showTo(p);
					}
				}
			}
		}, 150l);
		
		///////////////// 진짜 시작
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 2;
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						giveBaseItem();
						p.setGameMode(GameMode.CREATIVE);	
						gameTimer();
					}
				}
				topic = topicList.get(MyUtility.getRandom(0, topicList.size()-1));
				sendTitle("§e§l"+topic, "§c§l이것을 표현하세요!", 80);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
			}
		}, 250l);
	}
	
	//////////////////
	
	private void setTopic() {
		topicList.add("태극기");
		topicList.add("소방차");
		topicList.add("의자");
		topicList.add("가위");
		topicList.add("칼");
		topicList.add("나무");
		topicList.add("컴퓨터");
		topicList.add("마이크");
		topicList.add("키보드");
		topicList.add("시계");
		topicList.add("물통");
		topicList.add("안경");
		topicList.add("계단");
		topicList.add("엘레베이터");
		topicList.add("에스컬레이터");
		topicList.add("종이");
		topicList.add("풀잎");
		topicList.add("스피커");
		topicList.add("자동차");
		topicList.add("경찰차");
		topicList.add("인간");
		topicList.add("총");
		topicList.add("모자");
		topicList.add("군대");
		topicList.add("수건");
		topicList.add("약");
		topicList.add("약국");
		topicList.add("마인크래프트");
		topicList.add("카카오톡");
		topicList.add("신발");
		topicList.add("신발끈");
		topicList.add("요정");
		topicList.add("물");
		topicList.add("불");
		topicList.add("바람");
		topicList.add("다이아몬드");
		topicList.add("에메랄드");
		topicList.add("철");
		topicList.add("석탄");
		topicList.add("잔디");
		topicList.add("돌");
		topicList.add("점토");
		topicList.add("울타리");
		topicList.add("지옥");
		topicList.add("천국");
		topicList.add("화산");
		topicList.add("대한민국");
		topicList.add("제주도");
		topicList.add("독도");
		topicList.add("바다");
		topicList.add("계곡");
		topicList.add("폭포");
		topicList.add("장갑");
		topicList.add("책상");
		topicList.add("휴지");
		topicList.add("컴퓨터");
		topicList.add("단풍잎");
		topicList.add("나뭇가지");
		topicList.add("새싹");
		topicList.add("화분");
		topicList.add("사자");
		topicList.add("호랑이");
		topicList.add("토끼");
		topicList.add("거북이");
		topicList.add("개미");
		topicList.add("거미");
		topicList.add("거미줄");
		topicList.add("향수");
		topicList.add("짱구");
		topicList.add("도라에몽");
		topicList.add("가방");
		topicList.add("지갑");
		topicList.add("연필");
		topicList.add("지우개");
		topicList.add("책");
		topicList.add("학교");
		topicList.add("물방울");
		topicList.add("휴지통");
		topicList.add("열쇠");
		topicList.add("문");
		topicList.add("얼음");
		topicList.add("휴대전화");
		topicList.add("피자");
		topicList.add("치킨");
		topicList.add("돈");
		topicList.add("선풍기");
		topicList.add("아파트");
		topicList.add("주택");
		topicList.add("소");
		topicList.add("강아지");
		topicList.add("전구");
		topicList.add("유리");
		topicList.add("창문");
		topicList.add("사진");
		topicList.add("지구");
		topicList.add("바지");
		topicList.add("허리띠");
		topicList.add("날개");
		topicList.add("심장");
		topicList.add("아령");
		topicList.add("축구공");
		topicList.add("야구공");
		topicList.add("카드");
		topicList.add("양털");
		topicList.add("곡괭이");
		topicList.add("삽");
		topicList.add("도끼");
		topicList.add("활");
		topicList.add("화살");
		topicList.add("낚시대");
		topicList.add("당근");
		topicList.add("가지");
		topicList.add("토마토");
		topicList.add("라이터");
		topicList.add("칫솔");
		topicList.add("치약");
		topicList.add("한글");
		topicList.add("영어");
		topicList.add("수영");
		topicList.add("꽃");
		topicList.add("뼈다귀");
		topicList.add("새");
		topicList.add("횃불");
		topicList.add("모닥불");
		topicList.add("화석");
		topicList.add("감자");
		topicList.add("쿠키");
		topicList.add("상자");
		topicList.add("지구");
		topicList.add("사과");
		topicList.add("깔때기");
		topicList.add("가죽");
		topicList.add("나침반");
		topicList.add("우주선");
		topicList.add("고기");
		topicList.add("태양");
		topicList.add("병원");
		topicList.add("은행");
		topicList.add("스펀지");
		topicList.add("청금석");
		topicList.add("설탕");
		topicList.add("난로");
		topicList.add("괭이");
		topicList.add("사탕수수");
		topicList.add("지하철");
		topicList.add("오리");
		topicList.add("기차");
		topicList.add("비행기");
		topicList.add("자전거");
		topicList.add("스키");
		topicList.add("신문");
		topicList.add("소리");
		topicList.add("사전");
		topicList.add("놀이터");
		topicList.add("목걸이");
		topicList.add("반지");
		topicList.add("팔찌");
		topicList.add("팽귄");
		topicList.add("곰");
		topicList.add("지붕");
		topicList.add("구멍");
		topicList.add("철장");
		topicList.add("태극기");
		topicList.add("방송");
		topicList.add("영화");
		topicList.add("버섯");
		topicList.add("가로등");
		topicList.add("캠핑");
		topicList.add("텐트");
		topicList.add("냄비");
		topicList.add("후라이팬");
		topicList.add("라면");
		topicList.add("케이크");
		topicList.add("신호등");
		topicList.add("도로");
		topicList.add("인도");
		topicList.add("무지개");
		topicList.add("구름");
		topicList.add("하늘");
		topicList.add("산");
		topicList.add("밥그릇");
		topicList.add("농장");
		topicList.add("게임");
		topicList.add("횡단보도");
		topicList.add("반블럭");
		topicList.add("화이트데이");
		topicList.add("사탕");
		topicList.add("초콜릿");
		topicList.add("양초");
		topicList.add("헬리콥터");
		topicList.add("콘서트장");
		topicList.add("대나무");
		topicList.add("쇼파");
		topicList.add("텔레비젼");
		topicList.add("인터넷");
		topicList.add("여권");
		//200
		topicList.add("미국");
		topicList.add("전봇대");
		topicList.add("이마트");
		topicList.add("잠자리");
		topicList.add("이불");
		topicList.add("달");
		topicList.add("밤");
		topicList.add("도토리");
		topicList.add("교실");
		topicList.add("오토바이");
		//210
		topicList.add("달력");
		topicList.add("태풍");
		topicList.add("돋보기");
		topicList.add("클립");
		topicList.add("호수");
		topicList.add("해바라기");
		topicList.add("카메라");
		topicList.add("망치");
		topicList.add("세면대");
		topicList.add("바코드");
		//220
		topicList.add("김");
		topicList.add("탱크");
		topicList.add("피아노");
		topicList.add("전투기");
		topicList.add("원");
		topicList.add("소화기");
		topicList.add("올림픽");
		topicList.add("귤");
		topicList.add("포도");
		topicList.add("옥수수");
		//230
		topicList.add("그네");
		topicList.add("시소");
		topicList.add("미끄럼틀");
		topicList.add("철봉");
		topicList.add("스케이트");
		topicList.add("하트");
		topicList.add("클로버");
		topicList.add("우유");
		topicList.add("파도");
		topicList.add("자유의 여신상");
		//240
		topicList.add("비");
		topicList.add("우산");
		topicList.add("엔더드래곤");
		topicList.add("여행");
		topicList.add("하이킥");
		topicList.add("톱");
		topicList.add("축구장");
		topicList.add("진주");
		topicList.add("빵");
		topicList.add("방파제");
		//250
		topicList.add("양파");
		topicList.add("송전탑");
		topicList.add("컵");
		topicList.add("크레인");
		topicList.add("넥타이");
		topicList.add("별");
		topicList.add("큐브");
		topicList.add("주사위");
		topicList.add("음표");
		topicList.add("리본");
		//260
		topicList.add("단무지");
		topicList.add("언덕");
		topicList.add("빗자루");
		topicList.add("무인도");
		topicList.add("기타");
		topicList.add("막대기");
		topicList.add("와이파이");
		topicList.add("드릴");
		topicList.add("못");
		topicList.add("오목");
		//270		
	}
	
	private void setBanBlock() {
		banBlock.add(Material.TNT);
		banBlock.add(Material.ARMOR_STAND);
		banBlock.add(Material.BREWING_STAND);
		banBlock.add(Material.BARRIER);
		banBlock.add(Material.END_CRYSTAL);
		banBlock.add(Material.BEACON);
		banBlock.add(Material.DISPENSER);
		banBlock.add(Material.PISTON_BASE);
		banBlock.add(Material.PISTON_STICKY_BASE);
		banBlock.add(Material.ITEM_FRAME);
		banBlock.add(Material.MINECART);
		banBlock.add(Material.EXPLOSIVE_MINECART);
		banBlock.add(Material.STORAGE_MINECART);
		banBlock.add(Material.COMMAND_MINECART);
		banBlock.add(Material.HOPPER_MINECART);
		banBlock.add(Material.REDSTONE);
		banBlock.add(Material.POWERED_MINECART);
		banBlock.add(Material.ANVIL);
		banBlock.add(Material.RAILS);
		banBlock.add(Material.ACTIVATOR_RAIL);
		banBlock.add(Material.DETECTOR_RAIL);
		banBlock.add(Material.POWERED_RAIL);
		banBlock.add(Material.REDSTONE);
		banBlock.add(Material.REDSTONE_LAMP_OFF);
		banBlock.add(Material.REDSTONE_LAMP_ON);
		banBlock.add(Material.REDSTONE_TORCH_ON);
		banBlock.add(Material.REDSTONE_TORCH_OFF);
		banBlock.add(Material.DROPPER);
		banBlock.add(Material.ENDER_CHEST);
		banBlock.add(Material.CHEST);
		banBlock.add(Material.TRAPPED_CHEST);
	}
	
	private void setBanItem() {
		banItem.add(Material.ENDER_PEARL);
		banItem.add(Material.POTION);
		banItem.add(Material.LINGERING_POTION);
		banItem.add(Material.SPLASH_POTION);
		banItem.add(Material.BOAT);
		banItem.add(Material.BOAT_ACACIA);
		banItem.add(Material.BOAT_BIRCH);
		banItem.add(Material.BOAT_DARK_OAK);
		banItem.add(Material.BOAT_JUNGLE);
		banItem.add(Material.BOAT_SPRUCE);
		banItem.add(Material.EXP_BOTTLE);
		banItem.add(Material.ITEM_FRAME);
	}
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		SidebarString blank = new SidebarString("");
		if(gameStep == 2) {			
			textList.add(blank);
			SidebarString line = new SidebarString("§e주제");
			textList.add(line);
			line = new SidebarString("§c"+topic);
			textList.add(line);
			textList.add(blank);
			line = new SidebarString("§e참여인원 §f: §a"+ingamePlayer.size()+"명");
			textList.add(line);
			textList.add(blank);
			line = new SidebarString("§e남은시간 §f: §a"+mainSch.schTime);
			textList.add(line);
			textList.add(blank);
		} else if(gameStep >= 3) {	
			textList.add(blank);
			SidebarString line = new SidebarString("§e주제");
			textList.add(line);
			line = new SidebarString("§c"+topic);
			textList.add(line);
			textList.add(blank);	
			line = new SidebarString("§e작품 주인");
			textList.add(line);
			line = new SidebarString("§c"+screenedpName);
			textList.add(line);
			textList.add(blank);	
		}
		bdbSidebar.setEntries(textList);
		bdbSidebar.update();
	}
	
	public void gameTimer() {
		
		mainSch.cancelTask(true);
		mainSch.schTime = 330;
		sendBossbar(ChatColor.YELLOW+"§l심사까지 남은 시간", 330);
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(mainSch.schTime > 0) {
					mainSch.schTime--;
					updateSidebar();
					if(mainSch.schTime < 30) {					
						if(mainSch.schTime == 29) sendTitle("§6§l종료 임박!", "§e§l이제 마무리해주세요!", 100);
						if(mainSch.schTime <= 5) {
							sendTitle("§c§l"+mainSch.schTime, "§e§l심사 시작까지", 30);
							sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 2.0f);
						}else {
							sendSound(Sound.BLOCK_NOTE_HAT, 1.0f, 2.0f);
						}
					}
				}else {
					mainSch.cancelTask(true);
					screening();
				}
			}
		}, 0l, 20l);
		
	}
	
	public void screening() {
		if(!ingame || ingamePlayer.size() <= 0) return;
		gameStep = 3;
		mainSch.cancelTask(true);
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.setGameMode(GameMode.ADVENTURE);
				p.setAllowFlight(true);
				p.setFlying(true);
				giveScreenItem(p);
			}
		}
		mainSch.schTime = 30;
		mainSch.schTime2 = 0;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				while(mainSch.schTime2 < reConnectList.size()) {
					String pName = reConnectList.get(mainSch.schTime2);
					mainSch.schTime2++;
					if(ingamePlayer.contains(pName)) {
						screenedpName = pName;
						scoreMap.put(screenedpName, 0);
						giverMap.clear();
						sendBossbar("§a§l"+screenedpName+" §e§l님의 작품 심사시간", 30);
						sendTitle("§e§l"+pName, "§6§l님의 작품입니다.", 60);
						updateSidebar();
						for(String tName : ingamePlayer) {
							Player t = Bukkit.getPlayer(tName);
							if(existPlayer(t)) {
								t.teleport(loc_Start.get(reConnectList.indexOf(pName)), TeleportCause.PLUGIN);
							}
						}
						sendSound(Sound.BLOCK_NOTE_XYLOPHONE);
						Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
							public void run() {
								sendTitle("§e§l심사 요청", "§b§l아직 심사를 하지 않은 심사관은 채첨해주세요!", 80);
							}
						}, 300l);
						return;
					}
				}
				mainSch.cancelTask(true);
				screeningEnd();
			}
		}, 0l, 600l);
	}
	
	public void screeningEnd() {
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				MyUtility.allClear(p);
			}
		}
		gameStep = 4;
		List<String> rankList = MyUtility.sortByValue(scoreMap, false); //높은순으로 나열
		List<String> tmpList = new ArrayList<String>(scoreMap.keySet());
		if(rankList.size() <= 0) endGame(true);
		sendSound(Sound.BLOCK_NOTE_XYLOPHONE);
		sendMessage(ms+"결과 발표\n");
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime >= tmpList.size()) {
					sch.cancelTask(true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							String winner = rankList.get(0);
							win(winner);
						}
					}, 60l);
				}
				else {
					String pName = tmpList.get(sch.schTime);
					sendMessage("§b----------- §6"+pName+ " §7: §c"+scoreMap.get(pName)+"점");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 0.1f);
				}
				sch.schTime++;
			}
		}, 60l, 20l);
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		gameStep = 0;
		reConnectList.clear();
		//스케쥴
		scoreMap.clear();
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		for(int i = 1; i <= 12; i++) {
			Location l = loadLocation(gameName, "start"+i);
			if(l != null) {
				loc_Start.add(l);
			}
		}
		
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_spectate = loc_Start.get(0).clone().add(0,10,0);
		}
		if (loc_Start.size() < 12) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 지점이 완벽하게 설정되지 않았습니다.");
			ret = false;
		}
		for(int i = 0; i < 12; i++) {
			RepairMap map = mapList.get(i);
			if (!map.loadData(locPath)) {
				server.egPM.printLog("[" + disPlayGameName + "] 맵"+i+"이 설정되지 않았습니다.");
				ret = false;
			}
		}
		
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
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
							} else if (cmd[3].equalsIgnoreCase("map")) {
								if(cmd[5].equalsIgnoreCase("pos1")) {
									RepairMap map = mapList.get(Integer.valueOf(cmd[4]));
									map.setPos1(p.getTargetBlock(null, 3).getLocation());
									p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
									map.saveMapBlocksWithout(locPath, Material.SNOW_BLOCK);
								} else if(cmd[5].equalsIgnoreCase("pos2")) {
									RepairMap map = mapList.get(Integer.valueOf(cmd[4]));
									map.setPos2(p.getTargetBlock(null, 3).getLocation());
									p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
									map.saveMapBlocksWithout(locPath, Material.SNOW_BLOCK);
								}
							} else if (cmd[3].equalsIgnoreCase("start")) {
								saveLocation(gameName, "start"+cmd[4], p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
							}else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"은 올바르지 않은 인수");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"인수를 입력해주세요.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - 게임 시작 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc start 1~12 - 게임 시작 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map 1 pos1 - 구역1 맵 1지점");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map 1 pos2 - 구역1 맵 2지점");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + "/wog set loc- 게임 지점 설정");
				}
			} else if (cmd[1].equalsIgnoreCase("join")) {
				if(cmd.length > 2) joinGame(Bukkit.getPlayer(cmd[2]));
				else joinGame(p);
			} else if (cmd[1].equalsIgnoreCase("forceend")) {
				endGame(true);
			} else if (cmd[1].equalsIgnoreCase("quit")) {
				gameQuitPlayer(p, true, false);
			} else if (cmd[1].equalsIgnoreCase("block")) {
				if(joinBlock) joinBlock = false;
				else joinBlock = true;
			} else if (cmd[1].equalsIgnoreCase("start")) {
				startCount();
			} else if(cmd[1].equalsIgnoreCase("debug0")) {
				RepairMap map = mapList.get(Integer.valueOf(cmd[2]));
				map.Repair(Integer.valueOf(cmd[3]));
			} else if(cmd[1].equalsIgnoreCase("debug1")) {
				gameStep = 2;
			}
		} else {
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" join - 게임 참가");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" quit - 게임 퇴장");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set - 게임 설정");
		}
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			server.playerList.put(p.getName(), "로비");
			p.getInventory().clear();
			updateSidebar();
			if (ingame) {
				if(giveGold) {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						//sendTitle("", ChatColor.YELLOW+p.getName()+"님 퇴장", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
					p.sendMessage(ms+"게임 플레이 보상으로 10골드를 받으셨습니다.");
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(bdbSidebar != null) bdbSidebar.hideFrom(p);
				if(ingamePlayer.size() <= 1) {
					setWinner();
				}
			} else {
				if(announce) {
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다. "
							+ ChatColor.RESET + "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
					sendSound(Sound.BLOCK_CLOTH_BREAK,1.5f, 1.5f);			
				}
				if(ingamePlayer.size() <= 0) {
					if(lobbyStart) {
						lobbyStart = false;
						startSch.cancelTask(true);
					}				
				}
			}
		}
	}
	
	public void giveBaseItem() {
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.getInventory().setItem(8, helpItem);
			}
		}
	}
	
	public void giveScreenItem(Player p) {
		MyUtility.allClear(p);
		p.getInventory().setItem(0, item_score1);
		p.getInventory().setItem(1, item_score2);
		p.getInventory().setItem(2, item_score3);
		p.getInventory().setItem(3, item_score4);
		p.getInventory().setItem(4, item_score5);
		p.getInventory().setItem(8, helpItem);
	}
	
	public void giveScore(Player giver, int score) {
		if(!scoreMap.containsKey(screenedpName)) return;
		giver.playSound(giver.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
		if(giver.getName().equalsIgnoreCase(screenedpName)) {
			giver.sendMessage(ms+"스스로 평가는 불가능합니다.");
			return;
		}
		if(giverMap.containsKey(giver.getName())) {
			int givedScore = giverMap.get(giver.getName());
			int nowScore = scoreMap.get(screenedpName);
			scoreMap.put(screenedpName, nowScore - givedScore + score);
			giverMap.put(giver.getName(), score);
			TitleAPI.sendFullTitle(giver, 10, 60, 10, "§a§l"+score+"점", "§6§l으로 §c§l재평가 §6§l하였습니다.");
		} else {
			int nowScore = scoreMap.get(screenedpName);
			scoreMap.put(screenedpName, nowScore + score);
			giverMap.put(giver.getName(), score);
			TitleAPI.sendFullTitle(giver, 10, 60, 10, "§a§l"+score+"점", "§6§l으로 §c§l평가 §6§l하였습니다.");		
		}
	}
	
	public void moveToMyArea(Player p) {
		p.teleport(loc_Start.get(reConnectList.indexOf(p.getName())), TeleportCause.PLUGIN);
	}
	
	public RepairMap getMyArea(Player p) {
		return mapList.get(reConnectList.indexOf(p.getName()));
	}
	
	public void performence(Location loc) {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					for(int i = 0; i <3; i++) {
						Location tmpL = loc.clone();
						tmpL.add(MyUtility.getRandom(-3, 3), MyUtility.getRandom(-3, 3), MyUtility.getRandom(-3, 3));
						Firework fw = (Firework) tmpL.getWorld().spawnEntity(tmpL, EntityType.FIREWORK);
						FireworkMeta fwm = fw.getFireworkMeta();

						fwm.setPower(1);
						fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

						fw.setFireworkMeta(fwm);
						fw.detonate();
						
				        Firework fw2 = (Firework) tmpL.getWorld().spawnEntity(tmpL, EntityType.FIREWORK);
				        fw2.setFireworkMeta(fwm);
					}				
				}else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 20l);
	}

	private void win(String winner) {
		if(ending) return;
		ending = true;
		gameStep = 4;
		Player winnerP = Bukkit.getPlayer(winner);
		if(!existPlayer(winnerP)) endGame(true);
		if(ingamePlayer.size() >= 1) {
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					p.teleport(loc_Start.get(reConnectList.indexOf(winner)), TeleportCause.PLUGIN);
				}
			}
			screenedpName = winner;
			updateSidebar();
			sendTitle("§6§l"+winner, ChatColor.GRAY + "님이 1등을 차지하였습니다!", 120);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence(winnerP.getLocation());			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								if(winner.equalsIgnoreCase(pName)) {
									//server.egGM.giveGold(p.getName(), 40);
									//p.sendMessage(ms + "승리 보상으로 30골드를 받으셨습니다.");
								} else {
									//server.egGM.giveGold(p.getName(), 40);
									//p.sendMessage(ms + "게임 참여 보상으로 20골드를 받으셨습니다.");
								}							
							}
						}
					}
				}, 120L);
			} catch (Exception e) {
				
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c빌드 콘테스트§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		} else {
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c빌드 콘테스트§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	}
	
	private void setWinner() {
		if(ending) return;
		ending = true;
		gameStep = 4;
		mainSch.cancelTask(true);
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			Player winnerP = Bukkit.getPlayer(winner);
			if(!existPlayer(winnerP)) endGame(true);
			sendTitle("승리", ChatColor.GRAY + "당신을 제외한 모든 플레이어가 나갔습니다.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence(winnerP.getLocation());			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								//server.egGM.giveGold(p.getName(), 40);
								//p.sendMessage(ms + "승리 보상으로 10골드를 받으셨습니다.");
								sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
							}
						}
					}
				}, 120L);
			} catch (Exception e) {
				
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c빌드 콘테스트§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		} else {
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c빌드 콘테스트§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "빌드 콘테스트 게임이 강제 종료 되었습니다.");
		}
		divideSpawn();
		//Bukkit.getLogger().info("mainid: "+mainSch.schId);
		try {
			for(EGScheduler sch : schList) {
				sch.cancelTask(false);
			}
			
		}catch(Exception e) {
			
		}
		
		schList.clear();
		ingame = false;
		ending = false;
		ingamePlayer.clear();
		for(RepairMap map : mapList) {
			map.repaired = false;
			map.Repair(startCountTime);
		}
		initGame();	
	}

	//////////////// 이벤트
	public class EventHandlerBBT extends EGEventHandler {

		private BuildBattle game;

		public EventHandlerBBT(EGServer server, BuildBattle game) {
			super(server);
			this.game = game;
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
				if(server.cmdSpawn.contains(cmd[0])) {
					gameQuitPlayer(p, true, false);
				}
			}
		}

		@EventHandler
		public void onFall(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();
				if (ingamePlayer.contains(p.getName())) {
					e.setCancelled(true);
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //허공뎀 = 자기위치로
				            moveToMyArea(p);
				        }
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
			if (!ingamePlayer.contains(p.getName()))
				return;
				/*if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l게임설명")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelperAbClick(p, e.getSlot());	*/
			ItemStack item = e.getCurrentItem();
			if(item != null) {
				Material type = item.getType();
				if(banBlock.contains(type) || banItem.contains(type)) e.setCancelled(true);
			}
			} 
		}
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return;
			gameQuitPlayer(p, true, false);
		}

		@EventHandler
		public void onRightClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();
			if (!ingamePlayer.contains(p.getName()) //우클릭만 허용
					|| (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK))
				return;
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //아이템을 안들고 우클릭했을때 리턴
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score1.getItemMeta().getDisplayName())) {
				giveScore(p, 1);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score2.getItemMeta().getDisplayName())) {
				giveScore(p, 2);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score3.getItemMeta().getDisplayName())) {
				giveScore(p, 3);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score4.getItemMeta().getDisplayName())) {
				giveScore(p, 4);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score5.getItemMeta().getDisplayName())) {
				giveScore(p, 5);
			} else {
				if(banItem.contains(e.getItem().getType()) || banItem.contains(e.getItem().getType())) {
					p.sendMessage(ms+"해당 블럭은 사용이 불가능합니다.");
					e.setCancelled(true);
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
		public void onPlayerDeath(PlayerDeathEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(ingamePlayer.contains(p.getName())) 
					gameQuitPlayer(p, true, true);
			}
		}
		
		@EventHandler
		public void onVehicleSpawn(VehicleCreateEvent e) {
			Vehicle vehicle = e.getVehicle();
			List<Entity> nearby = vehicle.getNearbyEntities(8, 8, 8);
			Entity near = null;
			double minDis = 10;
			for(Entity entity : nearby) {
				if(entity instanceof Player) {
					double dis = entity.getLocation().distance(vehicle.getLocation());
					if(dis < minDis) {
						near = entity;
						minDis = dis;
					}
				}
			}
			
			if(near instanceof Player) {
				Player p = (Player) near;
				if(ingamePlayer.contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}
		
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
				if(gameStep >= 3) {
					if(p.getName().equals(screenedpName)) {
						String str = game.ms+p.getName()+" >> §6"+e.getMessage()+" §7[§c작품 주인§7]";
						server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);	
					}else {
						String str = game.ms+p.getName()+" >> §6"+e.getMessage()+" §7[§c심사관§7]";
						server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);	
					}
				}else {
					String str = game.ms+p.getName()+" >> §6"+e.getMessage();
					server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);	
				}			
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			e.setCancelled(true);
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(gameStep != 2) e.setCancelled(true);
				else {
					RepairMap map = getMyArea(p);
					if(!isInArea(e.getBlock().getLocation(), map.pos1, map.pos2)) {
						e.setCancelled(true);
					}
				}
			}
		}
		
		@EventHandler
		public void onUseBucket(PlayerBucketEmptyEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(e.getBucket().toString().contains("LAVA")) {
					p.sendMessage(ms+"해당 블럭은 사용이 불가능합니다.");
					e.setCancelled(true);
				}
			}
		}

		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(gameStep != 2) e.setCancelled(true);
				else {
					RepairMap map = getMyArea(p);
					if(!isInArea(e.getBlock().getLocation(), map.pos1, map.pos2)) {
						e.setCancelled(true);
					} else {
						if(banBlock.contains(e.getBlock().getType())) {
							p.sendMessage(ms+"해당 블럭은 사용이 불가능합니다.");
							e.setCancelled(true);
						}
					}
				}
			}
		}
		
		
	}
	
	///////////////////// 빌드 콘테스트에 참가한 플레이어들 클래스
	private class BdbPlayer {
		
		public BdbPlayer(Player p, String job) {

		}

	}
}
