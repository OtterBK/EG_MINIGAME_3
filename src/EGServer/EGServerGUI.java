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

	//////////������
	public ItemStack serverHelper;
	
	//////////////////////�κ��丮
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
	
	public boolean spawnParkour = false; //�������� ���� ���� �� �鰥��  ������
	
	public EGServerGUI(EGServer server) {
		super(server);
		
		/////////////////// ���� ����� �����۰� �κ��丮
		ItemStack item;
		ItemMeta meta;
		List<String> loreList;

		serverHelper = new ItemStack(Material.NETHER_STAR, 1);
		meta = serverHelper.getItemMeta();
		meta.setDisplayName("��f[ ��6���� ����� ��f]");
		List<String> lorelist = new ArrayList<String>(1);
		lorelist.add("��f- ��7��Ŭ���� ���� �޴��� ���ϴ�.");
		meta.setLore(lorelist);
		serverHelper.setItemMeta(meta);

		inven_serverHelper = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "EG����");

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
		meta.setDisplayName("��7:: ��b�̴ϰ��� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �������� �̴ϰ����� �÷����غ�����!");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(13, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b48ce1cf18af05a576d608123001b791fedb622911ef8d38a320da3bcbf6fd20");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��e������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ڽ��� ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(30, item);
		

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b02af3ca2d5a160ca1114048b7947594269afe2b1b5ec255ee72b683b60b99b9");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��a���� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- EG������ ��Ģ, ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(31, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/ec2ff244dfc9dd3a2cef63112e7502dc6367b0d02132950347b2b479a72366dd");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��6�ɼ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� �ɼ��� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(32, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/1d597aa448651aae60201ae13f7e030d13da397fbbe75c35d6714612b0c76a5b");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��bģ�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ģ�� ���� �޴��� ���ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_serverHelper.setItem(39, item);
	
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6e415f951dc9c34204c223f0f500cbc0f1dfe772bf4b9b3bd1730251de65810c");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��d��Ƽ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ٸ� ������ �Բ� ��Ƽ�� �ξ�");
		loreList.add("  ��7�÷����� �� �ֽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_serverHelper.setItem(40, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/af6402265dacaca3ab63143a9c9508c7191da83010e97707b47fb1092cfa987a");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c�Ű��ϱ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��Ģ�� ������ ������ �Ű��մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_serverHelper.setItem(41, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e5be22b5d4a875d77df3f7710ff4578ef27939a9684cbdb3d31d973f166849");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��ġ��Ʈ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ֽ� ������Ʈ ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverHelper.setItem(41, item);
		
		//////////////////////�̴ϰ��� �κ�
		
		inven_minigame = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "�̴ϰ���");

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
		meta.setDisplayName("��c�غ���...");
		item.setItemMeta(meta);
		for (int i = 20; i <= 24; i += 1) {
			inven_minigame.setItem(i, item);
		}

		for (int i = 29; i <= 33; i += 1) {
			inven_minigame.setItem(i, item);
		}*/
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/cee6ab0fcbc68257274cbc322e13a39503cc3d788a36166379ffd57214eec9d1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��cMain Games ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- EG������ Ư���ִ� �����Դϴ�.");
		loreList.add("��7  ���� �÷��� �ð��� ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(11, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6193f8064d36a01787d3e59f5266b0e497dffb5f59f9ed8dd9dd508406e486b3");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��cSimple Games ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ ����� �̴ϰ����Դϴ�.");
		loreList.add("��7  ������ ���� �� �ֽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(15, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c�����ڸ� ã�ƶ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ڽŸ��� ������ �̿��Ͽ� �����ڸ� ã�Ƴ����ϴ�");
		loreList.add("��7  �߸��� �ɸ������Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(19, item);
				
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��b�������� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �پ��� �ɷ°� ��ų�� ����ؼ� ������ �����Ͽ�");
		loreList.add("��7  ����� ��ȥ�� ���� ������Ű�� �¸��ϴ�");
		loreList.add("��7  �� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(20, item);

		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��6�̴� �ŵ��� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ī�̺����� ���� �ɷ��� �̿��Ͽ�");
		loreList.add("��7  ������� �ھ �ν����ϴ� �� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 15��");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(21, item);
		
		item = new ItemStack(Material.FEATHER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��a���� ���̽� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ������� ����Ͽ� ���ø� �����ϰ�");
		loreList.add("��7  �ٸ� �÷��̾��� �ְ����� ���߷�������!");
		loreList.add("");
		loreList.add("��7  �ο����Ѿ��� ���ο� ��������");
		loreList.add("��7  �����Ӱ� ���� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : ����");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(28, item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��b���� ���� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �������� �־����� Ư���� ���� �� �������� ������");
		loreList.add("��7  �ٸ� �÷��̾ óġ�ؾ��ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(29, item);
		
		item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��b������ �� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �δ��� ����� ����ũ����Ʈ ����!");
		loreList.add("��7  ���� ������ ��� ���� ��ŷ�� �ö󺸼���!");
		loreList.add("");
		loreList.add("��7  1�ο� ��������");
		loreList.add("��7  �����ϰ� ���� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 3��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(30, item);
		
		
		///////���� ����
		
		item = new ItemStack(Material.MINECART, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��8�ڸ����� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��Ƴ��� ���� �ٸ� �÷��̾ �����ϸ�");
		loreList.add("��7  ����īƮ�� �ö�Ÿ���ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(23, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��f�÷���ġ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����ؼ� ���ϴ� ���� ������ ���߷��� �̿���");
		loreList.add("��7  ������ ��Ƴ��ƾ��ϴ� ������ �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(24, item);
		
		item = new ItemStack(Material.TNT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��4��ź ������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ź�� ���� �������Լ� ����ġ�ų�");
		loreList.add("��7  ��ź�� �ٸ� �÷��̾�� �����ϸ�");
		loreList.add("��7  ���������� ��Ƴ����� �¸��մϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(25, item);
			
		item = new ItemStack(Material.PAINTING, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��e���� ���׽�Ʈ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �־����� ������ �´� ���๰�̳� ������ �����");
		loreList.add("��7  ���� ���� ǥ���� �� �ߴ��� ���� ���ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 7��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(32, item);
			
		item = new ItemStack(Material.STAINED_CLAY, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��d������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ������� ���� ������ �ִ��� ����");
		loreList.add("��7  �����Ͽ� ������ 1���� �Ǵ°� ��ǥ�Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(33, item);
		
		item = new ItemStack(Material.DIAMOND_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��1���ø��� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� �Ʒ��� �ִ� �� ���� �ı��ϰ� ����߸���");
		loreList.add("��7  Ż������ ��� �����ڸ� Ż����Ű����!");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(34, item);
		
		item = new ItemStack(Material.ANVIL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��8��� ���ϱ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ϴÿ��� �������� ��縦 ���ؼ�");
		loreList.add("��7  ������ �����ϼ���!");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(41, item);
		
		item = new ItemStack(Material.STICK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��6���� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ġ�� ����� ������ �̱��!");
		loreList.add("��7  ���� ���� ���� óġ�ϰ� ��ǥ������ �޼�����!");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(42, item);
		
		item = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��a�� ���� ��Ʋ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �������� �־����� ������ ����, ������ �̿��� ��þƮ�ϰ�");
		loreList.add("��7  �� PVP�� �����Ͽ� ���� ������ �ܷ�� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_minigame.setItem(39, item);
		
		item = new ItemStack(Material.SPRUCE_DOOR_ITEM, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��7�ڷΰ��� ��7::");
		loreList = new ArrayList<String>();
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(45, item);

		/////////////////////////�������� �κ��丮
		
		inven_serverInfo = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "��������");
		
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
		meta.setDisplayName("��7:: ��c���� ��Ģ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- EG������ �⺻���� ��Ģ�Դϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(19, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/f1345fc263ffd4c1654af205fa9b9e88605d4bedf81417bacb30f8a3cfd9626b");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��6��ɾ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �������� ��밡���� ��ɾ��Դϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(21, item);

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/527ec459ec8ba35af9bb4c7e049c7ce8d2345025bc47446647a51b814b309db8");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��dī�� �ּ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- EG������  ī���ּ��Դϴ�.");
		loreList.add("��7  ī�信�� �ٸ� ������� �����غ�����.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(23, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b2efa83c998233e9deaf7975ace4cd16b6362a859d5682c36314d1e60af");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��d���ڵ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- EG������  ���ڵ� �ּ��Դϴ�.");
		loreList.add("��7  ���ڵ忡�� �ߺ��� �ҽ��� �޾ƺ�����.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_serverInfo.setItem(25, item);
		
		/////////////////////////�ɼ� �κ��丮
		
		inven_option = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "�ɼ�");
		
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
		meta.setDisplayName("��7:: ��cȫ�� �޼��� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ȫ�� �޼��� ���� ���θ� ON/OFF �մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_option.setItem(20, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/317bc3c6f35a91030f2c68b95d78bc4eb848358f5089fcde437cd1faa943b");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��cä�� �Ҹ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ä�� �Ҹ� ���θ� ON/OFF �մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_option.setItem(22, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8a29a5be9a3b1f7fb1f761a862d5cd876c31298ecf47802946c5ef4eb39ac");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���ӽ���, ���� �˸��� �⺻ ����");
		loreList.add("��7  ���⿩�θ� ON/OFF �մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_option.setItem(24, item);	
		
		/////////////////////////��ġ��Ʈ �κ��丮
		
		inven_patchNote = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "��ġ��Ʈ");
		
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
		meta.setDisplayName("��7:: ��c20.02.20 ��ġ ��7::");
		//loreList = new ArrayList<String>();
		//loreList.add("");
		//loreList.add("��7- Ȯ���� �޼��� ���� ���θ� ON/OFF �մϴ�.");
		//loreList.add("");
		//meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_patchNote.setItem(3, item);	
			
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/e5be22b5d4a875d77df3f7710ff4578ef27939a9684cbdb3d31d973f166849");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� - 0.73.2b ��7::");
		//loreList = new ArrayList<String>();
		//loreList.add("");
		//loreList.add("��7- Ȯ���� �޼��� ���� ���θ� ON/OFF �մϴ�.");
		//loreList.add("");
		//meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_patchNote.setItem(5, item);
		
		int patchSetIndex = 18;
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/2e5793f0cc40a9368252714bc5263a5c3df2233bddf8a57e3d8d3f54af6726c");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��ġ��Ʈ ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_patchNote.setItem(patchSetIndex, item);
		patchSetIndex += 1;
		
		////���� ä�μ���
		
		inven_wogSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "�̴� �ŵ��� ����");
		
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
		meta.setDisplayName("��7:: ��c1ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �⺻ ���� ��ī�̺��Դϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_wogSelect.setItem(11, item);	
		

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c2ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ������ ��ī�̺��Դϴ�.");
		loreList.add("��7  ���̵��� �� ��ƽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_wogSelect.setItem(15, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��ũ ���� ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �׸��� ������ �������Դϴ�.");
		loreList.add("��7  ���� ����� MMR�� �ݿ��˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_wogSelect.setItem(31, item);
		
		/////////////////////////
		
		////����� ä�μ���
		
		inven_hrwSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "��������");
		
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
		meta.setDisplayName("��7:: ��c1ä�� ��f(��a�Ϲ� ����f)��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ұԸ� �����Դϴ�.");
		loreList.add("��7  ó���Ͻô� �е���  �� �ʿ��� �⺻���� ����");
		loreList.add("��7  ������ ���ô°� ��õ �帳�ϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 20��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_hrwSelect.setItem(11, item);	
		

		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c2ä�� ��f(��a�Ϲ� ����f)��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��Ը� �����Դϴ�.");
		loreList.add("��7  ���� �б⿡ �⺻���� ���� �����ϰ�");
		loreList.add("��7  �÷����Ͻô� ���� ��õ �帳�ϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 25��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_hrwSelect.setItem(15, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/31eead1e17ecf53f222475daeebad3526137b797e5cb767b98ac5e7ebb9fe9");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c������Ʈ ��� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ų�� �����غ��� ���� ����Դϴ�.");
		loreList.add("��7  [������ 1��], [���۾���], [��������] ���� ����ϸ�");
		loreList.add("��7 �ñر�� ��Ÿ�� ����� �ƴ� ���� ����� ����մϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 15��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_hrwSelect.setItem(29, item);	
		
		/////////////////////////
		
		////���θ� ä�μ���
		
		inven_ftmSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "�����ڸ� ã�ƶ�");
		
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
		meta.setDisplayName("��7:: ��c1ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ũ�� ��ϵ��� �ʴ� ä���Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_ftmSelect.setItem(13, item);	
		
		//��� ��Ÿ �Ӹ�
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��ũ ���� ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ����� MMR�� �ݿ��Ǵ� ä���Դϴ�.");
		loreList.add("��a��ŷ��: ��ġ���� ����, 8�� �̻�� ���� ������ ����");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_ftmSelect.setItem(31, item);
		
		////���� ä�μ���
		
		inven_rwwSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "���� ���� ����");
		
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
		meta.setDisplayName("��7:: ��c1ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- '������' �ʿ��� �����մϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_rwwSelect.setItem(13, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c2ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- '���� ��' �ʿ��� �����մϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 10��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_rwwSelect.setItem(31, item);	
		
		/////////////////////////
		
		////������ �� ä�μ���
		
		inven_komSelect = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "������ ��");
		
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
		meta.setDisplayName("��7:: ��c1ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- 1ä��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(10, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/4cd9eeee883468881d83848a46bf3012485c23f75753b8fbe8487341419847");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c2ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- 2ä��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(12, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/1d4eae13933860a6df5e8e955693b95a8c3b15c36b8b587532ac0996bc37e5");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c3ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- 3ä��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(14, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/d2e78fb22424232dc27b81fbcb47fd24c1acf76098753f2d9c28598287db5");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c4ä�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- 4ä��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(16, item);	
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/8216ee40593c0981ed28f5bd674879781c425ce0841b687481c4f7118bb5c3b1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��ŷ Ȯ�� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ŷ�� Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_komSelect.setItem(31, item);	
		
		/////////////////////////
		
		/////////////////////////������ �κ��丮
		
		inven_myInfo = Bukkit.createInventory(null, 45, "" + ChatColor.BLACK + ChatColor.BOLD + "�� ����");
		
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
		meta.setDisplayName("��7:: ��c�����ڸ� ã�ƶ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �����ڸ� ã�ƶ��� ���� ����� Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_myInfo.setItem(10, item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ���� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����  ���� ������ ���� ����� Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_myInfo.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c�ŵ��� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ŵ��� ������ ���� ����� Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_myInfo.setItem(14, item);
		
		////////////////////////������ �κ��丮

		inven_info_FindTheMurder = Bukkit.createInventory(null, 54, "��0��l�����ڸ� ã�ƶ� ���");

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
		meta.setDisplayName("��7:: ��cMMR ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����� MMR :��a ");
		loreList.add("��7- ����� ��ũ :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(13, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �� �÷��� �� :��a ");
		loreList.add("��7- �¸� :��a ");
		loreList.add("��7- �й� :��a ");
		loreList.add("��7- ������ �÷��̾ �����һ��� �� :��a ");
		loreList.add("��7- ������ ������ �ù����� ������ �� :��a ");
		loreList.add("��7- �ù������� ���������� ������ �� :��a ");
		loreList.add("��7- ���ڵ� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(19, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c�ǻ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �÷��̾ �츰 �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(20, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ ���� ã�� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(21, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c����� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(22, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����� ������ �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(23, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��ġ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ġ���̷� �ù����� ������ �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(24, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ɷ��� ����� �� :��a ");
		loreList.add("��7- �ɷ����� ���������� ã�� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(25, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ǥ�� ������� ����� �� :��a ");
		loreList.add("��7- ������ ������ �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(28, item);
			
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ɷ��� ����� �� :��a ");
		loreList.add("��7- ���������� ������� �ɷ��� ����� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(29, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c�߸� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �߰� �������� ���� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(30, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c��� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��η� óġ�� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(31, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c����� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �Ƿڸ� �ϼ��� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(32, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���Ż� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ɷ��� ����� �� :��a ");
		loreList.add("��7- ������������ �ɷ��� ����� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(33, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ ������ �� :��a ");
		loreList.add("��7- ������ ������ �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(34, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c����� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���踦 ����� �� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_FindTheMurder.setItem(37, item);
		
		////////////////////////������ ���� �κ��丮

		inven_info_RandomWeaponWar = Bukkit.createInventory(null, 54, "��0��l���� ���� ���� ���");

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
		meta.setDisplayName("��7:: ��cMMR ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����� MMR :��a ");
		loreList.add("��7- ����� ��ũ :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_RandomWeaponWar.setItem(13, item);
		
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �� �÷��� Ƚ�� :��a ");
		loreList.add("��7- 1���� �޼��� Ƚ�� :��a ");
		loreList.add("��7- ų�� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_RandomWeaponWar.setItem(19, item);
		
		////////////////////////������ ���� �κ��丮

		inven_info_WarOfGod = Bukkit.createInventory(null, 54, "��0��l�ŵ��� ���� ���");

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
		meta.setDisplayName("��7:: ��cMMR ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����� MMR :��a ");
		loreList.add("��7- ����� ��ũ :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_WarOfGod.setItem(13, item);

		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �� �÷��� Ƚ�� :��a ");
		loreList.add("��7- �¸� :��a ");
		loreList.add("��7- �ھ �ı��� Ƚ�� :��a ");
		loreList.add("��7- �ں����� ĵ Ƚ�� :��a ");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_info_WarOfGod.setItem(19, item);

			
		
		server.getServer().getPluginManager().registerEvents(new ServerGUIEvent(), server);

	}
	///���� ����
	public Inventory getMurderInfo(String pName) {
		
		PlayerData data = server.egDM.getPlayerData(pName);
		if(data == null) return null;
		MinigameData minigameData = data.getGameData("FindTheMurder");
		if(minigameData != null) {
								
			if(!(minigameData instanceof FtmData)) return null;
			FtmData ftmData = (FtmData) minigameData;
			Inventory inven = Bukkit.createInventory(null, 54, "��0��l���θ��� ã�ƶ� ���");
			
			inven.setContents(inven_info_FindTheMurder.getContents());
			
			ItemStack fixItem = inven.getItem(13); //MMR������
			ItemMeta meta = fixItem.getItemMeta();
			
			List<String> loreList = meta.getLore();
			loreList.set(1, "��7����� MMR : ��a"+ftmData.getMMR());
			loreList.set(2, "��7����� ��ũ : ��a"+ftmData.getRankName());
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			fixItem = inven.getItem(19); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �� �÷��� �� :��a "+ftmData.getPlayCount());
			loreList.add("��7- �¸� :��a "+ftmData.getWin());
			loreList.add("��7- �й� :��a "+ftmData.getDefeat());
			loreList.add("��7- ������ �÷��̾ �����һ��� �� :��a "+ftmData.innocent_kill);
			loreList.add("��7- ������ ������ �ù����� ������ �� :��a "+ftmData.murder_kill);
			loreList.add("��7- �ù������� ���������� ������ �� :��a "+ftmData.civil_kill);
			loreList.add("��7- ���ڵ� �� :��a "+ftmData.beVotedPlayer);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			//////////////////////////////////////
			fixItem = inven.getItem(20); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �÷��̾ �츰 �� :��a "+ftmData.doctor_revive);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(21); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ������ ���� ã�� �� :��a "+ftmData.police_success);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(22); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ������ �� :��a "+ftmData.spy_contact);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(23); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ����� ������ �� :��a "+ftmData.soldier_revive);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(24); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("");
			loreList.add("��7- ��ġ���̷� �ù����� ������ �� :��a "+ftmData.crazy_kill);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(25); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �ɷ��� ����� �� :��a "+ftmData.reporter_report);
			loreList.add("��7- �ɷ����� ���������� ã�� �� :��a "+ftmData.reporter_reportSuccess);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(28); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ǥ�� ������� ����� �� :��a "+ftmData.priest_effort);
			loreList.add("��7- ������ ������ �� :��a "+ftmData.priest_noVoted);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(29); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �ɷ��� ����� �� :��a "+ftmData.magician_take);
			loreList.add("��7- ���������� ������� �ɷ��� ����� �� :��a "+ftmData.magician_takeMurderTeam);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(30); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �߰� �������� ���� �� :��a "+ftmData.creator_addictionItem);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(31); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ��η� óġ�� �� :��a "+ftmData.farmer_kill);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(32); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �Ƿڸ� �ϼ��� �� :��a "+ftmData.contractor_success);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(33); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �ɷ��� ����� �� :��a "+ftmData.shaman_success);
			loreList.add("��7- ������������ �ɷ��� ����� �� :��a "+ftmData.shaman_successMurderTeam);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(34); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ������ ������ �� :��a "+ftmData.negotiator_success);
			loreList.add("��7- ������ ������ �� :��a "+ftmData.negotiator_fail);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			//////////////////////////////////////
			fixItem = inven.getItem(37); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- ���踦 ����� �� :��a "+ftmData.keySmith_Use);
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
			Inventory inven = Bukkit.createInventory(null, 54, "��0��l���� ���� ���� ���");
			
			inven.setContents(inven_info_RandomWeaponWar.getContents());
			
			ItemStack fixItem = inven.getItem(13); //MMR������
			ItemMeta meta = fixItem.getItemMeta();
			
			List<String> loreList = meta.getLore();
			loreList.set(1, "��7����� MMR : ��a"+rwwData.getMMR());
			loreList.set(2, "��7����� ��ũ : ��a"+rwwData.getRankName());
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			fixItem = inven.getItem(19); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �� �÷��� Ƚ�� :��a "+rwwData.getPlayCount());
			loreList.add("��7- 1���� �޼��� Ƚ�� :��a "+rwwData.first);
			loreList.add("��7- ų�� :��a "+rwwData.kill);
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
			Inventory inven = Bukkit.createInventory(null, 54, "��0��l�ŵ��� ���� ���");
			
			inven.setContents(inven_info_WarOfGod.getContents());
			
			ItemStack fixItem = inven.getItem(13); //MMR������
			ItemMeta meta = fixItem.getItemMeta();
			
			List<String> loreList = meta.getLore();
			loreList.set(1, "��7����� MMR : ��a"+wogData.getMMR());
			loreList.set(2, "��7����� ��ũ : ��a"+wogData.getRankName());
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			
			fixItem = inven.getItem(19); //���������
			meta = fixItem.getItemMeta();
			
			loreList = meta.getLore();
			
			loreList.clear();
			loreList.add("");
			loreList.add("��7- �� �÷��� Ƚ�� :��a "+wogData.getPlayCount());
			loreList.add("��7- �¸� :��a "+wogData.getWin());
			loreList.add("��7- �ھ �ı��� �� :��a "+wogData.breakCore);
			loreList.add("��7- ä���� �ں��� ���� :��a "+wogData.breakCobbleStone);
			loreList.add("");
			
			meta.setLore(loreList);
			fixItem.setItemMeta(meta);
			//////////////////////////////////////
			
			return inven;
		}else {
			return null;
		}
	}
			
			
			
	
	//////////�κ�Ŭ��
	public void serverHelperClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 13: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.openInventory(inven_minigame);
				break;
			case 30: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				//p.sendMessage(server.ms_alert+"�� �߰� �����Դϴ�. �����մϴ�.");
				p.openInventory(inven_myInfo);
				break;
			case 31: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.openInventory(inven_serverInfo);
				break;		
			case 32: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				//p.sendMessage(server.ms_alert+"�� �߰� �����Դϴ�. �����մϴ�.");
				p.openInventory(inven_option);
				closeInven = false;
				break;	
			case 39: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage(server.ms_alert+"�� �߰� �����Դϴ�. �����մϴ�.");
				//p.openInventory(inven_option);
				closeInven = true;
				break;
			case 40: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage(server.ms_alert+"�� �߰� �����Դϴ�. �����մϴ�.");
				//p.openInventory(inven_option);
				closeInven = true;
				break;
			/*case 41: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage(server.ms_alert+"�� �߰� �����Դϴ�. �����մϴ�.");
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
				p.sendMessage("\n��c1. ������ �����ϻ� ���� �� ������ �����ּ���.");
				p.sendMessage("\n��c2. Ÿ�ο� ���� ��� �� �弳�� ���� �����Դϴ�.");
				p.sendMessage("\n��c3. �������̸� ������ ��Ī�� ������ּ���.");
				closeInven = true;
				break;
			case 21: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				server.printCommandList(p);
				closeInven = true;
				break;
			case 23: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage("\n��7��Ŭ���Ͻø� �˴ϴ�.��\n��7https://cafe.naver.com/boli2");
				closeInven = true;
				break;	
			case 25: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				p.sendMessage("\n��7��Ŭ���Ͻø� �˴ϴ�.��\n��7https://discord.gg/QDzUwGn");
				closeInven = true;
				break;
				
		}
		if(closeInven) p.closeInventory();
	}
	
	public void patchNoteClick(Player p, int slot){
		boolean closeInven = false;
		
		switch (slot) {
			case 18: 
				p.sendMessage(server.ms_alert+"�˼��մϴ�. ���ڵ带 ���� Ȯ�����ּ���.");
				p.sendMessage("\n��7��Ŭ���Ͻø� �˴ϴ�.��\n��7https://discord.gg/QDzUwGn");
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
					p.sendMessage(server.ms_alert+"��c���� ȫ�� �޼����� �� �� �ֽ��ϴ�.");
				} else {
					server.egCM.noSpeakerList.add(p.getName());
					p.sendMessage(server.ms_alert+"��c���� ȫ�� �޼����� ���� �ʽ��ϴ�.");
				}
				closeInven = true;
				break;
			case 22: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(server.egCM.noSoundList.contains(p.getName())) {
					server.egCM.noSoundList.remove(p.getName());
					p.sendMessage(server.ms_alert+"��c���� ä�üҸ��� ����ϴ�.");
				} else {
					server.egCM.noSoundList.add(p.getName());
					p.sendMessage(server.ms_alert+"��c���� ä�üҸ��� ���� �ʽ��ϴ�.");
				}
				closeInven = true;
				break;	
			case 24: 
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
				if(server.egCM.noAlertList.contains(p.getName())) {
					server.egCM.noAlertList.remove(p.getName());
					p.sendMessage(server.ms_alert+"��c���� ���� �޼����� �� �� �ֽ��ϴ�.");
				} else {
					server.egCM.noAlertList.add(p.getName());
					p.sendMessage(server.ms_alert+"��c���� ���� �޼����� ���� �ʽ��ϴ�.");
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
					p.sendMessage(server.ms_alert+"�ش� ���ӿ� ���� ��ŷ ����� �����ϴ�.");	
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
					p.sendMessage(server.ms_alert+"�ش� ���ӿ� ���� ��ŷ ����� �����ϴ�.");	
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
					p.sendMessage(server.ms_alert+"�ش� ���ӿ� ���� ��ŷ ����� �����ϴ�.");	
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
				//p.sendMessage("�� �߰��˴ϴ�."); return;
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
		inven_minigame = Bukkit.createInventory(null, 54, "" + ChatColor.BLACK + ChatColor.BOLD + "�̴ϰ���");

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
		meta.setDisplayName("��c�غ���...");
		item.setItemMeta(meta);
		for (int i = 20; i <= 24; i += 1) {
			inven_minigame.setItem(i, item);
		}

		for (int i = 29; i <= 33; i += 1) {
			inven_minigame.setItem(i, item);
		}*/
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/cee6ab0fcbc68257274cbc322e13a39503cc3d788a36166379ffd57214eec9d1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��cMain Games ��7::");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- EG������ Ư���ִ� �����Դϴ�.");
		loreList.add("��7  ���� �÷��� �ð��� ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(11, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/6193f8064d36a01787d3e59f5266b0e497dffb5f59f9ed8dd9dd508406e486b3");
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��cSimple Games ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ ����� �̴ϰ����Դϴ�.");
		loreList.add("��7  ������ ���� �� �ֽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(15, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��c�����ڸ� ã�ƶ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ڽŸ��� ������ �̿��Ͽ� �����ڸ� ã�Ƴ����ϴ�");
		loreList.add("��7  �߸��� �ɸ������Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(19, item);
				
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��b�������� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �پ��� �ɷ°� ��ų�� ����ؼ� ������ �����Ͽ�");
		loreList.add("��7  ����� ��ȥ�� ���� ������Ű�� �¸��ϴ�");
		loreList.add("��7  �� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(20, item);

		item = new ItemStack(Material.DIAMOND_BLOCK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��6�̴� �ŵ��� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ī�̺����� ���� �ɷ��� �̿��Ͽ�");
		loreList.add("��7  ������� �ھ �ν����ϴ� �� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 15��");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(21, item);
		
		item = new ItemStack(Material.FEATHER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��a���� ���̽� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ������� ����Ͽ� ���ø� �����ϰ�");
		loreList.add("��7  �ٸ� �÷��̾��� �ְ����� ���߷�������!");
		loreList.add("");
		loreList.add("��7  �ο����Ѿ��� ���ο� ��������");
		loreList.add("��7  �����Ӱ� ���� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : ����");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(28, item);
		
		item = new ItemStack(Material.CHEST, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��b���� ���� ���� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �������� �־����� Ư���� ���� �� �������� ������");
		loreList.add("��7  �ٸ� �÷��̾ óġ�ؾ��ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 15��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(29, item);
		
		item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��a������ �� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �δ��� ����� ����ũ����Ʈ ����!");
		loreList.add("��7  ���� ������ ��� ���� ��ŷ�� �ö󺸼���!");
		loreList.add("");
		loreList.add("��7  1�ο� ��������");
		loreList.add("��7  �����ϰ� ���� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7  ��7��Ŭ��: ��b�������� ��7| ��7��Ŭ��: ��b���Ӽ���");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 3��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(30, item);
		
		///////���� ����
		
		item = new ItemStack(Material.MINECART, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��8�ڸ����� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��Ƴ��� ���� �ٸ� �÷��̾ �����ϸ�");
		loreList.add("��7  ����īƮ�� �ö�Ÿ���ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(23, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��f�÷���ġ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ����ؼ� ���ϴ� ���� ������ ���߷��� �̿���");
		loreList.add("��7  ������ ��Ƴ��ƾ��ϴ� ������ �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(24, item);
		
		item = new ItemStack(Material.TNT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��4��ź ������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��ź�� ���� �������Լ� ����ġ�ų�");
		loreList.add("��7  ��ź�� �ٸ� �÷��̾�� �����ϸ�");
		loreList.add("��7  ���������� ��Ƴ����� �¸��մϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(25, item);
			
		item = new ItemStack(Material.PAINTING, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��e���� ���׽�Ʈ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �־����� ������ �´� ���๰�̳� ������ �����");
		loreList.add("��7  ���� ���� ǥ���� �� �ߴ��� ���� ���ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 7��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(32, item);
			
		item = new ItemStack(Material.STAINED_CLAY, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��d������ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� ������� ���� ������ �ִ��� ����");
		loreList.add("��7  �����Ͽ� ������ 1���� �Ǵ°� ��ǥ�Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(33, item);
		
		item = new ItemStack(Material.DIAMOND_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��1���ø��� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� �Ʒ��� �ִ� �� ���� �ı��ϰ� ����߸���");
		loreList.add("��7  Ż������ ��� �����ڸ� Ż����Ű����!");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(34, item);
		
		item = new ItemStack(Material.ANVIL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��8��� ���ϱ� ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �ϴÿ��� �������� ��縦 ���ؼ�");
		loreList.add("��7  ������ �����ϼ���!");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(41, item);
		
		item = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��a�� ���� ��Ʋ ��7::");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- �������� �־����� ������ ����, ������ �̿��� ��þƮ�ϰ�");
		loreList.add("��7  �� PVP�� �����Ͽ� ���� ������ �ܷ�� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7  ��� �÷��� �ð� : 12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//inven_minigame.setItem(39, item);
		
		item = new ItemStack(Material.SPRUCE_DOOR_ITEM, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7:: ��7�ڷΰ��� ��7::");
		loreList = new ArrayList<String>();
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_minigame.setItem(45, item);
	}
	
	
	///////////�̺�Ʈ
	public class ServerGUIEvent implements Listener{
		
		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�ɼ�")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}

				optionClick(p, e.getSlot());
			}else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�� ����")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				myInfoClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().contains("���")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
			}
			
			if (!server.inLobby(p))
				return;
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��lEG����")) {				
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				serverHelperClick(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�̴ϰ���")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}		
				minigameClick(p, e.getSlot(), e.getClick().isLeftClick());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�̴� �ŵ��� ����")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				wogSelectClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l��������")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				hrwSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�����ڸ� ã�ƶ�")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				ftmSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l���� ���� ����")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}		
				rwwSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l������ ��")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}		
				komSelectClick(p, e.getSlot());	
			}else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l��������")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR ) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}
					
				serverInfoClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l��ġ��Ʈ")) {
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
