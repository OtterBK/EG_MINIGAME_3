package EGServer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import EGServer.DataManger.PlayerData;
import EGServer.DataManger.MinigameData.FtmData;
import EGServer.DataManger.MinigameData.MinigameData;
import EGServer.DataManger.MinigameData.RwwData;
import EGServer.DataManger.MinigameData.WogData;
import EGServer.ServerManager.EGPlugin;
import Utility.SkullCreator;
import net.md_5.bungee.api.ChatColor;

public class EGServerGUI extends EGPlugin {

	//////////아이템
	public ItemStack serverHelper;
	
	//////////////////////인벤토리
	public Inventory inven_serverHelper;
	public Inventory inven_minigame;
	public Inventory inven_myInfo;
	public Inventory inven_serverInfo;
	public Inventory inven_option;
	public Inventory inven_friend;
	public Inventory inven_party;
	public Inventory inven_report;
	public Inventory inven_ftmSelect;
	public Inventory inven_wogSelect;
	public Inventory inven_hrwSelect;
	public Inventory inven_rwwSelect;
	public Inventory inven_komSelect;
	public Inventory inven_patchNote;
	public Inventory inven_reportMenu;
	public Inventory inven_info_FindTheMurder;
	public Inventory inven_info_RandomWeaponWar;
	public Inventory inven_info_WarOfGod;
	
	public boolean spawnParkour = false; //스폰에서 파쿠르 쓰면 겜 들갈땐  꺼야함
	
	public EGServerGUI(EGServer server) {
		super(server);
		
		/////////////////// 서버 도우미 아이템과 인벤토리
		ItemStack item;
		ItemMeta meta;
		List<String> loreList;

		serverHelper = new ItemStack(Material.NETHER_STAR, 1);
		meta = serverHelper.getItemMeta();
		meta.setDisplayName("§f[ §6서버 도우미 §f]");
		List<String> lorelist = new ArrayList<String>(1);
		lorelist.add("§f- §7우클릭시 서버 메뉴를 엽니다.");
		meta.setLore(lorelist);
		serverHelper.setItemMeta(meta);

		inven_serverHelper = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "EG서버");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)5);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 8; i += 1) {
			inven_serverHelper.setItem(i, item);
		}
		for (int i = 18; i <= 27; i += 1) {
			inven_serverHelper.setItem(i, item);
		}
		inven_serverHelper.setItem(9, item);
		inven_serverHelper.setItem(17, item);
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 45; i <= 53; i += 1) {
			inven_serverHelper.setItem(i, item);
		}
		
		inven_serverHelper.setItem(27, item);
		inven_serverHelper.setItem(36, item);
		inven_serverHelper.setItem(35, item);
		inven_serverHelper.setItem(44, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4107c0e1d1b7f6087741482253182c2f85d66188a169d3ee8c6c354e43c8f333");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b미니게임 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 여러가지 미니게임을 플레이해보세요!");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(13, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b48ce1cf18af05a576d608123001b791fedb622911ef8d38a320da3bcbf6fd20");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §e내정보 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 자신의 정보를 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(30, item);
		

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b02af3ca2d5a160ca1114048b7947594269afe2b1b5ec255ee72b683b60b99b9");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §a서버 정보 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- EG서버의 규칙, 정보를 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(31, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/ec2ff244dfc9dd3a2cef63112e7502dc6367b0d02132950347b2b479a72366dd");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §6옵션 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 개인 옵션을 설정합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(32, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/1d597aa448651aae60201ae13f7e030d13da397fbbe75c35d6714612b0c76a5b");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b친구 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 친구 관련 메뉴를 엽니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_serverHelper.setItem(39, item);
	
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6e415f951dc9c34204c223f0f500cbc0f1dfe772bf4b9b3bd1730251de65810c");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §d파티 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 다른 유저와 함께 파티를 맺어");
		loreList.add("  §7플레이할 수 있습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_serverHelper.setItem(40, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/af6402265dacaca3ab63143a9c9508c7191da83010e97707b47fb1092cfa987a");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c신고하기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 규칙을 위반한 유저를 신고합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_serverHelper.setItem(41, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e5be22b5d4a875d77df3f7710ff4578ef27939a9684cbdb3d31d973f166849");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c패치노트 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 최신 업데이트 내역을 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(41, item);
		
		//////////////////////미니게임 인벤
		
		inven_minigame = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "미니게임");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 8; i += 1) {
			inven_minigame.setItem(i, item);
		}
		for (int i = 9; i <= 53; i += 9) {
			inven_minigame.setItem(i, item);
		}
		for (int i = 8; i <= 53; i += 9) {
			inven_minigame.setItem(i, item);
		}
		
		for (int i = 45; i <= 53; i += 1) {
			inven_minigame.setItem(i, item);
		}
		
		for (int i = 13; i <= 53; i += 9) {
			inven_minigame.setItem(i, item);
		}
		
		/*item = new ItemStack(Material.BARRIER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§c준비중...");
		item.setItemMeta(meta);
		for (int i = 20; i <= 24; i += 1) {
			inven_minigame.setItem(i, item);
		}

		for (int i = 29; i <= 33; i += 1) {
			inven_minigame.setItem(i, item);
		}*/
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/cee6ab0fcbc68257274cbc322e13a39503cc3d788a36166379ffd57214eec9d1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cMain Games §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- EG서버의 특색있는 게임입니다.");
		loreList.add("§7  비교적 플레이 시간이 깁니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(11, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6193f8064d36a01787d3e59f5266b0e497dffb5f59f9ed8dd9dd508406e486b3");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cSimple Games §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 간단한 방식의 미니게임입니다.");
		loreList.add("§7  가볍게 즐기실 수 있습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(15, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c살인자를 찾아라 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 자신만의 직업을 이용하여 살인자를 찾아내야하는");
		loreList.add("§7  추리와 심리게임입니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(19, item);
				
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b히어로즈워 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 다양한 능력과 스킬을 사용해서 신전을 점령하여");
		loreList.add("§7  상대의 영혼을 전부 소진시키면 승리하는");
		loreList.add("§7  팀 게임입니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(20, item);

		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §6미니 신들의 전쟁 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 스카이블럭에서 신의 능력을 이용하여");
		loreList.add("§7  상대팀의 코어를 부숴야하는 팀 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 15분");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(21, item);
		
		item = new ItemStack(Material.FEATHER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §a파쿠르 레이싱 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 파쿠르 기술들을 사용하여 도시를 질주하고");
		loreList.add("§7  다른 플레이어의 최고기록을 깨뜨려보세요!");
		loreList.add("");
		loreList.add("§7  인원제한없는 개인용 게임으로");
		loreList.add("§7  여유롭게 즐기실 수 있습니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 없음");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(28, item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b랜덤 무기 전쟁 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 무작위로 주어지는 특별한 무기 및 아이템을 가지고");
		loreList.add("§7  다른 플레이어를 처치해야하는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 12분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(29, item);
		
		item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b광물의 왕 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 두더지 잡기의 마인크래프트 버젼!");
		loreList.add("§7  높은 점수를 얻어 서버 랭킹에 올라보세요!");
		loreList.add("");
		loreList.add("§7  1인용 게임으로");
		loreList.add("§7  간단하게 즐기실 수 있습니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 3분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(30, item);
		
		
		///////심플 게임
		
		item = new ItemStack(Material.MINECART, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §8자리뺏기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 살아남기 위해 다른 플레이어를 방해하며");
		loreList.add("§7  마인카트에 올라타야하는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(23, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §f컬러매치 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 계속해서 변하는 양털 블럭에서 순발력을 이용해");
		loreList.add("§7  끝까지 살아남아야하는 개인전 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(24, item);
		
		item = new ItemStack(Material.TNT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §4폭탄 돌리기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 폭탄을 가진 술래에게서 도망치거나");
		loreList.add("§7  폭탄을 다른 플레이어에게 전달하며");
		loreList.add("§7  마지막까지 살아남으면 승리합니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 8분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(25, item);
			
		item = new ItemStack(Material.PAINTING, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §e빌드 콘테스트 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 주어지는 주제에 맞는 건축물이나 모형을 만들어");
		loreList.add("§7  누가 가장 표현을 잘 했는지 서로 평가하는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 7분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(32, item);
			
		item = new ItemStack(Material.STAINED_CLAY, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §d데스런 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 점차 사라지는 블럭들 위에서 최대한 오래");
		loreList.add("§7  생존하여 최후의 1인이 되는게 목표입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(33, item);
		
		item = new ItemStack(Material.DIAMOND_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §1스플리프 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 상대방 아래에 있는 눈 블럭을 파괴하고 떨어뜨리며");
		loreList.add("§7  탈락시켜 모든 경쟁자를 탈락시키세요!");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(34, item);
		
		item = new ItemStack(Material.ANVIL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §8모루 피하기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 하늘에서 떨어지는 모루를 피해서");
		loreList.add("§7  끝까지 생존하세요!");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(41, item);
		
		item = new ItemStack(Material.STICK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §6선빵 게임 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 먼저 치는 사람이 무조건 이긴다!");
		loreList.add("§7  적을 먼저 때려 처치하고 목표점수를 달성하자!");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(42, item);
		
		item = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §a팀 랜덤 배틀 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 무작위로 주어지는 레벨과 무기, 갑옷을 이용해 인첸트하고");
		loreList.add("§7  팀 PVP를 진행하여 서로 우위를 겨루는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 12분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_minigame.setItem(39, item);
		
		item = new ItemStack(Material.SPRUCE_DOOR_ITEM, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §7뒤로가기 §7::");
		loreList = new ArrayList<String>();
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(45, item);

		/////////////////////////서버정보 인벤토리
		
		inven_serverInfo = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "서버정보");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_serverInfo.setItem(i, item);
		}
		
		inven_serverInfo.setItem(17, item);
		inven_serverInfo.setItem(18, item);
		inven_serverInfo.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_serverInfo.setItem(i, item);
		}
		
		inven_serverInfo.setItem(27, item);
		inven_serverInfo.setItem(36, item);
		inven_serverInfo.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/fd761cc16562c88d2fbe40ad38502bc3b4a87859887dbc35f272e30d8070eeec");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c서버 규칙 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- EG서버의 기본적인 규칙입니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(19, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/f1345fc263ffd4c1654af205fa9b9e88605d4bedf81417bacb30f8a3cfd9626b");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §6명령어 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 서버에서 사용가능한 명령어입니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(21, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/527ec459ec8ba35af9bb4c7e049c7ce8d2345025bc47446647a51b814b309db8");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §d카페 주소 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- EG서버의  카페주소입니다.");
		loreList.add("§7  카페에서 다른 유저들과 소통해보세요.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(23, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b2efa83c998233e9deaf7975ace4cd16b6362a859d5682c36314d1e60af");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §d디스코드 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- EG서버의  디스코드 주소입니다.");
		loreList.add("§7  디스코드에서 발빠른 소식을 받아보세요.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(25, item);
		
		/////////////////////////옵션 인벤토리
		
		inven_option = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "옵션");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_option.setItem(i, item);
		}
		
		inven_option.setItem(17, item);
		inven_option.setItem(18, item);
		inven_option.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_option.setItem(i, item);
		}
		
		inven_option.setItem(27, item);
		inven_option.setItem(36, item);
		inven_option.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/5461518b74d5f7016f72294756fc68c5471110cc97f3bb093e0c6ed94a9e3");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c홍보 메세지 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 홍보 메세지 보기 여부를 ON/OFF 합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_option.setItem(20, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/317bc3c6f35a91030f2c68b95d78bc4eb848358f5089fcde437cd1faa943b");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c채팅 소리 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 채팅 소리 여부를 ON/OFF 합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_option.setItem(22, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8a29a5be9a3b1f7fb1f761a862d5cd876c31298ecf47802946c5ef4eb39ac");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c공지 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 게임시작, 종료 알림과 기본 공지");
		loreList.add("§7  보기여부를 ON/OFF 합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_option.setItem(24, item);	
		
		/////////////////////////패치노트 인벤토리
		
		inven_patchNote = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "패치노트");
		
		/*item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_patchNote.setItem(i, item);
		}
		
		inven_patchNote.setItem(17, item);
		inven_patchNote.setItem(18, item);
		inven_patchNote.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_patchNote.setItem(i, item);
		}
		
		inven_patchNote.setItem(27, item);
		inven_patchNote.setItem(36, item);
		inven_patchNote.setItem(35, item);*/
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e5be22b5d4a875d77df3f7710ff4578ef27939a9684cbdb3d31d973f166849");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c20.02.20 패치 §7::");
		//loreList = new ArrayList<String>();
		//loreList.add("");
		//loreList.add("§7- 확성기 메세지 보기 여부를 ON/OFF 합니다.");
		//loreList.add("");
		//meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_patchNote.setItem(3, item);	
			
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e5be22b5d4a875d77df3f7710ff4578ef27939a9684cbdb3d31d973f166849");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c버젼 - 0.73.2b §7::");
		//loreList = new ArrayList<String>();
		//loreList.add("");
		//loreList.add("§7- 확성기 메세지 보기 여부를 ON/OFF 합니다.");
		//loreList.add("");
		//meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_patchNote.setItem(5, item);
		
		int patchSetIndex = 18;
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/2e5793f0cc40a9368252714bc5263a5c3df2233bddf8a57e3d8d3f54af6726c");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c패치노트 보기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_patchNote.setItem(patchSetIndex, item);
		patchSetIndex += 1;
		
		////신전 채널선택
		
		inven_wogSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "미니 신들의 전쟁");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_wogSelect.setItem(i, item);
		}
		
		inven_wogSelect.setItem(17, item);
		inven_wogSelect.setItem(18, item);
		inven_wogSelect.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_wogSelect.setItem(i, item);
		}
			
		for (int i = 18; i <= 26; i++) {
			inven_wogSelect.setItem(i, item);
		}
		
		inven_wogSelect.setItem(27, item);
		inven_wogSelect.setItem(36, item);
		inven_wogSelect.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c1채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 기본 평지 스카이블럭입니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_wogSelect.setItem(11, item);	
		

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c2채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 지옥 컨셉의 스카이블럭입니다.");
		loreList.add("§7  난이도가 더 어렵습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_wogSelect.setItem(15, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c랭크 게임 채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 그리스 컨셉의 대형맵입니다.");
		loreList.add("§7  게임 결과가 MMR에 반영됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_wogSelect.setItem(31, item);
		
		/////////////////////////
		
		////히어워 채널선택
		
		inven_hrwSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "히어로즈워");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_hrwSelect.setItem(i, item);
		}
		
		inven_hrwSelect.setItem(17, item);
		inven_hrwSelect.setItem(18, item);
		inven_hrwSelect.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_hrwSelect.setItem(i, item);
		}
		
		for (int i = 18; i <= 26; i++) {
			inven_hrwSelect.setItem(i, item);
		}
		
		inven_hrwSelect.setItem(27, item);
		inven_hrwSelect.setItem(36, item);
		inven_hrwSelect.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c1채널 §f(§a일반 모드§f)§7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 소규모 전장입니다.");
		loreList.add("§7  처음하시는 분들은  이 맵에서 기본적인 것을");
		loreList.add("§7  익히고 가시는걸 추천 드립니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 20분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_hrwSelect.setItem(11, item);	
		

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c2채널 §f(§a일반 모드§f)§7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 대규모 전장입니다.");
		loreList.add("§7  맵이 넓기에 기본적인 룰을 이해하고");
		loreList.add("§7  플레이하시는 것을 추천 드립니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 25분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_hrwSelect.setItem(15, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/31eead1e17ecf53f222475daeebad3526137b797e5cb767b98ac5e7ebb9fe9");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c원포인트 모드 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 스킬을 연습해보기 좋은 모드입니다.");
		loreList.add("§7  [점령지 1개], [토템없음], [상점없음] 룰을 사용하며");
		loreList.add("§7 궁극기는 쿨타임 방식이 아닌 충전 방식을 사용합니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 15분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_hrwSelect.setItem(29, item);	
		
		/////////////////////////
		
		////살인마 채널선택
		
		inven_ftmSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "살인자를 찾아라");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_ftmSelect.setItem(i, item);
		}
		
		inven_ftmSelect.setItem(17, item);
		inven_ftmSelect.setItem(18, item);
		inven_ftmSelect.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_ftmSelect.setItem(i, item);
		}
		
		for (int i = 18; i <= 26; i++) {
			inven_ftmSelect.setItem(i, item);
		}
		
		inven_ftmSelect.setItem(27, item);
		inven_ftmSelect.setItem(36, item);
		inven_ftmSelect.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c1채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 랭크가 기록되지 않는 채널입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 12분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_ftmSelect.setItem(13, item);	
		
		//골드 스타 머리
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c랭크 게임 채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 게임 기록이 MMR에 반영되는 채널입니다.");
		loreList.add("§a랭킹룰: 미치광이 없음, 8명 이상시 협상가 무조건 등장");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 12분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_ftmSelect.setItem(31, item);
		
		////랜무 채널선택
		
		inven_rwwSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "랜덤 무기 전쟁");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_rwwSelect.setItem(i, item);
		}
		
		inven_rwwSelect.setItem(17, item);
		inven_rwwSelect.setItem(18, item);
		inven_rwwSelect.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_rwwSelect.setItem(i, item);
		}
		
		for (int i = 18; i <= 26; i++) {
			inven_rwwSelect.setItem(i, item);
		}
		
		inven_rwwSelect.setItem(27, item);
		inven_rwwSelect.setItem(36, item);
		inven_rwwSelect.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c1채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- '베를린' 맵에서 전투합니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 12분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_rwwSelect.setItem(13, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c2채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- '용의 배' 맵에서 전투합니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 10분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_rwwSelect.setItem(31, item);	
		
		/////////////////////////
		
		////광물의 왕 채널선택
		
		inven_komSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "광물의 왕");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_komSelect.setItem(i, item);
		}
		
		inven_komSelect.setItem(17, item);
		inven_komSelect.setItem(18, item);
		inven_komSelect.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_komSelect.setItem(i, item);
		}
		
		for (int i = 18; i <= 26; i++) {
			inven_komSelect.setItem(i, item);
		}
		
		inven_komSelect.setItem(27, item);
		inven_komSelect.setItem(36, item);
		inven_komSelect.setItem(35, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/71bc2bcfb2bd3759e6b1e86fc7a79585e1127dd357fc202893f9de241bc9e530");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c1채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 1채널");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(10, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c2채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 2채널");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(12, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/1d4eae13933860a6df5e8e955693b95a8c3b15c36b8b587532ac0996bc37e5");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c3채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 3채널");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(14, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/d2e78fb22424232dc27b81fbcb47fd24c1acf76098753f2d9c28598287db5");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c4채널 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 4채널");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(16, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c랭킹 확인 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 랭킹을 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(31, item);	
		
		/////////////////////////
		
		/////////////////////////내정보 인벤토리
		
		inven_myInfo = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "내 정보");
		
		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		
		for (int i = 0; i <= 9; i += 1) {
			inven_myInfo.setItem(i, item);
		}
		
		inven_myInfo.setItem(17, item);
		inven_myInfo.setItem(18, item);
		inven_myInfo.setItem(26, item);
		
		for (int i = 36; i <= 44; i += 1) {
			inven_myInfo.setItem(i, item);
		}
		
		inven_myInfo.setItem(27, item);
		inven_myInfo.setItem(36, item);
		inven_myInfo.setItem(35, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c살인자를 찾아라 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 살인자를 찾아라의 게임 기록을 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_myInfo.setItem(10, item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c랜덤 무기 전쟁 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 랜덤  무기 전쟁의 게임 기록을 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_myInfo.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c신들의 전쟁 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 신들의 전쟁의 게임 기록을 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_myInfo.setItem(14, item);
		
		////////////////////////내정보 인벤토리

		inven_info_FindTheMurder = Bukkit.createInventory(null, 54, "§0§l살인자를 찾아라 기록");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);

		for (int i = 0; i <= 9; i += 1) {
			inven_info_FindTheMurder.setItem(i, item);
		}

		inven_info_FindTheMurder.setItem(17, item);
		inven_info_FindTheMurder.setItem(18, item);
		inven_info_FindTheMurder.setItem(26, item);

		for (int i = 45; i <= 53; i += 1) {
			inven_info_FindTheMurder.setItem(i, item);
		}

		inven_info_FindTheMurder.setItem(27, item);
		inven_info_FindTheMurder.setItem(36, item);
		inven_info_FindTheMurder.setItem(35, item);
		inven_info_FindTheMurder.setItem(44, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cMMR §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 당신의 MMR :§a ");
		loreList.add("§7- 당신의 랭크 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(13, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c공통 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 총 플레이 수 :§a ");
		loreList.add("§7- 승리 :§a ");
		loreList.add("§7- 패배 :§a ");
		loreList.add("§7- 무고한 플레이어를 살행할뻔한 수 :§a ");
		loreList.add("§7- 살인자 팀으로 시민팀을 살해한 수 :§a ");
		loreList.add("§7- 시민팀으로 살인자팀을 살해한 수 :§a ");
		loreList.add("§7- 포박된 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(19, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c의사 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 플레이어를 살린 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(20, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c경찰 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 살인자 팀을 찾은 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(21, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c배신자 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 접선한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(22, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c군인 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 사망을 저지한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(23, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c미치광이 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 미치광이로 시민팀을 살해한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(24, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c기자 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 능력을 사용한 수 :§a ");
		loreList.add("§7- 능력으로 살인자팀을 찾은 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(25, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c성직자 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 표의 영향력을 행사한 수 :§a ");
		loreList.add("§7- 포박이 저지된 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(28, item);
			
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c마술사 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 능력을 사용한 수 :§a ");
		loreList.add("§7- 살인자팀을 대상으로 능력을 사용한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(29, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c발명가 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 추가 아이템을 얻은 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(30, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c농부 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 농부로 처치한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(31, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c계약자 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 의뢰를 완수한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(32, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c영매사 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 능력을 사용한 수 :§a ");
		loreList.add("§7- 살인자팀에게 능력을 사용한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(33, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c협상가 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 포섭에 성공한 수 :§a ");
		loreList.add("§7- 포섭에 실패한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(34, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c열쇠공 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 열쇠를 사용한 수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(37, item);
		
		////////////////////////내정보 랜무 인벤토리

		inven_info_RandomWeaponWar = Bukkit.createInventory(null, 54, "§0§l랜덤 무기 전쟁 기록");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);

		for (int i = 0; i <= 9; i += 1) {
			inven_info_RandomWeaponWar.setItem(i, item);
		}

		inven_info_RandomWeaponWar.setItem(17, item);
		inven_info_RandomWeaponWar.setItem(18, item);
		inven_info_RandomWeaponWar.setItem(26, item);

		for (int i = 45; i <= 53; i += 1) {
			inven_info_RandomWeaponWar.setItem(i, item);
		}

		inven_info_RandomWeaponWar.setItem(27, item);
		inven_info_RandomWeaponWar.setItem(36, item);
		inven_info_RandomWeaponWar.setItem(35, item);
		inven_info_RandomWeaponWar.setItem(44, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cMMR §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 당신의 MMR :§a ");
		loreList.add("§7- 당신의 랭크 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_RandomWeaponWar.setItem(13, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c공통 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 총 플레이 횟수 :§a ");
		loreList.add("§7- 1위를 달성한 횟수 :§a ");
		loreList.add("§7- 킬수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_RandomWeaponWar.setItem(19, item);
		
		////////////////////////내정보 신전 인벤토리

		inven_info_WarOfGod = Bukkit.createInventory(null, 54, "§0§l신들의 전쟁 기록");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);

		for (int i = 0; i <= 9; i += 1) {
			inven_info_WarOfGod.setItem(i, item);
		}

		inven_info_WarOfGod.setItem(17, item);
		inven_info_WarOfGod.setItem(18, item);
		inven_info_WarOfGod.setItem(26, item);

		for (int i = 45; i <= 53; i += 1) {
			inven_info_WarOfGod.setItem(i, item);
		}

		inven_info_WarOfGod.setItem(27, item);
		inven_info_WarOfGod.setItem(36, item);
		inven_info_WarOfGod.setItem(35, item);
		inven_info_WarOfGod.setItem(44, item);

		item = SkullCreator.itemFromUrl(
				"http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cMMR §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 당신의 MMR :§a ");
		loreList.add("§7- 당신의 랭크 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_WarOfGod.setItem(13, item);

		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c공통 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 총 플레이 횟수 :§a ");
		loreList.add("§7- 승리 :§a ");
		loreList.add("§7- 코어를 파괴한 횟수 :§a ");
		loreList.add("§7- 코블스톤을 캔 횟수 :§a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_WarOfGod.setItem(19, item);

			
		
		server.getServer().getPluginManager().registerEvents(new ServerGUIEvent(), server);

	}
	///게임 정보
	public Inventory getMurderInfo(String pName) {
		
		PlayerData data = server.egDM.getPlayerData(pName);
		if(data == null) return null;
		MinigameData minigameData = data.getGameData("FindTheMurder");
		if(minigameData != null) {
								
			if(!(minigameData instanceof FtmData)) return null;
			FtmData ftmData = (FtmData) minigameData;
			Inventory inven = Bukkit.createInventory(null, 54, "§0§l살인마를 찾아라 기록");
			
			inven.setContents(inven_info_FindTheMurder.getContents());
			
			ItemStack fixItem = inven.getItem(13); //MMR아이템
			ItemMeta meta = fixItem.getItemMeta();
			
			List<String> loreList = meta.getLore();
			loreList.set(1, "§7당신의 MMR : §a"+ftmData.getMMR());
			loreList.set(2, "§7당신의 랭크 : §a"+ftmData.getRankName());
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			fixItem = inven.getItem(19); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 총 플레이 수 :§a "+ftmData.getPlayCount());
			loreList.add("§7- 승리 :§a "+ftmData.getWin());
			loreList.add("§7- 패배 :§a "+ftmData.getDefeat());
			loreList.add("§7- 무고한 플레이어를 살행할뻔한 수 :§a "+ftmData.innocent_kill);
			loreList.add("§7- 살인자 팀으로 시민팀을 살해한 수 :§a "+ftmData.murder_kill);
			loreList.add("§7- 시민팀으로 살인자팀을 살해한 수 :§a "+ftmData.civil_kill);
			loreList.add("§7- 포박된 수 :§a "+ftmData.beVotedPlayer);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			//////////////////////////////////////
			fixItem = inven.getItem(20); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 플레이어를 살린 수 :§a "+ftmData.doctor_revive);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(21); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 살인자 팀을 찾은 수 :§a "+ftmData.police_success);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(22); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 접선한 수 :§a "+ftmData.spy_contact);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(23); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 사망을 저지한 수 :§a "+ftmData.soldier_revive);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(24); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("");
			loreList.add("§7- 미치광이로 시민팀을 살해한 수 :§a "+ftmData.crazy_kill);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(25); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 능력을 사용한 수 :§a "+ftmData.reporter_report);
			loreList.add("§7- 능력으로 살인자팀을 찾은 수 :§a "+ftmData.reporter_reportSuccess);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(28); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 표의 영향력을 행사한 수 :§a "+ftmData.priest_effort);
			loreList.add("§7- 포박이 저지된 수 :§a "+ftmData.priest_noVoted);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(29); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 능력을 사용한 수 :§a "+ftmData.magician_take);
			loreList.add("§7- 살인자팀을 대상으로 능력을 사용한 수 :§a "+ftmData.magician_takeMurderTeam);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(30); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 추가 아이템을 얻은 수 :§a "+ftmData.creator_addictionItem);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(31); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 농부로 처치한 수 :§a "+ftmData.farmer_kill);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(32); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 의뢰를 완수한 수 :§a "+ftmData.contractor_success);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(33); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 능력을 사용한 수 :§a "+ftmData.shaman_success);
			loreList.add("§7- 살인자팀에게 능력을 사용한 수 :§a "+ftmData.shaman_successMurderTeam);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(34); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 포섭에 성공한 수 :§a "+ftmData.negotiator_success);
			loreList.add("§7- 포섭에 실패한 수 :§a "+ftmData.negotiator_fail);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(37); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 열쇠를 사용한 수 :§a "+ftmData.keySmith_Use);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);			
			
			return inven;
		}else {
			return null;
		}
	}
	
	public Inventory getRandomWeaponWarInfo(String pName) {
		
		PlayerData data = server.egDM.getPlayerData(pName);
		if(data == null) return null;
		MinigameData minigameData = data.getGameData("RandomWeaponWar");
		if(minigameData != null) {
								
			if(!(minigameData instanceof RwwData)) return null;
			RwwData rwwData = (RwwData) minigameData;
			Inventory inven = Bukkit.createInventory(null, 54, "§0§l랜덤 무기 전쟁 기록");
			
			inven.setContents(inven_info_RandomWeaponWar.getContents());
			
			ItemStack fixItem = inven.getItem(13); //MMR아이템
			ItemMeta meta = fixItem.getItemMeta();
			
			List<String> loreList = meta.getLore();
			loreList.set(1, "§7당신의 MMR : §a"+rwwData.getMMR());
			loreList.set(2, "§7당신의 랭크 : §a"+rwwData.getRankName());
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			fixItem = inven.getItem(19); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 총 플레이 횟수 :§a "+rwwData.getPlayCount());
			loreList.add("§7- 1위를 달성한 횟수 :§a "+rwwData.first);
			loreList.add("§7- 킬수 :§a "+rwwData.kill);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			//////////////////////////////////////
			
			return inven;
		}else {
			return null;
		}
	}
	
	public Inventory getWarOfGodInfo(String pName) {
		
		PlayerData data = server.egDM.getPlayerData(pName);
		if(data == null) return null;
		MinigameData minigameData = data.getGameData("WarOfGod");
		if(minigameData != null) {
								
			if(!(minigameData instanceof WogData)) return null;
			WogData wogData = (WogData) minigameData;
			Inventory inven = Bukkit.createInventory(null, 54, "§0§l신들의 전쟁 기록");
			
			inven.setContents(inven_info_WarOfGod.getContents());
			
			ItemStack fixItem = inven.getItem(13); //MMR아이템
			ItemMeta meta = fixItem.getItemMeta();
			
			List<String> loreList = meta.getLore();
			loreList.set(1, "§7당신의 MMR : §a"+wogData.getMMR());
			loreList.set(2, "§7당신의 랭크 : §a"+wogData.getRankName());
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			fixItem = inven.getItem(19); //공통아이템
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("§7- 총 플레이 횟수 :§a "+wogData.getPlayCount());
			loreList.add("§7- 승리 :§a "+wogData.getWin());
			loreList.add("§7- 코어를 파괴한 수 :§a "+wogData.breakCore);
			loreList.add("§7- 채적한 코블스톤 갯수 :§a "+wogData.breakCobbleStone);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			//////////////////////////////////////
			
			return inven;
		}else {
			return null;
		}
	}
			
			
			
	
	//////////인벤클릭
	public void serverHelperClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 13: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.openInventory(inven_minigame);
				break;
			case 30: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				//p.sendMessage(server.ms_alert+"곧 추가 예정입니다. 감사합니다.");
				p.openInventory(inven_myInfo);
				break;
			case 31: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.openInventory(inven_serverInfo);
				break;		
			case 32: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				//p.sendMessage(server.ms_alert+"곧 추가 예정입니다. 감사합니다.");
				p.openInventory(inven_option);
				closeInven = false;
				break;	
			case 39: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage(server.ms_alert+"곧 추가 예정입니다. 감사합니다.");
				//p.openInventory(inven_option);
				closeInven = true;
				break;
			case 40: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage(server.ms_alert+"곧 추가 예정입니다. 감사합니다.");
				//p.openInventory(inven_option);
				closeInven = true;
				break;
			/*case 41: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage(server.ms_alert+"곧 추가 예정입니다. 감사합니다.");
				//p.openInventory(inven_option);
				closeInven = true;
				break;*/
			case 41:
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.openInventory(inven_patchNote);
				// p.openInventory(inven_option);
				closeInven = false;
			break;
		}
		if(closeInven) p.closeInventory();
	}
	
	public void minigameClick(Player p, int slot, boolean isLeft){
		boolean closeInven = false;
		
		switch (slot) {
			case 19: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					p.openInventory(inven_ftmSelect);
					closeInven = false;
				}else {
					p.openInventory(server.findTheMurder.gameHelper);
				}
				break;
			case 20: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					p.openInventory(inven_hrwSelect);
					closeInven = false;
				}else {
					p.openInventory(server.heroesWar.inven_gameHelper);
				}
				break;
			case 21: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					p.openInventory(inven_wogSelect);
					closeInven = false;
				}else {
					p.openInventory(server.warOfGod.inven_gameHelper);
				}
				break;				
			case 28: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.parkourRacing.joinGame(p);
					closeInven = true;
				}else {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.parkourRacing.joinGame(p);
					closeInven = true;
				}
				break;
			case 29: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					p.openInventory(inven_rwwSelect);
					closeInven = false;
				}else {
					p.openInventory(server.randomWeaponWar.inven_gameHelper);
					closeInven = false;
				}
				break;
			case 30: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					p.openInventory(inven_komSelect);
					closeInven = false;
				}else {
					p.openInventory(server.kingOfMine1.inven_gameHelper);
					closeInven = false;
				}
				break;
				
			case 23: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.takeASeet.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.takeASeet.inven_gameHelper);
				}
				break;
			case 24: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.colorMatch.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.colorMatch.inven_gameHelper);
				}
				break;
				
			case 25: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);			
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.tntBomber.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.tntBomber.inven_gameHelper);
				}
				break;
			case 32: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.buildBattle.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.buildBattle.inven_gameHelper);
				}
				break;
			case 33: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.deathRun.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.deathRun.inven_gameHelper);
				}
				break;
			case 34: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.spleef.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.spleef.inven_gameHelper);
				}
				break;	
				
			case 41: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.avoidTheAnvil.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.avoidTheAnvil.inven_gameHelper);
				}
				break;
				
			case 42: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(isLeft) {
					if(spawnParkour) server.egParkour.quit(p.getName());
					server.firstHit.joinGame(p);
					closeInven = true;
				}else {
					p.openInventory(server.firstHit.inven_gameHelper);
				}
				break;
				
			case 45: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.openInventory(inven_serverHelper);
				closeInven = false;
				break;
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void serverInfoClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 19: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage("\n§c1. 게임은 게임일뿐 서로 얼굴 붉히지 말아주세요.");
				p.sendMessage("\n§c2. 타인에 대한 비방 및 욕설은 절대 금지입니다.");
				p.sendMessage("\n§c3. 가급적이면 유저간 존칭을 사용해주세요.");
				closeInven = true;
				break;
			case 21: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				server.printCommandList(p);
				closeInven = true;
				break;
			case 23: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage("\n§7↓클릭하시면 됩니다.↓\n§7https://cafe.naver.com/boli2");
				closeInven = true;
				break;	
			case 25: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage("\n§7↓클릭하시면 됩니다.↓\n§7https://discord.gg/QDzUwGn");
				closeInven = true;
				break;
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void patchNoteClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 18: 
				p.sendMessage(server.ms_alert+"죄송합니다. 디스코드를 통해 확인해주세요.");
				p.sendMessage("\n§7↓클릭하시면 됩니다.↓\n§7https://discord.gg/QDzUwGn");
				closeInven = true;
				break;	
		}
		if(closeInven) p.closeInventory();
	}
	
	
	public void optionClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 20: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(server.egCM.noSpeakerList.contains(p.getName())) {
					server.egCM.noSpeakerList.remove(p.getName());
					p.sendMessage(server.ms_alert+"§c이제 홍보 메세지를 볼 수 있습니다.");
				} else {
					server.egCM.noSpeakerList.add(p.getName());
					p.sendMessage(server.ms_alert+"§c이제 홍보 메세지를 보지 않습니다.");
				}
				closeInven = true;
				break;
			case 22: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(server.egCM.noSoundList.contains(p.getName())) {
					server.egCM.noSoundList.remove(p.getName());
					p.sendMessage(server.ms_alert+"§c이제 채팅소리를 듣습니다.");
				} else {
					server.egCM.noSoundList.add(p.getName());
					p.sendMessage(server.ms_alert+"§c이제 채팅소리를 듣지 않습니다.");
				}
				closeInven = true;
				break;	
			case 24: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(server.egCM.noAlertList.contains(p.getName())) {
					server.egCM.noAlertList.remove(p.getName());
					p.sendMessage(server.ms_alert+"§c이제 공지 메세지를 볼 수 있습니다.");
				} else {
					server.egCM.noAlertList.add(p.getName());
					p.sendMessage(server.ms_alert+"§c이제 공지 메세지를 보지 않습니다.");
				}
				closeInven = true;
				break;	
				
		}
		if(closeInven) p.closeInventory();
	}	
	
	public void myInfoClick(Player p, int slot){
		boolean closeInven = false;
		Inventory myInfo = null;
		
		switch (slot) {
			case 10: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				
				myInfo = getMurderInfo(p.getName());
				if (myInfo != null) {
					p.openInventory(myInfo);
					closeInven = false;

					return;
				} else {
					p.sendMessage(server.ms_alert+"해당 게임에 대한 랭킹 기록이 없습니다.");	
				}
				
				closeInven = true;
				break;	
			case 12: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				
				myInfo = getRandomWeaponWarInfo(p.getName());
				if (myInfo != null) {
					p.openInventory(myInfo);
					closeInven = false;

					return;
				} else {
					p.sendMessage(server.ms_alert+"해당 게임에 대한 랭킹 기록이 없습니다.");	
				}
				
				closeInven = true;
				break;
			case 14: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				
				myInfo = getWarOfGodInfo(p.getName());
				if (myInfo != null) {
					p.openInventory(myInfo);
					closeInven = false;

					return;
				} else {
					p.sendMessage(server.ms_alert+"해당 게임에 대한 랭킹 기록이 없습니다.");	
				}
				
				closeInven = true;
				break;
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void wogSelectClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 11: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.warOfGod.joinGame(p);
				closeInven = true;
				break;
			case 15: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.warOfGod2.joinGame(p);
				closeInven = true;
				break;	
			case 31: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				//p.sendMessage("곧 추가됩니다."); return;
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.warOfGod3.joinGame(p);
				closeInven = true;
				break;	
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void hrwSelectClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 11: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.heroesWar.joinGame(p);
				closeInven = true;
				break;
			case 15: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.heroesWar2.joinGame(p);
				closeInven = true;
				break;	
			case 29: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.heroesWarUlaf.joinGame(p);
				closeInven = true;
				break;
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void ftmSelectClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 13: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.findTheMurder.joinGame(p);
				closeInven = true;
				break;
			case 31: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.findTheMurder2.joinGame(p);
				closeInven = true;
				break;		
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void komSelectClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 10: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.kingOfMine1.joinGame(p);
				closeInven = true;
				break;	
			case 12: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.kingOfMine2.joinGame(p);
				closeInven = true;
				break;	
			case 14: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.kingOfMine3.joinGame(p);
				closeInven = true;
				break;	
			case 16: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.kingOfMine4.joinGame(p);
				closeInven = true;
				break;	
				
			case 31: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.kingOfMine1.showRanking(p, "KingOfMineEasy");
				closeInven = true;
				break;	
		}
		if(closeInven) p.closeInventory();
	}
	
	public void rwwSelectClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 13: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.randomWeaponWar.joinGame(p);
				closeInven = true;
				break;	
			case 31: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(spawnParkour) server.egParkour.quit(p.getName());
				server.randomWeaponWar2.joinGame(p);
				closeInven = true;
				break;	
		}
		if(closeInven) p.closeInventory();
	}
	
	
	public void repairMinigameInven() {
		inven_minigame = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "미니게임");

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 8; i += 1) {
			inven_minigame.setItem(i, item);
		}
		for (int i = 9; i <= 53; i += 9) {
			inven_minigame.setItem(i, item);
		}
		for (int i = 8; i <= 53; i += 9) {
			inven_minigame.setItem(i, item);
		}
		
		for (int i = 45; i <= 53; i += 1) {
			inven_minigame.setItem(i, item);
		}
		
		for (int i = 13; i <= 53; i += 9) {
			inven_minigame.setItem(i, item);
		}
		
		/*item = new ItemStack(Material.BARRIER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§c준비중...");
		item.setItemMeta(meta);
		for (int i = 20; i <= 24; i += 1) {
			inven_minigame.setItem(i, item);
		}

		for (int i = 29; i <= 33; i += 1) {
			inven_minigame.setItem(i, item);
		}*/
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/cee6ab0fcbc68257274cbc322e13a39503cc3d788a36166379ffd57214eec9d1");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cMain Games §7::");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- EG서버의 특색있는 게임입니다.");
		loreList.add("§7  비교적 플레이 시간이 깁니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(11, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6193f8064d36a01787d3e59f5266b0e497dffb5f59f9ed8dd9dd508406e486b3");
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §cSimple Games §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 간단한 방식의 미니게임입니다.");
		loreList.add("§7  가볍게 즐기실 수 있습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(15, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §c살인자를 찾아라 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 자신만의 직업을 이용하여 살인자를 찾아내야하는");
		loreList.add("§7  추리와 심리게임입니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(19, item);
				
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b히어로즈워 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 다양한 능력과 스킬을 사용해서 신전을 점령하여");
		loreList.add("§7  상대의 영혼을 전부 소진시키면 승리하는");
		loreList.add("§7  팀 게임입니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(20, item);

		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §6미니 신들의 전쟁 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 스카이블럭에서 신의 능력을 이용하여");
		loreList.add("§7  상대팀의 코어를 부숴야하는 팀 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 15분");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(21, item);
		
		item = new ItemStack(Material.FEATHER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §a파쿠르 레이싱 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 파쿠르 기술들을 사용하여 도시를 질주하고");
		loreList.add("§7  다른 플레이어의 최고기록을 깨뜨려보세요!");
		loreList.add("");
		loreList.add("§7  인원제한없는 개인용 게임으로");
		loreList.add("§7  여유롭게 즐기실 수 있습니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 없음");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(28, item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §b랜덤 무기 전쟁 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 무작위로 주어지는 특별한 무기 및 아이템을 가지고");
		loreList.add("§7  다른 플레이어를 처치해야하는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 15분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(29, item);
		
		item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §a광물의 왕 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 두더지 잡기의 마인크래프트 버젼!");
		loreList.add("§7  높은 점수를 얻어 서버 랭킹에 올라보세요!");
		loreList.add("");
		loreList.add("§7  1인용 게임으로");
		loreList.add("§7  간단하게 즐기실 수 있습니다.");
		loreList.add("");
		loreList.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 3분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(30, item);
		
		///////심플 게임
		
		item = new ItemStack(Material.MINECART, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §8자리뺏기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 살아남기 위해 다른 플레이어를 방해하며");
		loreList.add("§7  마인카트에 올라타야하는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(23, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §f컬러매치 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 계속해서 변하는 양털 블럭에서 순발력을 이용해");
		loreList.add("§7  끝까지 살아남아야하는 개인전 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(24, item);
		
		item = new ItemStack(Material.TNT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §4폭탄 돌리기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 폭탄을 가진 술래에게서 도망치거나");
		loreList.add("§7  폭탄을 다른 플레이어에게 전달하며");
		loreList.add("§7  마지막까지 살아남으면 승리합니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 8분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(25, item);
			
		item = new ItemStack(Material.PAINTING, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §e빌드 콘테스트 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 주어지는 주제에 맞는 건축물이나 모형을 만들어");
		loreList.add("§7  누가 가장 표현을 잘 했는지 서로 평가하는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 7분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(32, item);
			
		item = new ItemStack(Material.STAINED_CLAY, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §d데스런 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 점차 사라지는 블럭들 위에서 최대한 오래");
		loreList.add("§7  생존하여 최후의 1인이 되는게 목표입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(33, item);
		
		item = new ItemStack(Material.DIAMOND_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §1스플리프 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 상대방 아래에 있는 눈 블럭을 파괴하고 떨어뜨리며");
		loreList.add("§7  탈락시켜 모든 경쟁자를 탈락시키세요!");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(34, item);
		
		item = new ItemStack(Material.ANVIL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §8모루 피하기 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 하늘에서 떨어지는 모루를 피해서");
		loreList.add("§7  끝까지 생존하세요!");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 5분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(41, item);
		
		item = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §a팀 랜덤 배틀 §7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7- 무작위로 주어지는 레벨과 무기, 갑옷을 이용해 인첸트하고");
		loreList.add("§7  팀 PVP를 진행하여 서로 우위를 겨루는 게임입니다.");
		loreList.add("");
		loreList.add("§7  평균 플레이 시간 : 12분");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_minigame.setItem(39, item);
		
		item = new ItemStack(Material.SPRUCE_DOOR_ITEM, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7:: §7뒤로가기 §7::");
		loreList = new ArrayList<String>();
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(45, item);
	}
	
	
	///////////이벤트
	public class ServerGUIEvent implements Listener{
		
		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§l옵션")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}

				optionClick(p, e.getSlot());
			}else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l내 정보")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				myInfoClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().contains("기록")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
			}
			
			if (!server.inLobby(p))
				return;
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§lEG서버")) {				
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				serverHelperClick(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l미니게임")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}		
				minigameClick(p, e.getSlot(), e.getClick().isLeftClick());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l미니 신들의 전쟁")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				wogSelectClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l히어로즈워")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				hrwSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l살인자를 찾아라")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				ftmSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l랜덤 무기 전쟁")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}		
				rwwSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l광물의 왕")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}		
				komSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l서버정보")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				serverInfoClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l패치노트")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				patchNoteClick(p, e.getSlot());	
			}
		}
		
	}

}
